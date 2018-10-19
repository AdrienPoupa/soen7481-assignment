package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class OvercatchExceptionTerminationBugPattern extends BugPattern {
    public OvercatchExceptionTerminationBugPattern(int line, File file, String functionName) {
        super(line, file, functionName);
    }

    @Override
    public String getIdentifier() {
        return "OC";
    }

    @Override
    public String getName() {
        return "Overcatch Exception Termination";
    }

    @Override
    public String getDescription() {
        return "Over-catching an exception with system-termination";
    }
}
