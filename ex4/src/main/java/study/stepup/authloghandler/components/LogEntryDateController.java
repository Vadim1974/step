package study.stepup.authloghandler.components;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.stepup.authloghandler.dto.LogEntry;
import java.util.List;

@Slf4j
public class LogEntryDateController implements Componetable {
    private static final Logger log1 = LoggerFactory.getLogger("nulldate");

    @Override
    public void operate(List<LogEntry> logEntries) {

        for (LogEntry entry : logEntries.stream().filter(x-> x.getDate()==null).toList()){
            log1.info(entry.toString());
        }

        logEntries.removeIf(entry -> entry.getDate()==null);
        log.info("operate date controller " + logEntries.size());
    }
}
