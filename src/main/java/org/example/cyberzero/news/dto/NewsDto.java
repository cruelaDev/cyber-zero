package org.example.cyberzero.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cyberzero.user.dto.UserResponseDto;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String status;
    private UserResponseDto author;
    private LocalDateTime publishedDate;
    private LocalDateTime lastUpdatedDate;
}
