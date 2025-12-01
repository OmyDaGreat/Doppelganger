package xyz.malefic.doppelganger

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/**
 * Main Kobweb page showcasing all SVG examples
 * This can be used as a standalone page or integrated into a Kobweb site
 *
 * To test: Set up a minimal Kobweb project and use this as a page component
 */
@Composable
fun SvgExamplesPage() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .minHeight(100.vh)
                .padding(24.px)
                .backgroundColor(Color("#F5F5F5")),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Title
        H1 {
            Text("Doppelganger SVG Examples - Kobweb")
        }

        Box(modifier = Modifier.height(32.px))

        // Simple Circle
        KobwebExampleSection(
            title = "Simple Circle",
            description = "A basic circle with fill and stroke",
        ) {
            SvgImage(
                modifier = Modifier.size(120.px),
            ) {
                width(100)
                height(100)
                viewBox(0, 0, 100, 100)

                circle {
                    cx(50)
                    cy(50)
                    r(40)
                    fill(androidx.compose.ui.graphics.Color.Blue)
                    stroke(androidx.compose.ui.graphics.Color.Black)
                    strokeWidth(2)
                }
            }
        }

        // Gradient Rectangle
        KobwebExampleSection(
            title = "Gradient Rectangle",
            description = "Rectangle with linear gradient from yellow to red",
        ) {
            SvgImage(
                modifier = Modifier.width(240.px).height(120.px),
            ) {
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
        }

        // Star
        KobwebExampleSection(
            title = "Star Shape",
            description = "A star created using path commands",
        ) {
            SvgImage(
                modifier = Modifier.size(120.px),
            ) {
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
        }

        // Smiley Face
        KobwebExampleSection(
            title = "Smiley Face",
            description = "Multiple shapes composed together",
        ) {
            SvgImage(
                modifier = Modifier.size(240.px),
            ) {
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
                    fill(androidx.compose.ui.graphics.Color.Black)
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
        }

        // Grouped Shapes
        KobwebExampleSection(
            title = "Grouped Shapes",
            description = "Rectangle, circle, and triangle in a group",
        ) {
            SvgImage(
                modifier = Modifier.width(360.px).height(240.px),
            ) {
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
        }

        // Styled Text
        KobwebExampleSection(
            title = "Styled Text",
            description = "Text elements with different fonts and styles",
        ) {
            SvgImage(
                modifier = Modifier.width(360.px).height(120.px),
            ) {
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
        }

        // Curvy Path
        KobwebExampleSection(
            title = "Curvy Path",
            description = "Smooth curves using Bézier curve commands",
        ) {
            SvgImage(
                modifier = Modifier.width(360.px).height(240.px),
            ) {
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
        }

        // Radial Gradient
        KobwebExampleSection(
            title = "Radial Gradient",
            description = "Circle with radial gradient from white to blue",
        ) {
            SvgImage(
                modifier = Modifier.size(240.px),
            ) {
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
        }

        // Clipped Shape
        KobwebExampleSection(
            title = "Clipped Shape",
            description = "Rectangle clipped by a circular clipPath",
        ) {
            SvgImage(
                modifier = Modifier.size(240.px),
            ) {
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
        }

        // Complex Example
        KobwebExampleSection(
            title = "Complex Composition",
            description = "Multiple gradients, groups, and transforms",
        ) {
            SvgImage(
                modifier = Modifier.size(300.px),
            ) {
                width(300)
                height(300)
                viewBox(0, 0, 300, 300)

                defs {
                    linearGradient {
                        id("complexGrad1")
                        x1(0)
                        y1(0)
                        x2(100)
                        y2(100)
                        gradientUnits("userSpaceOnUse")

                        stop {
                            offset("0%")
                            stopColor("#FF6B6B")
                        }
                        stop {
                            offset("50%")
                            stopColor("#4ECDC4")
                        }
                        stop {
                            offset("100%")
                            stopColor("#45B7D1")
                        }
                    }

                    radialGradient {
                        id("complexGrad2")
                        cx(150)
                        cy(150)
                        r(100)
                        gradientUnits("userSpaceOnUse")

                        stop {
                            offset("0%")
                            stopColor("#FEE140")
                            stopOpacity(0.8)
                        }
                        stop {
                            offset("100%")
                            stopColor("#FA709A")
                            stopOpacity(0.8)
                        }
                    }
                }

                // Background circle
                circle {
                    cx(150)
                    cy(150)
                    r(140)
                    fill("url(#complexGrad1)")
                    opacity(0.3)
                }

                // Central group with transform
                g {
                    transform("translate(150, 150)")

                    // Rotating squares effect
                    for (i in 0..7) {
                        rect {
                            x(-40)
                            y(-40)
                            width(80)
                            height(80)
                            fill("none")
                            stroke("url(#complexGrad2)")
                            strokeWidth(2)
                            transform("rotate(${i * 45})")
                            opacity(0.6)
                        }
                    }

                    // Center circle
                    circle {
                        cx(0)
                        cy(0)
                        r(50)
                        fill("url(#complexGrad2)")
                    }

                    // Center text
                    text {
                        x(0)
                        y(8)
                        textAnchor("middle")
                        fontSize(20)
                        fontWeight("bold")
                        fill("white")
                        content("SVG")
                    }
                }

                // Decorative paths
                path {
                    moveTo(50, 150)
                    curveTo(50, 100, 100, 50, 150, 50)
                    curveTo(200, 50, 250, 100, 250, 150)
                    fill("none")
                    stroke("#ffffff")
                    strokeWidth(3)
                    strokeDasharray("5,5")
                    opacity(0.5)
                }
            }
        }

        Box(modifier = Modifier.height(32.px))
    }
}

@Composable
fun KobwebExampleSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .maxWidth(800.px)
                .padding(bottom = 32.px),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.padding(bottom = 8.px),
        ) {
            H3 {
                Text(title)
            }
        }

        Box(
            modifier =
                Modifier
                    .padding(bottom = 16.px),
        ) {
            P(
                attrs = {
                    style {
                        color(Color("#666666"))
                        fontSize(14.px)
                        textAlign("center")
                    }
                },
            ) {
                Text(description)
            }
        }

        Box(
            modifier =
                Modifier
                    .backgroundColor(Color.white)
                    .padding(16.px)
                    .borderRadius(8.px)
                    .boxShadow(0.px, 2.px, 4.px, 0.px, Color("#00000010")),
        ) {
            content()
        }
    }
}
