package xyz.malefic.doppelganger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLElement

/**
 * Composable function to render SVG content in Kobweb applications
 * This provides Kobweb-style API using Kobweb's Modifier
 *
 * @param svgElement The SVG element to render
 * @param modifier Kobweb modifier for styling
 * @param attrs Additional HTML attributes for the container div
 */
@Composable
fun KobwebSvg(
    svgElement: Svg,
    modifier: Modifier = Modifier,
    attrs: AttrsScope<HTMLElement>.() -> Unit = {},
) {
    val svgString = remember(svgElement) { svgElement.render() }
    RawKobwebSvg(svgString, modifier, attrs)
}

/**
 * Composable function to create and render SVG using DSL in Kobweb
 * This is the recommended way to use Doppelganger with Kobweb
 *
 * @param modifier Kobweb modifier for styling
 * @param attrs Additional HTML attributes for the container div
 * @param content SVG DSL builder lambda
 *
 * Example:
 * ```kotlin
 * KobwebSvgImage(modifier = Modifier.fillMaxWidth()) {
 *     circle {
 *         cx = 50.0
 *         cy = 50.0
 *         r = 40.0
 *         fill = "blue"
 *     }
 * }
 * ```
 */
@Composable
fun KobwebSvgImage(
    modifier: Modifier = Modifier,
    attrs: AttrsScope<HTMLElement>.() -> Unit = {},
    content: SvgBuilder.() -> Unit,
) {
    val svgElement = remember(content) { svg(content) }
    KobwebSvg(svgElement, modifier, attrs)
}

/**
 * Composable function to render raw SVG string in Kobweb using a wrapper div
 *
 * @param svgString Raw SVG markup as string
 * @param modifier Kobweb modifier for styling
 * @param attrs Additional HTML attributes for the container div
 */
@Composable
fun RawKobwebSvg(
    svgString: String,
    modifier: Modifier = Modifier,
    attrs: AttrsScope<HTMLElement>.() -> Unit = {},
) {
    Div(
        attrs = {
            modifier.toAttrs<AttrsScope<HTMLElement>>().invoke(this)
            attrs()
            ref { element ->
                element.innerHTML = svgString
                onDispose { }
            }
        },
    )
}
