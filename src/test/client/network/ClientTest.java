package test.client.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import kindred.client.network.Client;
import kindred.client.view.AbstractView;
import kindred.common.ServerToClientMessage;
import kindred.server.Server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientTest {

    private static Server server;
    private static AbstractView view;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        view = new AbstractView() {

            @Override
            public void remoteEvent(ServerToClientMessage msg) {
            }

            @Override
            protected void readLanguageData() {
            }

            @Override
            public boolean promptForMenuAction(Client client) {
                return false;
            }

            @Override
            public String promptForIP() {
                return null;
            }

            @Override
            public boolean promptForGameAction(Client client) {
                return false;
            }

            @Override
            public void displayMap() {
            }

            @Override
            public void connectionResult(boolean success, String serverIP) {
            }

            @Override
            public void connectionLost() {
            }

            @Override
            public void close() {
            }

            @Override
            public String askForString(String message) {
                return null;
            }
        };
        server = new Server();
        server.runLoopInNewThread();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = RuntimeException.class)
    public void testClient_WrongIP() {
        Client client = new Client("fakehost", view);
    }

    @Test
    public void testClient_CorrectIP() {
        // Client client = new Client("localhost", view);
        // client.disconnect();
    }

    @Test
    public void testNick_Invalid() {
        Client client = new Client("localhost", view);
        client.start();
        for (String s : new String[] { "aa", "34444", "abc%", "inválido" }) {
            client.nick(s);
            wait(500);
            assertNull(client.getNickname());
        }
        client.disconnect();
    }

    @Test
    public void testNick_Valid() {
        Client client = new Client("localhost", view);
        client.start();
        for (String s : new String[] { "nickValido" }) {
            client.nick(s);
            wait(500);
            assertEquals(client.getNickname(), "nickValido");
        }
        client.disconnect();
    }

    @Test
    public void testHost_Invalid() {
        Client client = new Client("localhost", view);
        client.start();
        // no nickname, invalid map
        client.host("thisIsFake");
        wait(500);
        assertFalse(client.isHostingRoom());
        // no nickname, valid map
        client.host("simpleMap");
        wait(500);
        assertFalse(client.isHostingRoom());
        // valid nickname, invalid map
        client.nick("mynickname");
        client.host("thisIsFake");
        wait(500);
        assertFalse(client.isHostingRoom());
        // disconnect
        client.disconnect();
    }

    @Test
    public void testHost_Valid() {
        Client client = new Client("localhost", view);
        client.start();
        client.nick("mynickname");
        // first map
        client.host("simpleMap");
        wait(500);
        assertTrue(client.isHostingRoom());
        // after changing map (discards previous one)
        client.host("happyPlains");
        wait(500);
        assertTrue(client.isHostingRoom());
        // disconnect
        client.disconnect();
    }

    @Test
    public void testUnhost_Invalid() {
        Client client = new Client("localhost", view);
        client.start();
        // without nickname
        client.unhost();
        wait(500);
        assertFalse(client.isHostingRoom());
        // with nickname
        client.nick("mynickname");
        client.unhost();
        wait(500);
        assertFalse(client.isHostingRoom());
        // disconnect
        client.disconnect();
    }

    @Test
    public void testUnhost_Valid() {
        Client client = new Client("localhost", view);
        client.start();
        client.nick("mynickname");
        client.host("simpleMap");
        client.unhost();
        wait(1000);
        assertFalse(client.isHostingRoom());
        // disconnect
        client.disconnect();
    }

    @Test
    public void testJoin_Valid() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("host_");
        guest.nick("guest__");
        wait(400);
        host.host("simpleMap");
        wait(400);
        assertTrue(host.isHostingRoom());
        assertFalse(host.isPlaying());
        assertFalse(guest.isPlaying());
        guest.join("host_");
        wait(800);
        assertTrue(guest.isPlaying());
        assertTrue(host.isPlaying());
        guest.disconnect();
        host.disconnect();
    }

    @Test
    public void testQuit() {
        Client client = new Client("localhost", view);
        client.start();
        client.quit();
        wait(500);
        assertFalse(client.isConnected());
    }

    @Test
    public void testMove() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testAttack() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testSurrender() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("host_");
        guest.nick("guest__");
        wait(400);
        host.host("simpleMap");
        wait(400);
        guest.join("host_");
        wait(800);
        host.surrender();
        wait(800);
        assertFalse(host.isPlaying());
        assertFalse(guest.isPlaying());
        // disconnect
        host.disconnect();
        guest.disconnect();
    }

    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
