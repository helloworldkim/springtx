package hello.springtx.propagation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogJpaRepository extends JpaRepository<Log,Long> {
    Optional<Log> findByMessage(String username);
}
