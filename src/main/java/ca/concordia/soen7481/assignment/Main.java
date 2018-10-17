package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.checkers.*;
import ca.concordia.soen7481.assignment.visitors.*;

import java.io.File;

/**
 * Some code that uses JavaSymbolSolver.
 */
public class Main {
    public static void main(String[] args) {
    	initSetup();
    }

    static void initSetup()
    {
        Visitor visitor;

        // List classes
        File projectDir = new File("filesToParse");
        System.out.println("List classes");
        visitor = new ClassList();
        visitor.visit(projectDir);

        // List classes
        System.out.println("List methods");
        visitor = new MethodList();
        visitor.visit(projectDir);

        // Method calls
        System.out.println("Method calls");
        visitor = new MethodCalls();
        visitor.visit(projectDir);

        // Statement by lines
        System.out.println("Statement by lines");
        visitor = new StatementLines();
        visitor.visit(projectDir);

        // Run Checkers
        Checker checker;

        // Equals Hashcode
        System.out.println("Equals hashcode");
        checker = new EqualsHashcode();
        checker.check(projectDir);

        // Catch Block comments
        System.out.println("catch block containing TODO and FIXME comments");
        checker = new TodoFixMeCatchBlock();
        checker.check(projectDir);

        // String comparison
        System.out.println("String comparison");
        checker = new StringComparison();
        checker.check(projectDir);

        // Overcatch and System.exit
        checker = new OvercatchExceptionTermination();
        checker.check(projectDir);
    }
}
