package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.DirExplorer;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
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
import java.util.Objects;

public class OvercatchExceptionTermination implements Checker {
    public boolean check(File projectDir) {
        final boolean[] found = {false};
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
                                                    Node currentParent = n.getParentNode().orElse(null);
                                                    while (!(currentParent instanceof MethodDeclaration) && currentParent != null) {
                                                        currentParent = currentParent.getParentNode().orElse(null);
                                                    }

                                                    MethodDeclaration methodDeclaration = (MethodDeclaration) currentParent;

                                                    System.out.println("Call to System.exit found in "+file+" function name: "+ Objects.requireNonNull(methodDeclaration).getName().getIdentifier());

                                                    found[0] = true;
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
        return found[0];
        }
}
