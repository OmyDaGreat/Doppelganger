package xyz.malefic.doppelganger.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URLClassLoader

abstract class GenerateSvgResourcesTask : DefaultTask() {
    @get:InputFiles
    abstract val compiledClasses: ConfigurableFileCollection

    @get:InputFiles
    abstract val runtimeClasspath: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val generatedCodeDir: DirectoryProperty

    @get:OutputDirectory
    abstract val generatedSvgDir: DirectoryProperty

    @get:Input
    @get:Optional
    abstract val outputPackage: Property<String>

    @get:Input
    @get:Optional
    abstract val outputSubdir: Property<String>

    data class FunctionInfo(
        val packageName: String,
        val functionName: String,
        val fileName: String,
    )

    @TaskAction
    fun generate() {
        val codeOutput = generatedCodeDir.get().asFile
        val svgOutput = generatedSvgDir.get().asFile

        codeOutput.mkdirs()
        svgOutput.mkdirs()

        logger.lifecycle("=== Doppelganger Gradle Plugin ===")
        logger.lifecycle("Scanning for @SvgResource functions...")

        // Create classloader with compiled classes and dependencies
        val classpathUrls =
            (compiledClasses.files + runtimeClasspath.files)
                .filter { it.exists() }
                .map { it.toURI().toURL() }
                .toTypedArray()

        val classLoader = URLClassLoader(classpathUrls, this.javaClass.classLoader)

        // Find all @SvgResource annotated functions
        val functions =
            try {
                findSvgResourceFunctions(classLoader)
            } catch (e: ClassNotFoundException) {
                logger.lifecycle("⚠ Cannot load annotation class (JS-only project?)")
                logger.lifecycle("⚠ SVG generation skipped - use SvgResources accessor at runtime")
                return
            }

        if (functions.isEmpty()) {
            logger.lifecycle("No @SvgResource functions found")
            return
        }

        logger.lifecycle("Found ${functions.size} @SvgResource function(s)")

        // Generate accessor code
        generateAccessorCode(functions, codeOutput)

        // Generate SVG files
        generateSvgFiles(functions, svgOutput, classLoader)

        logger.lifecycle("✓ Code generation complete")
    }

