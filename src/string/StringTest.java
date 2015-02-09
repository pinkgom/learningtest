package string;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringTest {

	@Test
	public void testSubString() {
		
		String testString = "안경찬은 천재인가, 바보인가";
	
		assertThat(testString.substring(0, 2), is("안경") );
		
	}
	
}
