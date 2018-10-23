public class NeededComputation {

	public boolean makeALoop() {
    	int c = 0;
    	
        for(int i = 0; i < 4; i++) {
        	int a = 5 + c;
        	int b = 6; 
        	
        	c = a + b;
        	c = c + i;
        }
    }
	
	public boolean foreachLoop() {
    	int c = 0;
    	List<Integer> l = new ArrayList<Integer>();
    	
        for(int x : l) {
        	int a = 5;
        	
        	c =  a + x;
        }
    }
	
	public boolean whileLoop() {
    	int c = 0;
    	
        while(true) {
        	int a = 1;
        	c = a;
        }
    }
	
	public boolean doWhileLoop() {
    	int c = 0;
    	
        do {
        	int a = 1;
        	
        	c = a;
        }while(c == -1);
    }
}