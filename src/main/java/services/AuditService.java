package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    PrintWriter out;

    final DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;

    public void write(String action) throws IOException {
        out.append(action);
        out.append(",");
        out.append(formatter.format(LocalDateTime.now()));
        out.append("\n");
        out.flush();
    }

    public AuditService() throws IOException {
        this.out = new PrintWriter(new FileWriter("audit.csv", true));
    }
}
