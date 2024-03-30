package study.stepup.authloghandler.components;

import lombok.extern.slf4j.Slf4j;
import study.stepup.authloghandler.dto.LogEntry;
import java.util.List;

@Slf4j
public class LogEntryAppTypeCorrector implements Componetable {
    @Override
    public void operate(List<LogEntry> logEntries) {
        for (LogEntry entry:logEntries){
            entry.setAppType(correctAppType(entry.getAppType()));
        }
        log.info("operate App type corrector "  + logEntries.size());
    }

    private String correctAppType(String str)
    {
        if (str.equals("web")||str.equals("mobile")) {
            return str;
        }
        return "other";
    }
}
