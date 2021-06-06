package jms.system;


import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jms.config.Configuration;
import jms.enums.JobState;
import jms.job.Job;

/**
 * This class is used to schedule jobs in a priority based queue.
 * This class assumes jobs are to be ran asynchronous in the event of a blocking job such as waiting for a file
 * so that other jobs that are scheduled are not blocked to be executed.
 * @author Caleb Bishop
 *
 */
public class JobScheduler implements Runnable {
	
	private Thread thread;
	private Queue<Job> jobs;
	private Object jobLock = new Object();
	private boolean isRunning = false;
	private int pollTime = 500;
	private ExecutorService executorService;
	private JobExecutor jobExecutor;
	
	/**
	 * JobScheduler Constructor.
	 * Initializes a queue that prioritizes based off scheduled time.
	 * If scheduled times are similar the priority queue will take into account which job has the higher priority
	 * to schedule.
	 */
	public JobScheduler() {
		executorService = Executors.newFixedThreadPool(Configuration.SchedulerThreadPoolAmount);
		jobExecutor = new JobExecutor();
		
		
		jobs = new PriorityQueue<Job>((job1,job2) -> {		
			if(job1 == null && job2 == null) return 0;
			if(job1 == null) return -1;
			if(job2 == null) return 1;
			
			int result = -1;
			long jobOneSchedule = job1.getScheduledTime();
			long jobTwoSchedule = job2.getScheduledTime();
			
			if(jobOneSchedule == jobTwoSchedule) {
				result = job1.getPriority() < job2.getPriority() ? 1 : 
					job1.getPriority() > job2.getPriority() ? -1 : 0;
			}else {
				result = jobOneSchedule > jobTwoSchedule ? 1 : -1;
			}
			
			return result;	
		});
	}
	
	
	/**
	 * This method adds the job to the priority queue to be scheduled.
	 * @param job Job to execute
	 * @param time - time in milliseconds(Assumes user provides epoch date)
	 */
	public synchronized void scheduleJob(Job job, long time) {
		
		if(job == null) return;
		if(jobs.contains(job)) return;
		
		if(time <= 0) {
			jobExecutor.executeJobAsync(job);
			return;
		}
		job.setScheduledTime(time);
		job.setJobState(JobState.QUEUED);
		jobs.add(job);
		
		synchronized(jobLock) {
			jobLock.notify();
		}
		
		
	}
	
	/**
	 * Starts Job Scheduler
	 */
	public void start() {
		if(thread == null) {
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.setName("Job Scheduler Thread");	
			isRunning = true;
			this.thread.start();
		}		
	}
	
	/**
	 * Runs Job Scheduler
	 */
	@Override
	public void run() {
		
		while(isRunning) {
			try {
				//Lock until jobs scheduled.
				synchronized(jobs) {
					if(jobs.isEmpty()) {
						synchronized(jobLock) {
							jobLock.wait(pollTime);
						}
					}else {		
						Job next = jobs.peek();
						if(next.shouldExecute())
							jobExecutor.executeJobAsync(jobs.poll(),()->{
								System.out.println("Job Finished : " + next.getJobState());
							});
					}
				}

			}catch(Exception e) {
				//Log errors
				//e.printStackTrace();
			}		
		}	
	}
	
	/**
	 * @return true if empty, false otherwise
	 */
	public boolean jobQueueIsEmpty() {
		return jobs.isEmpty();
	}
	
	/**
	 * @return queue size
	 */
	public int getQueueSize() {
		return this.jobs.size();
	}
	
	/**
	 * Get next job to be scheduled
	 * @return head of queue
	 */
	public Job getNextJob() {
		return this.jobs.peek();
	}
	
	/**
	 * Gets the job queue
	 * @return jobs - queue
	 */
	public Queue<Job> getQueue() {
		return this.jobs;
	}
	
	/**
	 * Returns true if running, false otherwise.
	 * @return isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * Sets isRunning to false
	 */
	public void stop() {
		if(isRunning) {
			this.executorService.shutdownNow();
			this.isRunning = false;
			this.thread = null;
		}
	}
	
	

}
