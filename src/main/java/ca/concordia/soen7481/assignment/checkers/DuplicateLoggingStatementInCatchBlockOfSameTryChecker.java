package ca.concordia.soen7481.assignment.checkers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.Util;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.DuplicateLoggingStatementInCatchBlockOfSameTryBugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.TodoFixMeCatchBlockBugPattern;

public class DuplicateLoggingStatementInCatchBlockOfSameTryChecker implements Checker {

	@Override
	public List<BugPattern> check(File projectDir) {
		
		
		List<BugPattern> bugPatterns = new ArrayList<>();

        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
        	try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(TryStmt n, Object arg) {
                        super.visit(n, arg);
                       ;
                       ArrayList<String> al= new ArrayList<String>(); 
                       for ( CatchClause c : n.getCatchClauses()) {
                    	List<Node> childnodes=c.getChildNodes();
                    	for (Node nodesInOneCatchBlock : childnodes) {
                    		if(nodesInOneCatchBlock instanceof BlockStmt) {
    				List<Statement> ListStatementOfCatchBlock=((BlockStmt) nodesInOneCatchBlock).getStatements();
    				for (Statement statementofCatchBlock : ListStatementOfCatchBlock) {
    					List<Node> listNodeOfOneBlockStaementOfEachCatchBlock= statementofCatchBlock.getChildNodes();
    					for (Node nodeOfOneBlockStaementOfEachCatchBlock : listNodeOfOneBlockStaementOfEachCatchBlock) {
    						if(nodeOfOneBlockStaementOfEachCatchBlock instanceof MethodCallExpr) {
    							MethodCallExpr method=((MethodCallExpr) nodeOfOneBlockStaementOfEachCatchBlock);
    							String MethodName=method.getNameAsString();
    							System.out.println(MethodName);
    							if(MethodName.equals("warn") || MethodName.equals("println")|| MethodName.equals("info")|| MethodName.equals("debug") ||MethodName.equals("error")) {
    								if(method.getArguments().size()==0) {
    								
    									   int lineNumber = Util.getLineNumber(method);

			                               // Get the method name by going back up
			                               String functionName = Util.getFunctionName(n);

			                               bugPatterns.add(new DuplicateLoggingStatementInCatchBlockOfSameTryBugPattern(lineNumber, file, functionName));
    									System.out.println("No Logging information in catch block");
    								}
    								
    								if(method.getArguments().size()==1 && method.getArgument(0) instanceof StringLiteralExpr ) {
    									String args= method.getArgument(0).toString();
    									if(!(al.contains(args))){
    										al.add(args);
    									
    									}
    									else {
    										
    									    int lineNumber = Util.getLineNumber(method);

    			                               // Get the method name by going back up
    			                               String functionName = Util.getFunctionName(n);

    			                               bugPatterns.add(new DuplicateLoggingStatementInCatchBlockOfSameTryBugPattern(lineNumber, file, functionName));
    			                           
    										System.out.println("same logging information in two catch clauses of one try blocks");
    									}
    								}
    								
    							
    							}
    							
    							
    							
    							//forFile=forFile+MethodName+"\n";
    						}
    					}
    				}
                  }
                }
                    	System.out.println("oneCatchBolckFinish");
               }
                        al.clear();
                       System.out.println("onetryBolckFinish");
                    }
                   
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

        return bugPatterns;
	}

}
