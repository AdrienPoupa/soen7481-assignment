package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class DuplicateLoggingStatementInCatchBlockOfSameTryBugPattern extends BugPattern {

	public DuplicateLoggingStatementInCatchBlockOfSameTryBugPattern(int line, File file, String functionName) {
		super(line, file, functionName);
		// TODO Auto-generated constructor stub
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
		
		return "Developers usually rely on logs for error diagnostics when exceptions occur. However, sometimes, duplicate logging statements in different catch blocks of the same try blockmay cause debugging difficulties since the logs fail to tell which exception occurred.";
	}
	
	

}
