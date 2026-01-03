plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.dokka)
}

val user: String by project
val dev: String by project
val mail: String by project
val devURL: String by project
val repo: String by project
val g: String by project
val v: String by project
val desc: String by project
val inception: String by project
val artifact: String by project

group = g
version = v

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(kotlin("gradle-plugin"))
    compileOnly(libs.symbol.processing.api)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(g, "$artifact-gradle-plugin", v)

    pom {
        name = "$repo Gradle Plugin"
        description = "Gradle plugin for $desc"
        inceptionYear = inception
        url = "https://github.com/$user/$repo"
        licenses {
            license {
                name = "MIT License"
                url = "https://mit.malefic.xyz"
            }
        }
        developers {
            developer {
                name = dev
                email = mail
                url = devURL
            }
        }
        scm {
            url = "https://github.com/$user/$repo"
            connection = "scm:git:git://github.com/$user/$repo.git"
            developerConnection = "scm:git:ssh://github.com/$user/$repo.git"
        }
    }
}

gradlePlugin {
    plugins {
        register("doppelgangerPlugin") {
            id = "$g.doppelganger"
            implementationClass = "xyz.malefic.doppelganger.gradle.DoppelgangerPlugin"
            displayName = "Doppelganger SVG Plugin"
            description = "Gradle plugin for type-safe SVG resource generation"
        }
    }
}

dokka {
    pluginsConfiguration.html {
        footerMessage.set("&copy; 2025 $dev <$mail>")
    }
}
