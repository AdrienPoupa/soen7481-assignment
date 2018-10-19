package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;

import java.io.File;
import java.util.List;

public interface Checker {
    List<BugPattern> check(File projectDir);
}
