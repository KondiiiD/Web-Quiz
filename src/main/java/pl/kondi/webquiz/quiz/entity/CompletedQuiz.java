package pl.kondi.webquiz.quiz.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CompletedQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime completedAt;
    private String completedBy;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public CompletedQuiz() {
    }

    public CompletedQuiz(LocalDateTime completedAt, String completedBy, Quiz quiz) {
        this.completedAt = completedAt;
        this.completedBy = completedBy;
        this.quiz = quiz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
