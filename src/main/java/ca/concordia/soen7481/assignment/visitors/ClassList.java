package ca.concordia.soen7481.assignment.visitors;

import ca.concordia.soen7481.assignment.DirExplorer;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ClassList implements Visitor {

    /**
     * List the classes contained in the folder
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/ListClassesExample.java
     * @param projectDir
     */
    public HashMap<Node, Object> visit(File projectDir) {
        HashMap<Node, Object> listOfClasses = new HashMap<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        listOfClasses.put(n, arg);
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        return listOfClasses;
    }
}
