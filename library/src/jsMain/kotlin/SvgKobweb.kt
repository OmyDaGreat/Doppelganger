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
 * This provides Kobweb-style API using Modifier
 */
@Composable
fun SvgTag(
    svgElement: Svg,
    modifier: Modifier = Modifier,
    attrs: AttrsScope<HTMLElement>.() -> Unit = {},
) {
    val svgString = remember(svgElement) { svgElement.render() }
    RawSvgTag(svgString, modifier, attrs)
}

/**
 * Composable function to create and render SVG using DSL in Kobweb
 * This is the recommended way to use Doppelganger with Kobweb
 */
@Composable
fun SvgImage(
    modifier: Modifier = Modifier,
    attrs: AttrsScope<HTMLElement>.() -> Unit = {},
    content: SvgBuilder.() -> Unit,
) {
    val svgElement = remember(content) { svg(content) }
    SvgTag(svgElement, modifier, attrs)
}

/**
 * Composable function to render raw SVG string in Kobweb using a wrapper div
 */
@Composable
fun RawSvgTag(
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
