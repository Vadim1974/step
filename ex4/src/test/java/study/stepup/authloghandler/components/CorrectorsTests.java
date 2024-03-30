package study.stepup.authloghandler.components;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import study.stepup.authloghandler.dto.LogEntry;
import study.stepup.authloghandler.dto.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class CorrectorsTests {

    List<LogEntry> logEntries;

    @BeforeEach
    public void setup() {
        logEntries = new LinkedList<> (Arrays.asList(
                new LogEntry(1L, LocalDateTime.of(2024,1,1,1,1,1), new User(1L,"lastName1 firstName1 middleName1", "lastName1_fm" ), "ddd"),
                new LogEntry(2L, LocalDateTime.of(2024,1,1,0,0,0), new User(2L,"lastName2 firstName2 middleName2", "lastName2_fm" ), "mobile"),
                new LogEntry(3L, null , new User(3L,"lastName3 firstName3 middleName3", "lastName3_fm" ), "web")
        ));
    }

    @AfterEach
    public void teardown() {
        logEntries = null;
    }

    @Test
    @DisplayName("Проверка коррекции ФИО")
    void transformFio_correct() {
        LogEntryFioCorrector corrector = new LogEntryFioCorrector();
        String expectedFio = "LastName1 FirstName1 MiddleName1";
        corrector.operate(logEntries);
        Assertions.assertEquals(expectedFio, logEntries.stream().filter(x->x.getUser().getLogin().equals("lastName1_fm")).findFirst().get().getUser().getFio());
    }

    @Test
    @DisplayName("Проверка коррекции appType")
    void transformAppType_correct() {
        LogEntryAppTypeCorrector corrector = new LogEntryAppTypeCorrector();
        corrector.operate(logEntries);
        Assertions.assertEquals("other", logEntries.stream().filter(x->x.getId().equals(1L)).findFirst().orElse(new LogEntry()).getAppType());
        Assertions.assertEquals("mobile", logEntries.stream().filter(x->x.getId().equals(2L)).findFirst().orElse(new LogEntry()).getAppType());
        Assertions.assertEquals("web", logEntries.stream().filter(x->x.getId().equals(3L)).findFirst().orElse(new LogEntry()).getAppType());
    }

    @Test
    @DisplayName("Проверка коррекции даты логгирования")
    void transformDate_correct() {
        LogEntryDateController corrector = new LogEntryDateController();
        corrector.operate(logEntries);
        Assertions.assertEquals(LocalDateTime.of(2024,1,1,1,1,1),
                logEntries.stream().filter(x->x.getId().equals(1L)).findFirst().orElse(new LogEntry()).getDate());
        Assertions.assertEquals(LocalDateTime.of(2024,1,1,0,0,0),
                logEntries.stream().filter(x->x.getId().equals(2L)).findFirst().orElse(new LogEntry()).getDate());
        Assertions.assertEquals(0, logEntries.stream().filter(x->x.getId().equals(3L)).count());
    }


}
