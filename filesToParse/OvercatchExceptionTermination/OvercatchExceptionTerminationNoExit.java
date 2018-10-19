public class OvercatchExceptionTerminationNoExit {

    /**
     * A test function that overcatches Exceptions and does not perform a System.exit
     */
    public static void testTermination() {
        try {

        } catch (Exception e) {
            // No exit
        }
    }

}