package loganalyzer.calculation;

import loganalyzer.clf.CLFEntry;
import loganalyzer.report.AlertReporter;

import java.io.IOException;

public interface AvailabilityCalculationStrategy {

    void consume(Iterable<CLFEntry> entries, AlertReporter reporter) throws IOException;

}
