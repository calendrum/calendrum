// The names and licenses of what a binary contains, from the POMs of the dependencies it links. The one place for it. It gives
// the extra property thirdPartyLicensesOf, a function from the name of a configuration. A project that sets the extra property
// licensesConfiguration first also gets the task writeThirdPartyLicenses.
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.maven.MavenModule
import org.gradle.maven.MavenPomArtifact
import javax.xml.parsers.DocumentBuilderFactory

fun licensesOf(configuration: String): Provider<String> = provider {
    val ids = configurations.getByName(configuration).incoming.resolutionResult.allComponents
        .mapNotNull { it.id as? ModuleComponentIdentifier }
        .filter { it.group != "io.github.calendrum" }
        .sortedBy { it.toString() }
    val poms = dependencies.createArtifactResolutionQuery()
        .forComponents(ids)
        .withArtifacts(MavenModule::class.java, MavenPomArtifact::class.java)
        .execute()
        .resolvedComponents
    val parser = DocumentBuilderFactory.newInstance().apply {
        setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
    }
    val lines = poms.mapNotNull { component ->
        val id = component.id as ModuleComponentIdentifier
        val file = component.getArtifacts(MavenPomArtifact::class.java)
            .filterIsInstance<org.gradle.api.artifacts.result.ResolvedArtifactResult>()
            .firstOrNull()?.file ?: return@mapNotNull null
        val document = parser.newDocumentBuilder().parse(file)
        val licenses = document.getElementsByTagName("license")
        val names = (0 until licenses.length).map { index ->
            val license = licenses.item(index) as org.w3c.dom.Element
            val name = license.getElementsByTagName("name").item(0)?.textContent?.trim().orEmpty()
            val url = license.getElementsByTagName("url").item(0)?.textContent?.trim().orEmpty()
            "$name ($url)"
        }
        "${id.group}:${id.module}:${id.version}: ${names.joinToString("; ").ifEmpty { "see the POM of the project" }}"
    }
    buildString {
        append("The third-party libraries in this binary and their licenses, from their POMs.\n\n")
        lines.sorted().forEach { append(it).append('\n') }
    }
}

extra["thirdPartyLicensesOf"] = { configuration: String -> licensesOf(configuration) }

if (extra.has("licensesConfiguration")) {
    val licenses = licensesOf(extra["licensesConfiguration"] as String)
    tasks.register("writeThirdPartyLicenses") {
        description = "Lists the licenses of the libraries in the binary."
        val output = layout.buildDirectory.file("licenses/THIRD-PARTY-LICENSES.txt")
        inputs.property("licenses", licenses)
        outputs.file(output)
        doLast { output.get().asFile.writeText(licenses.get()) }
    }
}
