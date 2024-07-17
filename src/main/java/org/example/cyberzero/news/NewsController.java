package org.example.cyberzero.news;

import lombok.RequiredArgsConstructor;
import org.example.cyberzero.news.dto.NewsCreateDto;
import org.example.cyberzero.news.dto.NewsDto;
import org.example.cyberzero.news.dto.NewsResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    ResponseEntity<NewsResponseDto> create(@RequestBody NewsCreateDto createDto) {
        NewsResponseDto newsResponseDto = newsService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newsResponseDto);
    }

    @GetMapping
    ResponseEntity<List<NewsDto>> getAllNews(@RequestHeader(value = "Accept-Language", defaultValue = "uz") String acceptLanguage){
        List<NewsDto> responseDtos = newsService.getAllNews(acceptLanguage);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    ResponseEntity<NewsDto> getNews(@RequestHeader(value = "Accept-Language", defaultValue = "uz") String acceptLanguage, @PathVariable Long id){
        NewsDto responseDto = newsService.getNews(acceptLanguage, id);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("{id}")
    ResponseEntity<NewsDto> deleteNews(@PathVariable Long id){
        newsService.deleteNews(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
