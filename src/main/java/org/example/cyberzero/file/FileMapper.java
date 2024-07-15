package org.example.cyberzero.file;

import org.example.cyberzero.file.dto.FileResponseDto;
import org.example.cyberzero.file.entity.MyFile;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class FileMapper {
    public MyFile toFile(MultipartFile file) {
        long size = file.getSize();
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        String generatedFileName = UUID.randomUUID() + "_" + filename;

        return new MyFile(null, filename, generatedFileName, contentType, size, null, null);
    }

    public abstract FileResponseDto toResponse(MyFile myfile);
}
