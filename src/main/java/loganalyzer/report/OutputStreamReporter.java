package loganalyzer.report;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OutputStreamReporter implements AlertReporter {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss");

    private final Writer writer;

    public OutputStreamReporter(OutputStream outputStream) {
        this.writer = new OutputStreamWriter(outputStream);
    }

    @Override
    public void alert(LocalDateTime fromInterval, LocalDateTime toInterval, double availability) throws IOException {
        writer.write(fromInterval.format(format));
        writer.write(" ");
        writer.write(toInterval.format(format));
        writer.write(" ");
        writer.write(String.format("%.2f", availability));
        writer.write(System.lineSeparator());
    }
}
