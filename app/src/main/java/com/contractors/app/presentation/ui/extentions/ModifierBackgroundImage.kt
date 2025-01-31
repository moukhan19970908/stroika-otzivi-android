package com.contractors.app.presentation.ui.extentions


import androidx.annotation.FloatRange
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Draws an [ImageBitmap] with the given parameters and clipping shape
 * behind the content.
 *
 * @param painter Painter representing the image to be drawn
 * @param shape desired shape of the background
 * @param alignment Alignment of the image within the layout bounds
 * @param contentScale Strategy for scaling the image if its size does not
 * @param repeat CSS-like background repeat behavior
 * @param alpha Opacity to be applied to [painter], with `0` being
 *     completely transparent and `1` being completely opaque. The value
 *     must be between `0` and `1`.
 * @param colorFilter ColorFilter to apply to the image when drawn
 * @param drawBehind DrawScope to draw behind the image
 * @param drawFront DrawScope to draw in front of the image
 * @author  Ahmed Hosny
 */
@Stable
fun Modifier.backgroundImage(
    painter: Painter,
    shape: Shape = RectangleShape,
    alignment: Alignment = Alignment.TopStart,
    contentScale: ContentScale = ContentScale.None,
    repeat: BackgroundRepeat = BackgroundRepeat.Repeat,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    colorFilter: ColorFilter? = null,
    drawBehind: ContentDrawScope.() -> Unit = {},
    drawFront: ContentDrawScope.() -> Unit = {}
): Modifier = composed {
//    val bitmapPainter = remember(bitmap) { BitmapPainter(bitmap, filterQuality = filterQuality) }
    ImageBackgroundElement(
        painter = painter,
        shape = shape,
        alignment = alignment,
        contentScale = contentScale,
        repeat = repeat,
        alpha = alpha,
        colorFilter = colorFilter,
        drawBehind = drawBehind,
        drawFront = drawFront,
        inspectorInfo = debugInspectorInfo {
            name = "background"
            properties["painter"] = painter
            properties["shape"] = shape
            properties["alignment"] = alignment
            properties["contentScale"] = contentScale
            properties["repeat"] = repeat
            properties["alpha"] = alpha
            properties["colorFilter"] = colorFilter
            properties["drawBehind"] = drawBehind
            properties["drawFront"] = drawFront
        }
    )
}


private class ImageBackgroundElement(
    private val painter: Painter,
    private val shape: Shape,
    private val alignment: Alignment,
    private val contentScale: ContentScale,
    private val repeat: BackgroundRepeat,
    private val alpha: Float,
    private val colorFilter: ColorFilter?,
    private val drawBehind: ContentDrawScope.() -> Unit,
    private val drawFront: ContentDrawScope.() -> Unit,
    private val inspectorInfo: InspectorInfo.() -> Unit
) : ModifierNodeElement<ImageBackgroundNode>() {
    override fun create(): ImageBackgroundNode {
        return ImageBackgroundNode(
            painter,
            shape,
            alignment,
            contentScale,
            repeat,
            alpha,
            colorFilter,
            drawBehind,
            drawFront
        )
    }

    override fun update(node: ImageBackgroundNode) {
        node.painter = painter
        node.shape = shape
        node.alignment = alignment
        node.contentScale = contentScale
        node.repeat = repeat
        node.alpha = alpha
        node.colorFilter = colorFilter
        node.drawBehind = drawBehind
        node.drawFront = drawFront
    }

    override fun InspectorInfo.inspectableProperties() {
        inspectorInfo()
    }

    override fun hashCode(): Int {
        var result = painter.hashCode()
        result = 31 * result + shape.hashCode()
        result = 31 * result + alignment.hashCode()
        result = 31 * result + contentScale.hashCode()
        result = 31 * result + repeat.hashCode()
        result = 31 * result + alpha.hashCode()
        result = 31 * result + (colorFilter?.hashCode() ?: 0)
        result = 31 * result + drawBehind.hashCode()
        result = 31 * result + drawFront.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        val otherModifier = other as? ImageBackgroundElement ?: return false
        return painter == otherModifier.painter &&
                alignment == otherModifier.alignment &&
                contentScale == otherModifier.contentScale &&
                repeat == otherModifier.repeat &&
                shape == otherModifier.shape &&
                alpha == otherModifier.alpha &&
                colorFilter == otherModifier.colorFilter &&
                drawBehind == otherModifier.drawBehind &&
                drawFront == otherModifier.drawFront
    }
}

