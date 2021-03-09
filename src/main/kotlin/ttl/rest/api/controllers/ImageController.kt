package ttl.rest.api.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ttl.rest.api.services.ImageService

@RestController
class ImageController(var imageService: ImageService)
{
    @PostMapping("upload-image")
    fun uploadFile(@RequestParam(name = "file") fileRequest: MultipartFile): ResponseEntity<ByteArray>
    {
        val uploadFileName: String = fileRequest.originalFilename.toString()
        val mediaType : String = fileRequest.contentType.toString()
        val pixelatedImageByteArray: ByteArray = imageService.pixelateImage(fileRequest)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(mediaType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$uploadFileName\"")
            .body(pixelatedImageByteArray)
    }
}
