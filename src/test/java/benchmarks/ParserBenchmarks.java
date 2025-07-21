package benchmarks;

import loganalyzer.clf.*;
import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class ParserBenchmarks {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testParserBaseline(Blackhole blackhole) throws IOException {
        CLFParser noopParser = new NoopParser();

        try (var stream = new FileInputStream("src/test/resources/access.log")) {
            Iterable<CLFEntry> entries = noopParser.parse(stream);
            for (CLFEntry entry : entries) {
                blackhole.consume(entry);
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testAntlr4Parser(Blackhole blackhole) throws IOException {
        Antlr4BasedParser parser = new Antlr4BasedParser();

        try (var stream = new FileInputStream("src/test/resources/access.log")) {
            Iterable<CLFEntry> entries = parser.parse(stream);
            for (CLFEntry entry : entries) {
                blackhole.consume(entry);
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testCsvBasedParser(Blackhole blackhole) throws IOException {
        CsvBasedParser parser = new CsvBasedParser();

        try (var stream = new FileInputStream("src/test/resources/access.log")) {
            Iterable<CLFEntry> entries = parser.parse(stream);
            for (CLFEntry entry : entries) {
                blackhole.consume(entry);
            }
        }
    }

    @Benchmark
    @Fork(jvmArgsAppend = {"--enable-preview", "--add-modules", "jdk.incubator.vector"})
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSimdBasedParser(Blackhole blackhole) throws IOException {
        SimdBasedParser parser = new SimdBasedParser();

        try (var stream = new FileInputStream("src/test/resources/access.log")) {
            Iterable<CLFEntry> entries = parser.parse(stream);
            for (CLFEntry entry : entries) {
                blackhole.consume(entry);
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testTokenBasedParser(Blackhole blackhole) throws IOException {
        TokenBasedParser parser = new TokenBasedParser();

        try (var stream = new FileInputStream("src/test/resources/access.log")) {
            Iterable<CLFEntry> entries = parser.parse(stream);
            for (CLFEntry entry : entries) {
                blackhole.consume(entry);
            }
        }
    }

    private static class NoopParser implements CLFParser {
        @Override
        public Iterable<CLFEntry> parse(InputStream input) throws IOException {
            return () -> new NoopIterator(input);
        }
    }

    private static class NoopIterator implements Iterator<CLFEntry> {
        private final BufferedInputStream stream;
        private final CLFEntry empty = new CLFEntry();
        private final byte[] buf = new byte[4096];

        private NoopIterator(InputStream input) {
            this.stream = new BufferedInputStream(input);
        }

        @Override
        @SneakyThrows
        public boolean hasNext() {
            return stream.read(buf) != -1;
        }

        @Override
        public CLFEntry next() {
            return empty;
        }
    }
}
