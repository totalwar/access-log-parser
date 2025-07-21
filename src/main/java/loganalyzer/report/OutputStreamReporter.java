package loganalyzer.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
        writer.write(String.format("%s %s %.2f", fromInterval.format(format), toInterval.format(format), availability));
        writer.write(System.lineSeparator());
        writer.flush();
    }
}
