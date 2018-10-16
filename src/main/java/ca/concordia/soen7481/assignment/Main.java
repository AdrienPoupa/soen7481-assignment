package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.checkers.Checker;
import ca.concordia.soen7481.assignment.checkers.EqualsHashcode;
import ca.concordia.soen7481.assignment.checkers.StringComparison;
import ca.concordia.soen7481.assignment.checkers.TodoFixMeCatchBlock;
import ca.concordia.soen7481.assignment.visitors.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

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
    	// Set up a minimal type solver that only looks at the classes used to run this sample.
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);

        // Parse some code
        CompilationUnit cu = JavaParser.parse("class X { int x() { return 1 + 1.0 - 5; } }");

        // Find all the calculations with two sides:
        System.out.println("Find calculations");
        cu.findAll(BinaryExpr.class).forEach(be -> {
            // Find out what type it has:
            ResolvedType resolvedType = be.calculateResolvedType();

            // Show that it's "double" in every case:
            System.out.println(be.toString() + " is a: " + resolvedType);
        });

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

    }
}
