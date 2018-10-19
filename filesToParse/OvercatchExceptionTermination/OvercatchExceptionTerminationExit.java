public class OvercatchExceptionTerminationExit {

    /**
     * A test function that overcatches Exceptions and performs a System.exit
     */
    public static void testTermination() {
        try {

        } catch (Exception e) {
            System.exit(0);
        }
    }

}