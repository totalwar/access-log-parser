package loganalyzer.clf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class TokenBasedParserTest {

    @Test
    void testParser() throws IOException {
        String input = """
                192.168.32.181 - - [14/06/2017:16:48:52 +1000] "PUT /rest/v1.4/documents?zone=archive&_rid=57fb7cbb HTTP/1.1" 200 2 10.539 "-" "@list-item-updater" prio:0
                """;

        TokenBasedParser parser = new TokenBasedParser();
        Iterable<CLFEntry> entries = parser.parse(new ByteArrayInputStream(input.getBytes()));

        CLFEntry actual = entries.iterator().next();
        Assertions.assertEquals("192.168.32.181", actual.getIp());
        Assertions.assertEquals("200", actual.getStatus());
        Assertions.assertEquals(10.539, actual.getResponseTime(), 0.0001);
    }
}
