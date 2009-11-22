package com.javaxyq.util;

import groovy.lang.Closure;

class ClosureTask extends TimerTask{
	private Closure task;
	public ClosureTask(Closure task) {
		this.task = task;
	}
	public void run() {
		task.call();
	}
}