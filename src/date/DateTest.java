package date;

import java.text.ParseException;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Assert;
import org.junit.Test;



public class DateTest {

	@Test
	public void testAddDate() throws ParseException {
        Chronology chrono = GregorianChronology.getInstance();
        LocalDate theDay = new LocalDate(2014, 1, 30, chrono);
        String pattern = "yyyyMMdd";
        Assert.assertEquals(theDay.toString(pattern), "20140130");

				
        LocalDate nextDay = theDay.plusDays(1);
        Assert.assertEquals(nextDay.toString(pattern),"20140131");
    }
}
