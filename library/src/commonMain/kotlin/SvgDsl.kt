package xyz.malefic.doppelganger

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

/**
 * Extension function to convert a `Color` object to its SVG-compatible string representation.
 * The output is either a 6-digit hex color (e.g., `#RRGGBB`) for opaque colors or an `rgba()` string
 * for colors with transparency.
 */
private fun Color.toSvgString(): String {
    val r = (red * 255f).roundToInt().coerceIn(0, 255)
    val g = (green * 255f).roundToInt().coerceIn(0, 255)
    val b = (blue * 255f).roundToInt().coerceIn(0, 255)
    val a = alpha

    /**
     * Converts an integer value (0-255) to a two-digit hexadecimal string.
     *
     * @param value The integer value to convert.
     * @return A two-digit hexadecimal string.
     */
    fun toTwoDigitHex(value: Int): String {
        val hi = (value shr 4) and 0xF
        val lo = value and 0xF

        /**
         * Converts a nibble (4 bits) to its hexadecimal character representation.
         *
         * @param n The nibble value (0-15).
         * @return The corresponding hexadecimal character.
         */
        fun nibble(n: Int): Char = if (n < 10) ('0' + n) else ('A' + n - 10)
        return "${nibble(hi)}${nibble(lo)}"
    }

    return if (a >= 0.999f) {
        // For opaque colors, return a 6-digit hex string.
        "#${toTwoDigitHex(r)}${toTwoDigitHex(g)}${toTwoDigitHex(b)}"
    } else {
        // For transparent colors, return an rgba() string with alpha rounded to 3 decimal places.
        val aRounded = ((a * 1000f).roundToInt()).toFloat() / 1000f
        "rgba($r,$g,$b,$aRounded)"
    }
}

/**
 * DSL marker for the SVG builder
 */
@DslMarker
internal annotation class SvgDslMarker

/**
 * Abstract base class for building SVG elements. Provides methods to set attributes
 * and common properties for SVG elements.
 *
 * @param T The type of the SVG element being built.
 * @property element The SVG element instance being constructed.
 */
@SvgDslMarker
abstract class SvgElementBuilder<T : SvgElement>(
    protected val element: T,
) {
    /**
     * Sets an attribute on the SVG element with a string value.
     *
     * @param name The name of the attribute.
     * @param value The value of the attribute as a string.
     */
    fun attr(
        name: String,
        value: String,
    ) {
        element.attributes[name] = value
    }

    /**
     * Sets an attribute on the SVG element with a numeric value.
     *
     * @param name The name of the attribute.
     * @param value The value of the attribute as a number.
     */
    fun attr(
        name: String,
        value: Number,
    ) {
        element.attributes[name] = value.toString()
    }

    /**
     * Sets the `id` attribute of the SVG element.
     *
     * @param value The ID value.
     */
    fun id(value: String) = attr("id", value)

    /**
     * Sets the `class` attribute of the SVG element.
     *
     * @param value The class name(s).
     */
    fun className(value: String) = attr("class", value)

    /**
     * Sets the `style` attribute of the SVG element.
     *
     * @param value The style string.
     */
    fun style(value: String) = attr("style", value)

    /**
     * Sets the `transform` attribute of the SVG element.
     *
     * @param value The transformation string.
     */
    fun transform(value: String) = attr("transform", value)

    /**
     * Sets the `opacity` attribute of the SVG element.
     *
     * @param value The opacity value.
     */
    fun opacity(value: Number) = attr("opacity", value)

    /**
     * Sets the `fill` attribute of the SVG element with a color string.
     *
     * @param color The fill color as a string.
     */
    fun fill(color: String) = attr("fill", color)

    /**
     * Sets the `fill` attribute of the SVG element with a `Color` object.
     *
     * @param color The fill color as a `Color` object.
     */
    fun fill(color: Color) = fill(color.toSvgString())

    /**
     * Sets the `fill-opacity` attribute of the SVG element.
     *
     * @param value The fill opacity value.
     */
    fun fillOpacity(value: Number) = attr("fill-opacity", value)

    /**
     * Sets the `fill-rule` attribute of the SVG element.
     *
     * @param value The fill rule value.
     */
    fun fillRule(value: String) = attr("fill-rule", value)

    /**
     * Sets the `stroke` attribute of the SVG element with a color string.
     *
     * @param color The stroke color as a string.
     */
    fun stroke(color: String) = attr("stroke", color)

    /**
     * Sets the `stroke` attribute of the SVG element with a `Color` object.
     *
     * @param color The stroke color as a `Color` object.
     */
    fun stroke(color: Color) = stroke(color.toSvgString())

    /**
     * Sets the `stroke-width` attribute of the SVG element.
     *
     * @param value The stroke width value.
     */
    fun strokeWidth(value: Number) = attr("stroke-width", value)

    /**
     * Sets the `stroke-opacity` attribute of the SVG element.
     *
     * @param value The stroke opacity value.
     */
    fun strokeOpacity(value: Number) = attr("stroke-opacity", value)

    /**
     * Sets the `stroke-linecap` attribute of the SVG element.
     *
     * @param value The stroke line cap value.
     */
    fun strokeLinecap(value: String) = attr("stroke-linecap", value)

    /**
     * Sets the `stroke-linejoin` attribute of the SVG element.
     *
     * @param value The stroke line join value.
     */
    fun strokeLinejoin(value: String) = attr("stroke-linejoin", value)

    /**
     * Sets the `stroke-dasharray` attribute of the SVG element.
     *
     * @param value The stroke dash array value.
     */
    fun strokeDasharray(value: String) = attr("stroke-dasharray", value)

    /**
     * Sets the `stroke-dashoffset` attribute of the SVG element.
     *
     * @param value The stroke dash offset value.
     */
    fun strokeDashoffset(value: Number) = attr("stroke-dashoffset", value)

    /**
     * Sets the `clip-path` attribute of the SVG element.
     *
     * @param value The clip path value.
     */
    fun clipPath(value: String) = attr("clip-path", value)

    /**
     * Sets the `mask` attribute of the SVG element.
     *
     * @param value The mask value.
     */
    fun mask(value: String) = attr("mask", value)

    /**
     * Sets the `filter` attribute of the SVG element.
     *
     * @param value The filter value.
     */
    fun filter(value: String) = attr("filter", value)

    /**
     * Builds the SVG element and returns it.
     *
     * @return The constructed SVG element.
     */
    internal open fun build(): T = element
}

