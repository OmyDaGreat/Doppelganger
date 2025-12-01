package xyz.malefic.doppelganger

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

/**
 * Simplified Kobweb example page that can be tested standalone
 * This version uses minimal Kobweb features for easier testing
 */
@Composable
fun SimpleKobwebExamples() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.px)
            .backgroundColor(Color("#F5F5F5")),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Div(
            attrs = {
                style {
                    fontSize(32.px)
                    fontWeight("bold")
                    marginBottom(32.px)
                    textAlign("center")
                }
            }
        ) {
            Text("Doppelganger Kobweb Examples")
        }

        // Example 1: Simple Circle
        SimpleExample("Simple Circle") {
            SvgImage(modifier = Modifier.size(120.px)) {
                width(100)
                height(100)
                viewBox(0, 0, 100, 100)

                circle {
                    cx(50)
                    cy(50)
                    r(40)
                    fill("steelblue")
                    stroke("navy")
                    strokeWidth(2)
                }
            }
        }

        // Example 2: Star
        SimpleExample("Star Shape") {
            SvgImage(modifier = Modifier.size(120.px)) {
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

        // Example 3: Gradient
        SimpleExample("Linear Gradient") {
            SvgImage(modifier = Modifier.width(200.px).height(100.px)) {
                width(200)
                height(100)

                defs {
                    linearGradient {
                        id("testGrad")
                        x1(0)
                        y1(0)
                        x2(100)
                        y2(0)
                        gradientUnits("userSpaceOnUse")

                        stop {
                            offset("0%")
                            stopColor("#FF6B6B")
                        }
                        stop {
                            offset("100%")
                            stopColor("#4ECDC4")
                        }
                    }
                }

                rect {
                    width(200)
                    height(100)
                    fill("url(#testGrad)")
                    rx(8)
                }
            }
        }

        // Example 4: Text
        SimpleExample("Styled Text") {
            SvgImage(modifier = Modifier.width(250.px).height(80.px)) {
                width(250)
                height(80)

                text {
                    x(125)
                    y(45)
                    textAnchor("middle")
                    fontSize(28)
                    fontWeight("bold")
                    fill("#2C3E50")
                    content("Hello Kobweb!")
                }
            }
        }

        // Example 5: Complex composition
        SimpleExample("Complex Shape") {
            SvgImage(modifier = Modifier.size(200.px)) {
                width(200)
                height(200)
                viewBox(0, 0, 200, 200)

                defs {
                    radialGradient {
                        id("radTest")
                        cx(100)
                        cy(100)
                        r(80)
                        gradientUnits("userSpaceOnUse")

                        stop {
                            offset("0%")
                            stopColor("#FEE140")
                        }
                        stop {
                            offset("100%")
                            stopColor("#FA709A")
                        }
                    }
                }

                circle {
                    cx(100)
                    cy(100)
                    r(90)
                    fill("url(#radTest)")
                }

                g {
                    transform("translate(100, 100)")
                    
                    for (i in 0..5) {
                        line {
                            x1(0)
                            y1(0)
                            x2(60)
                            y2(0)
                            stroke("white")
                            strokeWidth(3)
                            transform("rotate(${i * 60})")
                        }
                    }
                }

                circle {
                    cx(100)
                    cy(100)
                    r(15)
                    fill("white")
                }
            }
        }
    }
}

@Composable
fun SimpleExample(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 24.px)
            .fillMaxWidth()
            .maxWidth(600.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        H2(
            attrs = {
                style {
                    fontSize(20.px)
                    marginBottom(12.px)
                    color(Color("#2C3E50"))
                }
            }
        ) {
            Text(title)
        }

        Box(
            modifier = Modifier
                .backgroundColor(Color.white)
                .padding(16.px)
                .borderRadius(8.px),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
