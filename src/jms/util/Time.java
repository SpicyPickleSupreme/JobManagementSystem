package jms.util;

public class Time {

	public static long milliToSec(long milliseconds) {
		return milliseconds / 1000;
	}
	
	public static long secToMilli(long seconds) {
		return seconds * 1000;
	}
	
}
