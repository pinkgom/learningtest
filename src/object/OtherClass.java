package object;

public class OtherClass {

	String className = getClass().toString();
	
	public OtherClass() {
		
	}
	
	public void printClassName() {
		System.out.println( className );
	}
}
