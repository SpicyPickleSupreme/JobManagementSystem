package jms.system;


import jms.enums.JobState;


/**
 * The job class is used to create jobs to execute actions.
 * These jobs can be executed right away, or scheduled.
 * @author Caleb
 *
 */
public abstract class Job {

	/**
	 * 
	 */
	private int priority;
	private JobState jobState;
	private long scheduledTime;
	private int jobResultCode;
	
	/**
	 * Default constructor
	 */
	public Job() {
		jobState = null;
		this.priority = 0;
		this.scheduledTime = -1;
	}
	
	
	/**
	 * Constructor sets priority, priority is only taken into consideration if jobs are scheduled at the same time.
	 * @param priority priority of the job
	 */
	public Job(int priority) {
		this();
		this.priority = priority;
	}
	
	/**
	 * Abstract method execute allows for the creation of different type of jobs apart from generic jobs.
	 * @return Integer - result of execution
	 */
	protected abstract int execute();
	
	/**
	 * Set the job state.
	 * @param state
	 */
	protected void setJobState(JobState state) {
		this.jobState = state;
	}
	
	/**
	 * Return the jobs current state.
	 * @return jobState
	 */
	public JobState getJobState() {
		return this.jobState;
	}
	
	/**
	 * Return the code result of the executed job.
	 * @return jobResultCode
	 */
	public int getJobResultCode() {
		return this.jobResultCode;
	}
	
	/**
	 * Set the job result code
	 */
	protected void setJobResultCode(int jobCode) {
		this.jobResultCode = jobCode;
	}
	
	/**
	 * Gets the current priority of the job.
	 * @return priority
	 */
	public int getPriority() {
		return this.priority;
	}
	
	/**
	 * Set the priority of the current job.
	 * @param priority - job priority
	 */
	protected void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * Set the time in milliseconds to allow for scheduling
	 * @param time - milliseconds
	 */
	protected void setScheduledTime(long time) {
		this.scheduledTime = time;
	}
	
	/**
	 * Get time for job to be scheduled in milliseconds.
	 * @return scheduled time.
	 */
	public long getScheduledTime() {
		return this.scheduledTime;
	}
	
	/**
	 * This method checks to see if the jobs scheduled time is within the time to execute.
	 * @return true if ready to be executed, false otherwise
	 */
	public boolean shouldExecute() {
		return getTimeTillExecute() <= 0 ? true : false;
	}
	
	/**
	 * Gets the time till job execution
	 * @return time till job execution.
	 */
	public long getTimeTillExecute() {
		return this.scheduledTime - System.currentTimeMillis();
	}
	
	
}
