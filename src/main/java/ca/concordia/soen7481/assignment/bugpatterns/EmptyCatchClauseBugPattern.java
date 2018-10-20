package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class EmptyCatchClauseBugPattern extends BugPattern {
    public EmptyCatchClauseBugPattern(int line, File file, String functionName) {
        super(line, file, functionName);
    }

    @Override
    public String getIdentifier() {
        return "EC";
    }

    @Override
    public String getName() {
        return "Empty Catch Clause";
    }

    @Override
    public String getDescription() {
        return "Empty Catch block found";
    }
}