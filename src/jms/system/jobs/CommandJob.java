package jms.system.jobs;

import jms.enums.JobCode;
import jms.enums.JobState;
import jms.system.Job;
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
	protected int execute() {
		try {			
			return this.command.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return JobCode.ERROR;
		}
	}

}
