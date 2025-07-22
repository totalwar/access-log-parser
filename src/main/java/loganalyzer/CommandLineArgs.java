package loganalyzer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.time.Duration;

@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CommandLineArgs {
    // required
    private final double availabilityThreshold;
    private final double responseTimeThreshold;
    private final Duration windowDuration;
    // optional
    private File inputFile;
    private File outputFile;
}
