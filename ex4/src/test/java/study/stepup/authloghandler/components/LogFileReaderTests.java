package study.stepup.authloghandler.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import study.stepup.authloghandler.dto.LogEntry;

import java.time.LocalDateTime;

@SpringBootTest
public class LogFileReaderTests {
    @Test
    @DisplayName("Проверка загрузки строки из лога")
    void readLine_correct() {
        String inStr = "2024-01-15T09:22:36.124;lastName_fm;lastName firstName middleName;web;";
        LogFileReader logFileReader = new LogFileReader("","");
        LogEntry logEntry = logFileReader.readLine(inStr);
        Assertions.assertEquals("lastName firstName middleName",logEntry.getUser().getFio());
        Assertions.assertEquals("lastName_fm",logEntry.getUser().getLogin());
        Assertions.assertEquals(LocalDateTime.parse("2024-01-15T09:22:36.124"),logEntry.getDate());
        Assertions.assertEquals("web",logEntry.getAppType());
    }

    @Test
    @DisplayName("Проверка фильтрации файлов")
    void filterFiles_correct() {
        String[] inFiles = new String[] { "file1.csv", "file2.txt", "file3.csv"};
        LogFileReader logFileReader = new LogFileReader("","");
        String[] outFiles = logFileReader.filterFiles(inFiles, "^.+\\.csv$");
        String[] expectedFiles = new String[] { "file1.csv", "file3.csv"};
        Assertions.assertArrayEquals(expectedFiles, outFiles);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-01-01T01:01:01.000", "2024-01-02T02:02:02.222"})
    @DisplayName("Проверка обработки времени")
    void readDate_correct(String strDate) {
        LogFileReader logFileReader = new LogFileReader("","");
        LocalDateTime resDateTime = logFileReader.readDate(strDate);
        Assertions.assertEquals(LocalDateTime.parse(strDate), resDateTime);
    }


}
