package study.stepup.authloghandler.components;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.stepup.authloghandler.dto.*;
import study.stepup.authloghandler.repository.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.h2.console.enabled=true",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"

        })
public class LogEntryWriterTests {
    @Autowired
    LogEntryRepository logEntryRepository;
    @Autowired
    UserRepository userRepository;

    LogEntryWriter logEntryWriter;
    List<LogEntry> logEntries;

    @BeforeEach
    public void setup() {
        logEntryWriter = new LogEntryWriter(logEntryRepository, userRepository);
        logEntries = new LinkedList<>(Arrays.asList(
                new LogEntry(1L, LocalDateTime.of(2024,1,1,1,1,1), new User(1L,"lastName1 firstName1 middleName1", "lastName1_fm" ), "ddd"),
                new LogEntry(2L, LocalDateTime.of(2024,1,1,0,0,0), new User(2L,"lastName2 firstName2 middleName2", "lastName2_fm" ), "mobile"),
                new LogEntry(3L, null , new User(3L,"lastName3 firstName3 middleName3", "lastName3_fm" ), "web")
        ));
    }

    @AfterEach
    public void teardown() {
        logEntryWriter = null;
        logEntries = null;
    }

    @Test
    @DisplayName("Проверка заполнения тестовой H2 БД")
    void test1_correct() {
        Assertions.assertEquals(0, logEntryRepository.count());
        Assertions.assertEquals(0, userRepository.count());
        logEntryWriter.operate(logEntries);
        Assertions.assertEquals(logEntries.size(), logEntryRepository.count());
        Assertions.assertEquals(3, userRepository.count());
        String[] expectedLogEntries = Arrays.stream(logEntries.toArray())
                .map(Object::toString)
                .toArray(String[]::new);
        String[] actualLogEntries = Arrays.stream(logEntryRepository.findAll().toArray())
                .map(Object::toString)
                .toArray(String[]::new);
        Assertions.assertArrayEquals(expectedLogEntries, actualLogEntries);


    }
}
