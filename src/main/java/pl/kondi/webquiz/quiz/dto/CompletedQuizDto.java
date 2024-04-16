package pl.kondi.webquiz.quiz.dto;

import java.time.LocalDateTime;

public record CompletedQuizDto(Long id, LocalDateTime completedAt) {

}