private class ImageBackgroundNode(
    var painter: Painter,
    var shape: Shape,
    var alignment: Alignment,
    var contentScale: ContentScale,
    var repeat: BackgroundRepeat,
    var alpha: Float,
    var colorFilter: ColorFilter?,
    var drawBehind: ContentDrawScope.() -> Unit,
    var drawFront: ContentDrawScope.() -> Unit
) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        val spaceSize = this.size
        drawIntoCanvas { canvas ->
            // 1. Clip to the destination bounds FIRST
            canvas.save()
            shape.createOutline(spaceSize, layoutDirection, this).apply {
                canvas.clipPath(Path().apply { addRect(spaceSize.toRect()) })
            }

            // 2. Calculate the painter source size
            val intrinsicSize = painter.intrinsicSize
            val srcWidth = if (intrinsicSize.isSpecified) intrinsicSize.width else spaceSize.width
            val srcHeight =
                if (intrinsicSize.isSpecified) intrinsicSize.height else spaceSize.height
            val imageSize = Size(srcWidth, srcHeight)

            drawBehind()

            // 3. Draw the image with repeat
            drawTiledImage(
                drawScope = this,
                painter,
                imageSize = imageSize, // Use original image size for tiling
                spaceSize = spaceSize, // Destination size
                alignment = alignment,
                contentScale = contentScale,
                repeat = repeat,
                layoutDirection = layoutDirection,
                alpha = alpha,
                colorFilter = colorFilter,
            )

            drawFront()

            // 4. Restore canvas state
            canvas.restore()
        }
        drawContent()
    }
}


/**
 * Draws a tiled image on the canvas based on the specified repeat mode,
 * respecting the aspect ratio defined by the contentScale.
 */
private fun drawTiledImage(
    drawScope: DrawScope,
    painter: Painter,
    imageSize: Size,
    spaceSize: Size,
    alignment: Alignment,
    contentScale: ContentScale,
    repeat: BackgroundRepeat,
    layoutDirection: LayoutDirection,
    alpha: Float,
    colorFilter: ColorFilter?,
) {
    val tileDstSize = calculateTileSize(imageSize, spaceSize, contentScale)

    val alignedPosition = alignment.align(
        IntSize(tileDstSize.width.roundToInt(), tileDstSize.height.roundToInt()),
        IntSize(spaceSize.width.roundToInt(), spaceSize.height.roundToInt()),
        layoutDirection
    )

    val floatAlignedPosition = Offset(alignedPosition.x.toFloat(), alignedPosition.y.toFloat())

    if (repeat == BackgroundRepeat.NoRepeat) {
        val dx = floatAlignedPosition.x
        val dy = floatAlignedPosition.y
        drawScope.translate(dx, dy) {
            with(painter) {
                draw(tileDstSize, alpha, colorFilter)
            }
        }
    } else {
        var x = floatAlignedPosition.x
        while (true) {
            var y = floatAlignedPosition.y
            while (y < spaceSize.height) {
                println("Repeat Ltr x: $x y: $y")
                drawScope.translate(x, y) {
                    with(painter) {
                        draw(tileDstSize, alpha, colorFilter)
                    }
                }
                if (repeat == BackgroundRepeat.RepeatX) break
                y += tileDstSize.height.toInt()
            }
            if (repeat == BackgroundRepeat.RepeatY) break

            when (layoutDirection) {
                LayoutDirection.Ltr -> if (x > spaceSize.width) break
                LayoutDirection.Rtl -> if (x < 0) break
            }
            x += if (layoutDirection == LayoutDirection.Ltr) {
                tileDstSize.width.toInt()
            } else {
                -tileDstSize.width.toInt()
            }
        }
    }
}

private fun calculateTileSize(
    imageSize: Size,
    dstSize: Size,
    contentScale: ContentScale
): Size {
    return when (contentScale) {
        ContentScale.Crop -> {
            // Scale to cover the entire area, maintaining the aspect ratio
            val scale = max(dstSize.width / imageSize.width, dstSize.height / imageSize.height)
            Size(imageSize.width * scale, imageSize.height * scale)
        }

        ContentScale.Fit -> {
            // Scale to fit completely within the area, maintaining the aspect ratio
            val scale = min(dstSize.width / imageSize.width, dstSize.height / imageSize.height)
            Size(imageSize.width * scale, imageSize.height * scale)
        }

        ContentScale.FillHeight -> {
            // Scale to match the destination height, maintaining the aspect ratio
            val scale = dstSize.height / imageSize.height
            Size(imageSize.width * scale, dstSize.height)
        }

        ContentScale.FillWidth -> {
            // Scale to match the destination width, maintaining the aspect ratio
            val scale = dstSize.width / imageSize.width
            Size(dstSize.width, imageSize.height * scale)
        }

        ContentScale.Inside -> {
            // If the image is smaller, draw at original size
            // If the image is larger, scale down to fit within the area, maintaining the aspect ratio
            if (imageSize.width <= dstSize.width && imageSize.height <= dstSize.height) {
                imageSize
            } else {
                val scale = min(dstSize.width / imageSize.width, dstSize.height / imageSize.height)
                Size(imageSize.width * scale, imageSize.height * scale)
            }
        }

        ContentScale.None -> {
            // Draw the image at its original size
            imageSize
        }

        ContentScale.FillBounds -> {
            // Scale to fill the entire area, ignoring the aspect ratio (might stretch)
            dstSize
        }

        else -> Size.Zero
    }
}

/** Enum class representing CSS-like background repeat behavior. */
enum class BackgroundRepeat {
    Repeat,
    RepeatX,
    RepeatY,
    NoRepeat
}
