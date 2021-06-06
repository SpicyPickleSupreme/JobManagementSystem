package jms.job;

import jms.enums.JobCode;
import jms.enums.JobState;
import jms.util.Command;

/**
 * Generic job for executing a command.
 * @author Caleb Bishop
 *
 */
public class CommandJob extends Job {

	private Command command;
	
	/**
	 * Constructor
	 * @param command - command to be executed
	 */
	public CommandJob(Command command) {
		super();
		this.command = command;
	}
	
	/**
	 * Constructor
	 * @param command - command to be executed
	 * @param priority - job priority
	 */
	public CommandJob(Command command, int priority) {
		super(priority);
		this.command = command;
	}

	/**
	 * Executes the given command and return a code to state whether job was success or failure.
	 */
	@Override
	public int execute() {
		try {
			super.setJobState(JobState.RUNNING);
			
			int result = this.command.execute();
			
			super.setJobState(result >= JobCode.SUCCESS ? JobState.SUCCESS : JobState.FAILED);
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return JobCode.ERROR;
		}
	}

}
