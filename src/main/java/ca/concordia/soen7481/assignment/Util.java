package ca.concordia.soen7481.assignment;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.Objects;

public class Util {

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
