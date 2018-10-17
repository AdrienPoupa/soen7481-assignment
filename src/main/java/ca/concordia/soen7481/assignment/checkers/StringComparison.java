package ca.concordia.soen7481.assignment.checkers;

import ca.concordia.soen7481.assignment.visitors.StatementLines;
import ca.concordia.soen7481.assignment.visitors.Visitor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.Statement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StringComparison implements Checker {
    public boolean check(File projectDir) {
    	ArrayList<String> variables = new ArrayList<>();
        Visitor listOfStatementsVisitor = new StatementLines();
    	for(Map.Entry<Node, Object> statementEntry : listOfStatementsVisitor.visit(projectDir).entrySet())
    	{
    	    Statement s = (Statement) statementEntry.getKey();
    		List nodes = s.getChildNodes();
            for (Object node : nodes) {
                Node n = (Node) node;
                String nClass = n.getClass().getSimpleName();

                if (nClass.equals("ExpressionStmt")) {
                    String stmt = n.toString();
                    if (stmt.contains("=")) {
                        if (stmt.substring(0, stmt.indexOf("=")).contains("String")) {
                            int loc = stmt.indexOf("String") + 6;
                            int eloc = stmt.indexOf("=");
                            String tmpAdd = stmt.substring(loc, eloc);
                            ;
                            tmpAdd = tmpAdd.replaceAll("\\s+", "");
                            variables.add(tmpAdd);
                        }
                    }
                }
                if (nClass.equals("IfStmt")) {
                    Node nCond = n.getChildNodes().get(0);
                    return checkConditions(nCond, variables);
                }
            }
    	}
    	return true;
    }

    private static boolean checkConditions(Node n, ArrayList<String> variables)
    {
        String nClass = n.getClass().getSimpleName();
        if(nClass.equals("BinaryExpr"))
        {
            boolean checkVariable = false;
            if(((BinaryExpr) n).getOperator().toString().equals("EQUALS"))
            {
                checkVariable = true;
            }
            else if(((BinaryExpr) n).getOperator().toString().equals("NOT_EQUALS"))
            {
                checkVariable = true;
            }
            if(checkVariable)
            {
                List nodes = n.getChildNodes();
                for (Object node : nodes) {
                    Node tmpN = (Node) node;
                    if (tmpN.getClass().getSimpleName().equals("NameExpr")) {
                        String variable_to_check = tmpN.toString();
                        if (variables.contains(variable_to_check)) {
                            return false;
                        }
                    }
                    return checkConditions(tmpN, variables);
                }
            }
        }

        List nodes = n.getChildNodes();
        for (Object node : nodes) {
            Node tmpN = (Node) node;
            if (!checkConditions(tmpN, variables)) {
                return false;
            }
        }

        return true;
    }
}
