package marshalling;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MarshallingTest {

	@Test
	public void testMarshalling() {
		assertThat("test", is("test"));
	}
}
