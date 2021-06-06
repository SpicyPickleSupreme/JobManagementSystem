package jms.util;

/**
 * Utility class to help with time operations
 * @author Caleb
 *
 */
public class Time {

	/**
	 * Converts milliseconds to seconds
	 * @param milliseconds
	 * @return seconds
	 */
	public static long milliToSec(long milliseconds) {
		return milliseconds / 1000;
	}
	
	/**
	 * Converts seconds to milliseconds
	 * @param seconds
	 * @return milliseconds
	 */
	public static long secToMilli(long seconds) {
		return seconds * 1000;
	}
	
}
