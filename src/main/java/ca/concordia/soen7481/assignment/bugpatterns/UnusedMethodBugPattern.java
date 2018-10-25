package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class UnusedMethodBugPattern extends BugPattern {

	public UnusedMethodBugPattern(int line, File file, String functionName) {
		super(line, file, functionName);
	}

	@Override
	public String getIdentifier() {
		return "UM";
	}

	@Override
	public String getName() {
		return "Unused method";
	}

	@Override
	public String getDescription() {
		return "The method is defined but never used";
	}

}
