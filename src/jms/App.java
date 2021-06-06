package jms;

import jms.enums.JobCode;
import jms.system.Job;
import jms.system.JobManagementService;
import jms.system.jobs.CommandJob;
import jms.system.jobs.WaitForFileEvent;

/**
 * Simple example application on using the JMS.
 * Runs until killed.
 * @author Caleb
 *
 */
public class App {

	/**
	 * Starts the JMS
	 * @param args
	 */
	public static void main(String[] args) {
		
		Runtime runtime = Runtime.getRuntime();
		
		Job printHelloWorld = new CommandJob(()->{
			System.out.println("Hello World");
			return JobCode.SUCCESS;
		},10);
		
		Job waitForFile = new WaitForFileEvent("WheresMyFile.txt",15,5000);
		long scheduleFor = System.currentTimeMillis() + 1000;
		
		JobManagementService.scheduleJob(waitForFile, scheduleFor);
		JobManagementService.scheduleJob(printHelloWorld, scheduleFor);
		
		
		runtime.addShutdownHook(new Thread(()->{
			JobManagementService.stop();
		}));
		
		
		JobManagementService.start();
		
	}

}
