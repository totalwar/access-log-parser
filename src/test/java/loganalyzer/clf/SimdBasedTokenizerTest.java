package loganalyzer.clf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimdBasedTokenizerTest {

    @Test
    void testParseWord_simd() {
        String input = "0123456789 1234567890 2345678901";

        SimdBasedTokenizer tokenizer = new SimdBasedTokenizer(input);
        String a = tokenizer.nextWord();
        String b = tokenizer.nextWord();
        String c = tokenizer.nextWord();
        String d = tokenizer.nextWord();

        Assertions.assertEquals("0123456789", a);
        Assertions.assertEquals("1234567890", b);
        Assertions.assertEquals("2345678901", c);
        Assertions.assertNull(d);
    }

    @Test
    void testParseWord_remainder() {
        String input = "абв где ёж";

        SimdBasedTokenizer tokenizer = new SimdBasedTokenizer(input);
        String a = tokenizer.nextWord();
        String b = tokenizer.nextWord();
        String c = tokenizer.nextWord();
        String d = tokenizer.nextWord();

        Assertions.assertEquals("абв", a);
        Assertions.assertEquals("где", b);
        Assertions.assertEquals("ёж", c);
        Assertions.assertNull(d);
    }

    @Test
    void testParseSquared_empty() {
        String input = "[]";

        SimdBasedTokenizer tokenizer = new SimdBasedTokenizer(input);
        String actual = tokenizer.nextSquared();

        Assertions.assertEquals("[]", actual);
    }

    @Test
    void testParseSquared_simd() {
        String input = "[012345678901234567890123456789] abc";

        SimdBasedTokenizer tokenizer = new SimdBasedTokenizer(input);
        String actual = tokenizer.nextSquared();
        String after = tokenizer.nextWord();

        Assertions.assertEquals("[012345678901234567890123456789]", actual);
        Assertions.assertEquals("abc", after);
    }

    @Test
    void testParseQuoted_empty() {
        String input = "\"\"";

        SimdBasedTokenizer tokenizer = new SimdBasedTokenizer(input);
        String actual = tokenizer.nextQuoted();

        Assertions.assertEquals("\"\"", actual);
    }
}
