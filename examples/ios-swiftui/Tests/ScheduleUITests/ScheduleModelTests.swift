import Foundation
import XCTest
@testable import ScheduleUI

final class ScheduleModelTests: XCTestCase {
    private func sample() throws -> String {
        let url = URL(fileURLWithPath: #filePath)
            .deletingLastPathComponent()
            .appendingPathComponent("../../../shared/sample.ics")
        return try String(contentsOf: url, encoding: .utf8)
    }

    func testRows() throws {
        let rows = try ScheduleModel.rows(
            feed: try sample(),
            from: "2026-09-01T00:00:00Z",
            to: "2026-12-01T00:00:00Z",
            zone: "Europe/Berlin"
        )
        XCTAssertEqual(rows.count, 12)
        XCTAssertEqual(rows[0].title, "Distributed systems seminar")
        XCTAssertEqual("\(rows[0].day) \(rows[0].time)", "2026-09-07 10:00–11:30")
        XCTAssertEqual(rows[1].title, "Distributed systems seminar (moved)")
        XCTAssertEqual("\(rows[1].day) \(rows[1].time)", "2026-09-14 12:00–13:30")
        XCTAssertEqual(rows[0].location, "Room 101")
    }

    // region swift-throws
    func testBadInstantThrows() {
        XCTAssertThrowsError(try ScheduleModel.rows(feed: "", from: "x", to: "y", zone: "Europe/Berlin"))
    }
    // endregion

    func testView() throws {
        let rows = try ScheduleModel.rows(
            feed: try sample(),
            from: "2026-09-01T00:00:00Z",
            to: "2026-10-01T00:00:00Z",
            zone: "Europe/Berlin"
        )
        XCTAssertEqual(rows.count, 4)
        _ = ScheduleView(rows: rows).body
    }
}
