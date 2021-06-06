package jms.job;

import java.io.File;

import jms.enums.JobCode;
import jms.enums.JobState;

/**
 * Simple event/job used to wait for a file provided the path/filename
 * @author Caleb Bishop
 *
 */
public class WaitForFileEvent extends Job {

	private int pollTime = 500; // 0.5 seconds
	private long timeout = 50000; //50 seconds
	private String fileName;
	
	/**
	 * Constructor
	 * @param fileName - file to wait for
	 */
	public WaitForFileEvent(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	/**
	 * Constructor to specify a timeout for waiting on the file
	 * @param fileName - file to wait on
	 * @param timeout - amount of time to wait until failure milliseconds
	 */
	public WaitForFileEvent(String fileName, long timeout) {
		this(fileName);
		this.timeout = timeout;
	}
	
	/**
	 * Constructor
	 * @param filename - file to wait for
	 * @param priority - priority for the job
	 */
	public WaitForFileEvent(String fileName, int priority, long timeout) {
		this(fileName,timeout);
		super.setPriority(priority);
		this.timeout = timeout;
	}	
	
	
	/**
	 * Executes the job.
	 * Checks and waits to see if the file exists.
	 * 
	 */
	@Override
	public int execute() {
		try {
			File file = new File(fileName);
			long startTime = System.currentTimeMillis();
			while(!file.exists()) {
				Thread.sleep(pollTime);
				
				if(System.currentTimeMillis() - startTime >= timeout) {
					return JobCode.ERROR_EVENT_WAITFORFILE;
				}
				
			}
			return JobCode.SUCCESS_EVENT_WAITFORFILE;
		}catch(Exception e) {
			e.printStackTrace();
			return JobCode.ERROR_EVENT_WAITFORFILE;
		}
	}

}
