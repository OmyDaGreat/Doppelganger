package xyz.malefic.doppelganger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * Composable function to render SVG content
 * This is a common interface - platform-specific implementations handle actual rendering
 */
@Composable
fun SvgContent(
    svgElement: Svg,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) = RawSvg(svgElement.render(), modifier, contentDescription)

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
 * Composable function to render raw SVG string
 */
@Composable
expect fun RawSvg(
    svgString: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
)
