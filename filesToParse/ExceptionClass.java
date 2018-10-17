public class ExceptionClass {

    /**
     * A test function that overcatches Exceptions and performs a System.exit
     */
    public static void testException() {
        try {

        } catch (Exception e) {
            System.exit(0);
        }
    }

}