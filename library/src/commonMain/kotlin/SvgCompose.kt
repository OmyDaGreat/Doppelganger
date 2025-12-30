package xyz.malefic.doppelganger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * Composable function to render SVG content from an Svg element
 */
@Composable
fun SvgContent(
    svgElement: Svg,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val svgBytes = remember(svgElement) { svgElement.render().encodeToByteArray() }
    SvgFromBytes(svgBytes, modifier, contentDescription)
}

/**
 * Composable function to create and render SVG using DSL
 */
@Composable
fun SvgImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    content: SvgBuilder.() -> Unit,
) {
    val svgElement =
        remember(content) {
            svg(content)
        }
    SvgContent(svgElement, modifier, contentDescription)
}

/**
 * Composable function to render SVG from byte array
 * This can be used with Compose Resources by converting the resource to bytes first
 *
 * Platform-specific implementations handle actual rendering
 */
@Composable
expect fun SvgFromBytes(
    svgBytes: ByteArray,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
)
