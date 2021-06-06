package test;

import java.io.File;
import org.junit.jupiter.api.Test;
import jms.config.Configuration;
import jms.enums.JobCode;
import jms.enums.JobState;
import jms.job.CommandJob;
import jms.job.Job;
import jms.job.WaitForFileEvent;
import jms.system.JobScheduler;
import jms.util.Time;

/**
 * Purpose of class is used to test the JMS.
 * @author Caleb Bishop
 *
 */
class TestJMS {
	

	/**
	 * Test job priority is being handled correctly.
	 */
	@Test
	void testJobPriority() {
		JobScheduler jobScheduler = new JobScheduler();
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
		
		jobScheduler.scheduleJob(jobTwo,timeSchedule);
		jobScheduler.scheduleJob(jobOne,timeSchedule);
		
		assert(!jobScheduler.jobQueueIsEmpty());
		assert(jobScheduler.getQueue().peek() == jobOne);
		
		jobScheduler.scheduleJob(jobFour,timeSchedule);
		
		assert(jobScheduler.getQueue().peek() == jobOne);
		
		jobScheduler.scheduleJob(jobThree,timeSchedule);
		assert(jobScheduler.getQueue().peek() == jobThree);		
		
	}
	
	/**
	 * Test that jobs are being scheduled accordingly to their time set to execute.
	 */
	@Test
	public void testTimeScheduleQueue() {
		JobScheduler jobScheduler = new JobScheduler();
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
		
		
		jobScheduler.scheduleJob(jobTwo,timeSchedule + 10000);
		assert(jobScheduler.getQueue().peek() == jobTwo);
		jobScheduler.scheduleJob(jobThree,timeSchedule + 25000);
		assert(jobScheduler.getQueue().peek() == jobTwo);
		jobScheduler.scheduleJob(jobOne,timeSchedule + 5000);
		assert(jobScheduler.getQueue().peek() == jobOne);
		jobScheduler.scheduleJob(jobFour,timeSchedule + 30000);
		assert(jobScheduler.getQueue().peek() == jobOne);
		jobScheduler.getQueue().remove();
		assert(jobScheduler.getQueue().peek() == jobTwo);
	}
	
	/**
	 * Test the state of the jobs are being set correctly upon executions.
	 * @throws InterruptedException
	 */
	@Test
	public void testJobState() throws InterruptedException {
		JobScheduler jobScheduler = new JobScheduler();
		Job jobOne = new CommandJob(()-> {
			return JobCode.SUCCESS;
		},5);
		long timeSchedule = System.currentTimeMillis();
		
		assert(jobOne.getJobState() == null);
		jobScheduler.scheduleJob(jobOne, timeSchedule + 1000);
		jobScheduler.start();
		
		
		assert(jobOne.getJobState() == JobState.QUEUED);
		
		JobState state = jobOne.getJobState();
		while((state = jobOne.getJobState()) != JobState.RUNNING) {
			Thread.sleep(0);
		}
		assert(state == JobState.RUNNING);
		
		Thread.sleep(1500);
		assert(jobOne.getJobState() == JobState.SUCCESS);
		
		assert(jobScheduler.isRunning());
		
		jobScheduler.stop();
	}
	
	
	
	
	
	/**
	 * Test waiting for file event executes correctly.
	 * Creates a temp file for the event to wait upon before finishing.
	 */
	@Test
	public void testWaitForFileEventSuccess() {
		WaitForFileEvent fileEvent = new WaitForFileEvent("testZandar123.txt");
		try {
			JobScheduler scheduler = new JobScheduler();
			long initTime =  System.currentTimeMillis() + 1000;
			scheduler.scheduleJob(fileEvent,initTime);
			scheduler.start();
			
			Thread.sleep(1100);
			File testFile = new File("testZandar123.txt");
			testFile.createNewFile();
			Thread.sleep(500);
			assert(fileEvent.getJobState() == JobState.SUCCESS);
			
			testFile.delete();
			 
			scheduler.stop();
			
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
	public void testWaitForFileEventTimeout() {
		WaitForFileEvent fileEvent = new WaitForFileEvent("testZandar1234.txt",1000);
		try {
			JobScheduler scheduler = new JobScheduler();
			long initTime =  System.currentTimeMillis() + 1000;
			scheduler.scheduleJob(fileEvent,initTime);
			scheduler.start();		
			Thread.sleep(2500);
			assert(fileEvent.getJobState() == JobState.FAILED); 
			scheduler.stop();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
