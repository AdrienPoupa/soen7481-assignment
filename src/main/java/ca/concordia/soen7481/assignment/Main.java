package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.checkers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Some code that uses JavaSymbolSolver.
 */
public class Main {
    public static void main(String[] args) {
    	initSetup();
    }

    static void initSetup()
    {
        // Delete the previous report
        FileUtil.deleteFiles();

        File projectDir = new File("filesToParse");

        // Create a hashset of bug patterns so that we won't have any duplicates
        Set<BugPattern> bugPatterns = new HashSet<>();

        // Equals Hashcode
        Checker checker = new EqualsHashcodeChecker();
        bugPatterns.addAll(checker.check(projectDir));

        // Catch Block comments
        checker = new TodoFixMeCatchBlockChecker();
        bugPatterns.addAll(checker.check(projectDir));

        // String comparison
        checker = new StringComparisonChecker();
        bugPatterns.addAll(checker.check(projectDir));

        // Overcatch and System.exit
        checker = new OvercatchExceptionTerminationChecker();
        bugPatterns.addAll(checker.check(projectDir));

        // Openstream
        checker = new OpenStreamChecker();
        bugPatterns.addAll(checker.check(projectDir));

        // Display the bug patterns found in the console
        System.out.println(bugPatterns);

        // Generate the report
        FileUtil.generateReport(new ArrayList(bugPatterns));
    }
}
