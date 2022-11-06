package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
public class AnswerRepositoryTest {
    private final long A1_ID = 1L;
    private final long A2_ID = 2L;

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("저장한 것 조회하여 userId 확인")
    public void saveTest() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        Answer actualA1 = answerRepository.findByIdAndDeletedFalse(A1_ID).get();
        Answer actualA2 = answerRepository.findByIdAndDeletedFalse(A2_ID).get();

        assertAll(
            () -> assertThat(actualA1.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
            () -> assertThat(actualA2.getWriterId()).isEqualTo(AnswerTest.A2.getWriterId())
        );
    }

    @Test
    @DisplayName("Writer 비어있으면 에러 UnAuthorizedException")
    public void writerNullErrorTest() {
        assertThrows(UnAuthorizedException.class, () ->
            new Answer(null, null, QuestionTest.Q1, "Answers Contents1"));
    }

    @Test
    @DisplayName("Question 비어있으면 에러 NotFoundException")
    public void questionNullErrorTest() {
        assertThrows(NotFoundException.class, () ->
            new Answer(UserTest.JAVAJIGI, null, "Answers Contents1"));
    }

    @Test
    @DisplayName("저장한 것 삭제하여 확인")
    public void deleteTest() {
        User user =  new User(1L, "raccoon", "password", "name", "raccoon@naver.com");
        Question question = new Question("바보같은 질문 제목", "바보같은 질문 내용").writeBy(user);
        Answer answer = new Answer(user, question, "바보같은 질문의 답변 내용");

        answerRepository.save(answer);

        Answer actual1 = answerRepository.findByIdAndDeletedFalse(1L).orElse(null);
        assertThat(actual1).isNotNull();

        answer.setDeleted(true);
        Answer actual2 = answerRepository.findByIdAndDeletedFalse(1L).orElse(null);
        assertThat(actual2).isNull();
    }

}
