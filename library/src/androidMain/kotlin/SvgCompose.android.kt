package xyz.malefic.doppelganger

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.caverock.androidsvg.SVG
import android.graphics.Canvas as AndroidCanvas

@Composable
actual fun RawSvg(
    svgString: String,
    modifier: Modifier,
    contentDescription: String?,
) {
    val painter =
        remember(svgString) {
            try {
                val svg = SVG.getFromString(svgString)
                val width = (svg.documentWidth.takeIf { it > 0 } ?: 512f).toInt()
                val height = (svg.documentHeight.takeIf { it > 0 } ?: 512f).toInt()

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = AndroidCanvas(bitmap)
                svg.renderToCanvas(canvas)

                BitmapPainter(bitmap.asImageBitmap())
            } catch (e: Exception) {
                null
            }
        }

    if (painter != null) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    } else {
        Box(modifier = modifier)
    }
}
