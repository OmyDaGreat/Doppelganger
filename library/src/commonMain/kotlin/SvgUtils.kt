package xyz.malefic.doppelganger

/**
 * Escapes special XML characters in text content.
 *
 * This function replaces special characters like `&`, `<`, `>`, `"`, and `'`
 * with their corresponding XML escape sequences.
 *
 * Typically used by rendering functions to ensure safe output (for example
 * internal `render()` implementations).
 *
 * @return The escaped string.
 */
fun String.escapeXml(): String =
    this
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;")

/**
 * Converts an SVG element to its string representation.
 *
 * This function renders the SVG element into a string by delegating to the
 * element's `render()` implementation.
 *
 * Useful when producing final output (for example calling from application code
 * or tests).
 *
 * @return The string representation of the SVG element.
 */
fun Svg.toSvgString(): String = this.render()

/**
 * Creates a `viewBox` string from coordinates.
 *
 * The `viewBox` defines the position and dimensions of the SVG viewport.
 * Commonly used when setting the `viewBox` attribute on `svg`/`symbol` builders.
 *
 * @param minX The x-coordinate of the viewBox's starting point.
 * @param minY The y-coordinate of the viewBox's starting point.
 * @param width The width of the viewBox.
 * @param height The height of the viewBox.
 * @return The `viewBox` string.
 */
fun viewBox(
    minX: Number,
    minY: Number,
    width: Number,
    height: Number,
): String = "$minX $minY $width $height"

/**
 * Creates a `translate` transform string.
 *
 * The `translate` transform moves an element by the specified x and y offsets.
 * This helper is intended to be used directly as a `transform` attribute value
 * or combined with other transform helpers via `transforms(...)`.
 *
 * @param x The x-offset.
 * @param y The y-offset.
 * @return The `translate` transform string.
 */
fun translate(
    x: Number,
    y: Number,
): String = "translate($x, $y)"

/**
 * Creates a `rotate` transform string.
 *
 * The `rotate` transform rotates an element by the specified angle, optionally
 * around a given center point. This helper can be used directly as a
 * `transform` attribute value or combined with other transform helpers via
 * `transforms(...)`.
 *
 * @param angle The rotation angle in degrees.
 * @param cx The x-coordinate of the rotation center (optional).
 * @param cy The y-coordinate of the rotation center (optional).
 * @return The `rotate` transform string.
 */
fun rotate(
    angle: Number,
    cx: Number? = null,
    cy: Number? = null,
): String = if (cx != null && cy != null) "rotate($angle, $cx, $cy)" else "rotate($angle)"

/**
 * Creates a `scale` transform string.
 *
 * The `scale` transform resizes an element by the specified x and y scaling factors.
 * This helper is intended to be used directly as a `transform` attribute value
 * or combined with other transform helpers via `transforms(...)`.
 *
 * @param x The scaling factor along the x-axis.
 * @param y The scaling factor along the y-axis (optional).
 * @return The `scale` transform string.
 */
fun scale(
    x: Number,
    y: Number? = null,
): String = if (y != null) "scale($x, $y)" else "scale($x)"

/**
 * Creates a `skewX` transform string.
 *
 * The `skewX` transform skews an element along the x-axis by the specified angle.
 * This helper can be combined with other transform helpers using `transforms(...)`.
 *
 * @param angle The skew angle in degrees.
 * @return The `skewX` transform string.
 */
fun skewX(angle: Number): String = "skewX($angle)"

/**
 * Creates a `skewY` transform string.
 *
 * The `skewY` transform skews an element along the y-axis by the specified angle.
 * This helper can be combined with other transform helpers using `transforms(...)`.
 *
 * @param angle The skew angle in degrees.
 * @return The `skewY` transform string.
 */
fun skewY(angle: Number): String = "skewY($angle)"

