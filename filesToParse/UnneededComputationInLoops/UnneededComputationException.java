public class UnneededComputationException {

	public boolean forLoop() {
		for (Map.Entry<String,Host> nodeEntry : nodeCollections.entrySet()) {
		      Host host = nodeEntry.getValue();
		      
		      for (Node nm : host.nms.values()) {
		          if (labels != null) {
		            labels.removeAll(labelsToRemove);
		          }
		        }
		 
		      if (null != host) {
		        host.labels.removeAll(labelsToRemove);
		        
		      }
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