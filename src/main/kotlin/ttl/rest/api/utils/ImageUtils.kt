package ttl.rest.api.utils

import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.Comparator
import java.util.HashMap

@Component
class ImageUtils
{
    fun pixelate(imageToPixelate: BufferedImage, pixelSize: Int): BufferedImage?
    {
        val pixelateImage = BufferedImage(
            imageToPixelate.width,
            imageToPixelate.height,
            imageToPixelate.type
        )
        var y = 0
        while (y < imageToPixelate.height)
        {
            var x = 0
            while (x < imageToPixelate.width)
            {
                val croppedImage = getCroppedImage(imageToPixelate, x, y, pixelSize, pixelSize)
                val dominantColor = getDominantColor(croppedImage)
                var yd = y
                while (yd < y + pixelSize && yd < pixelateImage.height)
                {
                    var xd = x
                    while (xd < x + pixelSize && xd < pixelateImage.width)
                    {
                        pixelateImage.setRGB(xd, yd, dominantColor.rgb)
                        xd++
                    }
                    yd++
                }
                x += pixelSize
            }
            y += pixelSize
        }

        return pixelateImage
    }

    fun getCroppedImage(image: BufferedImage, startx: Int, starty: Int, width: Int, height: Int): BufferedImage
    {
        var startx = startx
        var starty = starty
        var width = width
        var height = height
        if (startx < 0) startx = 0
        if (starty < 0) starty = 0
        if (startx > image.width) startx = image.width
        if (starty > image.height) starty = image.height
        if (startx + width > image.width) width = image.width - startx
        if (starty + height > image.height) height = image.height - starty

        return image.getSubimage(startx, starty, width, height)
    }

    fun getDominantColor(image: BufferedImage): Color
    {
        val colorCounter: MutableMap<Int, Int> = HashMap(100)
        for (x in 0 until image.width)
        {
            for (y in 0 until image.height)
            {
                val currentRGB = image.getRGB(x, y)
                val count = colorCounter.getOrDefault(currentRGB, 0)
                colorCounter[currentRGB] = count + 1
            }
        }

        return getDominantColor(colorCounter)
    }

    private fun getDominantColor(colorCounter: Map<Int, Int>): Color
    {
        val dominantRGB = colorCounter.entries.stream()
            .max(EntryComparator())
            .get()
            .key

        return Color(dominantRGB)
    }
}

@SuppressWarnings("rawtypes")
internal class EntryComparator : Comparator<Any>
{
    override fun compare(o1: Any, o2: Any): Int
    {
        val entry1 = o1 as Map.Entry<Int, Int>
        val entry2 = o2 as Map.Entry<Int, Int>

        return if (entry1.value > entry2.value) 1 else -1
    }
}