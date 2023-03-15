package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
//@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Transactional
    public void joinV1(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);


        log.info("memberRepository 시작 = {}", memberRepository);
        memberRepository.save(member);
        log.info("memberRepository 종료 = {}", memberRepository);

        log.info("logMessage 시작= {}", logMessage);
        logRepository.save(logMessage);
        log.info("logMessage 종료= {}", logMessage);
    }
    @Transactional
    public void joinV2(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);


        log.info("memberRepository 시작 = {}", memberRepository);
        memberRepository.save(member);
        log.info("memberRepository 종료 = {}", memberRepository);

        log.info("logMessage 시작= {}", logMessage);
        try {
            logRepository.save(logMessage);
        } catch (RuntimeException e) {
            log.info("log저장에 실패했습니다. logMessage = {}", logMessage);
            log.info("정상흐름 반환");
        }
        log.info("logMessage 종료= {}", logMessage);
    }
}
