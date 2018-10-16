package ca.concordia.soen7481.assignment.visitors;


import com.github.javaparser.ast.Node;

import java.io.File;
import java.util.HashMap;

public interface Visitor {
    HashMap<Node, Object> visit(File projectDir);
}
