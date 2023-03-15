package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogJapService {
    private final LogJpaRepository logJpaRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveWithREQUIRES_NEW(Log log) {
        logJpaRepository.save(log);
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveWithREQUIRED(Log log) {
        logJpaRepository.save(log);
        throw new RuntimeException();
    }
}
