package loganalyzer.clf;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.StringTokenizer;

public class TokenBasedParser implements CLFParser {

    @Override
    public Iterable<CLFEntry> parse(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        Iterable<String> rows = () -> new LineIterator(reader);
        Iterable<CLFEntry> entries = IterableUtils.transformedIterable(rows, this::parse);
        return IterableUtils.filteredIterable(entries, Objects::nonNull);
    }

    private CLFEntry parse(String row) {
        StringTokenizer tokenizer = new StringTokenizer(row);
        CLFEntry entry = new CLFEntry();

        String ip = tokenizer.nextToken();
        if (ip == null) {
            return null;
        } else {
            entry.setIp(ip);
        }

        String ident = tokenizer.nextToken();
        if (ident == null) {
            return null;
        } else {
            entry.setIdent(ident);
        }

        String userId = tokenizer.nextToken();
        if (userId == null) {
            return null;
        } else {
            entry.setUserid(userId);
        }

        String date = tokenizer.nextToken();
        String tz = tokenizer.nextToken();
        if (date == null || tz == null) {
            return null;
        } else {
            entry.setDatetime(DateUtils.parseDateWithBraces(date));
            entry.setTimezone(tz);
        }

        String method = tokenizer.nextToken();
        String url = tokenizer.nextToken();
        String protocol = tokenizer.nextToken();
        if (method == null || url == null || protocol == null) {
            return null;
        } else {
            entry.setRequest(method + " " + url + protocol);
        }

        String status = tokenizer.nextToken();
        if (status == null) {
            return null;
        } else {
            entry.setStatus(status);
        }

        Integer bytes = Integer.parseInt(tokenizer.nextToken());
        if (bytes == null) {
            return null;
        } else {
            entry.setTransferredBytes(bytes);
        }

        Double responseTime = Double.parseDouble(tokenizer.nextToken());
        if (responseTime == null) {
            return null;
        } else {
            entry.setResponseTime(responseTime);
        }

        String referer = tokenizer.nextToken();
        if (referer == null) {
            return null;
        } else {
            entry.setReferer(referer);
        }

        String userAgent = tokenizer.nextToken();
        if (userAgent == null) {
            return null;
        } else {
            entry.setUserAgent(userAgent);
        }

        String prio = tokenizer.nextToken();
        if (prio == null) {
            return null;
        } else {
            entry.setPrio(prio);
        }

        return entry;
    }
}
