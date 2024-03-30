package study.stepup.authloghandler.components;

import lombok.extern.slf4j.Slf4j;
import study.stepup.authloghandler.dto.LogEntry;
import study.stepup.authloghandler.repository.LogEntryRepository;
import study.stepup.authloghandler.repository.UserRepository;
import java.util.List;

@Slf4j
public class LogEntryWriter implements Componetable {
    private final LogEntryRepository logEntryRepository;
    private final UserRepository userRepository;

    public LogEntryWriter(LogEntryRepository logEntryRepository, UserRepository userRepository) {
        this.logEntryRepository = logEntryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void operate(List<LogEntry> logEntries) {
        for (LogEntry entry : logEntries){
            if(userRepository.existsByLogin(entry.getUser().getLogin()))
            {
                entry.setUser(userRepository.findByLogin(entry.getUser().getLogin()));
            }
            userRepository.save(entry.getUser());
            logEntryRepository.save(entry);

            log.info(entry.toString());
        }

        log.info("in DB " + logEntries.size());

        List<LogEntry> logEntries2 = logEntryRepository.findAllByIdAfter(0L);
        for(LogEntry x : logEntries2){
            log.info(x.toString());
        }
        log.info("operate writer " + logEntries.size());
    }

}
