package loganalyzer.clf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

class Antlr4BasedParserTest {

    Antlr4BasedParser parser = new Antlr4BasedParser();

    @Test
    void testParse() throws Exception {
        try (var file = new FileInputStream("src/test/resources/access.log")) {
            Iterable<CLFEntry> entries = parser.parse(file);
            for (CLFEntry entry : entries) {
                Assertions.assertEquals("192.168.32.181", entry.getIp());
            }
        }
    }
}
