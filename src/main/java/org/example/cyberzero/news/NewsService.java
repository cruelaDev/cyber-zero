package org.example.cyberzero.news;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.cyberzero.exceptions.UserNotFoundException;
import org.example.cyberzero.news.dto.NewsCreateDto;
import org.example.cyberzero.news.dto.NewsDto;
import org.example.cyberzero.news.dto.NewsResponseDto;
import org.example.cyberzero.news.entity.News;
import org.example.cyberzero.user.UserRepository;
import org.example.cyberzero.user.dto.UserResponseDto;
import org.example.cyberzero.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    private final NewsMapper mapper;

    public NewsResponseDto create(NewsCreateDto createDto) {
        News news = mapper.toNews(createDto);

        UUID authorId = createDto.getAuthorId();
        User user = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException("User not found"));
        news.setAuthor(user);

        News saved = newsRepository.save(news);
        return mapper.toResponse(saved);
    }

    public List<NewsDto> getAllNews(String acceptLanguage) {
        return newsRepository.findAll().stream()
                .map(news -> toDto(news, acceptLanguage))
                .collect(Collectors.toList());
    }

    private NewsDto toDto(News news, String acceptLanguage) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(news.getId());
        newsDto.setTitle(getTitleByLanguage(news, acceptLanguage));
        newsDto.setAuthor(new UserResponseDto(news.getAuthor().getId(), news.getAuthor().getFirstname(), news.getAuthor().getLastname(), news.getAuthor().getEmail(), news.getAuthor().getRole(), news.getAuthor().getCreatedAt(), news.getAuthor().getUpdatedAt()));
        newsDto.setContent(getContentByLanguage(news, acceptLanguage));
        newsDto.setSummary(getSummaryByLanguage(news, acceptLanguage));
        newsDto.setStatus(news.getStatus());
        newsDto.setPublishedDate(news.getPublishedDate());
        newsDto.setLastUpdatedDate(news.getLastUpdatedDate());
        return newsDto;
    }

    private String getTitleByLanguage(News news, String acceptLanguage) {
        if ("uz".equalsIgnoreCase(acceptLanguage)) {
            return news.getTitleUz();
        } else {
            return news.getTitleRu();
        }
    }

    private String getContentByLanguage(News news, String acceptLanguage) {
        if ("uz".equalsIgnoreCase(acceptLanguage)) {
            return news.getContentUz();
        } else {
            return news.getContentRu();
        }
    }

    private String getSummaryByLanguage(News news, String acceptLanguage) {
        if ("uz".equalsIgnoreCase(acceptLanguage)) {
            return news.getSummaryUz();
        } else {
            return news.getSummaryRu();
        }
    }


    public NewsDto getNews(String acceptLanguage, Long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("News not found with id: %s".formatted(id)));
        return toDto(news, acceptLanguage);
    }

    public void deleteNews(Long id) {
        newsRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found with id: %s".formatted(id)));
        newsRepository.deleteById(id);
    }
}
