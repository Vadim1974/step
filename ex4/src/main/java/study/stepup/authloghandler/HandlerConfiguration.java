package study.stepup.authloghandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import study.stepup.authloghandler.components.*;
import study.stepup.authloghandler.components.LogFileReader;
import study.stepup.authloghandler.repository.LogEntryRepository;
import study.stepup.authloghandler.repository.UserRepository;

@Configuration
public class HandlerConfiguration {

    @Value("${logReader.filesPath}")
    String filePath;
    @Value("${logReader.filesMask}")
    String fileMask;

    @Bean
    @Order(1)
    public Componetable getFirstElement(){
        return new LogFileReader(filePath, fileMask);
    }

    @Bean
    @Order(2)
    public Componetable getElement2() {
        return new LogEntryFioCorrector();
    }

    @Bean
    @Order(3)
    public Componetable getElement3() {
        return new LogEntryAppTypeCorrector();
    }

    @Bean
    @Order(4)
    public Componetable getElement4() {
        return new LogEntryDateController();
    }

    @Bean
    @Order(10)
    @Autowired
    public Componetable getLastElement(LogEntryRepository logEntryRepository, UserRepository userRepository) {

        return new LogEntryWriter(logEntryRepository, userRepository);
    }

}