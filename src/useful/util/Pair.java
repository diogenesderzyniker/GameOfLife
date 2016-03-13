package useful.util;

public class Pair {

	public int a;
	public int b;
	
	public Pair(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public String toString() {
		return "[ " + this.a + " : " + this.b +  " ]";
	}
}
