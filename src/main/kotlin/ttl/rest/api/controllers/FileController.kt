package ttl.rest.api.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
public class FileController {

    @PostMapping("uploadfile")
    fun uploadFile(@RequestParam(name = "file") file: MultipartFile): ResponseEntity<ByteArray> {

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(file.contentType.toString()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.name + "\"")
            .body(file.bytes)
    }
}



