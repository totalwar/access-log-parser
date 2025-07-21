package loganalyzer.clf;

import java.io.IOException;
import java.io.InputStream;

public interface CLFParser {

    Iterable<CLFEntry> parse(InputStream input) throws IOException;
}
