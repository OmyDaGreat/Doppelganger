package xyz.malefic.doppelganger

import kotlin.test.Test
import kotlin.test.assertTrue

class SvgDslTest {
    @Test
    fun testBasicSvgCreation() {
        val svgElement =
            svg {
                width(100)
                height(100)
            }

        val output = svgElement.render()
        assertTrue(output.contains("width=\"100\""))
        assertTrue(output.contains("height=\"100\""))
    }

    @Test
    fun testRectangleCreation() {
        val svgElement =
            svg {
                width(200)
                height(200)
                rect {
                    x(10)
                    y(10)
                    width(50)
                    height(50)
                    fill("red")
                }
            }

        val output = svgElement.render()
        assertTrue(output.contains("<rect"))
        assertTrue(output.contains("x=\"10\""))
        assertTrue(output.contains("fill=\"red\""))
    }

    @Test
    fun testCircleCreation() {
        val svgElement =
            svg {
                circle {
                    cx(50)
                    cy(50)
                    r(25)
                    fill("blue")
                    stroke("black")
                    strokeWidth(2)
                }
            }

        val output = svgElement.render()
        assertTrue(output.contains("<circle"))
        assertTrue(output.contains("cx=\"50\""))
        assertTrue(output.contains("r=\"25\""))
        assertTrue(output.contains("fill=\"blue\""))
    }

    @Test
    fun testPathCreation() {
        val svgElement =
            svg {
                path {
                    moveTo(10, 10)
                    lineTo(90, 10)
                    lineTo(50, 90)
                    closePath()
                    fill("green")
                }
            }

        val output = svgElement.render()
        assertTrue(output.contains("<path"))
        assertTrue(output.contains("d=\"M 10 10 L 90 10 L 50 90 Z\""))
    }

    @Test
    fun testGroupCreation() {
        val svgElement =
            svg {
                g {
                    transform("translate(50, 50)")
                    circle {
                        r(20)
                        fill("red")
                    }
                    circle {
                        cx(30)
                        r(15)
                        fill("blue")
                    }
                }
            }

        val output = svgElement.render()
        assertTrue(output.contains("<g"))
        assertTrue(output.contains("transform=\"translate(50, 50)\""))
        assertTrue(output.contains("fill=\"red\""))
        assertTrue(output.contains("fill=\"blue\""))
    }

    @Test
    fun testTextCreation() {
        val svgElement =
            svg {
                text {
                    x(10)
                    y(20)
                    fontSize(16)
                    fill("black")
                    content("Hello SVG!")
                }
            }

        val output = svgElement.render()
        assertTrue(output.contains("<text"))
        assertTrue(output.contains("Hello SVG!"))
        assertTrue(output.contains("font-size=\"16\""))
    }

    @Test
    fun testLinearGradient() {
        val svgElement =
            svg {
                defs {
                    linearGradient {
                        id("gradient1")
                        x1(0)
                        y1(0)
                        x2(100)
                        y2(0)
                        stop {
                            offset(0)
                            stopColor("red")
                        }
                        stop {
                            offset(100)
                            stopColor("blue")
                        }
                    }
                }
                rect {
                    width(100)
                    height(100)
                    fill("url(#gradient1)")
                }
            }

        val output = svgElement.render()
        assertTrue(output.contains("<linearGradient"))
        assertTrue(output.contains("id=\"gradient1\""))
        assertTrue(output.contains("<stop"))
    }

    @Test
    fun testPolylineWithPoints() {
        val svgElement =
            svg {
                polyline {
                    points(10 to 10, 20 to 30, 30 to 10, 40 to 30)
                    stroke("black")
                    fill("none")
                }
            }

        val output = svgElement.render()
        assertTrue(output.contains("<polyline"))
        assertTrue(output.contains("points="))
    }

    @Test
    fun testViewBox() {
        val svgElement =
            svg {
                viewBox(0, 0, 100, 100)
                preserveAspectRatio("xMidYMid meet")
            }

        val output = svgElement.render()
        assertTrue(output.contains("viewBox=\"0 0 100 100\""))
        assertTrue(output.contains("preserveAspectRatio"))
    }

    @Test
    fun testComplexPath() {
        val svgElement =
            svg {
                path {
                    moveTo(10, 10)
                    curveTo(20, 20, 40, 20, 50, 10)
                    smoothCurveTo(70, 20, 80, 10)
                    lineTo(80, 40)
                    closePath()
                    fill("purple")
                    stroke("black")
                }
            }

        val output = svgElement.render()
        assertTrue(output.contains("M 10 10"))
        assertTrue(output.contains("C"))
        assertTrue(output.contains("S"))
    }
}
