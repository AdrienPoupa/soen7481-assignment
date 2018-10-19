package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class OpenStreamBugPattern extends BugPattern {
    public OpenStreamBugPattern(int line, File file, String functionName) {
        super(line, file, functionName);
    }

    @Override
    public String getIdentifier() {
        return "OS";
    }

    @Override
    public String getName() {
        return "Open Stream";
    }

    @Override
    public String getDescription() {
        return "Method may fail to close stream";
    }
}
