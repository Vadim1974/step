package study.stepup.authloghandler;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.stepup.authloghandler.components.Componetable;
import study.stepup.authloghandler.dto.LogEntry;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LogEntryHandler {

    private final List<LogEntry> logEntries = new ArrayList<>();

    @Autowired(required = false)
    private List<Componetable> componentList = new ArrayList<>();

    public LogEntryHandler() {
        log.debug("LogEntryHandler init");
    }

    public void run(){
        log.info("LogEntryHandler operate");
        for ( Componetable x : componentList){
            x.operate(logEntries);
        }
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("Started after LogEntryHandler");
        run();
    }
}
