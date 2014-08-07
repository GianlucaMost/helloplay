import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTest implements TestInterface {

	@Test
	public void testSum() {
		int a = 1 + 1;
		assertEquals(2, a);
	}
	
	@Test
	public void testString(){
		String str = "Hello world";
		assertFalse(str.isEmpty());
	}

	@Override
	public void testInteger() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		
		TestInterface obj = new SimpleTest();
		obj.testInteger();
	}
	
}

interface TestInterface {
	void testSum();
	void testInteger();
}
