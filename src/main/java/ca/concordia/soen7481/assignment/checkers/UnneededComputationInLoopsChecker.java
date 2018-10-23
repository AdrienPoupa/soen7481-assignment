package ca.concordia.soen7481.assignment.checkers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.Util;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.EqualsHashcodeBugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.OpenStreamBugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.OvercatchExceptionTerminationBugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.UnneededComputationInLoopsBugPattern;

/**
 * This class will identify the presence of the bug pattern : Unneeded computation in loops.
 */
public class UnneededComputationInLoopsChecker implements Checker {
	// If we store variables as nodes, they arent equals
	private ArrayList<String> variables = new ArrayList<String>();
	private ArrayList<Node> variableNodes = new ArrayList<Node>();
	private String declarationClass = "class com.github.javaparser.ast.expr.VariableDeclarationExpr";
	File file;
	
	/**
	 * Start the inspection of the file for the bug pattern : Unneeded computation in loops.
	 * @param projectDir is the file to inspect
	 */
	@Override
	public List<BugPattern> check(File projectDir) {
		List<BugPattern> bugPatterns = new ArrayList<>();
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
        	this.file = file;
            try {
            	/* For visitor */
            	new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ForStmt forStmt, Object arg) {
                        super.visit(forStmt, arg);
                        visitFor(forStmt, bugPatterns);
                    }
                }.visit(JavaParser.parse(file), null);
            	
                /* Foreach visitor */
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ForeachStmt forEachStmt, Object arg) {
                        super.visit(forEachStmt, arg);
                        visitForEach(forEachStmt, bugPatterns);
                    }
                }.visit(JavaParser.parse(file), null);
                
                /* While visitor */
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(WhileStmt whileStmt, Object arg) {
                        super.visit(whileStmt, arg);
                        visitWhile(whileStmt, bugPatterns);
                    }
                }.visit(JavaParser.parse(file), null);
                
                /* Do while visitor */
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(DoStmt doWhileStmt, Object arg) {
                        super.visit(doWhileStmt, arg);
                        visitDoWhile(doWhileStmt, bugPatterns);
                    }
                }.visit(JavaParser.parse(file), null);
                
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
		
		return bugPatterns;
	}

	protected void visitDoWhile(DoStmt doWhileStmt, List<BugPattern> bugPatterns) {
		inspectBody((BlockStmt) doWhileStmt.getBody(), bugPatterns);
		
		variables.clear();
		variableNodes.clear();
	}

	private void visitWhile(WhileStmt whileStmt, List<BugPattern> bugPatterns) {
		inspectBody((BlockStmt) whileStmt.getBody(), bugPatterns);
		
		variables.clear();
		variableNodes.clear();
	}

	/**
	 * Check for loops and find the iterator variable.
	 * @param forStmt
	 * @param bugPatterns 
	 */
	private void visitFor(ForStmt forStmt, List<BugPattern> bugPatterns) {
		Node iteratorDeclaration = forStmt.getInitialization().get(0);
		Node iterator = iteratorDeclaration.getChildNodes().get(0).getChildNodes().get(1);
		variables.add(iterator.toString());
		variableNodes.add(iterator);
		inspectBody((BlockStmt) forStmt.getBody(), bugPatterns);
		
		variables.clear();
		variableNodes.clear();
	}
	
	/**
	 * Check foreach loops and find the iterator variable.
	 * @param bugPatterns 
	 * @param forStmt
	 */
	private void visitForEach(ForeachStmt foreachStmt, List<BugPattern> bugPatterns) {
		
		Node iterator = foreachStmt.getVariable().getChildNodes().get(0);
		variables.add(iterator.toString());
		variableNodes.add(iterator);
		inspectBody((BlockStmt) foreachStmt.getBody(), bugPatterns);
		
		variables.clear();
		variableNodes.clear();
	}

	/**
	 * To inspect all the body of any kind of loops. Check if there is any unneeded computation.
	 * @param body block to inspect
	 * @param bugPatterns 
	 * @return 
	 */
	protected void inspectBody(BlockStmt body, List<BugPattern> bugPatterns) {
		NodeList<Statement> lines = body.getStatements();
		
		// For each line of code in loop's body
		for(Statement line : lines) {
			if (line.isExpressionStmt()) {
				Node expression = line.getChildNodes().get(0).getChildNodes().get(0);
				// a or int a
				Node leftVar = expression.getChildNodes().get(0);
				boolean declarationStmt = line.getChildNodes().get(0).getClass().toString().equals(declarationClass);
				
				if(declarationStmt) {
					leftVar = expression.getChildNodes().get(1);
					variables.add(leftVar.toString());
					variableNodes.add(leftVar);
				}
				
				// If a declaration statement has a right member
				if(declarationStmt && expression.getChildNodes().size() > 1) {
					Node rightExpression = expression.getChildNodes().get(2);
					
					for(Node rightVar : rightExpression.getChildNodes()) {
						if(variables.contains(rightVar.toString())) {
							int index = variables.indexOf(rightVar.toString());
							variableNodes.remove(index);
							variables.remove(rightVar.toString());
						}
					}
				} 
				// If a simple statement has a right member
				else if(line.getChildNodes().get(0).getChildNodes().size() > 0) {
					Node rightExpression = line.getChildNodes().get(0).getChildNodes().get(1);
					
					for(Node rightVar : rightExpression.getChildNodes()) {
						if(variables.contains(rightVar.toString())) {
							int index = variables.indexOf(rightVar.toString());
							variableNodes.remove(index);
							variables.remove(rightVar.toString());
						}
					}
				}
			}
		}
				
		if(variableNodes.size() > 0) {
			for(Node n : variableNodes) {
				String functionName = Util.getFunctionName(n);
	            int lineNumber = Util.getLineNumber(n);
	            
	            bugPatterns.add(new UnneededComputationInLoopsBugPattern(lineNumber, file, functionName));
			}            
		}
	}

}
