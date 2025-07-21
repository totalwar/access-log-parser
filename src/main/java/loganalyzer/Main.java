package loganalyzer;

import loganalyzer.calculation.MeanAvailabilityCalculationStrategy;
import loganalyzer.clf.SimdBasedParser;
import loganalyzer.facade.LogAnalyzer;
import org.apache.commons.cli.*;

import java.time.Duration;

public class Main {
    private static final String USAGE = "java -jar loganalyzer -u <availability_threshold> -t <response_time_threshold>";

    public static void main(String[] args) {
        CommandLineArgs cmdArgs = parseCommandLineArgs(args);

        if (cmdArgs == null) {
            System.exit(1);
        }

        try {
            LogAnalyzer analyzer = new LogAnalyzer(new SimdBasedParser(), new MeanAvailabilityCalculationStrategy(cmdArgs));
            analyzer.analyze(System.in, System.out);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    private static CommandLineArgs parseCommandLineArgs(String[] args) {
        Options options = new Options();

        options.addOption(Option.builder("u")
                .longOpt("availability")
                .hasArg()
                .required()
                .desc("Целевой перцентиль доступности (например, 99.9)")
                .build());

        options.addOption(Option.builder("t")
                .longOpt("response-time")
                .hasArg()
                .required()
                .desc("Максимально допустимое время ответа в миллисекундах (например, 45)")
                .build());

        options.addOption(Option.builder("tw")
                .longOpt("time-window")
                .hasArg()
                .required(false)
                .desc("Размер скользящего окна в секундах (по умолчанию 60)")
                .build());

        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Показать help")
                .build());

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                printHelp(options);
                return null;
            }

            double availabilityThreshold = Double.parseDouble(cmd.getOptionValue("u"));
            double responseTimeThreshold = Double.parseDouble(cmd.getOptionValue("t"));

            if (availabilityThreshold <= 0 || availabilityThreshold > 100) {
                System.err.println("Значение доступности должно быть в диапазоне 0 .. 100");
                return null;
            }

            if (responseTimeThreshold <= 0) {
                System.err.println("Время ответа не должно быть ниже нуля");
                return null;
            }

            Duration timeWindow = Duration.ofSeconds(Integer.parseInt(cmd.getOptionValue("tw", "60")));

            return new CommandLineArgs(availabilityThreshold, responseTimeThreshold, timeWindow);

        } catch (ParseException e) {
            System.err.println("Ошибка в аргументах команды: " + e.getMessage());
            printHelp(options);
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Ошибка при парсинге числового значения: " + e.getMessage());
            printHelp(options);
            return null;
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(USAGE, options);
    }
}
