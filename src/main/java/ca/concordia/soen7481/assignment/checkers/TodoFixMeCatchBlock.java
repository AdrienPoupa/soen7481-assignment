package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.DirExplorer;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;

public class TodoFixMeCatchBlock implements Checker {
    public boolean check(File projectDir) {
        final boolean[] found = {false};
            new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
                try {
                    new VoidVisitorAdapter<Object>() {
                        @Override
                        public void visit(CatchClause n, Object arg) {
                            super.visit(n, arg);
                           for (Comment comment : n.getAllContainedComments()) {
                               if(comment.getContent().toLowerCase().contains("fixme") || comment.getContent().toLowerCase().contains("todo")){
                                   System.out.println("There is a comment such as TODO or FIXME in the catch block of exceptions on line number "+comment.getBegin().get().line);
                                   found[0] = true;
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
