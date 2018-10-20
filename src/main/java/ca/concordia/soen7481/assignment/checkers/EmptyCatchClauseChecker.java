package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.Util;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.EmptyCatchClauseBugPattern;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmptyCatchClauseChecker  implements Checker {
    public List<BugPattern> check(File projectDir) {
        List<BugPattern> bugPatterns = new ArrayList<>();

        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(CatchClause n, Object arg) {
                       super.visit(n, arg);

                       // Get catch statements from body
                       BlockStmt blockStatement = n.getBody();
                       NodeList<Statement> statements = blockStatement.getStatements();

                       // If there is no statement in the catch block
                       if(statements.isEmpty()) {
                           int lineNumber = Util.getLineNumber(blockStatement);

                           // Get the method name by going back up
                           String functionName = Util.getFunctionName(n);

                           bugPatterns.add(new EmptyCatchClauseBugPattern(lineNumber, file, functionName));
                       }
                       }
                    
                    
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        return bugPatterns;
    }
}
