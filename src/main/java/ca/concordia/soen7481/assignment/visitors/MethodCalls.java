package ca.concordia.soen7481.assignment.visitors;

import ca.concordia.soen7481.assignment.DirExplorer;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MethodCalls implements Visitor {
    /**
     * List method calls
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/MethodCallsExample.java
     * @param projectDir
     */
    public HashMap<Node, Object> visit(File projectDir) {
        HashMap<Node, Object> listMethodCalls = new HashMap<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodCallExpr n, Object arg) {
                        super.visit(n, arg);
                        listMethodCalls.put(n, arg);
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        return listMethodCalls;
    }
}
