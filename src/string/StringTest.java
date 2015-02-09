package string;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringTest {

		
	String testString = "안경찬은 천재인가, 바보인가";
	
	@Test
	public void testSubString() {
		assertThat(testString.substring(0, 2), is("안경") );		
	}
	
	@Test
	public void testSplit() {
		assertThat(testString.split(",")[0], is("안경찬은 천재인가") );
	}
}
