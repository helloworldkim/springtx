package hello.springtx.propagation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogJpaRepository extends JpaRepository<Log,Long> {
}
