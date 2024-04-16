package pl.kondi.webquiz.quiz.dto;


import java.util.List;

public record QuizRequestDto(String title, String text, List<String> options, List<Integer> answer) {
}
