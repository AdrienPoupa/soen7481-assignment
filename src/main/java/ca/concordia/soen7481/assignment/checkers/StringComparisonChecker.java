package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.Util;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.StringComparisonBugPattern;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class StringComparisonChecker implements Checker {
    public List<BugPattern> check(File projectDir) {
        List<BugPattern> bugPatterns = new ArrayList<>();

        Map<String, Set<String>> variables = new HashMap<>();

    	// First, extract the String arguments from the function
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);

                        NodeList<Parameter> nodes = n.getParameters();

                        for (Parameter parameter : nodes) {
                            if (parameter.getTypeAsString().equals("String")) {
                                String className = Util.getClassName(n);

                                if (variables.containsKey(className)) {
                                    // This class is known: add the method name to the hashset
                                    variables.get(className).add(parameter.getNameAsString());
                                } else {
                                    // First time we loop on this class: create the hashset and add the method name
                                    Set<String> methodSet = new HashSet<>();
                                    methodSet.add(parameter.getNameAsString());
                                    variables.put(className, methodSet);
                                }
                            }
                        }
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        // Then, look at the expression statements, and look at if statements for String == String
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ExpressionStmt n, Object arg) {
                        super.visit(n, arg);
                        String stmt = n.toString();
                        if (stmt.contains("=") && stmt.substring(0, stmt.indexOf("=")).contains("String")) {
                            int loc = stmt.indexOf("String") + 6;
                            int eloc = stmt.indexOf("=");

                            String tmpAdd = stmt.substring(loc, eloc);
                            tmpAdd = tmpAdd.replaceAll("\\s+", "");

                            String className = Util.getClassName(n);

                            if (variables.containsKey(className)) {
                                // This class is known: add the method name to the hashset
                                variables.get(className).add(tmpAdd);
                            } else {
                                // First time we loop on this class: create the hashset and add the method name
                                Set<String> methodSet = new HashSet<>();
                                methodSet.add(tmpAdd);
                                variables.put(className, methodSet);
                            }
                        }
                    }

                    @Override
                    public void visit(IfStmt n, Object arg) {
                        super.visit(n, arg);
                        Node nCond = n.getChildNodes().get(0);
                        String className = Util.getClassName(n);
                        if (variables.containsKey(className) && shouldAddBugPattern(nCond, variables.get(className))) {
                            // Get line
                            int line = (n.getRange().isPresent() ? n.getRange().get().begin.line : 0);

                            // Get the method name by going back up
                            String functionName = Util.getFunctionName(n);

                            // Append to bug pattern
                            bugPatterns.add(new StringComparisonBugPattern(line, file, functionName));
                        }
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

    	return bugPatterns;
    }

    /**
     * Detect problematic equal testing between Strings
     * @param n
     * @param variables
     * @return
     */
    private static boolean shouldAddBugPattern(Node n, Set<String> variables)
    {
        if (n instanceof BinaryExpr) {
            boolean checkVariable = false;

            if (((BinaryExpr) n).getOperator().toString().equals("EQUALS")) {
                checkVariable = true;
            }
            else if (((BinaryExpr) n).getOperator().toString().equals("NOT_EQUALS")) {
                checkVariable = true;
            }
            
            if(((BinaryExpr) n).getLeft().toString().equals("null")) {
            	checkVariable = false;
            }
            if(((BinaryExpr) n).getRight().toString().equals("null")) {
            	checkVariable = false;
            }
              if((((BinaryExpr) n).getRight() instanceof IntegerLiteralExpr)||(((BinaryExpr) n).getRight() instanceof DoubleLiteralExpr)) {	
            	checkVariable = false;
            }
            
			if((((BinaryExpr) n).getLeft() instanceof IntegerLiteralExpr)||(((BinaryExpr) n).getLeft() instanceof DoubleLiteralExpr)) {          	
				checkVariable = false;
			 }

            if (checkVariable) {
                for (Node node : n.getChildNodes()) {
                    if (node.getClass().getSimpleName().equals("NameExpr")) {
                        String variableToCheck = node.toString();
                        if (variables.contains(variableToCheck)) {
                            return true;
                        }
                    }
                    return shouldAddBugPattern(node, variables);
                }
            }
        }

        for (Node node : n.getChildNodes()) {
            if (!shouldAddBugPattern(node, variables)) {
                return false;
            }
        }

        return false;
    }
}
