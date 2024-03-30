package study.stepup.authloghandler.repository;

import study.stepup.authloghandler.dto.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry,Long> {
     List<LogEntry> findAllByIdAfter(Long id);
}
