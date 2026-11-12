package io.github.calendrum.buildlogic

import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters

// Build service with one slot shared by all benchmark run tasks.
abstract class BenchmarkRunLock : BuildService<BuildServiceParameters.None>