/**
 * Builder for SVG root element. This class provides methods to define attributes
 * and child elements for the root `<svg>` element in an SVG document.
 */
class SvgBuilder : SvgElementBuilder<Svg>(Svg()) {
    /**
     * Sets the `width` attribute of the SVG element.
     *
     * @param value The width of the SVG element.
     */
    fun width(value: Number) = attr("width", value)

    /**
     * Sets the `height` attribute of the SVG element.
     *
     * @param value The height of the SVG element.
     */
    fun height(value: Number) = attr("height", value)

    /**
     * Sets the `viewBox` attribute of the SVG element.
     *
     * @param minX The x-coordinate of the viewBox's starting point.
     * @param minY The y-coordinate of the viewBox's starting point.
     * @param width The width of the viewBox.
     * @param height The height of the viewBox.
     */
    fun viewBox(
        minX: Number,
        minY: Number,
        width: Number,
        height: Number,
    ) {
        attr("viewBox", "$minX $minY $width $height")
    }

    /**
     * Sets the `preserveAspectRatio` attribute of the SVG element.
     *
     * @param value The value for the `preserveAspectRatio` attribute.
     */
    fun preserveAspectRatio(value: String) = attr("preserveAspectRatio", value)

    /**
     * Sets the `xmlns` attribute of the SVG element.
     *
     * @param value The XML namespace for the SVG element. Defaults to "http://www.w3.org/2000/svg".
     */
    fun xmlns(value: String = "http://www.w3.org/2000/svg") = attr("xmlns", value)

