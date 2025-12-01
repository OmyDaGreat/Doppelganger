package xyz.malefic.doppelganger

/**
 * Base interface for all SVG elements.
 * Represents a generic SVG element with attributes and child elements.
 */
sealed interface SvgElement {
    /**
     * A map of attributes for the SVG element.
     * Keys are attribute names, and values are their corresponding values.
     */
    val attributes: MutableMap<String, String>

    /**
     * A list of child SVG elements.
     */
    val children: MutableList<SvgElement>

    /**
     * Renders the SVG element as a string.
     * @return The string representation of the SVG element.
     */
    fun render(): String
}

/**
 * Represents the root `<svg>` element.
 */
class Svg : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<svg>` element and its children as a string.
     * @return The string representation of the `<svg>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        val childContent = children.joinToString("") { it.render() }
        return "<svg $attrs>$childContent</svg>"
    }
}

/**
 * Represents the `<g>` (group) element.
 */
class G : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<g>` element and its children as a string.
     * @return The string representation of the `<g>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        val childContent = children.joinToString("") { it.render() }
        return "<g $attrs>$childContent</g>"
    }
}

/**
 * Represents the `<rect>` (rectangle) element.
 */
class Rect : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<rect>` element as a string.
     * @return The string representation of the `<rect>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<rect $attrs/>"
    }
}

/**
 * Represents the `<circle>` element.
 */
class Circle : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<circle>` element as a string.
     * @return The string representation of the `<circle>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<circle $attrs/>"
    }
}

/**
 * Represents the `<ellipse>` element.
 */
class Ellipse : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<ellipse>` element as a string.
     * @return The string representation of the `<ellipse>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<ellipse $attrs/>"
    }
}

/**
 * Represents the `<line>` element.
 */
class Line : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<line>` element as a string.
     * @return The string representation of the `<line>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<line $attrs/>"
    }
}

/**
 * Represents the `<polyline>` element.
 */
class Polyline : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<polyline>` element as a string.
     * @return The string representation of the `<polyline>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<polyline $attrs/>"
    }
}

/**
 * Represents the `<polygon>` element.
 */
class Polygon : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<polygon>` element as a string.
     * @return The string representation of the `<polygon>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<polygon $attrs/>"
    }
}

/**
 * Represents the `<path>` element.
 */
class Path : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<path>` element as a string.
     * @return The string representation of the `<path>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<path $attrs/>"
    }
}

/**
 * Represents the `<text>` element.
 */
class Text : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * The text content of the `<text>` element.
     */
    var textContent: String = ""

    /**
     * Renders the `<text>` element and its children as a string.
     * @return The string representation of the `<text>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        val childContent = children.joinToString("") { it.render() }
        return "<text $attrs>$textContent$childContent</text>"
    }
}

/**
 * Represents the `<tspan>` element.
 */
class TSpan : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * The text content of the `<tspan>` element.
     */
    var textContent: String = ""

    /**
     * Renders the `<tspan>` element as a string.
     * @return The string representation of the `<tspan>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<tspan $attrs>$textContent</tspan>"
    }
}

/**
 * Represents the `<defs>` element for definitions.
 */
class Defs : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<defs>` element and its children as a string.
     * @return The string representation of the `<defs>` element.
     */
    override fun render(): String {
        val childContent = children.joinToString("") { it.render() }
        return "<defs>$childContent</defs>"
    }
}

/**
 * Represents the `<linearGradient>` element.
 */
class LinearGradient : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<linearGradient>` element and its children as a string.
     * @return The string representation of the `<linearGradient>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        val childContent = children.joinToString("") { it.render() }
        return "<linearGradient $attrs>$childContent</linearGradient>"
    }
}

/**
 * Represents the `<radialGradient>` element.
 */
class RadialGradient : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<radialGradient>` element and its children as a string.
     * @return The string representation of the `<radialGradient>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        val childContent = children.joinToString("") { it.render() }
        return "<radialGradient $attrs>$childContent</radialGradient>"
    }
}

/**
 * Represents the `<stop>` element for gradients.
 */
class Stop : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<stop>` element as a string.
     * @return The string representation of the `<stop>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<stop $attrs/>"
    }
}

/**
 * Represents the `<clipPath>` element.
 */
class ClipPath : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<clipPath>` element and its children as a string.
     * @return The string representation of the `<clipPath>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        val childContent = children.joinToString("") { it.render() }
        return "<clipPath $attrs>$childContent</clipPath>"
    }
}

/**
 * Represents the `<mask>` element.
 */
class Mask : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<mask>` element and its children as a string.
     * @return The string representation of the `<mask>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        val childContent = children.joinToString("") { it.render() }
        return "<mask $attrs>$childContent</mask>"
    }
}

/**
 * Represents the `<use>` element.
 */
class Use : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<use>` element as a string.
     * @return The string representation of the `<use>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        return "<use $attrs/>"
    }
}

/**
 * Represents the `<symbol>` element.
 */
class Symbol : SvgElement {
    override val attributes = mutableMapOf<String, String>()
    override val children = mutableListOf<SvgElement>()

    /**
     * Renders the `<symbol>` element and its children as a string.
     * @return The string representation of the `<symbol>` element.
     */
    override fun render(): String {
        val attrs = attributes.entries.joinToString(" ") { "${it.key}=\"${it.value}\"" }
        val childContent = children.joinToString("") { it.render() }
        return "<symbol $attrs>$childContent</symbol>"
    }
}
