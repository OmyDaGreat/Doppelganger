package xyz.malefic.doppelganger

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM

@Composable
actual fun RawSvg(
    svgString: String,
    modifier: Modifier,
    contentDescription: String?,
) {
    val svgDom =
        remember(svgString) {
            try {
                SVGDOM(Data.makeFromBytes(svgString.encodeToByteArray()))
            } catch (e: Exception) {
                null
            }
        }

    if (svgDom != null) {
        Canvas(modifier = modifier) {
            drawIntoCanvas { canvas ->
                val root = svgDom.root
                if (root != null) {
                    val scaleX = size.width / root.width.value
                    val scaleY = size.height / root.height.value
                    canvas.nativeCanvas.save()
                    canvas.nativeCanvas.scale(scaleX, scaleY)
                    svgDom.render(canvas.nativeCanvas)
                    canvas.nativeCanvas.restore()
                }
            }
        }
    } else {
        Box(modifier = modifier)
    }
}
