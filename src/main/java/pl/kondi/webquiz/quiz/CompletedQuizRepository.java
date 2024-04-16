package pl.kondi.webquiz.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.kondi.webquiz.quiz.entity.CompletedQuiz;

interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Long>, CrudRepository<CompletedQuiz, Long> {
    Page<CompletedQuiz> findAllByCompletedByOrderByCompletedAtDesc(String user, Pageable pageable);
}
