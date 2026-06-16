package engine.logs;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * @author Pr. Olivier Gruber
 */
public class LoggerManager {
	private static Map<String, Logger> loggers;
	static {
		loggers = new Hashtable<String, Logger>();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down logging...");
			for (Logger logger : loggers.values()) {
				for (Handler h : logger.getHandlers()) {
					try {
						h.close();
					} catch (Exception e) {
						// nothing to do here.
					}
				}
			}
			System.out.println("--> handlers are flushed and closed.");
		}));
	}

	/**
	 * Get a logger by name
	 * 
	 * @param s like a class name for example
	 * @return a logger
	 */
	public static Logger getLogger(String s) {
		Logger l = loggers.get(s);
		if (l == null) {
			l = Logger.getLogger(s);
			loggers.put(s, l);
		}
		return l;
	}
}