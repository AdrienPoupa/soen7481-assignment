public class EqualsWithoutHashcode {
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		String a = "acd";
		String b = "acd";
		String c = "pol";
		String d = "acd";
		
		if((a.equals(d)) && (b.equals(c)) && (c.equals(d)))
		{
			//
		}
		
		return super.equals(obj);
	}

}