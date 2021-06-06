package jms.enums;

/**
 * To allow creation of different types of jobs and supporting codes
 * @author Caleb Bishop
 *
 */
public class JobCode {
	
	/**
	 * Code <0-699 Error Codes
	 * Codes 700+ Success Codes
	 */
	public static int ERROR = 0;
	public static int ERROR_EVENT_WAITFORFILE=1;
	public static int ERROR_EXCEPTION = 2;
	
	
	public static int SUCCESS = 700;
	public static int SUCCESS_EVENT_WAITFORFILE = 701;
	
	
	
}
