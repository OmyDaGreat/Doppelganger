package xyz.malefic.doppelganger

import androidx.compose.ui.graphics.Color

/**
 * Creates a simple circle
 */
fun createSimpleCircle() =
    svg {
        width(100)
        height(100)
        viewBox(0, 0, 100, 100)

        circle {
            cx(50)
            cy(50)
            r(40)
            fill(Color.Blue)
            stroke(Color.Black)
            strokeWidth(2)
        }
    }

/**
 * Creates a colorful rectangle with gradient
 */
fun createGradientRect() =
    svg {
        width(200)
        height(100)

        defs {
            linearGradient {
                id("grad1")
                x1(0)
                y1(0)
                x2(100)
                y2(0)
                gradientUnits("userSpaceOnUse")

                stop {
                    offset("0%")
                    stopColor("rgb(255,255,0)")
                }
                stop {
                    offset("100%")
                    stopColor("rgb(255,0,0)")
                }
            }
        }

        rect {
            width(200)
            height(100)
            fill("url(#grad1)")
        }
    }

/**
 * Creates a star shape using path
 */
fun createStar() =
    svg {
        width(100)
        height(100)
        viewBox(0, 0, 100, 100)

        path {
            moveTo(50, 10)
            lineTo(61, 35)
            lineTo(88, 35)
            lineTo(66, 52)
            lineTo(77, 77)
            lineTo(50, 60)
            lineTo(23, 77)
            lineTo(34, 52)
            lineTo(12, 35)
            lineTo(39, 35)
            closePath()

            fill("gold")
            stroke("orange")
            strokeWidth(2)
        }
    }

/**
 * Creates a smiley face
 */
fun createSmileyFace() =
    svg {
        width(200)
        height(200)
        viewBox(0, 0, 200, 200)

        // Face
        circle {
            cx(100)
            cy(100)
            r(80)
            fill("yellow")
            stroke("black")
            strokeWidth(2)
        }

        // Left eye
        circle {
            cx(75)
            cy(85)
            r(10)
            fill(Color.Black)
        }

        // Right eye
        circle {
            cx(125)
            cy(85)
            r(10)
            fill("black")
        }

        // Smile
        path {
            moveTo(70, 120)
            quadraticCurveTo(100, 150, 130, 120)
            fill("none")
            stroke("black")
            strokeWidth(3)
            strokeLinecap("round")
        }
    }

/**
 * Creates a grouped composition
 */
fun createGroupedShapes() =
    svg {
        width(300)
        height(200)

        g {
            id("shapes")
            transform("translate(20, 20)")

            rect {
                x(0)
                y(0)
                width(50)
                height(50)
                fill("red")
            }

            circle {
                cx(100)
                cy(25)
                r(25)
                fill("green")
            }

            polygon {
                points(
                    150 to 0,
                    175 to 50,
                    125 to 50,
                )
                fill("blue")
            }
        }
    }

/**
 * Creates text with styling
 */
fun createStyledText() =
    svg {
        width(300)
        height(100)

        text {
            x(10)
            y(40)
            fontFamily("Arial, sans-serif")
            fontSize(24)
            fontWeight("bold")
            fill("darkblue")
            content("Hello, SVG!")
        }

        text {
            x(10)
            y(70)
            fontFamily("Georgia, serif")
            fontSize(18)
            fontStyle("italic")
            fill("darkgreen")
            content("Compose Multiplatform")
        }
    }

/**
 * Creates a path with curves
 */
fun createCurvyPath() =
    svg {
        width(300)
        height(200)
        viewBox(0, 0, 300, 200)

        path {
            moveTo(10, 100)
            curveTo(40, 10, 80, 10, 110, 100)
            smoothCurveTo(180, 190, 210, 100)
            smoothCurveTo(260, 10, 290, 100)

            fill("none")
            stroke("purple")
            strokeWidth(3)
            strokeLinecap("round")
        }
    }

/**
 * Creates a radial gradient circle
 */
fun createRadialGradient() =
    svg {
        width(200)
        height(200)

        defs {
            radialGradient {
                id("radial1")
                cx(50)
                cy(50)
                r(50)
                gradientUnits("userSpaceOnUse")

                stop {
                    offset("0%")
                    stopColor("white")
                }
                stop {
                    offset("100%")
                    stopColor("blue")
                }
            }
        }

        circle {
            cx(100)
            cy(100)
            r(80)
            fill("url(#radial1)")
        }
    }

/**
 * Creates a clipped shape
 */
fun createClippedShape() =
    svg {
        width(200)
        height(200)

        defs {
            clipPath {
                id("clip1")
                circle {
                    cx(100)
                    cy(100)
                    r(80)
                }
            }
        }

        rect {
            x(20)
            y(20)
            width(160)
            height(160)
            fill("coral")
            clipPath("url(#clip1)")
        }
    }
