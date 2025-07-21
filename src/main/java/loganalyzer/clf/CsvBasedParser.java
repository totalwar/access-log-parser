package loganalyzer.clf;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Парсим строку лога как csv запись, где разделитель не запятая, а пробел.
 * Выглядит как сырой прототип, но работает в 5 раз быстрее Antlr имплементации.
 */
public class CsvBasedParser implements CLFParser {

    @Override
    public Iterable<CLFEntry> parse(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(Columns.class)
                .setDelimiter(" ")
                .get();

        CSVParser rawData = csvFormat.parse(reader);
        Iterable<CSVRecord> consistentData = IterableUtils.filteredIterable(rawData, CSVRecord::isConsistent);
        return IterableUtils.transformedIterable(consistentData, this::transform);
    }

    private CLFEntry transform(CSVRecord row) {
        return new CLFEntry(
                row.get(Columns.IP),
                row.get(Columns.IDENT),
                row.get(Columns.USERID),
                DateUtils.parseDateWithBraces(row.get(Columns.DATETIME)),
                row.get(Columns.TIMEZONE),
                row.get(Columns.REQUEST),
                row.get(Columns.STATUS),
                Integer.parseInt(row.get(Columns.TRANSFERRED_SIZE)),
                Double.parseDouble(row.get(Columns.RESPONSE_TIME)),
                row.get(Columns.REFERER),
                row.get(Columns.USER_AGENT),
                row.get(Columns.PRIO)
        );
    }

    private enum Columns {
        IP,
        IDENT,
        USERID,
        DATETIME,
        TIMEZONE,
        REQUEST,
        STATUS,
        TRANSFERRED_SIZE,
        RESPONSE_TIME,
        REFERER,
        USER_AGENT,
        PRIO,
    }
}
