using System;
using System.Buffers;

namespace Calendrum.Internal;

/// <summary>
/// The buffer of a request, from the pool. A new array for each call of more than 85 KB would go to the large object heap, whose
/// allocations start collections of every generation: a loop over a calendar of 100 KB then pays for hundreds of them.
/// </summary>
internal sealed class PooledBufferWriter : IBufferWriter<byte>, IDisposable
{
    private byte[] _buffer = ArrayPool<byte>.Shared.Rent(4096);
    private int _written;

    internal ReadOnlySpan<byte> WrittenSpan => _buffer.AsSpan(0, _written);

    public void Advance(int count) => _written += count;

    public Memory<byte> GetMemory(int sizeHint = 0)
    {
        Reserve(sizeHint);
        return _buffer.AsMemory(_written);
    }

    public Span<byte> GetSpan(int sizeHint = 0)
    {
        Reserve(sizeHint);
        return _buffer.AsSpan(_written);
    }

    public void Dispose()
    {
        byte[] buffer = _buffer;
        if (buffer.Length != 0)
        {
            _buffer = [];
            Release(buffer, _written);
        }
    }

    private void Reserve(int sizeHint)
    {
        int wanted = Math.Max(sizeHint, 1);
        if (_buffer.Length - _written >= wanted)
        {
            return;
        }
        long size = Math.Max(_buffer.Length * 2L, (long)_written + wanted);
        if (size > Array.MaxLength)
        {
            size = (long)_written + wanted;
            if (size > Array.MaxLength)
            {
                throw new TooLargeException(ErrorCodes.TooLarge, "The request is larger than the limit");
            }
        }
        byte[] larger = ArrayPool<byte>.Shared.Rent((int)size);
        WrittenSpan.CopyTo(larger);
        Release(_buffer, _written);
        _buffer = larger;
    }

    /// <summary>
    /// A calendar is private, and the array goes back to a pool that the whole process shares. Only what was written is cleared: an array
    /// for the worst case of a large text is mostly pages that nothing has touched.
    /// </summary>
    private static void Release(byte[] buffer, int written)
    {
        buffer.AsSpan(0, written).Clear();
        ArrayPool<byte>.Shared.Return(buffer);
    }
}
