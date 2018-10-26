package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class IneffectiveConditionBugPattern extends BugPattern {

	public IneffectiveConditionBugPattern(int line, File file, String functionName) {
		super(line, file, functionName);
	}

	@Override
	public String getIdentifier() {
		return "IC";
	}

	@Override
	public String getName() {
		return "Ineffective condition";
	}

	@Override
	public String getDescription() {
		return "Condition is ineffective";
	}

}
