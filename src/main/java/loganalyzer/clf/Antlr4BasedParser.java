package loganalyzer.clf;

import clf.antlr4.CommonLogFormatBaseListener;
import clf.antlr4.CommonLogFormatLexer;
import clf.antlr4.CommonLogFormatParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.IntStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class Antlr4BasedParser implements CLFParser {

    @Override
    public Iterable<CLFEntry> parse(InputStream input) throws IOException {
        BufferedTokenStream tokenStream = new BufferedTokenStream(new CommonLogFormatLexer(new ANTLRInputStream(input)));
        return () -> new CLFIterable(tokenStream);
    }

    private static class CLFIterable implements Iterator<CLFEntry> {
        private final BufferedTokenStream tokenStream;
        private final CommonLogFormatParser parser;
        private final EntryListener listener;

        private CLFIterable(BufferedTokenStream tokenStream) {
            this.tokenStream = tokenStream;
            this.parser = new CommonLogFormatParser(tokenStream);
            this.listener = new EntryListener();
            this.parser.addParseListener(listener);
        }

        @Override
        public boolean hasNext() {
            return tokenStream.LA(1) != IntStream.EOF;
        }

        @Override
        public CLFEntry next() {
            parser.entry();
            return listener.getCurrent();
        }
    }

    private static class EntryListener extends CommonLogFormatBaseListener {

        private CLFEntry current;

        public CLFEntry getCurrent() {
            if (current == null) {
                throw new IllegalStateException("Current log entry is not set");
            }
            return current;
        }

        @Override
        public void enterEntry(CommonLogFormatParser.EntryContext ctx) {
            current = new CLFEntry();
        }

        @Override
        public void exitLog(CommonLogFormatParser.LogContext ctx) {
            current = null;
        }

        @Override
        public void exitIp(CommonLogFormatParser.IpContext ctx) {
            current.setIp(ctx.IPV4().getText());
        }

        @Override
        public void exitIdent(CommonLogFormatParser.IdentContext ctx) {
            current.setIdent(ctx.getText());
        }

        @Override
        public void exitUser(CommonLogFormatParser.UserContext ctx) {
            current.setUserid(ctx.getText());
        }

        @Override
        public void exitDateTime(CommonLogFormatParser.DateTimeContext ctx) {
            current.setDatetime(DateUtils.parseDateWithBraces(ctx.getText()));
        }

        @Override
        public void exitRequest(CommonLogFormatParser.RequestContext ctx) {
            current.setRequest(ctx.getText());
        }

        @Override
        public void exitStatus(CommonLogFormatParser.StatusContext ctx) {
            current.setStatus(ctx.getText());
        }

        @Override
        public void exitBytes(CommonLogFormatParser.BytesContext ctx) {
            current.setTransferredBytes(Integer.parseInt(ctx.getText()));
        }

        @Override
        public void exitResponseTime(CommonLogFormatParser.ResponseTimeContext ctx) {
            current.setResponseTime(Double.parseDouble(ctx.getText()));
        }

        @Override
        public void exitReferer(CommonLogFormatParser.RefererContext ctx) {
            current.setReferer(ctx.getText());
        }

        @Override
        public void exitUserAgent(CommonLogFormatParser.UserAgentContext ctx) {
            current.setUserAgent(ctx.getText());
        }

        @Override
        public void exitPrio(CommonLogFormatParser.PrioContext ctx) {
            current.setPrio(ctx.getText());
        }
    }
}
