package hotswap.runtime;

public class IncreasableStuff {

	private static int number = 0;
	
	public int getNumber() {
		return number++; 
	}
}
