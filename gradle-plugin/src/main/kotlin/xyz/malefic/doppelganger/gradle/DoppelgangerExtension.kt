package xyz.malefic.doppelganger.gradle

import org.gradle.api.provider.Property

/**
 * This class defines the configuration extension for the Doppelganger Gradle plugin.
 * It allows users to configure the plugin's behavior via Gradle build scripts.
 */
abstract class DoppelgangerExtension {
    /**
     * Specifies the package name where the generated files will be placed.
     * This is a required property and should be set in the Gradle build script.
     */
    abstract val outputPackage: Property<String>

    /**
     * Specifies the subdirectory within the output package where the generated files will be placed.
     * This is an optional property and can be used to further organize the output.
     */
    abstract val outputSubdir: Property<String>

    /**
     * Determines whether the plugin should automatically detect certain configurations.
     * This is a boolean property that can be enabled or disabled in the Gradle build script.
     */
    abstract val autoDetect: Property<Boolean>
}
