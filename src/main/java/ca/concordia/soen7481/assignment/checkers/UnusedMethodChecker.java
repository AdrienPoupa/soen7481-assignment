package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.Util;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.UnusedMethodBugPattern;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UnusedMethodChecker implements Checker {

    @Override
    public List<BugPattern> check(File projectDir) {
        List<BugPattern> bugPatterns = new ArrayList<>();
        Map<String, Set<String>> methodCallsInClass = new HashMap<>();

        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {

                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodCallExpr n, Object arg) {
                        super.visit(n, arg);

                        // Get the class name by going back up
                        Node currentParent = n.getParentNode().orElse(null);
                        while (!(currentParent instanceof ClassOrInterfaceDeclaration) && currentParent != null) {
                            currentParent = currentParent.getParentNode().orElse(null);
                        }

                        if (currentParent != null) {
                            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) currentParent;
                            CompilationUnit compilationUnit = (CompilationUnit) currentParent.getParentNode().orElse(null);
                            if (compilationUnit != null) {

                                // Get imports
                                List<Node> imports = compilationUnit.getChildNodes().stream().filter(ImportDeclaration.class::isInstance).collect(Collectors.toList());

                                // Detect if jUnit was imported
                                boolean jUnit = false;
                                for (Node i : imports) {
                                    String importString = ((ImportDeclaration) i).getNameAsString();
                                    if (importString.startsWith("org.junit")) {
                                        jUnit = true;
                                        break;
                                    }
                                }

                                // If jUnit was imported, do not add the method in the array list: test cases will never be called from the code directly
                                if (!jUnit) {
                                    String className = classOrInterfaceDeclaration.getNameAsString();

                                    if (methodCallsInClass.containsKey(className)) {
                                        // This class is known: add the method name to the hashset
                                        methodCallsInClass.get(className).add(n.getNameAsString());
                                    } else {
                                        // First time we loop on this class: create the hashset and add the method name
                                        Set<String> methodSet = new HashSet<>();
                                        methodSet.add(n.getNameAsString());
                                        methodCallsInClass.put(className, methodSet);
                                    }
                                }
                            }

                        }
                    }
                }.visit(JavaParser.parse(file), null);

            } catch (RuntimeException | IOException ignored) {

            }
        }).explore(projectDir);

        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);

                        String className = n.getNameAsString();
                        // If the set contains the current class
                        if (methodCallsInClass.containsKey(className)) {
                            Set<String> classMethods = methodCallsInClass.get(className);

                            // For every method of the current class
                            for (MethodDeclaration method : n.getMethods()) {
                                String currentClassMethodName = method.getNameAsString();
                                boolean found = false;
                                // Search our list of methods found above
                                for (String methodName : classMethods) {
                                    if (methodName.equals(currentClassMethodName)) {
                                        found = true;
                                        break;
                                    }
                                }

                                // To report unused method which is not a main method
                                if (!found && !currentClassMethodName.equals("main")) {
                                    bugPatterns.add(new UnusedMethodBugPattern(Util.getLineNumber(method), file, currentClassMethodName));
                                }
                            }
                        }
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (RuntimeException | IOException ignored) {
            }
        }).explore(projectDir);

        return bugPatterns;
    }
}
