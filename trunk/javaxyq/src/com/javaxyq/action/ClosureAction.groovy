package com.javaxyq.action;

import com.javaxyq.event.ActionEvent;

class ClosureAction extends BaseAction {
	
	private Closure closure;
	public ClosureAction(Closure closure) {
		super();
		this.closure = closure;
	}

	@Override
	public void doAction(ActionEvent e) {
		this.closure.call e;
	}
	
}