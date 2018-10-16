package ca.concordia.soen7481.assignment;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Some code that uses JavaSymbolSolver.
 */
public class MyAnalysis {

	static HashMap<MethodDeclaration, Object> list_of_methods = new HashMap<MethodDeclaration, Object>();
	static ArrayList<Statement> list_of_statements = new ArrayList<Statement>();

    public static void main(String[] args) {

    	init_setup();
        make_checks_gagan();
    }

    public static void init_setup()
    {
    	// Set up a minimal type solver that only looks at the classes used to run this sample.
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);

        // Parse some code
        CompilationUnit cu = JavaParser.parse("class X { int x() { return 1 + 1.0 - 5; } }");

        // Find all the calculations with two sides:
        System.out.println("Find calculations");
        cu.findAll(BinaryExpr.class).forEach(be -> {
            // Find out what type it has:
            ResolvedType resolvedType = be.calculateResolvedType();

            // Show that it's "double" in every case:
            System.out.println(be.toString() + " is a: " + resolvedType);
        });

        // List classes
        File projectDir = new File("filesToParse");
        System.out.println("List classes");
        listClasses(projectDir);

        // List classes
        System.out.println("List methods");
        listMethods(projectDir);

        // Method calls
        System.out.println("Method calls");
        listMethodCalls(projectDir);

        // Statement by lines
        System.out.println("Statement by lines");
        statementsByLine(projectDir);

