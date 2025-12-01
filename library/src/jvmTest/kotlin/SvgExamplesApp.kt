package xyz.malefic.doppelganger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * Main entry point for the JVM Desktop test application
 * Run this to see all SVG examples in action
 */
fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Doppelganger SVG Examples - Desktop",
        ) {
            MaterialTheme {
                SvgExamplesScreen()
            }
        }
    }

@Composable
fun SvgExamplesScreen() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Doppelganger SVG Examples",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        // Simple Circle
        ExampleSection(
            title = "Simple Circle",
            description = "A basic circle with fill and stroke",
        ) {
            SvgContent(
                svgElement = createSimpleCircle(),
                modifier = Modifier.size(120.dp),
            )
        }

        // Gradient Rectangle
        ExampleSection(
            title = "Gradient Rectangle",
            description = "Rectangle with linear gradient from yellow to red",
        ) {
            SvgContent(
                svgElement = createGradientRect(),
                modifier = Modifier.width(240.dp).height(120.dp),
            )
        }

        // Star
        ExampleSection(
            title = "Star Shape",
            description = "A star created using path commands",
        ) {
            SvgContent(
                svgElement = createStar(),
                modifier = Modifier.size(120.dp),
            )
        }

        // Smiley Face
        ExampleSection(
            title = "Smiley Face",
            description = "Multiple shapes composed together",
        ) {
            SvgContent(
                svgElement = createSmileyFace(),
                modifier = Modifier.size(240.dp),
            )
        }

        // Grouped Shapes
        ExampleSection(
            title = "Grouped Shapes",
            description = "Rectangle, circle, and triangle in a group",
        ) {
            SvgContent(
                svgElement = createGroupedShapes(),
                modifier = Modifier.width(360.dp).height(240.dp),
            )
        }

        // Styled Text
        ExampleSection(
            title = "Styled Text",
            description = "Text elements with different fonts and styles",
        ) {
            SvgContent(
                svgElement = createStyledText(),
                modifier = Modifier.width(360.dp).height(120.dp),
            )
        }

        // Curvy Path
        ExampleSection(
            title = "Curvy Path",
            description = "Smooth curves using Bézier curve commands",
        ) {
            SvgContent(
                svgElement = createCurvyPath(),
                modifier = Modifier.width(360.dp).height(240.dp),
            )
        }

        // Radial Gradient
        ExampleSection(
            title = "Radial Gradient",
            description = "Circle with radial gradient from white to blue",
        ) {
            SvgContent(
                svgElement = createRadialGradient(),
                modifier = Modifier.size(240.dp),
            )
        }

        // Clipped Shape
        ExampleSection(
            title = "Clipped Shape",
            description = "Rectangle clipped by a circular clipPath",
        ) {
            SvgContent(
                svgElement = createClippedShape(),
                modifier = Modifier.size(240.dp),
            )
        }

        // Custom Example: Animated-style SVG
        ExampleSection(
            title = "Complex Composition",
            description = "Multiple gradients, groups, and transforms",
        ) {
            SvgContent(
                svgElement = createComplexExample(),
                modifier = Modifier.size(300.dp),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ExampleSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
            text = description,
            style = MaterialTheme.typography.body2,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        Box(
            modifier =
                Modifier
                    .background(Color.White)
                    .padding(16.dp),
        ) {
            content()
        }
    }
}

/**
 * Creates a complex example demonstrating multiple features
 */
fun createComplexExample() =
    svg {
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
