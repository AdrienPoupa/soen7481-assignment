public class UnneededComputationException {

	public boolean forLoop() {
    	int c = 0;
    	
        for(int i = 0; i < 4; i++) {
        	int a = 5 + c;
        	int b = 6; 
        	
        	c = a + i;
        }
    }
	
	public boolean foreachLoop() {
    	int c = 0;
    	List<Integer> l = new ArrayList<Integer>();
    	
        for(int x : l) {
        	int a = 5;
        	
        	c = l;
        }
    }
	
	public boolean whileLoop() {
    	int c = 0;
    	
        while(true) {
        	int a = 1;
        	c = 4;
        }
    }
	
	public boolean doWhileLoop() {
    	int c = 0;
    	
        do {
        	int a = 1;
        	
        	c = 4;
        }while(c == -1);
    }
}