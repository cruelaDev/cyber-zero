package org.example.cyberzero.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cyberzero.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleUz;
    private String titleRu;

    private String contentUz;
    private String contentRu;

    private String summaryUz;
    private String summaryRu;

    private String status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @CreatedDate
    private LocalDateTime publishedDate;

    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;
}
