package ca.concordia.soen7481.assignment;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;

/**
 * Some code that uses JavaSymbolSolver.
 */
public class MyAnalysis {

    public static void main(String[] args) {
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

        // List classes
        File projectDir = new File("filesToParse");
        System.out.println("List classes");
        listClasses(projectDir);

        // List classes
        System.out.println("List methods");
        listMethods(projectDir);

        // Method calls
        System.out.println("Method calls");
        listMethodCalls(projectDir);

        // Statement by lines
        System.out.println("Statement by lines");
        statementsByLine(projectDir);
        
        // Catch Block comments
        System.out.println("catch block containing TODO and FIXME comments");
        listCatchBlockTODOANDFIXMEERRORS(projectDir);
    
    }

    /**
     * List the classes contained in the folder
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/ListClassesExample.java
     * @param projectDir
     */
    public static void listClasses(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" * " + n.getName());
                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    /**
     * List the methods of each class
     * Based on the listClasses above
     * @param projectDir
     */
    public static void listMethods(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" * " + n.getName());
                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    /**
     * List method calls
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/MethodCallsExample.java
     * @param projectDir
     */
    public static void listMethodCalls(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodCallExpr n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" [L " + n.getBegin().get().line + "] " + n);
                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    /**
     * Lists Statements by line
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/StatementsLinesExample.java
     * @param projectDir
     */
    public static void statementsByLine(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new NodeIterator(new NodeIterator.NodeHandler() {
                    @Override
                    public boolean handle(Node node) {
                        if (node instanceof Statement) {
                            System.out.println(" [Lines " + node.getBegin().get().line
                                    + " - " + node.getEnd().get().line + " ] " + node);
                            return false;
                        } else {
                            return true;
                        }
                    }
                }).explore(JavaParser.parse(file));
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    public static void listCatchBlockTODOANDFIXMEERRORS(File projectDir) {
            new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
                System.out.println(path);
                System.out.println(Strings.repeat("=", path.length()));
                try {
                    new VoidVisitorAdapter<Object>() {
                        @Override
                        public void visit(CatchClause n, Object arg) {
                            super.visit(n, arg);
                           ;
                           for (Comment comment : n.getAllContainedComments()) {
                               if(comment.getContent().toLowerCase().contains("fixme") || comment.getContent().toLowerCase().contains("todo")){
                                   System.out.println("There is a comment such as TODO or FIXME in the catch block of exceptions on line number "+comment.getBegin().get().line);

                               }
                        }

                        }
                    }.visit(JavaParser.parse(file), null);
                    System.out.println(); // empty line
                } catch (IOException e) {
                    new RuntimeException(e);
                }
            }).explore(projectDir);
        }
}
