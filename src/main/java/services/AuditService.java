package services;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    FileWriter writer;

    final DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;

    public void write(String action) throws IOException {
        writer.append(action);
        writer.append(",");
        writer.append(formatter.format(LocalDateTime.now()));
        writer.append("\n");
        writer.flush();
    }

    public AuditService() throws IOException {
        this.writer = new FileWriter("audit.csv");
    }
}
