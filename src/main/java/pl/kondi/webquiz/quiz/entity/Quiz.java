package pl.kondi.webquiz.quiz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;

    @Size(min = 2)
    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "options", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "option")
    private List<String> options = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "answers", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "answer")
    private List<Integer> answer = new ArrayList<>();
    @NotNull
    private String author;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<CompletedQuiz> completedQuiz = new ArrayList<>();

    public Quiz() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<CompletedQuiz> getCompletedQuiz() {
        return completedQuiz;
    }

    public void setCompletedQuiz(List<CompletedQuiz> completedQuiz) {
        this.completedQuiz = completedQuiz;
    }

    public boolean isAuthor(Authentication auth) {
        return auth.getName().equals(this.author);
    }

    public boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
