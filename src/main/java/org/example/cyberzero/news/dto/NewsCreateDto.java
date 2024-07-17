package org.example.cyberzero.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCreateDto {
    private UUID authorId;
    private String titleUz;
    private String titleRu;

    private String contentUz;
    private String contentRu;

    private String summaryUz;
    private String summaryRu;

    private String status;
}
