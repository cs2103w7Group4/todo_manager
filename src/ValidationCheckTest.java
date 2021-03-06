package todo_manager;
/*
 * @author: A0085159W
 */
import static org.junit.Assert.*;

import org.junit.Test;

public class ValidationCheckTest {

	@Test
	public void test() {
		assertDate();
		assertTime();
		assertStatus();
	}
	private void assertStatus() {
		assertTrue("valid status", ValidationCheck.isValidStatus("done"));
		assertTrue("valid status", ValidationCheck.isValidStatus("undone"));
		
		assertFalse("valid status", ValidationCheck.isValidStatus("HELLO"));
	}
	
	private void assertDate() {
		assertTrue("random date", ValidationCheck.isValidDate("260314"));
		assertTrue("random date", ValidationCheck.isValidDate("261012"));
		
		//boundary cases: leap year
		assertTrue("non-leap year, feb 28", ValidationCheck.isValidDate("280214"));
		assertFalse("non-leap year, feb 29", ValidationCheck.isValidDate("290214"));
		assertTrue("leap year, feb 28", ValidationCheck.isValidDate("280212"));
		assertTrue("leap year, feb 29", ValidationCheck.isValidDate("290212"));
		
		//boundary cases: last day of month
		assertTrue("check for 31st", ValidationCheck.isValidDate("310113"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("310213"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("310313"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("310413"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("310513"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("310613"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("310713"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("310813"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("310913"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("311013"));
		assertFalse("check for 31st", ValidationCheck.isValidDate("311113"));
		assertTrue("check for 31st", ValidationCheck.isValidDate("311213"));
	}
	private void assertTime() {
		assertTrue("random time", ValidationCheck.isValidTime("1234"));
		
		//boundary cases: first and last value in "valid" partition
		assertTrue("max time", ValidationCheck.isValidTime("2359"));
		assertTrue("min time", ValidationCheck.isValidTime("0000"));
		
		//boundary cases: first and last value in "invalid" partition
		assertFalse("exceed max hours", ValidationCheck.isValidTime("2459"));
		assertFalse("exceed max min", ValidationCheck.isValidTime("2360"));
		assertFalse("negative", ValidationCheck.isValidTime("-120"));
		assertFalse("alphabets", ValidationCheck.isValidTime("abcd"));
	}

}
