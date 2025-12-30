package xyz.malefic.doppelganger.gradle

import org.gradle.api.provider.Property

abstract class DoppelgangerExtension {
    abstract val outputPackage: Property<String>
    abstract val outputSubdir: Property<String>
    abstract val autoDetect: Property<Boolean>
}
