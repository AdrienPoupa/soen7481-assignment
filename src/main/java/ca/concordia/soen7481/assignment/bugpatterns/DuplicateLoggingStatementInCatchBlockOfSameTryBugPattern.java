package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class DuplicateLoggingStatementInCatchBlockOfSameTryBugPattern extends BugPattern {
	public DuplicateLoggingStatementInCatchBlockOfSameTryBugPattern(int line, File file, String functionName) {
		super(line, file, functionName);
	}

	@Override
	public String getIdentifier() {
		return "IL";
	}

	@Override
	public String getName() {
		return "Duplicate Logging Statement In Catch Block Of Same Try";
	}

	@Override
	public String getDescription() {
		return "Duplicate logging statements in different catch blocks of the same try block may cause debugging difficulties.";
	}
}
