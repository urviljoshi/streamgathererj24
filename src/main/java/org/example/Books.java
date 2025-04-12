package org.example;

import java.time.LocalDateTime;

public record Books(
        Long id,
        String title,
        String author,
        String summary,
        String category,
        LocalDateTime publishedDate
) {}