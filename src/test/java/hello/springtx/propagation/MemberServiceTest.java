package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired LogRepository logRepository;

    /**
     * memberService    Transactional : off
     * memberRepository Transactional : on
     * logRepository    Transactional : on
     */
    @Test
    @DisplayName("outerTxOff_success")
    void outerTxOff_success() {
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then : 모든데이터가 정상 저장된다.
        assertThat(memberRepository.find(username)).isNotEmpty();
        assertThat(logRepository.find(username)).isNotEmpty();
    }


    /**
     * memberService    Transactional : off
     * memberRepository Transactional : on
     * logRepository    Transactional : on Exception 발생
     */
    @Test
    @DisplayName("outerTxOff_fail")
    void outerTxOff_fail() {
        //given
        String username = "로그예외_outerTxOff_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);


        //then : 모든데이터가 정상 저장된다.
        assertThat(memberRepository.find(username)).isNotEmpty();
        assertThat(logRepository.find(username)).isEmpty();
    }


    /**
     * memberService    Transactional : on
     * memberRepository Transactional : off
     * logRepository    Transactional : off
     */
    @Test
    @DisplayName("singleTx")
    void singleTx() {
        //given
        String username = "singleTx";

        //when
        memberService.joinV1(username);

        //then : 모든데이터가 정상 저장된다.
        assertThat(memberRepository.find(username)).isNotEmpty();
        assertThat(logRepository.find(username)).isNotEmpty();
    }

    /**
     * memberService    Transactional : on
     * memberRepository Transactional : on
     * logRepository    Transactional : on
     */
    @Test
    @DisplayName("outerTxOn_success")
    void outerTxOn_success() {
        //given
        String username = "outerTxOn_success";

        //when
        memberService.joinV1(username);

        //then : 모든데이터가 정상 저장된다.
        assertThat(memberRepository.find(username)).isNotEmpty();
        assertThat(logRepository.find(username)).isNotEmpty();
    }


    /**
     * memberService    Transactional : on
     * memberRepository Transactional : on
     * logRepository    Transactional : on Exception 발생
     */
    @Test
    @DisplayName("outerTxOff_fail")
    void outerTxOn_fail() {
        //given
        String username = "로그예외_outerTxOn_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);


        //then : 모든데이터가 롤백 된다.
        assertThat(memberRepository.find(username)).isEmpty();
        assertThat(logRepository.find(username)).isEmpty();
    }

    /**
     * memberService    Transactional : on
     * memberRepository Transactional : on
     * logRepository    Transactional : on Exception 발생
     */
    @Test
    @DisplayName("recoverException_fail")
    void recoverException_fail() {
        //given
        String username = "로그예외_recoverException_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV2(username)).isInstanceOf(UnexpectedRollbackException.class);


        //then : 모든데이터가 롤백 된다.
        assertThat(memberRepository.find(username)).isEmpty();
        assertThat(logRepository.find(username)).isEmpty();
    }


    /**
     * memberService    Transactional : on
     * memberRepository Transactional : on
     * logRepository    Transactional : on(REQUIRES_NEW) Exception 발생
     */
    @Test
    @DisplayName("recoverException_success")
    void recoverException_success() {
        //given
        String username = "로그예외_recoverException_success";

        //when
        memberService.joinV2(username);


        //then : member 저장, log 롤백
        assertThat(memberRepository.find(username)).isNotEmpty();
        assertThat(logRepository.find(username)).isEmpty();
    }

}