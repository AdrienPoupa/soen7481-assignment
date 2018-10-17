package ca.concordia.soen7481.assignment.visitors;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.NodeIterator;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.Statement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class StatementLines implements Visitor {
    /**
     * Lists Statements by line
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/StatementsLinesExample.java
     * @param projectDir
     */
    public HashMap<Node, Object> visit(File projectDir) {
        HashMap<Node, Object> listOfStatements = new HashMap<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new NodeIterator(node -> {
                    if (node instanceof Statement) {
                        listOfStatements.put(node, null);
                        return false;
                    } else {
                        return true;
                    }
                }).explore(JavaParser.parse(file));
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        return listOfStatements;
    }
}
