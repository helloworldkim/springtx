package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
//@Transactional
@RequiredArgsConstructor
public class MemberJpaService {

    private final MemberJpaRepository memberJpaRepository;
    private final LogJpaRepository logJpaRepository;
    private final LogJapService logJpaService;

    @Transactional
    public void 전체트랜잭션1개_성공(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("memberRepository 시작 = {}", member);
        memberJpaRepository.save(member);
        log.info("memberJpaRepository 종료 = {}", member);

        log.info("logJpaRepository 시작= {}", logMessage);
        logJpaRepository.save(logMessage);
        log.info("logJpaRepository 종료= {}", logMessage);
    }

    @Transactional
    public void 전체트랜잭션1개_전체실패(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);


        log.info("memberRepository 시작 = {}", member);
        memberJpaRepository.save(member);
        log.info("memberJpaRepository 종료 = {}", member);

        log.info("logJpaRepository 시작= {}", logMessage);
        try {
            logJpaService.saveWithREQUIRED(logMessage);
        } catch (RuntimeException e) {
            log.info("log저장에 실패했습니다. logJpaRepository = {}", logMessage);
            log.info("정상흐름 반환");
        }
        log.info("logMessage 종료= {}", logMessage);
    }

    @Transactional
    public void 전체트랜잭션2개_회원만성공(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);


        log.info("memberRepository 시작 = {}", member);
        memberJpaRepository.save(member);
        log.info("memberJpaRepository 종료 = {}", member);

        log.info("logJpaRepository 시작= {}", logMessage);
        try {
            logJpaService.saveWithREQUIRES_NEW(logMessage);
        } catch (RuntimeException e) {
            log.info("log저장에 실패했습니다. logJpaRepository = {}", logMessage);
            log.info("정상흐름 반환");
        }
        log.info("logMessage 종료= {}", logMessage);
    }

    @Transactional
    public void 전체트랜잭션2개_모두실패(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);


        log.info("memberRepository 시작 = {}", member);
        memberJpaRepository.save(member);
        log.info("memberJpaRepository 종료 = {}", member);

        log.info("logJpaRepository 시작= {}", logMessage);
        logJpaService.saveWithREQUIRED(logMessage);
        log.info("logMessage 종료= {}", logMessage);
    }
}
