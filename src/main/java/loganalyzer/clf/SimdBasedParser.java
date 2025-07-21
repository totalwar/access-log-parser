package loganalyzer.clf;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class SimdBasedParser implements CLFParser {

    @Override
    public Iterable<CLFEntry> parse(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        Iterable<String> rows = () -> new LineIterator(reader);
        Iterable<CLFEntry> entries = IterableUtils.transformedIterable(rows, this::parse);
        return IterableUtils.filteredIterable(entries, Objects::nonNull);
    }

    private CLFEntry parse(String row) {
        SimdBasedTokenizer tokenizer = new SimdBasedTokenizer(row);
        CLFEntry entry = new CLFEntry();

        String ip = tokenizer.nextWord();
        if (ip == null) {
            return null;
        } else {
            entry.setIp(ip);
        }

        String ident = tokenizer.nextWord();
        if (ident == null) {
            return null;
        } else {
            entry.setIdent(ident);
        }

        String userId = tokenizer.nextWord();
        if (userId == null) {
            return null;
        } else {
            entry.setUserid(userId);
        }

        String date = tokenizer.nextSquared();
        if (date == null) {
            return null;
        } else {
            entry.setDatetime(DateUtils.parseDateWithBraces(date));
        }

        String request = tokenizer.nextQuoted();
        if (request == null) {
            return null;
        } else {
            entry.setRequest(request);
        }

        String status = tokenizer.nextWord();
        if (status == null) {
            return null;
        } else {
            entry.setStatus(status);
        }

        Integer bytes = tokenizer.nextInt();
        if (bytes == null) {
            return null;
        } else {
            entry.setTransferredBytes(bytes);
        }

        Double responseTime = tokenizer.nextDouble();
        if (responseTime == null) {
            return null;
        } else {
            entry.setResponseTime(responseTime);
        }

        String referer = tokenizer.nextQuoted();
        if (referer == null) {
            return null;
        } else {
            entry.setReferer(referer);
        }

        String userAgent = tokenizer.nextQuoted();
        if (userAgent == null) {
            return null;
        } else {
            entry.setUserAgent(userAgent);
        }

        String prio = tokenizer.nextWord();
        if (prio == null) {
            return null;
        } else {
            entry.setPrio(prio);
        }

        return entry;
    }
}
