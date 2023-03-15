package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberJpaServiceTest {

    @Autowired
    MemberJpaService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;

    @Test
    void 전체트랜잭션1개_성공() {
        //given
        String username = "전체트랜잭션1개_성공";

        //when
        memberService.전체트랜잭션1개_성공(username);

        //then : 모든데이터가 정상 저장된다.
        assertThat(memberRepository.find(username)).isNotEmpty();
        assertThat(logRepository.find(username)).isNotEmpty();
    }

    @Test
    void 전체트랜잭션1개_전체실패() {
        //given
        String username = "로그예외_전체트랜잭션1개_전체실패";

        //when
        assertThatThrownBy(()-> memberService.전체트랜잭션1개_전체실패(username)).isInstanceOf(RuntimeException.class);

        //then : 로그 저장 실패호 전체 롤백된다
        assertThat(memberRepository.find(username)).isEmpty();
        assertThat(logRepository.find(username)).isEmpty();
    }

    @Test
    void 전체트랜잭션2개_회원만성공() {
        //given
        String username = "로그예외_전체트랜잭션2개_회원만성공";

        //when
        memberService.전체트랜잭션2개_회원만성공(username);

        //then : 로그 저장실패 회원만 저장된다.
        assertThat(memberRepository.find(username)).isNotEmpty();
        assertThat(logRepository.find(username)).isEmpty();
    }

    @Test
    void 전체트랜잭션2개_모두실패() {
        //given
        String username = "로그예외_전체트랜잭션2개_모두실패";

        //when
        assertThatThrownBy(()-> memberService.전체트랜잭션2개_모두실패(username)).isInstanceOf( RuntimeException.class);

        //then : runtimeException가 별도 처리되지않아 모두 롤백처리된다.
        assertThat(memberRepository.find(username)).isEmpty();
        assertThat(logRepository.find(username)).isEmpty();
    }

}