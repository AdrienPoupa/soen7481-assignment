public class UnusedMethodTestClass {
	
	public UnusedMethodTestClass() {
		this.usedMethod();
	}
	
	public int anUnusedMethod() {
		return 2 * 2;
	}
	
	public void usedMethod() {
		int a = 2 * 2;
	}
}