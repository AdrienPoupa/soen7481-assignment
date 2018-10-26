
public class NoEffectConditionTestClass {
	
	public NoEffectConditionTestClass() {
		
	}
	
	private string methodWithSeveralUneffectiveCondition() {
		string result = "";
		String a = "2";
		int b = 4;
		if(a.endsWith("2") == a.endsWith("2")) {
			// Do something
		}
		
		if(b == b) {
			// Do something
		}
		
		if(5) {
			// Do something
		}
		
		if('g') {
			// Do something
		}
		
		if("abc") {
			// Do something
		}
		
		if(true) {
			// Do something
		}
		
		if(2 * 5 - 3 == 2 * 5 - 3) {
			// Do something
		}
		
		if(2 > 2) {
			// Do something
		}
		return result;
	}
	
	private void mehodWithAnEffectiveCondition() {
		boolean b = false;
		
		if(b) {
			String str = "This is valid";
		}
	}
}