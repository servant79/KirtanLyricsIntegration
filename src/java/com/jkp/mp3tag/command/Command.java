package com.jkp.mp3tag.command;

import java.io.File;

public interface Command {

	void execute(File file);

	boolean isAction();

	void setAction(boolean action);

	void setNext(Command command);
	
	Command next();

	void finalExecute();

}
