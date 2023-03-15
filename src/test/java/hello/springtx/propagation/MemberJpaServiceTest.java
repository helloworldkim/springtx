package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class MemberJpaServiceTest {

    @Autowired
    MemberJpaService memberJpaService;
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    LogJpaRepository logJpaRepository;

    @Test
    void 전체트랜잭션1개_성공() {
        //given
        String username = "전체트랜잭션1개_성공";

        //when
        memberJpaService.전체트랜잭션1개_성공(username);

        //then : 모든데이터가 정상 저장된다.
        assertThat(memberJpaRepository.findByUsername(username)).isNotEmpty();
        assertThat(logJpaRepository.findByMessage(username)).isNotEmpty();
    }

    @Test
    void 전체트랜잭션1개_전체실패() {
        //given
        String username = "로그예외_전체트랜잭션1개_전체실패";

        //when
        assertThatThrownBy(()-> memberJpaService.전체트랜잭션1개_전체실패(username)).isInstanceOf(RuntimeException.class);

        //then : 로그 저장 실패호 전체 롤백된다
        assertThat(memberJpaRepository.findByUsername(username)).isEmpty();
        assertThat(logJpaRepository.findByMessage(username)).isEmpty();
    }

    @Test
    void 전체트랜잭션2개_회원만성공() {
        //given
        String username = "로그예외_전체트랜잭션2개_회원만성공";

        //when
        memberJpaService.전체트랜잭션2개_회원만성공(username);

        //then : 로그 저장실패 회원만 저장된다.
        assertThat(memberJpaRepository.findByUsername(username)).isNotEmpty();
        assertThat(logJpaRepository.findByMessage(username)).isEmpty();
    }

    @Test
    void 전체트랜잭션2개_모두실패() {
        //given
        String username = "로그예외_전체트랜잭션2개_모두실패";

        //when
        assertThatThrownBy(()-> memberJpaService.전체트랜잭션2개_모두실패(username)).isInstanceOf( RuntimeException.class);

        //then : runtimeException가 별도 처리되지않아 모두 롤백처리된다.
        assertThat(memberJpaRepository.findByUsername(username)).isEmpty();
        assertThat(logJpaRepository.findByMessage(username)).isEmpty();
    }

}