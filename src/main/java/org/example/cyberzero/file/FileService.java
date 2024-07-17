package org.example.cyberzero.file;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.cyberzero.exceptions.CustomFileUploadException;
import org.example.cyberzero.file.dto.FileResponseDto;
import org.example.cyberzero.file.entity.MyFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.storage.path}")
    private String fileStoragePath;

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    @Transactional
    public FileResponseDto uploadFile(MultipartFile file) throws CustomFileUploadException {
        MyFile myfile = fileMapper.toFile(file);
        MyFile savedFile = fileRepository.save(myfile);

        try {
            Path uploadPath = Paths.get(fileStoragePath);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(savedFile.getGeneratedName());
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new CustomFileUploadException("Failed to upload file", e);
        }

        return fileMapper.toResponse(myfile);
    }

    public ResponseEntity<Resource> downloadFile(String filename) {
        try {
            Path filePath = Paths.get(fileStoragePath).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType = Files.probeContentType(filePath);

                return ResponseEntity.ok()
                        .contentType(contentType == null ? org.springframework.http.MediaType.APPLICATION_OCTET_STREAM : org.springframework.http.MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IOException ex) {
            return ResponseEntity.status(500).build();
        }
    }

    public boolean deleteFile(String filename) {
        try {
            Path filePath = Paths.get(fileStoragePath).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                fileRepository.deleteByFilename(filename);

                boolean isDeleted = Files.deleteIfExists(filePath);
                if (isDeleted) {
                    System.out.println("File deleted successfully: " + filename);
                    return true;
                } else {
                    System.out.println("Failed to delete the file: " + filename);
                    return false;
                }
            } else {
                System.out.println("File does not exist: " + filename);
                return false;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IOException during file deletion: " + e.getMessage(), e);
        }
    }
}