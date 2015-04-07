package hotswap.runtime;

public class HotSwap {

	public static void main(String[] args) throws Exception {
				
		while(true) {
			
			IncreasableStuff stuff = new IncreasableStuff();
			System.out.println( stuff.getNumber() );
			Thread.sleep(1000);
		
		}
	}
}
