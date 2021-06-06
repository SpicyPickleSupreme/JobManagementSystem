package jms;

import jms.enums.JobCode;
import jms.job.CommandJob;
import jms.job.Job;
import jms.job.WaitForFileEvent;
import jms.system.JobManagementService;

/**
 * Simple example application on using the JMS.
 * @author Caleb Bishop
 *
 */
public class App {

	public static void main(String[] args) {
		
		
		JobManagementService.start();
		
		Job printHelloWorld = new CommandJob(()->{
			System.out.println("Hello World");
			return JobCode.SUCCESS;
		},10);
		
		Job waitForFile = new WaitForFileEvent("WheresMyFile.txt",15,5000);
		
		long scheduleFor = System.currentTimeMillis() + 1000;
		
		JobManagementService.scheduleJob(waitForFile, scheduleFor);
		JobManagementService.scheduleJob(printHelloWorld, scheduleFor);
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JobManagementService.stop();
		}
		
	}

}
