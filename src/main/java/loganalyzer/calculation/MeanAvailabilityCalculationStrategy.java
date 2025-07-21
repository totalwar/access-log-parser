package loganalyzer.calculation;

import loganalyzer.CommandLineArgs;
import loganalyzer.clf.CLFEntry;
import loganalyzer.report.AlertReporter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class MeanAvailabilityCalculationStrategy implements AvailabilityCalculationStrategy {

    private final CommandLineArgs commandLineArgs;

    @Override
    public void consume(Iterable<CLFEntry> entries, AlertReporter reporter) throws IOException {
        WindowAvg window = new WindowAvg(commandLineArgs);

        for (CLFEntry entry : entries) {
            if (!window.processEntry(entry)) {
                if (window.isBrokenAvailability()) {
                    reporter.alert(window.getFirstError(), window.getLastError(), window.getAvailability());
                }
                window.reset();
                window.processEntry(entry);
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    private static class WindowAvg {
        private final CommandLineArgs commandLineArgs;

        private LocalDateTime windowBegin;
        private LocalDateTime windowEnd;
        private LocalDateTime firstError;
        private LocalDateTime lastError;

        private int success;
        private int errors;

        public void reset() {
            this.windowBegin = null;
            this.windowEnd = null;
            this.firstError = null;
            this.lastError = null;
            this.success = 0;
            this.errors = 0;
        }

        public boolean isInitialized() {
            return windowBegin != null && windowEnd != null;
        }

        public double getAvailability() {
            double s = success;
            double e = errors;
            return s / (s + e) * 100;
        }

        public boolean processEntry(CLFEntry entry) {
            if (!isInitialized()) {
                init(entry.getDatetime());
            }

            if (entry.getDatetime().isAfter(windowEnd)) {
                return false;
            }

            if (isError(entry)) {
                processError(entry.getDatetime());
            } else {
                processSuccess();
            }

            return true;
        }

        private void init(LocalDateTime windowBegin) {
            this.windowBegin = windowBegin;
            this.windowEnd = windowBegin.plus(commandLineArgs.getWindowDuration());
        }

        private boolean isError(CLFEntry entry) {
            String status = entry.getStatus() == null ? "" : entry.getStatus();
            return status.charAt(0) == '5' || entry.getResponseTime() > commandLineArgs.getResponseTimeThreshold();
        }

        private void processError(LocalDateTime errorTime) {
            if (firstError == null) {
                firstError = errorTime;
            }
            lastError = errorTime;
            errors++;
        }

        private void processSuccess() {
            success++;
        }

        public boolean isBrokenAvailability() {
            return getAvailability() < commandLineArgs.getAvailabilityThreshold();
        }
    }
}
