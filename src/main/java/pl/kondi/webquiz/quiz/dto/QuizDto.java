package pl.kondi.webquiz.quiz.dto;

import java.util.List;

public record QuizDto(Long id, String title, String text, List<String> options) {
}
