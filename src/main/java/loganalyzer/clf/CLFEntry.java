package loganalyzer.clf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Common Log Format
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CLFEntry {
    private String ip;
    private String ident;
    private String userid;
    private LocalDateTime datetime;
    private String timezone;
    private String request;
    private String status;
    private int transferredBytes;
    private double responseTime;
    private String referer;
    private String userAgent;
    private String prio;
}
