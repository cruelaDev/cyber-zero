package org.example.cyberzero.news;

import org.example.cyberzero.news.dto.NewsCreateDto;
import org.example.cyberzero.news.dto.NewsResponseDto;
import org.example.cyberzero.news.entity.News;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class NewsMapper {
    public abstract News toNews(NewsCreateDto createDto);
    public abstract NewsResponseDto toResponse(News news);
}
