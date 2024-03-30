package study.stepup.authloghandler.components;

import study.stepup.authloghandler.dto.LogEntry;
import java.util.List;

//public interface Componetable<T> {
public interface Componetable {
    void operate(List<LogEntry> logEntries);
}
