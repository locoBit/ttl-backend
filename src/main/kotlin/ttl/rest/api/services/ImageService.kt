package ttl.rest.api.services

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ttl.rest.api.utils.ImageUtils
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

private const val PIXEL_SIZE : Int = 5;

@Service
class ImageService(var imageUtils: ImageUtils)
{

    fun pixelateImage(fileRequest: MultipartFile) : ByteArray
    {
        val fileExtension: String = fileRequest.originalFilename.toString().split("\\.")[0]
        val uploadFileInputStream: InputStream = fileRequest.inputStream
        val byteArrayOutputStream = ByteArrayOutputStream()
        val image : BufferedImage = ImageIO.read(uploadFileInputStream)
        val pixelatedImage: BufferedImage? = imageUtils.pixelate(image, PIXEL_SIZE)

        ImageIO.write(pixelatedImage, fileExtension, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
    }
}