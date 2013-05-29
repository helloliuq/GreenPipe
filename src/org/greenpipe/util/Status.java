package org.greenpipe.util;

/**
 * Indicate the status of the workflows and blocks
 * @author 
 *
 */
public class Status {
	public static final int WAITING = 0;
	public static final int QUEUING = 1;
	public static final int READY = 2;
	public static final int RUNNING = 3;
	public static final int COMPLETED = 4;
	public static final int FAILED = 5;
	public static final int KILLED = 6;
}
