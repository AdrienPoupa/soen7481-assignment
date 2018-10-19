package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class StringComparisonBugPattern extends BugPattern {
    public StringComparisonBugPattern(int line, File file, String functionName) {
        super(line, file, functionName);
    }

    @Override
    public String getIdentifier() {
        return "ES";
    }

    @Override
    public String getName() {
        return "String Comparison";
    }

    @Override
    public String getDescription() {
        return "Comparison of String objects using == or !=";
    }
}
