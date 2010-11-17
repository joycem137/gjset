package gjset.tests;

import static org.junit.Assert.assertNotNull;
import gjset.client.ClientGUIController;
import gjset.client.ConcreteClientGUIController;

import org.junit.Before;
import org.junit.Test;


/**
 * This test suite tests the GUI Controller itself.
 */
public class TestGUIController
{
	private MockClientCommunicator client;
	
	/**
	 * This method sets up each test before running it.
	 */
	@Before
	public void setUp()
	{
		client = new MockClientCommunicator();
	}
	
	/**
	 * This method tests the initial configuration of the controller.
	 */
	@Test
	public void testInitialSetup()
	{
		ClientGUIController controller = new ConcreteClientGUIController(client);
		
		assertNotNull(controller.getClientGUIModel());
		
	}
	
	/**
	 * 
	 * This method tests whether or not sets can be called.
	 *
	 */
	@Test
	public void testCallSet()
	{
	}
}
