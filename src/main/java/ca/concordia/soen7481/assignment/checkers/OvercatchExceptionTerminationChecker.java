package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.Util;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.OvercatchExceptionTerminationBugPattern;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OvercatchExceptionTerminationChecker implements Checker {
    public List<BugPattern> check(File projectDir) {
        List<BugPattern> bugPatterns = new ArrayList<>();

        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(CatchClause n, Object arg) {
                        super.visit(n, arg);

                        Parameter parameter = n.getParameter();
                        // First, match the high level exceptions
                        if (parameter.getType().asString().matches("Exception|ClassNotFoundException|" +
                                "ClassNotSupportedException|IllegalAccessException|" +
                                "InstantiationException|InterruptedException|" +
                                "NoSuchFieldException|NoSuchMethodException|" +
                                "RuntimeException")) {

                            // Get catch statements from body
                            BlockStmt blockStatement = n.getBody();
                            NodeList<Statement> statements = blockStatement.getStatements();

                            // For all statements
                            for (Statement s: statements) {

                                // Then, we'll go down to the MethodCallExpr and NameExpr to look for a System.exit
                                // within the catch block
                                if (s.isExpressionStmt()) {
                                    ExpressionStmt expressionStmt = s.asExpressionStmt();
                                    Expression expression = expressionStmt.getExpression();

                                    if (expression.isMethodCallExpr()) {

                                        MethodCallExpr methodCallExpr = expression.asMethodCallExpr();

                                        if (methodCallExpr.getScope().isPresent() &&
                                                methodCallExpr.getScope().get().isNameExpr()) {

                                            NameExpr nameExpr = methodCallExpr.getScope().get().asNameExpr();

                                            // Found it!
                                            if (nameExpr.getName().getIdentifier().equals("System") &&
                                                    methodCallExpr.getName().getIdentifier().equals("exit")) {

                                                // Get the method name by going back up
                                                String functionName = Util.getFunctionName(n);

                                                int lineNumber = Util.getLineNumber(s);

                                                bugPatterns.add(new OvercatchExceptionTerminationBugPattern(lineNumber, file, functionName));
                                            }

                                        }

                                    }
                                }

                            }
                        }
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        return bugPatterns;
     }
}
