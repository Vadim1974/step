package study.stepup.authloghandler.components;

import lombok.extern.slf4j.Slf4j;
import study.stepup.authloghandler.dto.LogEntry;
import study.stepup.authloghandler.dto.User;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LogFileReader implements Componetable {
    private final String filePath;
    private final String fileMask;

    public LogFileReader(String filePath, String fileMask) {
        this.filePath = filePath;
        this.fileMask = fileMask;
    }

    @Override
    public void operate(List<LogEntry> logEntries) {
        List<LogEntry> entries = readFiles();
        logEntries.addAll(entries);
        log.info("operate reader " + logEntries.size());
    }

    List<LogEntry> readFiles(){
        List<LogEntry> result = new ArrayList<>();
        log.info(filePath + " " + fileMask);
        String[] files = getFilesByPath(filePath);
        String[] filteredFiles = filterFiles(files, fileMask);
        for (String file : filteredFiles){
            List<LogEntry> entries = readFile(file);
            if (!entries.isEmpty()){
                result.addAll(entries);
            }
        }
        return result;
    }

    List<LogEntry> readFile(String file) {
        List<LogEntry> result = new ArrayList<>();
        int rownum = 0;
        try (Scanner sc = new Scanner(new File(file))) {
            while (sc.hasNextLine()) {
                LogEntry entry = readLine(sc.nextLine());
                if (entry!= null) {
                    result.add(entry);
                }
                rownum++;
            }
            sc.close();
            return result;
        } catch (Exception ex) {
            log.warn("Ошибка обработки файла:" + file + " строка:" + rownum + " " + ex.getMessage());
            log.debug(Arrays.toString(ex.getStackTrace()));
            return result;
        }
    }

    LogEntry readLine(String str){
        LogEntry logEntry = new LogEntry();
        if (str.isEmpty())
        {
            log.warn("Ошибка. пустая строка");
            return  null;
        }
        String[] fields = str.split(";");
        if (fields.length != 4)
        {
            log.warn("Ошибка. В строке " + str + " не соответствует число разделителей");
            return  null;
        }

        User user = new User();
        logEntry.setUser(user);
        logEntry.setDate(readDate(fields[0]));
        user.setLogin(fields[1]);
        user.setFio(fields[2]);
        logEntry.setAppType(fields[3]);
        return logEntry;
    }

    String[] getFilesByPath(String filesPath)
    {
        try{
            File dir = new File(filesPath);
            if (!dir.exists() || !dir.isDirectory())
            {
                log.warn("Ошибка: директория " + filesPath + " не существует!");
                return new String[0];
            }
            File[] files = dir.listFiles() == null ? new File[0] : dir.listFiles();
            return Arrays.stream(files).map(File::getAbsolutePath).toArray(String[]::new);
        } catch (Exception ex)
        {
            log.warn("Ошибка получения списка файлов"  +  ex.getMessage());
            log.debug(Arrays.toString(ex.getStackTrace()));
            return new String[0];
        }
    }

    String[] filterFiles(String[] files, String fileMask){
        List<String> filteredFiles = new ArrayList<>();
        final Pattern pattern = Pattern.compile(fileMask);

        if (files != null)
        {
            for (String fileName : files)
            {
                Matcher matcher = pattern.matcher(fileName);
                if (matcher.find())
                {
                    filteredFiles.add(fileName);
                }
            }
        }
        return filteredFiles.toArray(new String[0]);
    }

    LocalDateTime readDate(String strDate){
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(strDate);
        } catch (DateTimeParseException e) {
            log.warn("Ошибка обработки времени:{}", strDate);
        }
        return dateTime;
    }
}