    /**
     * Adds a `<rect>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<rect>` element.
     */
    fun rect(block: RectBuilder.() -> Unit) {
        val builder = RectBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<circle>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<circle>` element.
     */
    fun circle(block: CircleBuilder.() -> Unit) {
        val builder = CircleBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds an `<ellipse>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<ellipse>` element.
     */
    fun ellipse(block: EllipseBuilder.() -> Unit) {
        val builder = EllipseBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<line>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<line>` element.
     */
    fun line(block: LineBuilder.() -> Unit) {
        val builder = LineBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<polyline>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<polyline>` element.
     */
    fun polyline(block: PolylineBuilder.() -> Unit) {
        val builder = PolylineBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<polygon>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<polygon>` element.
     */
    fun polygon(block: PolygonBuilder.() -> Unit) {
        val builder = PolygonBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<path>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<path>` element.
     */
    fun path(block: PathBuilder.() -> Unit) {
        val builder = PathBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<text>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<text>` element.
     */
    fun text(block: TextBuilder.() -> Unit) {
        val builder = TextBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<g>` (group) child element to the SVG element.
     *
     * @param block A lambda to configure the `<g>` element.
     */
    fun g(block: GBuilder.() -> Unit) {
        val builder = GBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<defs>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<defs>` element.
     */
    fun defs(block: DefsBuilder.() -> Unit) {
        val builder = DefsBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<use>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<use>` element.
     */
    fun use(block: UseBuilder.() -> Unit) {
        val builder = UseBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<symbol>` child element to the SVG element.
     *
     * @param block A lambda to configure the `<symbol>` element.
     */
    fun symbol(block: SymbolBuilder.() -> Unit) {
        val builder = SymbolBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Builder for SVG group (`<g>`) element. This class provides methods to add child elements
 * such as `<rect>`, `<circle>`, `<ellipse>`, `<line>`, `<polyline>`, `<polygon>`, `<path>`,
 * `<text>`, nested `<g>`, and `<use>` elements to the group.
 */
class GBuilder : SvgElementBuilder<G>(G()) {
    /**
     * Adds a `<rect>` child element to the group.
     *
     * @param block A lambda to configure the `<rect>` element.
     */
    fun rect(block: RectBuilder.() -> Unit) {
        val builder = RectBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<circle>` child element to the group.
     *
     * @param block A lambda to configure the `<circle>` element.
     */
    fun circle(block: CircleBuilder.() -> Unit) {
        val builder = CircleBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds an `<ellipse>` child element to the group.
     *
     * @param block A lambda to configure the `<ellipse>` element.
     */
    fun ellipse(block: EllipseBuilder.() -> Unit) {
        val builder = EllipseBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<line>` child element to the group.
     *
     * @param block A lambda to configure the `<line>` element.
     */
    fun line(block: LineBuilder.() -> Unit) {
        val builder = LineBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<polyline>` child element to the group.
     *
     * @param block A lambda to configure the `<polyline>` element.
     */
    fun polyline(block: PolylineBuilder.() -> Unit) {
        val builder = PolylineBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<polygon>` child element to the group.
     *
     * @param block A lambda to configure the `<polygon>` element.
     */
    fun polygon(block: PolygonBuilder.() -> Unit) {
        val builder = PolygonBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<path>` child element to the group.
     *
     * @param block A lambda to configure the `<path>` element.
     */
    fun path(block: PathBuilder.() -> Unit) {
        val builder = PathBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<text>` child element to the group.
     *
     * @param block A lambda to configure the `<text>` element.
     */
    fun text(block: TextBuilder.() -> Unit) {
        val builder = TextBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a nested `<g>` (group) child element to the group.
     *
     * @param block A lambda to configure the nested `<g>` element.
     */
    fun g(block: GBuilder.() -> Unit) {
        val builder = GBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<use>` child element to the group.
     *
     * @param block A lambda to configure the `<use>` element.
     */
    fun use(block: UseBuilder.() -> Unit) {
        val builder = UseBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Builder for SVG rectangle (`<rect>`) element. This class provides methods to set
 * attributes specific to the `<rect>` element, such as position, size, and corner radii.
 */
class RectBuilder : SvgElementBuilder<Rect>(Rect()) {
    /**
     * Sets the `x` attribute of the rectangle, which defines the x-coordinate of the
     * rectangle's top-left corner.
     *
     * @param value The x-coordinate value.
     */
    fun x(value: Number) = attr("x", value)

    /**
     * Sets the `y` attribute of the rectangle, which defines the y-coordinate of the
     * rectangle's top-left corner.
     *
     * @param value The y-coordinate value.
     */
    fun y(value: Number) = attr("y", value)

    /**
     * Sets the `width` attribute of the rectangle, which defines the width of the rectangle.
     *
     * @param value The width value.
     */
    fun width(value: Number) = attr("width", value)

    /**
     * Sets the `height` attribute of the rectangle, which defines the height of the rectangle.
     *
     * @param value The height value.
     */
    fun height(value: Number) = attr("height", value)

    /**
     * Sets the `rx` attribute of the rectangle, which defines the x-axis radius of the
     * rectangle's rounded corners.
     *
     * @param value The x-axis radius value.
     */
    fun rx(value: Number) = attr("rx", value)

    /**
     * Sets the `ry` attribute of the rectangle, which defines the y-axis radius of the
     * rectangle's rounded corners.
     *
     * @param value The y-axis radius value.
     */
    fun ry(value: Number) = attr("ry", value)
}

/**
 * Builder for SVG circle (`<circle>`) element. This class provides methods to set
 * attributes specific to the `<circle>` element, such as its center coordinates and radius.
 */
class CircleBuilder : SvgElementBuilder<Circle>(Circle()) {
    /**
     * Sets the `cx` attribute of the circle, which defines the x-coordinate of the circle's center.
     *
     * @param value The x-coordinate value.
     */
    fun cx(value: Number) = attr("cx", value)

    /**
     * Sets the `cy` attribute of the circle, which defines the y-coordinate of the circle's center.
     *
     * @param value The y-coordinate value.
     */
    fun cy(value: Number) = attr("cy", value)

    /**
     * Sets the `r` attribute of the circle, which defines the radius of the circle.
     *
     * @param value The radius value.
     */
    fun r(value: Number) = attr("r", value)
}

/**
 * Builder for SVG ellipse (`<ellipse>`) element. This class provides methods to set
 * attributes specific to the `<ellipse>` element, such as its center coordinates
 * and radii.
 */
class EllipseBuilder : SvgElementBuilder<Ellipse>(Ellipse()) {
    /**
     * Sets the `cx` attribute of the ellipse, which defines the x-coordinate of the
     * ellipse's center.
     *
     * @param value The x-coordinate value.
     */
    fun cx(value: Number) = attr("cx", value)

    /**
     * Sets the `cy` attribute of the ellipse, which defines the y-coordinate of the
     * ellipse's center.
     *
     * @param value The y-coordinate value.
     */
    fun cy(value: Number) = attr("cy", value)

    /**
     * Sets the `rx` attribute of the ellipse, which defines the x-axis radius of the
     * ellipse.
     *
     * @param value The x-axis radius value.
     */
    fun rx(value: Number) = attr("rx", value)

    /**
     * Sets the `ry` attribute of the ellipse, which defines the y-axis radius of the
     * ellipse.
     *
     * @param value The y-axis radius value.
     */
    fun ry(value: Number) = attr("ry", value)
}

/**
 * Builder for SVG line (`<line>`) element. This class provides methods to set
 * attributes specific to the `<line>` element, such as the start and end points.
 */
class LineBuilder : SvgElementBuilder<Line>(Line()) {
    /**
     * Sets the `x1` attribute of the line, which defines the x-coordinate of the
     * line's starting point.
     *
     * @param value The x-coordinate value.
     */
    fun x1(value: Number) = attr("x1", value)

    /**
     * Sets the `y1` attribute of the line, which defines the y-coordinate of the
     * line's starting point.
     *
     * @param value The y-coordinate value.
     */
    fun y1(value: Number) = attr("y1", value)

    /**
     * Sets the `x2` attribute of the line, which defines the x-coordinate of the
     * line's ending point.
     *
     * @param value The x-coordinate value.
     */
    fun x2(value: Number) = attr("x2", value)

    /**
     * Sets the `y2` attribute of the line, which defines the y-coordinate of the
     * line's ending point.
     *
     * @param value The y-coordinate value.
     */
    fun y2(value: Number) = attr("y2", value)
}

/**
 * Builder for SVG polyline (`<polyline>`) element. This class provides methods to set
 * the `points` attribute, which defines the list of points that make up the polyline.
 */
class PolylineBuilder : SvgElementBuilder<Polyline>(Polyline()) {
    /**
     * Sets the `points` attribute of the polyline using a string representation.
     *
     * @param value The points as a string, where each point is represented as "x,y"
     *              and points are separated by spaces.
     */
    fun points(value: String) = attr("points", value)

    /**
     * Sets the `points` attribute of the polyline using a vararg of coordinate pairs.
     *
     * @param coords A vararg of coordinate pairs, where each pair represents a point
     *               as (x, y).
     */
    fun points(vararg coords: Pair<Number, Number>) {
        val pointsStr = coords.joinToString(" ") { "${it.first},${it.second}" }
        attr("points", pointsStr)
    }
}

/**
 * Builder for SVG polygon (`<polygon>`) element. This class provides methods to set
 * the `points` attribute, which defines the list of points that make up the polygon.
 */
class PolygonBuilder : SvgElementBuilder<Polygon>(Polygon()) {
    /**
     * Sets the `points` attribute of the polygon using a string representation.
     *
     * @param value The points as a string, where each point is represented as "x,y"
     *              and points are separated by spaces.
     */
    fun points(value: String) = attr("points", value)

    /**
     * Sets the `points` attribute of the polygon using a vararg of coordinate pairs.
     *
     * @param coords A vararg of coordinate pairs, where each pair represents a point
     *               as (x, y).
     */
    fun points(vararg coords: Pair<Number, Number>) {
        val pointsStr = coords.joinToString(" ") { "${it.first},${it.second}" }
        attr("points", pointsStr)
    }
}

/**
 * Builder for SVG path (`<path>`) element with path data DSL. This class provides
 * methods to construct complex path data using a fluent API.
 */
class PathBuilder : SvgElementBuilder<Path>(Path()) {
    private val pathData = StringBuilder()

    /**
     * Sets the `d` attribute of the path, which defines the path data.
     *
     * @param value The path data as a string.
     */
    fun d(value: String) = attr("d", value)

    /**
     * Adds a "move to" command (`M`) to the path, which moves the pen to the specified
     * absolute coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    fun moveTo(
        x: Number,
        y: Number,
    ) = apply { pathData.append("M $x $y ") }

    /**
     * Adds a relative "move to" command (`m`) to the path, which moves the pen to the
     * specified relative coordinates.
     *
     * @param x The x-offset.
     * @param y The y-offset.
     */
    fun moveToRelative(
        x: Number,
        y: Number,
    ) = apply { pathData.append("m $x $y ") }

    /**
     * Adds a "line to" command (`L`) to the path, which draws a straight line to the
     * specified absolute coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    fun lineTo(
        x: Number,
        y: Number,
    ) = apply { pathData.append("L $x $y ") }

    /**
     * Adds a relative "line to" command (`l`) to the path, which draws a straight line
     * to the specified relative coordinates.
     *
     * @param x The x-offset.
     * @param y The y-offset.
     */
    fun lineToRelative(
        x: Number,
        y: Number,
    ) = apply { pathData.append("l $x $y ") }

    /**
     * Adds a "horizontal line to" command (`H`) to the path, which draws a horizontal
     * line to the specified absolute x-coordinate.
     *
     * @param x The x-coordinate.
     */
    fun horizontalLineTo(x: Number) = apply { pathData.append("H $x ") }

    /**
     * Adds a relative "horizontal line to" command (`h`) to the path, which draws a
     * horizontal line to the specified relative x-offset.
     *
     * @param x The x-offset.
     */
    fun horizontalLineToRelative(x: Number) = apply { pathData.append("h $x ") }

    /**
     * Adds a "vertical line to" command (`V`) to the path, which draws a vertical line
     * to the specified absolute y-coordinate.
     *
     * @param y The y-coordinate.
     */
    fun verticalLineTo(y: Number) = apply { pathData.append("V $y ") }

    /**
     * Adds a relative "vertical line to" command (`v`) to the path, which draws a
     * vertical line to the specified relative y-offset.
     *
     * @param y The y-offset.
     */
    fun verticalLineToRelative(y: Number) = apply { pathData.append("v $y ") }

    /**
     * Adds a cubic Bezier curve command (`C`) to the path, which draws a curve to the
     * specified absolute coordinates using two control points.
     *
     * @param x1 The x-coordinate of the first control point.
     * @param y1 The y-coordinate of the first control point.
     * @param x2 The x-coordinate of the second control point.
     * @param y2 The y-coordinate of the second control point.
     * @param x The x-coordinate of the end point.
     * @param y The y-coordinate of the end point.
     */
    fun curveTo(
        x1: Number,
        y1: Number,
        x2: Number,
        y2: Number,
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("C $x1 $y1 $x2 $y2 $x $y ")
    }

    /**
     * Adds a relative cubic Bezier curve command (`c`) to the path, which draws a curve
     * to the specified relative coordinates using two control points.
     *
     * @param x1 The x-offset of the first control point.
     * @param y1 The y-offset of the first control point.
     * @param x2 The x-offset of the second control point.
     * @param y2 The y-offset of the second control point.
     * @param x The x-offset of the end point.
     * @param y The y-offset of the end point.
     */
    fun curveToRelative(
        x1: Number,
        y1: Number,
        x2: Number,
        y2: Number,
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("c $x1 $y1 $x2 $y2 $x $y ")
    }

    /**
     * Adds a smooth cubic Bezier curve command (`S`) to the path, which draws a curve
     * to the specified absolute coordinates using one control point.
     *
     * @param x2 The x-coordinate of the control point.
     * @param y2 The y-coordinate of the control point.
     * @param x The x-coordinate of the end point.
     * @param y The y-coordinate of the end point.
     */
    fun smoothCurveTo(
        x2: Number,
        y2: Number,
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("S $x2 $y2 $x $y ")
    }

    /**
     * Adds a relative smooth cubic Bezier curve command (`s`) to the path, which draws
     * a curve to the specified relative coordinates using one control point.
     *
     * @param x2 The x-offset of the control point.
     * @param y2 The y-offset of the control point.
     * @param x The x-offset of the end point.
     * @param y The y-offset of the end point.
     */
    fun smoothCurveToRelative(
        x2: Number,
        y2: Number,
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("s $x2 $y2 $x $y ")
    }

    /**
     * Adds a quadratic Bezier curve command (`Q`) to the path, which draws a curve to
     * the specified absolute coordinates using one control point.
     *
     * @param x1 The x-coordinate of the control point.
     * @param y1 The y-coordinate of the control point.
     * @param x The x-coordinate of the end point.
     * @param y The y-coordinate of the end point.
     */
    fun quadraticCurveTo(
        x1: Number,
        y1: Number,
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("Q $x1 $y1 $x $y ")
    }

    /**
     * Adds a relative quadratic Bezier curve command (`q`) to the path, which draws a
     * curve to the specified relative coordinates using one control point.
     *
     * @param x1 The x-offset of the control point.
     * @param y1 The y-offset of the control point.
     * @param x The x-offset of the end point.
     * @param y The y-offset of the end point.
     */
    fun quadraticCurveToRelative(
        x1: Number,
        y1: Number,
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("q $x1 $y1 $x $y ")
    }

    /**
     * Adds a smooth quadratic Bezier curve command (`T`) to the path, which draws a
     * curve to the specified absolute coordinates.
     *
     * @param x The x-coordinate of the end point.
     * @param y The y-coordinate of the end point.
     */
    fun smoothQuadraticCurveTo(
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("T $x $y ")
    }

    /**
     * Adds a relative smooth quadratic Bezier curve command (`t`) to the path, which
     * draws a curve to the specified relative coordinates.
     *
     * @param x The x-offset of the end point.
     * @param y The y-offset of the end point.
     */
    fun smoothQuadraticCurveToRelative(
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("t $x $y ")
    }

    /**
     * Adds an elliptical arc command (`A`) to the path, which draws an arc to the
     * specified absolute coordinates.
     *
     * @param rx The x-axis radius of the ellipse.
     * @param ry The y-axis radius of the ellipse.
     * @param xAxisRotation The rotation of the ellipse's x-axis, in degrees.
     * @param largeArcFlag The large-arc flag (1 for large arc, 0 for small arc).
     * @param sweepFlag The sweep flag (1 for clockwise, 0 for counterclockwise).
     * @param x The x-coordinate of the end point.
     * @param y The y-coordinate of the end point.
     */
    fun arcTo(
        rx: Number,
        ry: Number,
        xAxisRotation: Number,
        largeArcFlag: Int,
        sweepFlag: Int,
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("A $rx $ry $xAxisRotation $largeArcFlag $sweepFlag $x $y ")
    }

    /**
     * Adds a relative elliptical arc command (`a`) to the path, which draws an arc to
     * the specified relative coordinates.
     *
     * @param rx The x-axis radius of the ellipse.
     * @param ry The y-axis radius of the ellipse.
     * @param xAxisRotation The rotation of the ellipse's x-axis, in degrees.
     * @param largeArcFlag The large-arc flag (1 for large arc, 0 for small arc).
     * @param sweepFlag The sweep flag (1 for clockwise, 0 for counterclockwise).
     * @param x The x-offset of the end point.
     * @param y The y-offset of the end point.
     */
    fun arcToRelative(
        rx: Number,
        ry: Number,
        xAxisRotation: Number,
        largeArcFlag: Int,
        sweepFlag: Int,
        x: Number,
        y: Number,
    ) = apply {
        pathData.append("a $rx $ry $xAxisRotation $largeArcFlag $sweepFlag $x $y ")
    }

    /**
     * Adds a "close path" command (`Z`) to the path, which closes the current subpath
     * by drawing a straight line back to the start point.
     */
    fun closePath() = apply { pathData.append("Z ") }

    /**
     * Builds the `<path>` element and sets the `d` attribute with the constructed path
     * data.
     *
     * @return The constructed `<path>` element.
     */
    override fun build(): Path {
        if (pathData.isNotEmpty()) {
            element.attributes["d"] = pathData.toString().trim()
        }
        return super.build()
    }
}

/**
 * Builder for SVG text (`<text>`) element. This class provides methods to set
 * attributes specific to the `<text>` element, such as position, font properties,
 * and content.
 */
class TextBuilder : SvgElementBuilder<Text>(Text()) {
    /**
     * Sets the `x` attribute of the text, which defines the x-coordinate of the
     * text's starting point.
     *
     * @param value The x-coordinate value.
     */
    fun x(value: Number) = attr("x", value)

    /**
     * Sets the `y` attribute of the text, which defines the y-coordinate of the
     * text's starting point.
     *
     * @param value The y-coordinate value.
     */
    fun y(value: Number) = attr("y", value)

    /**
     * Sets the `dx` attribute of the text, which defines a horizontal shift from
     * the current text position.
     *
     * @param value The horizontal shift value.
     */
    fun dx(value: Number) = attr("dx", value)

    /**
     * Sets the `dy` attribute of the text, which defines a vertical shift from
     * the current text position.
     *
     * @param value The vertical shift value.
     */
    fun dy(value: Number) = attr("dy", value)

    /**
     * Sets the `text-anchor` attribute of the text, which defines the alignment
     * of the text relative to its starting point.
     *
     * @param value The text anchor value (e.g., "start", "middle", "end").
     */
    fun textAnchor(value: String) = attr("text-anchor", value)

    /**
     * Sets the `font-size` attribute of the text, which defines the size of the
     * font used to render the text.
     *
     * @param value The font size value.
     */
    fun fontSize(value: Number) = attr("font-size", value)

    /**
     * Sets the `font-family` attribute of the text, which defines the font family
     * used to render the text.
     *
     * @param value The font family name.
     */
    fun fontFamily(value: String) = attr("font-family", value)

    /**
     * Sets the `font-weight` attribute of the text, which defines the weight
     * (thickness) of the font.
     *
     * @param value The font weight value (e.g., "normal", "bold").
     */
    fun fontWeight(value: String) = attr("font-weight", value)

    /**
     * Sets the `font-style` attribute of the text, which defines the style of the
     * font (e.g., "normal", "italic").
     *
     * @param value The font style value.
     */
    fun fontStyle(value: String) = attr("font-style", value)

    /**
     * Sets the `text-decoration` attribute of the text, which defines the
     * decoration applied to the text (e.g., "underline", "line-through").
     *
     * @param value The text decoration value.
     */
    fun textDecoration(value: String) = attr("text-decoration", value)

    /**
     * Sets the content of the text element.
     *
     * @param text The text content.
     */
    fun content(text: String) {
        element.textContent = text
    }

    /**
     * Sets the content of the text element using the unary plus operator.
     *
     * @receiver The text content.
     */
    @Suppress("FunctionNaming")
    operator fun String.unaryPlus() {
        element.textContent = this
    }

    /**
     * Adds a `<tspan>` child element to the text element.
     *
     * @param block A lambda to configure the `<tspan>` element.
     */
    fun tspan(block: TSpanBuilder.() -> Unit) {
        val builder = TSpanBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Builder for SVG `<tspan>` element. This class provides methods to set
 * attributes specific to the `<tspan>` element, such as position and content.
 */
class TSpanBuilder : SvgElementBuilder<TSpan>(TSpan()) {
    /**
     * Sets the `x` attribute of the `<tspan>` element, which defines the x-coordinate
     * of the text's starting point.
     *
     * @param value The x-coordinate value.
     */
    fun x(value: Number) = attr("x", value)

    /**
     * Sets the `y` attribute of the `<tspan>` element, which defines the y-coordinate
     * of the text's starting point.
     *
     * @param value The y-coordinate value.
     */
    fun y(value: Number) = attr("y", value)

    /**
     * Sets the `dx` attribute of the `<tspan>` element, which defines a horizontal
     * shift from the current text position.
     *
     * @param value The horizontal shift value.
     */
    fun dx(value: Number) = attr("dx", value)

    /**
     * Sets the `dy` attribute of the `<tspan>` element, which defines a vertical
     * shift from the current text position.
     *
     * @param value The vertical shift value.
     */
    fun dy(value: Number) = attr("dy", value)

    /**
     * Sets the content of the `<tspan>` element.
     *
     * @param text The text content.
     */
    fun content(text: String) {
        element.textContent = text
    }

    /**
     * Sets the content of the `<tspan>` element using the unary plus operator.
     *
     * @receiver The text content.
     */
    operator fun String.unaryPlus() {
        element.textContent = this
    }
}

/**
 * Builder for SVG `<defs>` element. This class provides methods to add child elements
 * such as `<linearGradient>`, `<radialGradient>`, `<clipPath>`, `<mask>`, and `<symbol>`.
 */
class DefsBuilder : SvgElementBuilder<Defs>(Defs()) {
    /**
     * Adds a `<linearGradient>` child element to the `<defs>` element.
     *
     * @param block A lambda to configure the `<linearGradient>` element.
     */
    fun linearGradient(block: LinearGradientBuilder.() -> Unit) {
        val builder = LinearGradientBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<radialGradient>` child element to the `<defs>` element.
     *
     * @param block A lambda to configure the `<radialGradient>` element.
     */
    fun radialGradient(block: RadialGradientBuilder.() -> Unit) {
        val builder = RadialGradientBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<clipPath>` child element to the `<defs>` element.
     *
     * @param block A lambda to configure the `<clipPath>` element.
     */
    fun clipPath(block: ClipPathBuilder.() -> Unit) {
        val builder = ClipPathBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<mask>` child element to the `<defs>` element.
     *
     * @param block A lambda to configure the `<mask>` element.
     */
    fun mask(block: MaskBuilder.() -> Unit) {
        val builder = MaskBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<symbol>` child element to the `<defs>` element.
     *
     * @param block A lambda to configure the `<symbol>` element.
     */
    fun symbol(block: SymbolBuilder.() -> Unit) {
        val builder = SymbolBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Enum representing the gradient units for an SVG gradient element.
 *
 * @property svgValue The string value of the gradient unit as used in SVG.
 */
enum class GradientUnits(
    private val svgValue: String,
) {
    /**
     * Specifies that the gradient's coordinate system is relative to the bounding box
     * of the element to which the gradient is applied.
     */
    OBJECT_BOUNDING_BOX("objectBoundingBox"),

    /**
     * Specifies that the gradient's coordinate system uses the user space of the SVG canvas.
     */
    USER_SPACE_ON_USE("userSpaceOnUse"),
    ;

    /**
     * Returns the SVG-compatible string representation of the gradient unit.
     *
     * @return The string value of the gradient unit.
     */
    override fun toString(): String = svgValue
}

/**
 * Builder for SVG `<linearGradient>` element. This class provides methods to set
 * attributes specific to the `<linearGradient>` element, such as start and end points,
 * gradient units, transformations, and spread methods.
 */
class LinearGradientBuilder : SvgElementBuilder<LinearGradient>(LinearGradient()) {
    /**
     * Sets the `x1` attribute of the `<linearGradient>` element, which defines the
     * x-coordinate of the starting point of the gradient.
     *
     * @param value The x-coordinate value.
     */
    fun x1(value: Number) = attr("x1", value)

    /**
     * Sets the `y1` attribute of the `<linearGradient>` element, which defines the
     * y-coordinate of the starting point of the gradient.
     *
     * @param value The y-coordinate value.
     */
    fun y1(value: Number) = attr("y1", value)

    /**
     * Sets the `x2` attribute of the `<linearGradient>` element, which defines the
     * x-coordinate of the ending point of the gradient.
     *
     * @param value The x-coordinate value.
     */
    fun x2(value: Number) = attr("x2", value)

    /**
     * Sets the `y2` attribute of the `<linearGradient>` element, which defines the
     * y-coordinate of the ending point of the gradient.
     *
     * @param value The y-coordinate value.
     */
    fun y2(value: Number) = attr("y2", value)

    /**
     * Sets the `gradientUnits` attribute of the `<linearGradient>` element, which
     * specifies the coordinate system for the gradient.
     *
     * @param value The gradient units as a string (e.g., "userSpaceOnUse").
     */
    fun gradientUnits(value: String) = attr("gradientUnits", value)

    /**
     * Sets the `gradientUnits` attribute of the `<linearGradient>` element using
     * the `GradientUnits` enum.
     *
     * @param value The gradient units as a `GradientUnits` enum value.
     */
    fun gradientUnits(value: GradientUnits) = gradientUnits(value.toString())

    /**
     * Sets the `gradientTransform` attribute of the `<linearGradient>` element, which
     * applies a transformation to the gradient.
     *
     * @param value The transformation string.
     */
    fun gradientTransform(value: String) = attr("gradientTransform", value)

    /**
     * Sets the `spreadMethod` attribute of the `<linearGradient>` element, which
     * defines how the gradient is spread outside its bounds.
     *
     * @param value The spread method value (e.g., "pad", "reflect", "repeat").
     */
    fun spreadMethod(value: String) = attr("spreadMethod", value)

    /**
     * Adds a `<stop>` child element to the `<linearGradient>` element.
     *
     * @param block A lambda to configure the `<stop>` element.
     */
    fun stop(block: StopBuilder.() -> Unit) {
        val builder = StopBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Builder for SVG `<radialGradient>` element. This class provides methods to set
 * attributes specific to the `<radialGradient>` element, such as center coordinates,
 * radii, gradient units, transformations, and spread methods.
 */
class RadialGradientBuilder : SvgElementBuilder<RadialGradient>(RadialGradient()) {
    /**
     * Sets the `cx` attribute of the `<radialGradient>` element, which defines the
     * x-coordinate of the gradient's center.
     *
     * @param value The x-coordinate value.
     */
    fun cx(value: Number) = attr("cx", value)

    /**
     * Sets the `cy` attribute of the `<radialGradient>` element, which defines the
     * y-coordinate of the gradient's center.
     *
     * @param value The y-coordinate value.
     */
    fun cy(value: Number) = attr("cy", value)

    /**
     * Sets the `r` attribute of the `<radialGradient>` element, which defines the
     * radius of the gradient.
     *
     * @param value The radius value.
     */
    fun r(value: Number) = attr("r", value)

    /**
     * Sets the `fx` attribute of the `<radialGradient>` element, which defines the
     * x-coordinate of the focal point of the gradient.
     *
     * @param value The x-coordinate value.
     */
    fun fx(value: Number) = attr("fx", value)

    /**
     * Sets the `fy` attribute of the `<radialGradient>` element, which defines the
     * y-coordinate of the focal point of the gradient.
     *
     * @param value The y-coordinate value.
     */
    fun fy(value: Number) = attr("fy", value)

    /**
     * Sets the `gradientUnits` attribute of the `<radialGradient>` element, which
     * specifies the coordinate system for the gradient.
     *
     * @param value The gradient units as a string (e.g., "userSpaceOnUse").
     */
    fun gradientUnits(value: String) = attr("gradientUnits", value)

    /**
     * Sets the `gradientUnits` attribute of the `<radialGradient>` element using
     * the `GradientUnits` enum.
     *
     * @param value The gradient units as a `GradientUnits` enum value.
     */
    fun gradientUnits(value: GradientUnits) = gradientUnits(value.toString())

    /**
     * Sets the `gradientTransform` attribute of the `<radialGradient>` element, which
     * applies a transformation to the gradient.
     *
     * @param value The transformation string.
     */
    fun gradientTransform(value: String) = attr("gradientTransform", value)

    /**
     * Sets the `spreadMethod` attribute of the `<radialGradient>` element, which
     * defines how the gradient is spread outside its bounds.
     *
     * @param value The spread method value (e.g., "pad", "reflect", "repeat").
     */
    fun spreadMethod(value: String) = attr("spreadMethod", value)

    /**
     * Adds a `<stop>` child element to the `<radialGradient>` element.
     *
     * @param block A lambda to configure the `<stop>` element.
     */
    fun stop(block: StopBuilder.() -> Unit) {
        val builder = StopBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Builder for SVG `<stop>` element. This class provides methods to set attributes
 * specific to the `<stop>` element, such as offset, stop color, and stop opacity.
 */
class StopBuilder : SvgElementBuilder<Stop>(Stop()) {
    /**
     * Sets the `offset` attribute of the `<stop>` element with a numeric value.
     *
     * @param value The offset value as a number.
     */
    fun offset(value: Number) = attr("offset", value)

    /**
     * Sets the `offset` attribute of the `<stop>` element with a string value.
     *
     * @param value The offset value as a string.
     */
    fun offset(value: String) = attr("offset", value)

    /**
     * Sets the `stop-color` attribute of the `<stop>` element with a color string.
     *
     * @param value The stop color as a string.
     */
    fun stopColor(value: String) = attr("stop-color", value)

    /**
     * Sets the `stop-color` attribute of the `<stop>` element with a `Color` object.
     *
     * @param value The stop color as a `Color` object.
     */
    fun stopColor(value: Color) = stopColor(value.toSvgString())

    /**
     * Sets the `stop-opacity` attribute of the `<stop>` element.
     *
     * @param value The stop opacity value.
     */
    fun stopOpacity(value: Number) = attr("stop-opacity", value)
}

/**
 * Builder for SVG `<clipPath>` element. This class provides methods to set attributes
 * and add child elements specific to the `<clipPath>` element.
 */
class ClipPathBuilder : SvgElementBuilder<ClipPath>(ClipPath()) {
    /**
     * Sets the `clipPathUnits` attribute of the `<clipPath>` element, which specifies
     * the coordinate system for the contents of the `<clipPath>`.
     *
     * @param value The value for the `clipPathUnits` attribute (e.g., "userSpaceOnUse").
     */
    fun clipPathUnits(value: String) = attr("clipPathUnits", value)

    /**
     * Adds a `<rect>` child element to the `<clipPath>` element.
     *
     * @param block A lambda to configure the `<rect>` element.
     */
    fun rect(block: RectBuilder.() -> Unit) {
        val builder = RectBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<circle>` child element to the `<clipPath>` element.
     *
     * @param block A lambda to configure the `<circle>` element.
     */
    fun circle(block: CircleBuilder.() -> Unit) {
        val builder = CircleBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds an `<ellipse>` child element to the `<clipPath>` element.
     *
     * @param block A lambda to configure the `<ellipse>` element.
     */
    fun ellipse(block: EllipseBuilder.() -> Unit) {
        val builder = EllipseBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<path>` child element to the `<clipPath>` element.
     *
     * @param block A lambda to configure the `<path>` element.
     */
    fun path(block: PathBuilder.() -> Unit) {
        val builder = PathBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Builder for SVG `<mask>` element. This class provides methods to set attributes
 * and add child elements specific to the `<mask>` element.
 */
class MaskBuilder : SvgElementBuilder<Mask>(Mask()) {
    /**
     * Sets the `maskUnits` attribute of the `<mask>` element, which specifies
     * the coordinate system for the mask's contents.
     *
     * @param value The value for the `maskUnits` attribute (e.g., "userSpaceOnUse").
     */
    fun maskUnits(value: String) = attr("maskUnits", value)

    /**
     * Sets the `maskContentUnits` attribute of the `<mask>` element, which specifies
     * the coordinate system for the content within the mask.
     *
     * @param value The value for the `maskContentUnits` attribute (e.g., "objectBoundingBox").
     */
    fun maskContentUnits(value: String) = attr("maskContentUnits", value)

    /**
     * Adds a `<rect>` child element to the `<mask>` element.
     *
     * @param block A lambda to configure the `<rect>` element.
     */
    fun rect(block: RectBuilder.() -> Unit) {
        val builder = RectBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<circle>` child element to the `<mask>` element.
     *
     * @param block A lambda to configure the `<circle>` element.
     */
    fun circle(block: CircleBuilder.() -> Unit) {
        val builder = CircleBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<g>` (group) child element to the `<mask>` element.
     *
     * @param block A lambda to configure the `<g>` element.
     */
    fun g(block: GBuilder.() -> Unit) {
        val builder = GBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Builder for SVG `<use>` element. This class provides methods to set attributes
 * specific to the `<use>` element, such as `href`, `xlink:href`, position, and size.
 */
class UseBuilder : SvgElementBuilder<Use>(Use()) {
    /**
     * Sets the `href` attribute of the `<use>` element, which specifies the URL
     * of the element to reuse.
     *
     * @param value The URL or fragment identifier.
     */
    fun href(value: String) = attr("href", value)

    /**
     * Sets the `xlink:href` attribute of the `<use>` element, which specifies the URL
     * of the element to reuse (deprecated in favor of `href`).
     *
     * @param value The URL or fragment identifier.
     */
    fun xlinkHref(value: String) = attr("xlink:href", value)

    /**
     * Sets the `x` attribute of the `<use>` element, which defines the x-coordinate
     * of the referenced element's position.
     *
     * @param value The x-coordinate value.
     */
    fun x(value: Number) = attr("x", value)

    /**
     * Sets the `y` attribute of the `<use>` element, which defines the y-coordinate
     * of the referenced element's position.
     *
     * @param value The y-coordinate value.
     */
    fun y(value: Number) = attr("y", value)

    /**
     * Sets the `width` attribute of the `<use>` element, which defines the width
     * of the referenced element.
     *
     * @param value The width value.
     */
    fun width(value: Number) = attr("width", value)

    /**
     * Sets the `height` attribute of the `<use>` element, which defines the height
     * of the referenced element.
     *
     * @param value The height value.
     */
    fun height(value: Number) = attr("height", value)
}

/**
 * Builder for SVG `<symbol>` element. This class provides methods to set attributes
 * specific to the `<symbol>` element, such as `viewBox` and `preserveAspectRatio`,
 * and to add child elements like `<rect>`, `<circle>`, `<path>`, and `<g>`.
 */
class SymbolBuilder : SvgElementBuilder<Symbol>(Symbol()) {
    /**
     * Sets the `viewBox` attribute of the `<symbol>` element, which defines the
     * position and dimension of the viewBox.
     *
     * @param minX The x-coordinate of the viewBox's starting point.
     * @param minY The y-coordinate of the viewBox's starting point.
     * @param width The width of the viewBox.
     * @param height The height of the viewBox.
     */
    fun viewBox(
        minX: Number,
        minY: Number,
        width: Number,
        height: Number,
    ) = attr("viewBox", "$minX $minY $width $height")

    /**
     * Sets the `preserveAspectRatio` attribute of the `<symbol>` element, which
     * specifies how the aspect ratio of the symbol is preserved.
     *
     * @param value The value for the `preserveAspectRatio` attribute.
     */
    fun preserveAspectRatio(value: String) = attr("preserveAspectRatio", value)

    /**
     * Adds a `<rect>` child element to the `<symbol>` element.
     *
     * @param block A lambda to configure the `<rect>` element.
     */
    fun rect(block: RectBuilder.() -> Unit) {
        val builder = RectBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<circle>` child element to the `<symbol>` element.
     *
     * @param block A lambda to configure the `<circle>` element.
     */
    fun circle(block: CircleBuilder.() -> Unit) {
        val builder = CircleBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<path>` child element to the `<symbol>` element.
     *
     * @param block A lambda to configure the `<path>` element.
     */
    fun path(block: PathBuilder.() -> Unit) {
        val builder = PathBuilder()
        builder.block()
        element.children.add(builder.build())
    }

    /**
     * Adds a `<g>` (group) child element to the `<symbol>` element.
     *
     * @param block A lambda to configure the `<g>` element.
     */
    fun g(block: GBuilder.() -> Unit) {
        val builder = GBuilder()
        builder.block()
        element.children.add(builder.build())
    }
}

/**
 * Creates an SVG element using a DSL-style builder.
 *
 * This function serves as the entry point for constructing SVG elements. It initializes
 * an `SvgBuilder` instance, applies the provided configuration block to it, and then
 * builds and returns the resulting `Svg` element.
 *
 * @param block A lambda with receiver that configures the `SvgBuilder`.
 * @return The constructed `Svg` element.
 */
fun svg(block: SvgBuilder.() -> Unit): Svg {
    val builder = SvgBuilder()
    builder.block()
    return builder.build()
}
