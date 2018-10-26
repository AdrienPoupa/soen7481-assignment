package ca.concordia.soen7481.assignment.checkers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import ca.concordia.soen7481.assignment.DirExplorer;
import ca.concordia.soen7481.assignment.Util;
import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;
import ca.concordia.soen7481.assignment.bugpatterns.UnusedMethodBugPattern;

public class UnusedMethodChecker implements Checker {

	@SuppressWarnings("deprecation")
	@Override
	public List<BugPattern> check(File projectDir) {
		List<BugPattern> bugPatterns = new ArrayList<>();
		Map<String, List<String>> classMethodsMap = new HashMap<>();
		
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
			 try {
				TypeSolver typeSolver = new ReflectionTypeSolver();
				 
				JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
				JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
				CompilationUnit cu = JavaParser.parse(file);
				  cu.findAll(MethodCallExpr.class).forEach(mce -> {
				  String objClassName = mce.resolveInvokedMethod().getClassName();
				  if(classMethodsMap.containsKey(objClassName)) {
					  List<String> methodNames = classMethodsMap.get(objClassName);
					  methodNames.add(mce.getNameAsString());
				  } else {
					  List<String> list = new ArrayList<String>();
					  list.add(mce.getNameAsString());
					  classMethodsMap.put(objClassName, list);
				  }
				  });
			 } catch(IOException e) {
				  new RuntimeException(e);
			 }
		 }).explore(projectDir);
		
		 new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
	            try {
	                new VoidVisitorAdapter<Object>() {
	                    @Override
	                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
	                       super.visit(n, arg);

	                       String className = n.getNameAsString();
	                       if(classMethodsMap.containsKey(className)) {
	                    	   List<String> classMethods = classMethodsMap.get(className);
	                    	   
	                    	   for(MethodDeclaration method : n.getMethods()) {
	                    		   String currentClassMethodName = method.getNameAsString();
	                    		   boolean found = false;
		                    	   for(String methodName : classMethods) {
		                    		   if(methodName.equals(currentClassMethodName)) {
		                    			   found = true;
		                    			   break;
		                    		   }
		                    	   }
		                    	   
		                    	   // To report unused method which is not a main method
		                    	   if(!found && !currentClassMethodName.equals("main")) {
		                    		   bugPatterns.add(new UnusedMethodBugPattern(Util.getLineNumber(method), file, currentClassMethodName));  
		                    	   }
	                    		   
	                    	   }
	                    	   
	                    	   
	                       } else {
	                    	   for(MethodDeclaration method : n.getMethods()) {
	                    		   if(!method.getNameAsString().equals("main")) {
	                    			   bugPatterns.add(new UnusedMethodBugPattern(Util.getLineNumber(method), file, method.getNameAsString()));
	                    		   }
	                    	   }
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