    private fun findSvgResourceFunctions(classLoader: ClassLoader): List<FunctionInfo> {
        val functions = mutableListOf<FunctionInfo>()
        val annotationClass = classLoader.loadClass("xyz.malefic.doppelganger.SvgResource")

        compiledClasses.files.forEach { classDir ->
            if (!classDir.exists() || !classDir.isDirectory) return@forEach

            classDir
                .walkTopDown()
                .filter { it.extension == "class" }
                .forEach { classFile ->
                    try {
                        val className =
                            classFile
                                .relativeTo(classDir)
                                .path
                                .removeSuffix(".class")
                                .replace(File.separatorChar, '.')

                        val clazz = classLoader.loadClass(className)

                        // Check for top-level functions (in *Kt classes)
                        if (className.endsWith("Kt")) {
                            clazz.declaredMethods.forEach { method ->
                                if (method.isAnnotationPresent(annotationClass as Class<out Annotation>)) {
                                    val annotation = method.getAnnotation(annotationClass)
                                    val resourceName =
                                        try {
                                            val resourceNameMethod = annotationClass.getDeclaredMethod("resourceName")
                                            (resourceNameMethod.invoke(annotation) as? String)?.takeIf { it.isNotBlank() }
                                        } catch (e: Exception) {
                                            null
                                        }

                                    val fileName = resourceName ?: method.name
                                    val packageName = clazz.`package`?.name ?: ""

                                    logger.lifecycle("  → Found: $packageName.${method.name} -> $fileName")

                                    functions.add(
                                        FunctionInfo(
                                            packageName = packageName,
                                            functionName = method.name,
                                            fileName = fileName,
                                        ),
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        // Skip classes that can't be loaded
                        logger.debug("Could not load class from {}: {}", classFile, e.message)
                    }
                }
        }

        return functions
    }

    private fun generateAccessorCode(
        functions: List<FunctionInfo>,
        outputDir: File,
    ) {
        logger.lifecycle("Generating SvgResources accessor...")

        val code =
            buildString {
                appendLine("// Generated by Doppelganger Gradle Plugin")
                appendLine("// DO NOT EDIT - This file is automatically generated")
                appendLine("@file:Suppress(\"unused\", \"RedundantVisibilityModifier\")")
                appendLine()
                appendLine("package xyz.malefic.doppelganger.generated")
                appendLine()
                appendLine("import xyz.malefic.doppelganger.Svg")

                // Import all packages containing icons
                val uniquePackages =
                    functions
                        .map { it.packageName }
                        .filter { it.isNotEmpty() }
                        .toSet()

                uniquePackages.forEach { pkg ->
                    functions
                        .filter { it.packageName == pkg }
                        .forEach { info ->
                            appendLine("import ${info.packageName}.${info.functionName}")
                        }
                }

                appendLine()
                appendLine("/**")
                appendLine(" * Centralized access to all SVG resources marked with @SvgResource.")
                appendLine(" * ")
                appendLine(" * Usage:")
                appendLine(" * ```kotlin")
                appendLine(" * // Get Svg object (lazy-initialized)")
                appendLine(" * val icon: Svg = SvgResources.homeIcon")
                appendLine(" * ")
                appendLine(" * // Get rendered SVG string (cached)")
                appendLine(" * val svgString: String = SvgResources.homeIconSvg")
                appendLine(" * ")
                appendLine(" * // Use in Compose")
                appendLine(" * KobwebSvg(SvgResources.homeIcon)")
                appendLine(" * ```")
                appendLine(" */")
                appendLine("public object SvgResources {")
                appendLine()

                // Generate properties for each icon
                functions.forEach { info ->
                    appendLine("    /**")
                    appendLine("     * SVG resource from ${info.packageName}.${info.functionName}()")
                    appendLine("     * Lazy-initialized on first access.")
                    appendLine("     */")
                    appendLine("    public val ${info.fileName}: Svg by lazy { ${info.functionName}() }")
                    appendLine()

                    appendLine("    /**")
                    appendLine("     * Rendered SVG string for ${info.fileName}")
                    appendLine("     * Cached after first render.")
                    appendLine("     */")
                    appendLine("    public val ${info.fileName}Svg: String by lazy { ${info.fileName}.render() }")
                    appendLine()
                }

                // Generate utility properties and functions
                appendLine("    /**")
                appendLine("     * All available SVG resource names.")
                appendLine("     */")
                appendLine("    public val allResourceNames: Set<String> = setOf(")
                functions.forEachIndexed { index, info ->
                    val comma = if (index < functions.size - 1) "," else ""
                    appendLine("        \"${info.fileName}\"$comma")
                }
                appendLine("    )")
                appendLine()

                appendLine("    /**")
                appendLine("     * Get an SVG resource by name.")
                appendLine("     * @return Svg object or null if not found")
                appendLine("     */")
                appendLine("    public fun getSvg(name: String): Svg? = when (name) {")
                functions.forEach { info ->
                    appendLine("        \"${info.fileName}\" -> ${info.fileName}")
                }
                appendLine("        else -> null")
                appendLine("    }")
                appendLine()

                appendLine("    /**")
                appendLine("     * Render all SVG resources to a map.")
                appendLine("     * Useful for bulk export or testing.")
                appendLine("     */")
                appendLine("    public fun renderAll(): Map<String, String> = buildMap {")
                functions.forEach { info ->
                    appendLine("        put(\"${info.fileName}\", ${info.fileName}Svg)")
                }
                appendLine("    }")
                appendLine("}")
            }

        val packageDir = outputDir.resolve("xyz/malefic/doppelganger/generated")
        packageDir.mkdirs()

        val file = packageDir.resolve("SvgResources.kt")
        file.writeText(code)

        logger.lifecycle("  ✓ Generated SvgResources.kt with ${functions.size} icons")
    }

    private fun generateSvgFiles(
        functions: List<FunctionInfo>,
        outputDir: File,
        classLoader: URLClassLoader,
    ) {
        logger.lifecycle("Generating static SVG files...")

        var successCount = 0

        functions.forEach { info ->
            try {
                // Invoke the function to get the SVG object
                val className =
                    if (info.packageName.isNotEmpty()) {
                        "${info.packageName}.${info.functionName.replaceFirstChar { it.uppercase() }}Kt"
                    } else {
                        "${info.functionName.replaceFirstChar { it.uppercase() }}Kt"
                    }

                // Try different class name patterns
                val possibleClassNames =
                    listOf(
                        className,
                        if (info.packageName.isNotEmpty()) "${info.packageName}.${findContainingFileName(info.functionName)}Kt" else null,
                    ).filterNotNull()

                var clazz: Class<*>? = null
                for (possibleName in possibleClassNames) {
                    try {
                        clazz = classLoader.loadClass(possibleName)
                        break
                    } catch (e: ClassNotFoundException) {
                        // Try next
                    }
                }

                if (clazz == null) {
                    logger.warn("  ✗ ${info.fileName}.svg: Could not find class for function")
                    return@forEach
                }

                val method =
                    clazz.declaredMethods.find { it.name == info.functionName }
                        ?: throw NoSuchMethodException("Method ${info.functionName} not found")

                val svgObject = method.invoke(null)
                val renderMethod =
                    svgObject.javaClass.methods.find { it.name == "render" }
                        ?: throw NoSuchMethodException("render() method not found")

                val svgContent = renderMethod.invoke(svgObject) as String

                // Write SVG file
                val pkg = outputPackage.getOrElse("")
                val subdir = outputSubdir.getOrElse("")

                val targetDir =
                    if (pkg.isNotEmpty()) {
                        outputDir.resolve(pkg.replace('.', File.separatorChar))
                    } else {
                        outputDir
                    }.let { dir ->
                        if (subdir.isNotEmpty()) dir.resolve(subdir) else dir
                    }

                targetDir.mkdirs()

                val svgFile = targetDir.resolve("${info.fileName}.svg")
                svgFile.writeText(svgContent)

                logger.lifecycle("  ✓ ${info.fileName}.svg")
                successCount++
            } catch (e: Exception) {
                logger.warn("  ✗ ${info.fileName}.svg: ${e.message}")
                logger.debug("Stack trace:", e)
            }
        }

        if (successCount > 0) {
            logger.lifecycle("✓ Generated $successCount/${functions.size} static SVG files")
        }
    }

    private fun findContainingFileName(functionName: String): String {
        // This is a heuristic - in practice, we'd need to track this during compilation
        // For now, assume the file is named after the function or use a common convention
        return functionName.replaceFirstChar { it.uppercase() }
    }
}
