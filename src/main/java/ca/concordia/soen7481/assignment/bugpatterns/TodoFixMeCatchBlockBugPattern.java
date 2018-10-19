package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class TodoFixMeCatchBlockBugPattern extends BugPattern {
    public TodoFixMeCatchBlockBugPattern(int line, File file, String functionName) {
        super(line, file, functionName);
    }

    @Override
    public String getIdentifier() {
        return "UF";
    }

    @Override
    public String getName() {
        return "Todo FixMe Catch Block";
    }

    @Override
    public String getDescription() {
        return "Unfinished exception handling code";
    }
}
