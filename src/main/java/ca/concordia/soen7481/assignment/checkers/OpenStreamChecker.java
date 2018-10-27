package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.OpenStreamBugPattern;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Check open streams
 * Inspired by https://github.com/spotbugs/spotbugs/blob/release-3.1/spotbugs/src/main/java/edu/umd/cs/findbugs/detect/FindOpenStream.java
 */
public class OpenStreamChecker implements Checker {

    private final String streamClasses = "InputStream|OutputStream" +
            "ByteArrayInputStream|ByteArrayOutputStream|" +
            "StringReader|StringWriter|" +
            "ServletRequest|ServletResponse|" +
            "FileInputStream|FileOutputStream|" +
            "ZipFile|Reader|Writer|Connection|" +
            "Statement|ResultSet|Socket";

    /**
     * List of base classes of tracked resources.
     */

    @Override
    public List<BugPattern> check(File projectDir) {
        // Stream list: stream name, stream type
        HashMap<String, BugPattern> streams = new HashMap<>();

        List<BugPattern> bugPatterns = new ArrayList<>();

        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);

                        // Get catch statements from body
                        if (n.getBody().isPresent()) {
                            BlockStmt blockStatement = n.getBody().get();

                            // For all try statements
                            for (TryStmt s: blockStatement.findAll(TryStmt.class)) {

                                    // First, let's put all the streams declarations in the streams hashmap
                                    // by searching all the expression statements
                                    for (ExpressionStmt tryStatement: s.getTryBlock().findAll(ExpressionStmt.class)) {

                                            // Assign expression, like "in = new FileInputStream("input.txt");"
                                            if (tryStatement.getExpression().isAssignExpr()) {

                                                // Value, like "new FileInputStream("input.txt")"
                                                AssignExpr assignExpr = (AssignExpr) tryStatement.getExpression();
                                                Expression value = assignExpr.getValue();

                                                if (assignExpr.getTarget().isNameExpr()) {
                                                    String streamName = ((NameExpr) assignExpr.getTarget()).getName().getIdentifier();

                                                    if (value.isObjectCreationExpr()) {

                                                        // Get type
                                                        ClassOrInterfaceType type = ((ObjectCreationExpr) value).getType();

                                                        // Append the stream name and type to the hashmap
                                                        if (type.getName().getIdentifier().matches(streamClasses)) {

                                                            int line = (assignExpr.getRange().isPresent() ? assignExpr.getRange().get().begin.line : 0);
                                                            streams.put(streamName, new OpenStreamBugPattern(line, file, n.getNameAsString()));
                                                        }
                                                    }
                                                }
                                            }

                                            // Assign and declare expression, like "FileInputStream test = new FileInputStream("test.txt");"
                                            else if (tryStatement.getExpression().isVariableDeclarationExpr()) {

                                                NodeList<VariableDeclarator> variables = ((VariableDeclarationExpr) tryStatement.getExpression()).getVariables();
                                                variables.forEach(variable -> {
                                                    // If it is a stream
                                                    if (variable.getType().isClassOrInterfaceType()) {
                                                        String streamName = variable.getName().getIdentifier();
                                                        ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) variable.getType();
                                                        String streamType = classOrInterfaceType.getName().getIdentifier();

                                                        // Append the stream name and type to the hashmap
                                                        if (streamType.matches(streamClasses)) {
                                                            int line = (variable.getRange().isPresent() ? variable.getRange().get().begin.line : 0);
                                                            streams.put(streamName, new OpenStreamBugPattern(line, file, n.getNameAsString()));
                                                        }
                                                    }
                                                });
                                            }
                                    }

                                    // Now search the finally block
                                    if (s.getFinallyBlock().isPresent()) {
                                        // Get all the expression statements
                                        for (ExpressionStmt expressionStmt: s.getFinallyBlock().get().findAll(ExpressionStmt.class)) {

                                            // Look for all the .close calls
                                            if (expressionStmt.getExpression().isMethodCallExpr()) {
                                                MethodCallExpr methodCallExpr = (MethodCallExpr) expressionStmt.getExpression();

                                                if (methodCallExpr.getScope().isPresent() &&  methodCallExpr.getScope().get().isNameExpr()) {
                                                    NameExpr nameExpr = (NameExpr) methodCallExpr.getScope().get();
                                                    String streamName = nameExpr.getName().getIdentifier();

                                                    // If the stream is closed, remove it from the hashmap
                                                    if (methodCallExpr.getName().getIdentifier().equals("close")) {
                                                        streams.remove(streamName);
                                                    }

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


        streams.forEach((streamName, bugPattern) -> bugPatterns.add(bugPattern));

        return bugPatterns;
    }
}
