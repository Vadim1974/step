package study.stepup.authloghandler.components;

import lombok.extern.slf4j.Slf4j;
import study.stepup.authloghandler.annotations.LogTransformation;
import study.stepup.authloghandler.dto.LogEntry;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class LogEntryFioCorrector implements Componetable {

    @LogTransformation
    @Override
    public void operate(List<LogEntry> logEntries) {
        for (LogEntry entry:logEntries){
            entry.getUser().setFio(correctFio(entry.getUser().getFio()));
        }
        log.info("operate fio corrector " + logEntries.size());
    }

    String correctFio(String fio)
    {
        String[] names = fio.split(" ");
        String[] rez = Arrays.stream(names).map(s->Character.toUpperCase(s.charAt(0))+s.substring(1)).toArray(String[]::new);
        return String.join(" ",rez);
    }

}
