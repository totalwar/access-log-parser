package loganalyzer.clf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

class SimdBasedParserTest {

    SimdBasedParser parser = new SimdBasedParser();

    @Test
    void testParseSingleEntry() throws IOException {
        String incorrectLogEntry = "127.0.0.1 - - ";
        Iterable<CLFEntry> entries = parser.parse(new ByteArrayInputStream(incorrectLogEntry.getBytes()));
        Assertions.assertFalse(entries.iterator().hasNext());
    }

    @Test
    void testParse() throws IOException {
        try (var file = new FileInputStream("src/test/resources/access.log")) {
            Iterable<CLFEntry> entries = parser.parse(file);
            for (CLFEntry entry : entries) {
                Assertions.assertEquals("192.168.32.181", entry.getIp());
            }
        }
    }
}
