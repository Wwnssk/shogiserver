package server.services.connection;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import org.junit.Test;

import server.services.ServiceManager;
import server.services.user.NoSuchUserException;
import server.services.user.User;


public class ConnectionManagerTest {

	@Test
	public void testSingleConnection() throws InterruptedException, UnknownHostException, IOException, NoSuchUserException {
		String port = "54091";
		Properties properties = new Properties();
		properties.put("port", port);
		ServiceManager.loadConfiguration(ConnectionManager.SERVICE_NAME, properties);
		ConnectionManager cmgr = ServiceManager.getConnectionManager();
		//Thread.sleep(1000);
		Socket s = new Socket("localhost", Integer.parseInt(port));
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(s.getOutputStream());
		out.println("ping");
		out.flush();
		String inLine = in.readLine();
		assertEquals("pong", inLine);
		out.println("login admin admin");
		out.flush();
		Thread.sleep(10);
		User adminUser = ServiceManager.getUserManager().getUser("admin");
		ClientConnection adminConnection = cmgr.getUserConnection(adminUser);
		assertNotNull(adminConnection);
		assertEquals(1, cmgr.getNumberOfUsersLoggedIn());
		
		cmgr.disconnectUser(adminUser);
		assertNull(cmgr.getUserConnection(adminUser));
		assertEquals(0, cmgr.getNumberOfUsersLoggedIn());
		
		System.out.println("Done");
	}
	
	@Test
	public void testMultipleConnections() throws UnknownHostException, IOException, NoSuchUserException, InterruptedException {
		int port = 54091;
		Socket[] sockets = new Socket[101];
		Properties properties = new Properties();
		properties.put("port", port);
		ServiceManager.loadConfiguration(ConnectionManager.SERVICE_NAME, properties);
		ConnectionManager cmgr = ServiceManager.getConnectionManager();
		
		for (int i = 1; i <= 100; i++) {
			Socket s = new Socket("localhost", port);
			sockets[i] = s;
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter out = new PrintWriter(s.getOutputStream());
			out.println("ping");
			out.flush();
			String inLine = in.readLine();
			assertEquals("pong", inLine);
			out.println("login user" + i + " pass");
			out.flush();
			Thread.sleep(10);
			User currentUser = ServiceManager.getUserManager().getUser("user" + i);
			ClientConnection currentUserConnection = cmgr.getUserConnection(currentUser);
			assertNotNull(currentUserConnection);
			assertEquals(i, cmgr.getNumberOfUsersLoggedIn());
			
			System.out.println("Connected Client #" + i);
		}
		
		for (int i = 100; i >= 1; i--) {
			User dieUser = ServiceManager.getUserManager().getUser("user" + i);
			assertTrue(cmgr.checkUserLoggedIn(dieUser));
			assertEquals(i, cmgr.getNumberOfUsersLoggedIn());
			
			cmgr.disconnectUser(dieUser);
			
			assertFalse(cmgr.checkUserLoggedIn(dieUser));
			assertEquals(i-1, cmgr.getNumberOfUsersLoggedIn());
			assertNull(cmgr.getUserConnection(dieUser));
			
			System.out.println("Disconnected Client #" + i);
		}
		
		for (int i = 1; i <= 100; i++) {
			Socket s = new Socket("localhost", port);
			sockets[i] = s;
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter out = new PrintWriter(s.getOutputStream());
			out.println("ping");
			out.flush();
			String inLine = in.readLine();
			assertEquals("pong", inLine);
			out.println("login user" + i + " pass");
			out.flush();
			Thread.sleep(10);
			User currentUser = ServiceManager.getUserManager().getUser("user" + i);
			ClientConnection currentUserConnection = cmgr.getUserConnection(currentUser);
			assertNotNull(currentUserConnection);
			assertEquals(i, cmgr.getNumberOfUsersLoggedIn());
			
			System.out.println("Connected Client #" + i);
		}
		
		for (int i = 100; i >= 1; i--) {
			User dieUser = ServiceManager.getUserManager().getUser("user" + i);
			assertTrue(cmgr.checkUserLoggedIn(dieUser));
			assertEquals(i, cmgr.getNumberOfUsersLoggedIn());
			
			cmgr.disconnectUser(dieUser);
			
			assertFalse(cmgr.checkUserLoggedIn(dieUser));
			assertEquals(i-1, cmgr.getNumberOfUsersLoggedIn());
			assertNull(cmgr.getUserConnection(dieUser));
			
			System.out.println("Disconnected Client #" + i);
		}
	}
}
