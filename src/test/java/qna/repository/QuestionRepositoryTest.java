package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    public QuestionRepository questionRepository;

    private final long Q1_ID = 1L;
    private final long Q2_ID = 2L;

    @Test
    @DisplayName("저장한 것 조회하여 userId 확인")
    public void saveTest() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);

        Question actualQ1 = questionRepository.findByIdAndDeletedFalse(Q1_ID).get();
        Question actualQ2 = questionRepository.findByIdAndDeletedFalse(Q2_ID).get();

        assertAll(
            () -> assertThat(actualQ1.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId()),
            () -> assertThat(actualQ2.getWriterId()).isEqualTo(QuestionTest.Q2.getWriterId())
        );
    }

    @Test
    @DisplayName("저장한 것 삭제하여 확인")
    public void deleteTest() {
        User user =  new User(1L, "raccoon", "password", "name", "raccoon@naver.com");
        Question question = new Question("바보같은 질문 제목", "바보같은 질문 내용").writeBy(user);

        questionRepository.save(question);

        Question actual1 = questionRepository.findByIdAndDeletedFalse(1L).get();
        assertThat(actual1).isNotNull();

        question.setDeleted(true);
        List<Question> actual2 = questionRepository.findByDeletedFalse();
        assertThat(actual2).isEmpty();

        Question actual3 = questionRepository.findByIdAndDeletedFalse(1L).orElse(null);
        assertThat(actual3).isNull();
    }
}
