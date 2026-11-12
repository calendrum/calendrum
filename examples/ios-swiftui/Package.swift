// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "ScheduleUI",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "ScheduleUI", targets: ["ScheduleUI"]),
    ],
    targets: [
        .binaryTarget(
            name: "CalendrumSchedule",
            path: "shared/build/XCFrameworks/release/CalendrumSchedule.xcframework"
        ),
        .target(name: "ScheduleUI", dependencies: ["CalendrumSchedule"]),
        .testTarget(name: "ScheduleUITests", dependencies: ["ScheduleUI"]),
    ]
)
