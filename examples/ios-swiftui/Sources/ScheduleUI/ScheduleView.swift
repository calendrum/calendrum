import SwiftUI

public struct ScheduleView: View {
    private let rows: [ScheduleRow]

    public init(rows: [ScheduleRow]) {
        self.rows = rows
    }

    public var body: some View {
        List(rows) { row in
            VStack(alignment: .leading) {
                Text(row.title).font(.headline)
                Text("\(row.day) \(row.time)").font(.subheadline)
                if !row.location.isEmpty {
                    Text(row.location).font(.caption)
                }
            }
        }
    }
}
