package pl.kondi.webquiz.quiz;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.kondi.webquiz.quiz.entity.Quiz;

interface QuizRepository extends PagingAndSortingRepository<Quiz, Long>, CrudRepository<Quiz, Long> {


}
