package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.NodeIterator;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.StringComparisonBugPattern;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringComparisonChecker implements Checker {
    public List<BugPattern> check(File projectDir) {
        List<BugPattern> bugPatterns = new ArrayList<>();

    	ArrayList<String> variables = new ArrayList<>();

        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ExpressionStmt n, Object arg) {
                        super.visit(n, arg);
                        String stmt = n.toString();
                        if (stmt.contains("=")) {
                            if (stmt.substring(0, stmt.indexOf("=")).contains("String")) {
                                int loc = stmt.indexOf("String") + 6;
                                int eloc = stmt.indexOf("=");
                                String tmpAdd = stmt.substring(loc, eloc);
                                tmpAdd = tmpAdd.replaceAll("\\s+", "");
                                variables.add(tmpAdd);
                            }
                        }
                    }

                    @Override
                    public void visit(IfStmt n, Object arg) {
                        super.visit(n, arg);
                        Node nCond = n.getChildNodes().get(0);
                        if (!checkConditions(nCond, variables)) {
                            // Get line
                            int line = (n.getRange().isPresent() ? n.getRange().get().begin.line : 0);

                            // Get the method name by going back up
                            String functionName = NodeIterator.getFunctionName(n);

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

    private static boolean checkConditions(Node n, ArrayList<String> variables)
    {
        String nClass = n.getClass().getSimpleName();
        if(nClass.equals("BinaryExpr"))
        {
            boolean checkVariable = false;

            if (((BinaryExpr) n).getOperator().toString().equals("EQUALS")) {
                checkVariable = true;
            }

            else if (((BinaryExpr) n).getOperator().toString().equals("NOT_EQUALS")) {
                checkVariable = true;
            }

            if (checkVariable) {
                List nodes = n.getChildNodes();
                for (Object node : nodes) {
                    Node tmpN = (Node) node;
                    if (tmpN.getClass().getSimpleName().equals("NameExpr")) {
                        String variableToCheck = tmpN.toString();
                        if (variables.contains(variableToCheck)) {
                            return false;
                        }
                    }
                    return checkConditions(tmpN, variables);
                }
            }
        }

        List nodes = n.getChildNodes();
        for (Object node : nodes) {
            Node tmpN = (Node) node;
            if (!checkConditions(tmpN, variables)) {
                return false;
            }
        }

        return true;
    }
}