        // Catch Block comments
        System.out.println("catch block containing TODO and FIXME comments");
        listCatchBlockTODOANDFIXMEERRORS(projectDir);

    }

    /**
     * List the classes contained in the folder
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/ListClassesExample.java
     * @param projectDir
     */
    public static void listClasses(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" * " + n.getName());
                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    /**
     * List the methods of each class
     * Based on the listClasses above
     * @param projectDir
     */
    public static void listMethods(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);
                        list_of_methods.put(n, arg);
                        System.out.println(" * " + n.getName());
                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    /**
     * List method calls
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/MethodCallsExample.java
     * @param projectDir
     */
    public static void listMethodCalls(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodCallExpr n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" [L " + n.getBegin().get().line + "] " + n);
                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    /**
     * Lists Statements by line
     * Credits: https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/StatementsLinesExample.java
     * @param projectDir
     */
    public static void statementsByLine(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new NodeIterator(new NodeIterator.NodeHandler() {
                    @Override
                    public boolean handle(Node node) {
                        if (node instanceof Statement) {
                        	list_of_statements.add((Statement)node);
                            System.out.println(" [Lines " + node.getBegin().get().line
                                    + " - " + node.getEnd().get().line + " ] " + node);
                            return false;
                        } else {
                            return true;
                        }
                    }
                }).explore(JavaParser.parse(file));
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    public static void listCatchBlockTODOANDFIXMEERRORS(File projectDir) {
            new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
                System.out.println(path);
                System.out.println(Strings.repeat("=", path.length()));
                try {
                    new VoidVisitorAdapter<Object>() {
                        @Override
                        public void visit(CatchClause n, Object arg) {
                            super.visit(n, arg);
                           ;
                           for (Comment comment : n.getAllContainedComments()) {
                               if(comment.getContent().toLowerCase().contains("fixme") || comment.getContent().toLowerCase().contains("todo")){
                                   System.out.println("There is a comment such as TODO or FIXME in the catch block of exceptions on line number "+comment.getBegin().get().line);

                               }
                        }

                        }
                    }.visit(JavaParser.parse(file), null);
                    System.out.println(); // empty line
                } catch (IOException e) {
                    new RuntimeException(e);
                }
            }).explore(projectDir);
        }


    public static void make_checks_gagan()
    {
        if(check_for_equal_hashcode() == false)
        {
        	System.out.println("Error: Class defines equals() but not hashCode()");
        }
        if(check_string_comparison() == false)
        {
        	System.out.println("Error: Strings should not be compared using == or != unless constants or interned");
        }
    }

    public static boolean check_conditions(Node n, ArrayList<String> variables)
    {
     	String n_class = n.getClass().getSimpleName().toString();
    	if(n_class.equals("BinaryExpr") == true)
    	{
    		boolean checkVariable = false;
    		if(((BinaryExpr)n).getOperator().toString().equals("EQUALS") == true)
    		{
    			checkVariable = true;
    			//System.out.println("Class is : " + ((BinaryExpr)n).toString());
    		}
    		else if(((BinaryExpr)n).getOperator().toString().equals("NOT_EQUALS") == true)
    		{
    			checkVariable = true;
    			//System.out.println("Class is : " + ((BinaryExpr)n).getOperator().toString());
    		}
    		if(checkVariable == true)
    		{
    			List nodes = (List)n.getChildNodes();
    			for(int i = 0; i < nodes.size(); i++)
    	    	{
    	    		Node tmpN = (Node)nodes.get(i);
    	    		if(tmpN.getClass().getSimpleName().equals("NameExpr") == true)
    	    		{
    	    			String variable_to_check = tmpN.toString();
    	    			if(variables.contains(variable_to_check) == true)
    	    			{
    	    				return false;
    	    			}
    	    		}
    	    		return check_conditions(tmpN, variables);
    	    	}
    		}
    	}

    	List nodes = (List)n.getChildNodes();
    	for(int i = 0; i < nodes.size(); i++)
    	{
    		Node tmpN = (Node)nodes.get(i);
    		if(check_conditions(tmpN, variables) == false)
    		{
    			return false;
    		}
    	}

    	return true;
    }

    public static boolean check_string_comparison() {
    	ArrayList<String> variables = new ArrayList<String>();
    	for(Statement s : list_of_statements)
    	{
    		List nodes = (List)s.getChildNodes();
    		for(int i =0; i < nodes.size(); i++)
    		{
    			Node n = (Node) nodes.get(i);
    			String n_class = n.getClass().getSimpleName().toString();

    			if(n_class.equals("ExpressionStmt"))
    			{
    				String stmt = n.toString();
    				if((stmt.indexOf("=") != -1) && (stmt.substring(0, stmt.indexOf("=")) != null))
    				{
	    				if(stmt.substring(0, stmt.indexOf("=")).contains("String"))
	    				{
	    					int loc = stmt.indexOf("String") + 6;
	    					int eloc = stmt.indexOf("=");
	    					String tmp_add = stmt.substring(loc, eloc);
	;    					tmp_add = tmp_add.replaceAll("\\s+","");
	    					variables.add(tmp_add);
	    				}
    				}
    			}
    			if(n_class.equals("IfStmt") == true)
    			{
    				Node nCond = n.getChildNodes().get(0);
    				return check_conditions(nCond, variables);
    			}
    		}
    	}
    	return true;
    }

    public static boolean check_for_equal_hashcode() {
    	boolean equals_found = false;
    	boolean hc_found = false;
    	Iterator it = list_of_methods.entrySet().iterator();
    	while(it.hasNext())
    	{
    		Map.Entry pair = (Map.Entry)it.next();
    		MethodDeclaration md = (MethodDeclaration) pair.getKey();
    		Object arg = pair.getValue();
    		//System.out.println("Method: " + md.getNameAsString());
    		if(md.getNameAsString().equals("equals") == true)
    		{
    			if(md.getTypeAsString().equals("boolean") == true)
    			{
    				NodeList<Parameter> nodes = md.getParameters();
    				if((nodes.size() == 1) && (nodes.get(0).getTypeAsString().equals("Object") == true))
    				{
    					equals_found = true;
    				}
    			}
    		}
    		else if(md.getNameAsString().equals("hashCode") == true)
    		{
    			if(md.getTypeAsString().equals("int") == true)
    			{
    				NodeList<Parameter> nodes = md.getParameters();
    				if(nodes.size() == 0) {
    					hc_found = true;
    				}
    			}
    		}
    	}

    	if(equals_found == true && hc_found == false)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
}
