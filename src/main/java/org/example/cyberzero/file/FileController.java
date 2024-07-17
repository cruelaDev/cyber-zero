package org.example.cyberzero.file;

import lombok.RequiredArgsConstructor;
import org.example.cyberzero.exceptions.CustomFileUploadException;
import org.example.cyberzero.file.dto.FileResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public FileResponseDto uploadFile(@RequestParam("file") MultipartFile file) throws CustomFileUploadException {
        return fileService.uploadFile(file);
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        return fileService.downloadFile(filename);
    }

    @DeleteMapping("/delete/{filename:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        boolean isDeleted = fileService.deleteFile(filename);

        if (isDeleted) {
            return ResponseEntity.ok().body("File deleted successfully: " + filename);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + filename);
        }
    }
}
