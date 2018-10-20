public class InadequateLoggingInformationInCatchBlocks {

    /**
     * A test function that has a DUPLICATE LOGGING INFORMATION IN catch block of same try or catch block has no logging information
     */
	

    public static void anotherMain(String[] args) {
        
    	try {
        	
        }
        catch(ArrayIndexOutOfBoundsException e) {
        	System.out.println("hello");
        }
        catch(ArrayIndexOutOfBoundsException e) {
        	System.out.println("hello1");
        }
    }
    public static void another(String[] args) {

    try {
    	
    }
    catch(ArrayIndexOutOfBoundsException e) {
    	System.out.println("test");
    }}
}