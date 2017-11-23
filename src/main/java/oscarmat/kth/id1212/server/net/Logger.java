package oscarmat.kth.id1212.server.net;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logs messages from server to a designated output stream.
 */
class Logger {

    private static final String ERROR = "ERROR";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final PrintStream stream;
    private final DateTimeFormatter formatter;

    /**
     * Create a new logger with the assigned output stream.
     * @param out Stream to log messages to.
     */
    Logger(OutputStream out) {
        stream = new PrintStream(out);
        formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    }

    /**
     * Log an error message.
     * @param senderName Name of the sender.
     * @param message Contents of the message.
     */
    void putError(String senderName, String message) {
        putMessage(senderName, message, ERROR);
    }

    /**
     * Print a message to the log.
     * @param senderName Name of the sender.
     * @param message Contents of the message.
     * @param messageType Type of the message.
     */
    public void putMessage(String senderName, String message, String messageType) {
        String timeStamp = getTimeStamp();

        StringBuilder builder = new StringBuilder();
        builder.append("[").append(timeStamp).append("] ");
        builder.append("[").append(messageType).append("] ");
        builder.append("<").append(senderName).append("> ");
        builder.append(message);

        stream.println(builder.toString());
    }

    /**
     * @return Current timestamp.
     */
    private String getTimeStamp() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(formatter);
    }
}
