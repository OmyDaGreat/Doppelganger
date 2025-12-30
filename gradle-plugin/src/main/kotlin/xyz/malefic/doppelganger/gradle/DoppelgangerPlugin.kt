package xyz.malefic.doppelganger.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension

class DoppelgangerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("doppelganger", DoppelgangerExtension::class.java)

        // Set default: auto-detect enabled
        extension.autoDetect.convention(true)

        project.afterEvaluate {
            if (extension.autoDetect.get()) {
                configureDefaults(project, extension)
            }

            // Configure generation tasks for each Kotlin target
            configureGenerationTasks(project, extension)
        }
    }

    private fun configureDefaults(
        project: Project,
        extension: DoppelgangerExtension,
    ) {
        // Skip if user already configured
        if (extension.outputPackage.isPresent) {
            project.logger.info("Doppelganger: Using user-configured output package (${extension.outputPackage.get()})")
            return
        }

        val hasKobwebPlugin =
            project.plugins.hasPlugin("com.varabyte.kobweb.application") ||
                project.plugins.hasPlugin("com.varabyte.kobweb.library")

        val isMultiplatform = project.extensions.findByType(KotlinMultiplatformExtension::class.java) != null

        val defaultPackage =
            when {
                hasKobwebPlugin -> {
                    project.logger.info("Doppelganger: Detected Kobweb project, defaulting to public/images")
                    "public.images"
                }

                isMultiplatform -> {
                    val kmpExt = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
                    val hasCommonMain = kmpExt.sourceSets.findByName("commonMain") != null
                    if (hasCommonMain) {
                        project.logger.info("Doppelganger: Detected multiplatform project, defaulting to files/drawable")
                        "files"
                    } else {
                        project.logger.info("Doppelganger: Detected multiplatform without commonMain, defaulting to public/images")
                        "public.images"
                    }
                }

                else -> {
                    project.logger.info("Doppelganger: No specific project type detected, using default")
                    "public.images"
                }
            }

        extension.outputPackage.set(defaultPackage)

        // Set subdirectory for multiplatform compose resources
        if (isMultiplatform && !hasKobwebPlugin) {
            extension.outputSubdir.set("drawable")
        }
    }

    private fun configureGenerationTasks(
        project: Project,
        extension: DoppelgangerExtension,
    ) {
        val kotlinExt = project.extensions.findByType(KotlinProjectExtension::class.java) ?: return

        when (kotlinExt) {
            is KotlinMultiplatformExtension -> configureMultiplatform(project, kotlinExt, extension)
            is KotlinSingleTargetExtension<*> -> configureSingleTarget(project, kotlinExt, extension)
        }
    }

    private fun configureMultiplatform(
        project: Project,
        kotlinExt: KotlinMultiplatformExtension,
        extension: DoppelgangerExtension,
    ) {
        kotlinExt.targets.all { target ->
            val targetName = target.name
            val compilation = target.compilations.findByName("main") ?: return@all

            project.tasks.register(
                "generate${targetName.replaceFirstChar { it.uppercase() }}SvgResources",
                GenerateSvgResourcesTask::class.java,
            ) { task ->
                task.compiledClasses.from(compilation.output.classesDirs)
                task.runtimeClasspath.from(compilation.runtimeDependencyFiles)

                task.generatedCodeDir.set(project.layout.buildDirectory.dir("generated/doppelganger/$targetName/kotlin"))
                task.generatedSvgDir.set(project.layout.buildDirectory.dir("generated/doppelganger/$targetName/resources"))

                task.outputPackage.set(extension.outputPackage)
                task.outputSubdir.set(extension.outputSubdir)

                // Depend on compilation
                task.dependsOn(compilation.compileAllTaskName)
            }

            project.logger.info("Doppelganger: Configured SVG generation for target '$targetName'")
        }
    }

    private fun configureSingleTarget(
        project: Project,
        kotlinExt: KotlinSingleTargetExtension<*>,
        extension: DoppelgangerExtension,
    ) {
        val compilation = kotlinExt.target.compilations.findByName("main") ?: return

        project.tasks.register(
            "generateSvgResources",
            GenerateSvgResourcesTask::class.java,
        ) { task ->
            task.compiledClasses.from(compilation.output.classesDirs)
            task.runtimeClasspath.from(compilation.runtimeDependencyFiles)

            task.generatedCodeDir.set(project.layout.buildDirectory.dir("generated/doppelganger/kotlin"))
            task.generatedSvgDir.set(project.layout.buildDirectory.dir("generated/doppelganger/resources"))

            task.outputPackage.set(extension.outputPackage)
            task.outputSubdir.set(extension.outputSubdir)

            // Depend on compilation
            task.dependsOn(compilation.compileAllTaskName)
        }

        project.logger.info("Doppelganger: Configured SVG generation")
    }
}
