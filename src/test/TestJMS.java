package test;

import java.io.File;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jms.enums.JobCode;
import jms.enums.JobState;
import jms.job.CommandJob;
import jms.job.Job;
import jms.job.WaitForFileEvent;
import jms.system.JobManagementService;
import jms.system.JobScheduler;

/**
 * Purpose of class is used to test the JMS.
 * @author Caleb Bishop
 *
 */
@TestMethodOrder(OrderAnnotation.class)
class TestJMS {
	

	@BeforeAll
	static void setup() {
		JobManagementService.startAsync();
	}
	
	@Test
	@Order(1)
	void testServiceStarted() {
		assert(JobManagementService.isRunning);
	}
	
	
	/**
	 * Test job priority is being handled correctly.
	 */
	@Test
	@Order(2)
	void testJobPriority() {
		
		Job jobOne = new CommandJob(()-> {
			return JobCode.SUCCESS;
		},5);
		
		Job jobTwo = new CommandJob(()->{
			return JobCode.ERROR;
		});
		
		Job jobFour = new CommandJob(()->{
			return JobCode.ERROR;
		});
		
		Job jobThree = new CommandJob(()->{
			return JobCode.SUCCESS;
		},25);
		
		long timeSchedule = System.currentTimeMillis();
		
		JobManagementService.scheduleJob(jobTwo,timeSchedule);
		JobManagementService.scheduleJob(jobOne,timeSchedule);
		
		assert(JobManagementService.getScheduledJobCount() != 0);
		assert(JobManagementService.isJobScheduledNext(jobOne));
		
		JobManagementService.scheduleJob(jobFour,timeSchedule);
		
		assert(JobManagementService.isJobScheduledNext(jobOne));
		
		JobManagementService.scheduleJob(jobThree,timeSchedule);
		assert(JobManagementService.isJobScheduledNext(jobThree));		
		
	}
	
	/**
	 * Test that jobs are being scheduled accordingly to their time set to execute.
	 */
	@Test
	@Order(3)
	public void testTimeScheduleQueue() {
		
		Job jobOne = new CommandJob(()-> {
			return JobCode.SUCCESS;
		},5);
		
		Job jobTwo = new CommandJob(()->{
			return JobCode.ERROR;
		});
		
		Job jobFour = new CommandJob(()->{
			
			return JobCode.ERROR;
		});
		
		Job jobThree = new CommandJob(()->{
			return JobCode.SUCCESS;
		},25);
		
		long timeSchedule = System.currentTimeMillis() + 5000;
		
		
		JobManagementService.scheduleJob(jobTwo,timeSchedule + 10000);
		assert(JobManagementService.isJobScheduledNext(jobTwo));
		JobManagementService.scheduleJob(jobThree,timeSchedule + 25000);
		assert(JobManagementService.isJobScheduledNext(jobTwo));
		JobManagementService.scheduleJob(jobOne,timeSchedule + 5000);
		assert(JobManagementService.isJobScheduledNext(jobOne));
		JobManagementService.scheduleJob(jobFour,timeSchedule + 30000);
		assert(JobManagementService.isJobScheduledNext(jobOne));
		assert(JobManagementService.unscheduleJob(jobOne));
		assert(JobManagementService.isJobScheduledNext(jobTwo));
	}
	
	/**
	 * Test the state of the jobs are being set correctly upon executions.
	 * @throws InterruptedException
	 */
	@Test
	@Order(4)
	public void testJobState() throws InterruptedException {
		
		
		Job jobOne = new CommandJob(()-> {
			return JobCode.SUCCESS;
		},5);
		long timeSchedule = System.currentTimeMillis();
		
		assert(jobOne.getJobState() == null);
		JobManagementService.scheduleJob(jobOne, timeSchedule);
		
		
		
		assert(jobOne.getJobState() == JobState.QUEUED);
		
		JobState state = jobOne.getJobState();
		while((state = jobOne.getJobState()) != JobState.RUNNING) {
			
		}
		assert(state == JobState.RUNNING);
		
		Thread.sleep(1500);
		assert(jobOne.getJobState() == JobState.SUCCESS);
		
		
	}
	
	
	
	
	
	/**
	 * Test waiting for file event executes correctly.
	 * Creates a temp file for the event to wait upon before finishing.
	 */
	@Test
	@Order(5)
	public void testWaitForFileEventSuccess() {
		WaitForFileEvent fileEvent = new WaitForFileEvent("testZandar123.txt");
		try {
			
			long initTime =  System.currentTimeMillis() + 1000;
			JobManagementService.scheduleJob(fileEvent, initTime);
			
			Thread.sleep(1100);
			File testFile = new File("testZandar123.txt");
			testFile.createNewFile();
			Thread.sleep(500);
			assert(fileEvent.getJobState() == JobState.SUCCESS);
			
			testFile.delete();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test that the WaitForFileEvent will timeout after the timeout provided
	 * and get the result accordingly
	 */
	@Test
	@Order(6)
	public void testWaitForFileEventTimeout() {
		WaitForFileEvent fileEvent = new WaitForFileEvent("testZandar1234.txt",1000);
		try {
			
			long initTime =  System.currentTimeMillis() + 1000;
			JobManagementService.scheduleJob(fileEvent, initTime);	
			Thread.sleep(2500);
			assert(fileEvent.getJobState() == JobState.FAILED); 	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(7)
	void testServiceStopped() {
		JobManagementService.stop();
		assert(!JobManagementService.isRunning);
	}
	
	@AfterAll
	static void after() {
		JobManagementService.stop();
	}
	
	
	
	
	
	

}
