package benchmarks;

import loganalyzer.CommandLineArgs;
import loganalyzer.calculation.MeanAvailabilityCalculationStrategy;
import loganalyzer.clf.SimdBasedParser;
import loganalyzer.facade.LogAnalyzer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class LoadTestBenchmark {

    @Benchmark
    @Fork(jvmArgsAppend = {"--enable-preview", "--add-modules", "jdk.incubator.vector"})
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSimdBasedParser(Blackhole blackhole) throws IOException {
        CommandLineArgs args = new CommandLineArgs(99.9, 100, Duration.ofSeconds(60));
        LogAnalyzer logAnalyzer = new LogAnalyzer(
                new SimdBasedParser(), new MeanAvailabilityCalculationStrategy(args)
        );
        OutputStream ignore = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                blackhole.consume(b);
            }
        };

        try (var input = new FileInputStream("access.log")) {
            logAnalyzer.analyze(input, ignore);
        }
    }
}
