package org.example.cyberzero.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cyberzero.user.dto.UserResponseDto;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponseDto {
    private Long id;

    private String titleUz;
    private String titleRu;

    private String contentUz;
    private String contentRu;

    private String summaryUz;
    private String summaryRu;

    private String status;

    private UserResponseDto author;

    private LocalDateTime publishedDate;

    private LocalDateTime lastUpdatedDate;
}
