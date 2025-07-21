package loganalyzer.facade;

import loganalyzer.calculation.AvailabilityCalculationStrategy;
import loganalyzer.clf.CLFEntry;
import loganalyzer.clf.CLFParser;
import loganalyzer.report.OutputStreamReporter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RequiredArgsConstructor
public class LogAnalyzer {

    private final CLFParser commonLogFormatParser;
    private final AvailabilityCalculationStrategy calculationStrategy;

    public void analyze(InputStream input, OutputStream out) throws IOException {
        Iterable<CLFEntry> data = commonLogFormatParser.parse(input);
        calculationStrategy.consume(data, new OutputStreamReporter(out));
    }
}
