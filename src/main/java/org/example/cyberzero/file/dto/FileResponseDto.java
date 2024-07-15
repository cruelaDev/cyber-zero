package org.example.cyberzero.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NonNull
public class FileResponseDto {
    private UUID id;
    private String name;
    private String generatedName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
