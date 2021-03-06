package ttl.rest.api.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import javax.imageio.ImageIO

import java.awt.image.BufferedImage
import java.io.*


@RestController
public class FileController {

    @PostMapping("uploadfile")
    fun uploadFile(@RequestParam(name = "file") file: MultipartFile): ResponseEntity<ByteArray> {
        val initialStream: InputStream = file.inputStream
        val uploadFileName: String = file.originalFilename.toString()
        val buffer = ByteArray(initialStream.available())
        initialStream.read(buffer)

        val targetFile = File("/home/locobit/IdeaProjects/ttl/uploads/$uploadFileName")

        FileOutputStream(targetFile).use { outStream -> outStream.write(buffer) }

        val bufferedImage: BufferedImage = ImageIO.read(targetFile)
        val finalHeight = bufferedImage.height / 2
        val finalWidth = bufferedImage.width / 2

        val croppedImage: BufferedImage = bufferedImage.getSubimage(0, 0, finalWidth, finalHeight)

        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(croppedImage, "png", byteArrayOutputStream)
        val croppedImageBytes: ByteArray = byteArrayOutputStream.toByteArray()

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(file.contentType.toString()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$uploadFileName\"")
            .body(croppedImageBytes)
    }
}



