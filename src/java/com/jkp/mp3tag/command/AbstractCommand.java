package com.jkp.mp3tag.command;

public abstract class AbstractCommand implements Command{
	protected String commandStr;
	protected boolean action = false;
	private Command next;

	@Override
	public boolean isAction() {
		return action;
	}

	@Override
	public void setAction(boolean action) {
		this.action = action;
	}
	
	@Override
	public void setNext(Command command) {
		this.next = command;
	}
	
	@Override
	public Command next() {
		// TODO Auto-generated method stub
		return next;
	}


}
