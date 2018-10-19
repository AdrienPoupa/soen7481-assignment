package ca.concordia.soen7481.assignment.bugpatterns;

import java.io.File;

public class EqualsHashcodeBugPattern extends BugPattern {
    public EqualsHashcodeBugPattern(int line, File file, String functionName) {
        super(line, file, functionName);
    }

    @Override
    public String getIdentifier() {
        return "HE";
    }

    @Override
    public String getName() {
        return "Equals Hashcode";
    }

    @Override
    public String getDescription() {
        return "Class defines equals() but not hashCode()";
    }
}
