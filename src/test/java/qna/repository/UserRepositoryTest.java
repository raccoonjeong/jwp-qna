package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.UnAuthorizedException;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    public UserRepository userRepository;

    private final String U1_USER_ID = "javajigi";
    private final String U2_USER_ID = "sanjigi";


    @Test
    @DisplayName("저장한 것 조회하여 userId 확인")
    public void saveTest() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);

        User actualU1 = userRepository.findByUserId(U1_USER_ID).get();
        User actualU2 = userRepository.findByUserId(U2_USER_ID).get();

        assertAll(
            () -> assertThat(actualU1.getId()).isEqualTo(UserTest.JAVAJIGI.getId()),
            () -> assertThat(actualU2.getId()).isEqualTo(UserTest.SANJIGI.getId())
        );
    }
    //        if (!matchUserId(loginUser.userId)) {
    //            throw new UnAuthorizedException();
    //        }
    //
    //        if (!matchPassword(target.password)) {
    //            throw new UnAuthorizedException();
    //        }
    @Test
    @DisplayName("로그인 에러 테스트")
    public void loginErrorTest() {
        User user1 =  new User(1L, "raccoon", "password", "name", "raccoon@naver.com");
        User user2 =  new User(1L, "raccoon", "word", "mola", "raccoon@naver.com");
        User user3 =  new User(1L, "laccoon", "password", "mola", "raccoon@naver.com");
        assertThrows(UnAuthorizedException.class,
            () -> user1.update(user1, user2));
        assertThrows(UnAuthorizedException.class,
            () -> user1.update(user3, user2));
    }

    @Test
    @DisplayName("수정 테스트")
    public void updateTest() {
        User user =
            userRepository.save(new User(1L, "raccoon", "password", "name", "raccoon@naver.com"));

        String emailToChange = "qqqqq@naver.com";
        user.changeEmail(emailToChange);
        User actual = userRepository.findByUserId("raccoon").get();
        assertThat(actual.getEmail()).isEqualTo(emailToChange);
    }
}
