package io.github.calendrum.sizebudget

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.CommandLineArgumentProvider

abstract class R8Arguments : CommandLineArgumentProvider {
    @get:Classpath
    abstract val program: ConfigurableFileCollection

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val rules: RegularFileProperty

    @get:Input
    abstract val jdkHome: Property<String>

    @get:OutputFile
    abstract val output: RegularFileProperty

    @get:OutputFile
    abstract val mapping: RegularFileProperty

    override fun asArguments(): Iterable<String> = buildList {
        add("--release")
        add("--classfile")
        add("--output")
        add(output.get().asFile.absolutePath)
        add("--pg-conf")
        add(rules.get().asFile.absolutePath)
        add("--pg-map-output")
        add(mapping.get().asFile.absolutePath)
        add("--lib")
        add(jdkHome.get())
        program.files.forEach { add(it.absolutePath) }
    }
}
