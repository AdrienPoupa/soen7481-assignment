package ca.concordia.soen7481.assignment.visitors;

import ca.concordia.soen7481.assignment.DirExplorer;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MethodList implements Visitor {

    /**
     * List the methods of each class
     * Based on the listClasses above
     * @param projectDir
     */
    public HashMap<Node, Object> visit(File projectDir) {
        HashMap<Node, Object> listOfMethods = new HashMap<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);
                        listOfMethods.put(n, arg);
                        System.out.println(" * " + n.getName());
                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        return listOfMethods;
    }
}