/**
 * Creates a `matrix` transform string.
 *
 * The SVG `matrix(a, b, c, d, e, f)` is a 2D affine transform with the matrix
 * layout:
 *   [ a  c  e ]
 *   [ b  d  f ]
 *   [ 0  0  1 ]
 *
 * It maps points `(x, y)` to:
 *   x' = a * x + c * y + e
 *   y' = b * x + d * y + f
 *
 * Parameter roles:
 * - `a` — X scale and part of rotation (cos component for pure rotation).
 * - `b` — Y shear / mixes X into Y (sin component for rotation).
 * - `c` — X shear / mixes Y into X (-sin component for rotation).
 * - `d` — Y scale and part of rotation (cos component for pure rotation).
 * - `e` — X translation (tx).
 * - `f` — Y translation (ty).
 *
 * Common examples:
 * - Identity: `matrix(1, 0, 0, 1, 0, 0)`
 * - Translate(tx, ty): `matrix(1, 0, 0, 1, tx, ty)`
 * - Scale(sx, sy): `matrix(sx, 0, 0, sy, 0, 0)`
 * - Rotate(θ): `matrix(cosθ, sinθ, -sinθ, cosθ, 0, 0)` (θ in radians for cos/sin)
 * - SkewX(α): `matrix(1, 0, tanα, 1, 0, 0)`
 * - SkewY(β): `matrix(1, tanβ, 0, 1, 0, 0)`
 *
 * Use this helper directly as a `transform` attribute value or combine it with
 * other transform helpers via `transforms(...)`.
 *
 * @param a The matrix value at position (1, 1).
 * @param b The matrix value at position (1, 2).
 * @param c The matrix value at position (2, 1).
 * @param d The matrix value at position (2, 2).
 * @param e The matrix value at position (3, 1).
 * @param f The matrix value at position (3, 2).
 * @return The `matrix` transform string.
 */
fun matrix(
    a: Number,
    b: Number,
    c: Number,
    d: Number,
    e: Number,
    f: Number,
): String = "matrix($a, $b, $c, $d, $e, $f)"

/**
 * Combines multiple transform strings into a single transform string.
 *
 * Use this to combine outputs from `translate()`, `rotate()`, `scale()`,
 * `skewX()`, `skewY()`, `matrix()`, etc., into a single `transform` attribute.
 *
 * @param transforms The individual transform strings to combine.
 * @return The combined transform string.
 */
fun transforms(vararg transforms: String): String = transforms.joinToString(" ")

/**
 * Creates a `points` string from coordinate pairs.
 *
 * The `points` string is used in SVG elements like `<polygon>` and `<polyline>`.
 * Builders for those elements typically call this helper to produce the attribute
 * value.
 *
 * @param coords The coordinate pairs.
 * @return The `points` string.
 */
fun points(vararg coords: Pair<Number, Number>): String = coords.joinToString(" ") { "${it.first},${it.second}" }

/**
 * Creates an `rgb` color string.
 *
 * The `rgb` color format specifies a color using red, green, and blue components.
 * Useful when setting `fill`/`stroke` attributes or inline styles.
 *
 * @param r The red component (0-255).
 * @param g The green component (0-255).
 * @param b The blue component (0-255).
 * @return The `rgb` color string.
 */
fun rgb(
    r: Int,
    g: Int,
    b: Int,
): String = "rgb($r, $g, $b)"

/**
 * Creates an `rgba` color string.
 *
 * The `rgba` color format specifies a color using red, green, blue components,
 * and an alpha (opacity) value. Useful for `fill`/`stroke` or style values.
 *
 * @param r The red component (0-255).
 * @param g The green component (0-255).
 * @param b The blue component (0-255).
 * @param a The alpha component (0.0-1.0).
 * @return The `rgba` color string.
 */
fun rgba(
    r: Int,
    g: Int,
    b: Int,
    a: Float,
): String = "rgba($r, $g, $b, $a)"

/**
 * Creates an `hsl` color string.
 *
 * The `hsl` color format specifies a color using hue, saturation, and lightness components.
 * Useful when setting `fill`/`stroke` attributes or inline styles.
 *
 * @param h The hue component (0-360).
 * @param s The saturation component (0-100).
 * @param l The lightness component (0-100).
 * @return The `hsl` color string.
 */
fun hsl(
    h: Int,
    s: Int,
    l: Int,
): String = "hsl($h, $s%, $l%)"

/**
 * Creates an `hsla` color string.
 *
 * The `hsla` color format specifies a color using hue, saturation, lightness components,
 * and an alpha (opacity) value. Useful for `fill`/`stroke` or style values.
 *
 * @param h The hue component (0-360).
 * @param s The saturation component (0-100).
 * @param l The lightness component (0-100).
 * @param a The alpha component (0.0-1.0).
 * @return The `hsla` color string.
 */
fun hsla(
    h: Int,
    s: Int,
    l: Int,
    a: Float,
): String = "hsla($h, $s%, $l%, $a)"
