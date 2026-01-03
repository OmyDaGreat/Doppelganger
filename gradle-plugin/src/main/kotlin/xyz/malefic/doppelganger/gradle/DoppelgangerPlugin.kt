package xyz.malefic.doppelganger.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension

class DoppelgangerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("com.google.devtools.ksp")

        val extension = project.extensions.create("doppelganger", DoppelgangerExtension::class.java)
        extension.autoDetect.convention(true)

        project.afterEvaluate {
            if (extension.autoDetect.get()) {
                configureDefaults(project, extension)
            }

            configureKspProcessor(project)
            configureGenerationTasks(project, extension)
        }
    }

    private fun configureDefaults(
        project: Project,
        extension: DoppelgangerExtension,
    ) {
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

    private fun configureKspProcessor(project: Project) {
        val kotlinExt = project.extensions.findByType(KotlinProjectExtension::class.java) ?: return

        // Get the jar containing this plugin (which includes the KSP processor)
        val processorJar =
            project.files(
                DoppelgangerPlugin::class.java.protectionDomain.codeSource.location,
            )

        when (kotlinExt) {
            is KotlinMultiplatformExtension -> {
                try {
                    project.dependencies.add("kspCommonMainMetadata", processorJar)
                    project.logger.info("Doppelganger: Registered KSP processor for commonMain")
                } catch (e: Exception) {
                    project.logger.warn("Doppelganger: Could not register KSP for commonMain: ${e.message}")
                }

                kotlinExt.targets.forEach { target ->
                    val configName = "ksp${target.name.replaceFirstChar { it.uppercase() }}"
                    try {
                        project.dependencies.add(configName, processorJar)
                        project.logger.debug("Doppelganger: Registered KSP processor for ${target.name}")
                    } catch (e: Exception) {
                        project.logger.debug("Doppelganger: Skipping KSP config for ${target.name}: ${e.message}")
                    }
                }
            }

            is KotlinSingleTargetExtension<*> -> {
                try {
                    project.dependencies.add("ksp", processorJar)
                    project.logger.info("Doppelganger: Registered KSP processor")
                } catch (e: Exception) {
                    project.logger.warn("Doppelganger: Could not register KSP: ${e.message}")
                }
            }
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

                task.metadataFile.set(
                    project.layout.buildDirectory
                        .dir("generated/ksp/commonMain/resources")
                        .map { it.file("svg-resources.json") },
                )

                task.generatedSvgDir.set(project.layout.buildDirectory.dir("generated/doppelganger/$targetName/resources"))

                task.outputPackage.set(extension.outputPackage)
                task.outputSubdir.set(extension.outputSubdir)

                task.dependsOn("kspKotlinCommonMainMetadata")
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

            task.metadataFile.set(
                project.layout.buildDirectory
                    .dir("generated/ksp/${kotlinExt.target.name}/resources")
                    .map { it.file("svg-resources.json") },
            )

            task.generatedSvgDir.set(project.layout.buildDirectory.dir("generated/doppelganger/resources"))

            task.outputPackage.set(extension.outputPackage)
            task.outputSubdir.set(extension.outputSubdir)

            task.dependsOn("kspKotlin")
            task.dependsOn(compilation.compileAllTaskName)
        }

        project.logger.info("Doppelganger: Configured SVG generation")
    }
}
