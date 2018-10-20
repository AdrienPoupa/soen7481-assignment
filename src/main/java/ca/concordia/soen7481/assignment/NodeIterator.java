package ca.concordia.soen7481.assignment;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.Objects;

/**
 * A simple Node Iterator
 * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/support/NodeIterator.java
 */
public class NodeIterator {
    public interface NodeHandler {
        boolean handle(Node node);
    }

    private NodeHandler nodeHandler;

    public NodeIterator(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    public void explore(Node node) {
        if (nodeHandler.handle(node)) {
            for (Node child : node.getChildNodes()) {
                explore(child);
            }
        }
    }

    public static String getFunctionName(Node node) {
        // Get the method name by going back up
        Node currentParent = node.getParentNode().orElse(null);
        while (!(currentParent instanceof MethodDeclaration) && currentParent != null) {
            currentParent = currentParent.getParentNode().orElse(null);
        }

        MethodDeclaration methodDeclaration = (MethodDeclaration) currentParent;

        return Objects.requireNonNull(methodDeclaration).getName().getIdentifier();
    }

    public static int getLineNumber(Node statement) {
        return statement.getRange().isPresent() ? statement.getRange().get().begin.line : 0;
    }
}
