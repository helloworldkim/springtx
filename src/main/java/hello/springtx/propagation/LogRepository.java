package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LogRepository {
    private final EntityManager em;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Log logMessage) {
        log.info("log 저장 ");
        em.persist(logMessage);

        if (logMessage.getMessage().contains("로그예외")) {
            log.info("log 저장시 예외 상황 발생");
            throw new RuntimeException("예외발생");
        }
    }

    @Transactional
    public Optional<Log> find(String logMessage) {
        return em.createQuery("SELECT l FROM Log l where l.message =:logMessage", Log.class)
                .setParameter("logMessage", logMessage)
                .getResultList().stream().findAny();

    }
}
