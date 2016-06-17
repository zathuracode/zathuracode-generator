package org.zcode.eclipse.plugin.generator.utilities;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.eclipse.plugin.generator.ZathuraGeneratorActivator;


// TODO: Auto-generated Javadoc
/**
 * The Class ZathuraGeneratorLog.
 *
 * @author Diego Armando Gomez Mosquera dgomez@vortexbird.com
 */

public class ZathuraGeneratorLog {
	
	 private static final Logger log = LoggerFactory.getLogger(ZathuraGeneratorLog.class);

	/**
	 * Log info.
	 *
	 * @param message the message
	 */
	public static void logInfo(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);
		log.info(message);
	}

	/**
	 * Log error.
	 *
	 * @param exception the exception
	 */
	public static void logError(Throwable exception) {
		logError("Unexpected Exception", exception);
		log.error(exception.getMessage());
	}

	/**
	 * Log error.
	 *
	 * @param message the message
	 * @param exception the exception
	 */
	public static void logError(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
		log.error(message);
	}

	/**
	 * Log.
	 *
	 * @param severity the severity
	 * @param code the code
	 * @param message the message
	 * @param exception the exception
	 */
	public static void log(int severity, int code, String message, Throwable exception) {
		log(createStatus(severity, code, message, exception));
		log.info(message);
	}

	/**
	 * Creates the status.
	 *
	 * @param severity the severity
	 * @param code the code
	 * @param message the message
	 * @param exception the exception
	 * @return the i status
	 */
	public static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		IStatus status = new Status(severity, ZathuraGeneratorActivator.PLUGIN_ID, code, message, exception);
		return status;
	}

	/**
	 * Log.
	 *
	 * @param status the status
	 */
	public static void log(IStatus status) {
		ZathuraGeneratorActivator.getDefault().getLog().log(status);
	}

}
