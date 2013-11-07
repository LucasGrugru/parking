package logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {

	public String format(LogRecord rec) {
		StringBuffer buf = new StringBuffer(1000);

		buf.append("\n\n--------------------------\n--  ");
		buf.append(calcDate(rec.getMillis()));
		buf.append("  --\n");
		buf.append(formatMessage(rec));
		return buf.toString();
	}

	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}

	// This method is called just after the handler using this
	// formatter is created
	public String getHead(Handler h) {
		return "\t"+new Date();
	}

	// This method is called just after the handler using this
	// formatter is closed
	public String getTail(Handler h) {
		return "";
	}

}
