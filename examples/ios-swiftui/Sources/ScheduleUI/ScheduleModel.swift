import Foundation
import CalendrumSchedule

public struct ScheduleRow: Identifiable, Equatable {
    public let id: Int
    public let title: String
    public let day: String
    public let time: String
    public let location: String
}

// region swift-model
public enum ScheduleModel {
    public static func rows(feed: String, from: String, to: String, zone: String) throws -> [ScheduleRow] {
        let entries = try ScheduleFeed(zoneId: zone).entries(feed: feed, from: from, to: to)
        return entries.enumerated().map { index, entry in
            ScheduleRow(id: index, title: entry.title, day: entry.day, time: entry.time, location: entry.location)
        }
    }
}
// endregion
