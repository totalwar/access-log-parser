package loganalyzer.clf;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;

public class SimdBasedTokenizer {

    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_PREFERRED;
    private static final byte DELIM = ' ';
    private static final byte BRACKET_OPEN = '[';
    private static final byte BRACKET_CLOSE = ']';
    private static final byte ANGLE_OPEN = '<';
    private static final byte ANGLE_CLOSE = '>';
    private static final byte QUOTE = '"';
    private final byte[] bytes;

    private int offset;

    public SimdBasedTokenizer(String str) {
        this.bytes = str.getBytes();
        this.offset = 0;
    }

    /**
     * Парсим следующий элемент до разделителя
     */
    public String nextWord() {
        int upperBound = SPECIES.loopBound(bytes.length);
        int i = offset;
        for (; i + SPECIES.length() <= upperBound; i += SPECIES.length()) {
            ByteVector vec = ByteVector.fromArray(SPECIES, bytes, i);
            VectorMask<Byte> mask = vec.eq(DELIM);
            if (mask.anyTrue()) {
                // подстрока до ближайшего разделителя
                int from = offset;
                int to = i + mask.firstTrue();

                // сдвиг на следующий символ
                offset = to + 1;

                return new String(bytes, from, to - from);
            }
        }

        // обработать остаток не кратный размеру SIMD вектора
        while (i <= bytes.length) {
            if (i == bytes.length || bytes[i] == DELIM) {
                // подстрока до ближайшего разделителя
                int from = offset;
                int to = i;

                // сдвиг на следующий символ
                offset = to + 1;

                return new String(bytes, from, to - from);
            }
            i++;
        }

        return null;
    }

    /**
     * Парсим строку, обрамленную кавычками "..."
     */
    public String nextQuoted() {
        if (bytes[offset] != QUOTE) {
            return null;
        }

        int upperBound = SPECIES.loopBound(bytes.length);
        int i = offset + 1;
        for (; i + SPECIES.length() <= upperBound; i += SPECIES.length()) {
            ByteVector vec = ByteVector.fromArray(SPECIES, bytes, i);
            VectorMask<Byte> mask = vec.eq(QUOTE);
            if (mask.anyTrue()) {
                // подстрока до кавычки включительно
                int from = offset;
                int to = i + mask.firstTrue() + 1;

                // сдвиг на следующий символ
                offset = to + 1;

                return new String(bytes, from, to - from);
            }
        }

        // обработать остаток не кратный размеру SIMD вектора
        while (i < bytes.length) {
            if (bytes[i] == QUOTE) {
                // подстрока до кавычки включительно
                int from = offset;
                int to = i + 1;

                // сдвиг на следующий символ
                offset = to + 1;

                return new String(bytes, from, to - from);
            }
            i++;
        }

        return null;
    }

    /**
     * Парсим строку, обрамленную скобками [ ]
     */
    public String nextSquared() {
        if (offset < bytes.length && bytes[offset] != BRACKET_OPEN) {
            return null;
        }

        int upperBound = SPECIES.loopBound(bytes.length);
        int i = offset;
        for (; i + SPECIES.length() <= upperBound; i += SPECIES.length()) {
            ByteVector vec = ByteVector.fromArray(SPECIES, bytes, i);
            VectorMask<Byte> mask = vec.eq(BRACKET_CLOSE);
            if (mask.anyTrue()) {
                // подстрока до закрывающей скобки "]" включительно
                int from = offset;
                int to = i + mask.firstTrue() + 1;

                // сдвиг на следующий символ
                offset = to + 1;

                return new String(bytes, from, to - from);
            }
        }

        // обработать остаток не кратный размеру SIMD вектора
        while (i < bytes.length) {
            if (bytes[i] == BRACKET_CLOSE) {
                // подстрока до закрывающей скобки "]" включительно
                int from = offset;
                int to = i + 1;

                // сдвиг на следующий символ
                offset = to + 1;

                return new String(bytes, from, to - from);
            }
            i++;
        }

        return null;
    }

    /**
     * Парсим строку, обрамленную скобками < >
     */
    public String nextAngled() {
        if (bytes[offset] != ANGLE_OPEN) {
            return null;
        }

        int upperBound = SPECIES.loopBound(bytes.length);
        int i = offset;
        for (; i + SPECIES.length() <= upperBound; i += SPECIES.length()) {
            ByteVector vec = ByteVector.fromArray(SPECIES, bytes, i);
            VectorMask<Byte> mask = vec.eq(ANGLE_CLOSE);
            if (mask.anyTrue()) {
                // подстрока до закрывающей скобки ">" включительно
                int from = offset;
                int to = i + mask.firstTrue() + 1;

                // сдвиг на следующий символ
                offset = to + 1;

                return new String(bytes, from, to - from);
            }
        }

        // обработать остаток не кратный размеру SIMD вектора
        while (i < bytes.length) {
            if (bytes[i] == ANGLE_CLOSE) {
                // подстрока до закрывающей скобки ">" включительно
                int from = offset;
                int to = i + 1;

                // сдвиг на следующий символ
                offset = to + 1;

                return new String(bytes, from, to - from);
            }
            i++;
        }

        return null;
    }

    /**
     * Парсим целое число
     */
    public Integer nextInt() {
        String s = nextWord();
        if (s == null) {
            return null;
        }
        return Integer.parseInt(s);
    }

    /**
     * Парсим дробное число
     */
    public Double nextDouble() {
        String s = nextWord();
        if (s == null) {
            return null;
        }
        return Double.parseDouble(s);
    }
}
