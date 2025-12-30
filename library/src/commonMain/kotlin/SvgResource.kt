package xyz.malefic.doppelganger

/**
 * Marks a function that returns an `Svg` for automatic resource file generation during compilation.
 *
 * **Requirements:**
 * - The annotated function should be a top-level function or a member of an object
 * - Must return [Svg]
 * - Should use the `svg {}` DSL to define the SVG structure
 * - **KSP plugin must be configured** in your project (see below)
 *
 * **Setup:**
 * ```kotlin
 * // build.gradle.kts
 * plugins {
 *     id("com.google.devtools.ksp") version "2.3.0"
 * }
 *
 * dependencies {
 *     implementation("xyz.malefic:doppelganger:1.0.1")
 *     add("kspCommonMainMetadata", "xyz.malefic:doppelganger-ksp:1.0.1")
 * }
 * ```
 *
 * **Configuration (Optional):**
 *
 * Configure output directory for generated SVG files:
 * ```kotlin
 * // Default: Generates to build/generated/ksp/.../resources/
 * ksp {
 *     arg("doppelganger.output.package", "")
 *     arg("doppelganger.output.subdir", "")
 * }
 *
 * // For Kobweb web resources:
 * ksp {
 *     arg("doppelganger.output.package", "public.images.icons")
 *     arg("doppelganger.output.subdir", "")
 * }
 * // Files accessible via: /images/icons/myIcon.svg
 *
 * // For Compose Resources:
 * ksp {
 *     arg("doppelganger.output.package", "files")
 *     arg("doppelganger.output.subdir", "svg")
 * }
 * // Files in: composeResources/files/svg/
 * ```
 *
 * **Usage Example:**
 * ```kotlin
 * @SvgResource
 * fun myIcon() = svg {
 *     width(24)
 *     height(24)
 *     path {
 *         d("M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z")
 *         fill("currentColor")
 *     }
 * }
 *
 * // In Kobweb (if configured for public resources):
 * Image(src = "/images/icons/myIcon.svg")
 *
 * // In Compose Multiplatform (if configured for composeResources):
 * val svgBytes = Res.readBytes("files/svg/myIcon.svg")
 * ```
 *
 * The processor will generate an SVG file at compile time in the configured location.
 *
 * @param resourceName Optional custom name for the generated SVG file (without .svg extension).
 *                     If not provided, the function name will be used.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class SvgResource(
    val resourceName: String = "",
)
