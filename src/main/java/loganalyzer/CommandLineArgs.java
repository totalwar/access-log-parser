package loganalyzer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
@Getter
public class CommandLineArgs {
    private final double availabilityThreshold;
    private final double responseTimeThreshold;
    private final Duration windowDuration;
}
