package eg.edu.guc.dbms.utils;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggerFormater extends Formatter {

	@Override
	public String format(LogRecord record) {
	    String formattedMessage = formatMessage(record);
	    String throwable = "";
	    String outputFormat = "%1$s : %2$s\n";
	    return String.format(outputFormat,record.getLevel().getName(),formattedMessage,throwable);
	}

}