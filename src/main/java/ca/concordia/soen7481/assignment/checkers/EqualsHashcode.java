package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.visitors.MethodList;
import ca.concordia.soen7481.assignment.visitors.Visitor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import java.io.File;
import java.util.Map;

public class EqualsHashcode implements Checker {
    public boolean check(File projectDir) {
    	boolean equalsFound = false;
    	boolean hcFound = false;
        Visitor listOfMethodsVisitor = new MethodList();
        for (Map.Entry<Node, Object> methodDeclarationObjectEntry : listOfMethodsVisitor.visit(projectDir).entrySet()) {
            MethodDeclaration md = (MethodDeclaration) ((Map.Entry) methodDeclarationObjectEntry).getKey();
            if (md.getNameAsString().equals("equals")) {
                if (md.getTypeAsString().equals("boolean")) {
                    NodeList<Parameter> nodes = md.getParameters();
                    if ((nodes.size() == 1) && (nodes.get(0).getTypeAsString().equals("Object"))) {
                        equalsFound = true;
                    }
                }
            } else if (md.getNameAsString().equals("hashCode")) {
                if (md.getTypeAsString().equals("int")) {
                    NodeList<Parameter> nodes = md.getParameters();
                    if (nodes.size() == 0) {
                        hcFound = true;
                    }
                }
            }
        }

        return !equalsFound || hcFound;
    }
}
