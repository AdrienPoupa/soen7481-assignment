public class EqualsWithHashcode {
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		String a = "acd";
		String b = "acd";
		String c = "pol";
		String d = "acd";
		
		if((a.equals(d) == true) && (b.equals(c) == true) && (c.equals(d) == true))
		{
			//
		}
		
		return super.equals(obj);
	}
	
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

}