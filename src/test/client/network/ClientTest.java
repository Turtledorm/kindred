package test.client.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import kindred.client.network.Client;
import kindred.client.view.AbstractView;
import kindred.common.ServerToClientMessage;
import kindred.server.Server;

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
        server.close();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = RuntimeException.class)
    public void testClient_WrongIP() {
        @SuppressWarnings("unused")
        Client client = new Client("fakehost", view);
    }

    @Test
    public void testClient_CorrectIP() {
        Client client = new Client("localhost", view);
        client.disconnect();
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
    public void testNick_Invalid() {
        Client client = new Client("localhost", view);
        client.start();
        for (String s : new String[] { "aa", "33444", "abc%", "inválido",
                "a1234567890" }) {
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
        for (String s : new String[] { "niceNick" }) {
            client.nick(s);
            wait(500);
            assertEquals(client.getNickname(), "niceNick");
        }
        client.disconnect();
    }

    @Test
    public void testHost_Invalid() {
        Client client = new Client("localhost", view);
        client.start();
        // No nickname, invalid map
        client.host("thisIsFake");
        wait(500);
        assertFalse(client.isHostingRoom());
        // No nickname, valid map
        client.host("testmap");
        wait(500);
        assertFalse(client.isHostingRoom());
        // Valid nickname, invalid map
        client.nick("hostInvalid");
        client.host("thisIsFake");
        wait(500);
        assertFalse(client.isHostingRoom());

        // Disconnect
        client.disconnect();
    }

    @Test
    public void testHost_Valid() {
        Client client = new Client("localhost", view);
        client.start();
        client.nick("hostValid");
        // First map
        client.host("testmap");
        wait(500);
        assertTrue(client.isHostingRoom());
        // After changing map (discards previous one)
        client.host("simpleMap");
        wait(500);
        assertTrue(client.isHostingRoom());

        // Disconnect
        client.disconnect();
    }

    @Test
    public void testUnhost_Invalid() {
        Client client = new Client("localhost", view);
        client.start();
        // Without nickname
        client.unhost();
        wait(500);
        assertFalse(client.isHostingRoom());
        // With nickname
        client.nick("unhostInv");
        client.unhost();
        wait(500);
        assertFalse(client.isHostingRoom());

        // Disconnect
        client.disconnect();
    }

    @Test
    public void testUnhost_Valid() {
        Client client = new Client("localhost", view);
        client.start();
        client.nick("unhostVal");
        client.host("testmap");
        client.unhost();
        wait(1000);
        assertFalse(client.isHostingRoom());

        // Disconnect
        client.disconnect();
    }

    @Test
    public void testJoin_Valid() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("hostJoin");
        guest.nick("guestJoin");
        wait(500);
        host.host("testmap");
        wait(800);
        assertTrue(host.isHostingRoom());
        assertFalse(host.isPlaying());
        assertFalse(guest.isPlaying());
        guest.join("hostJoin");
        wait(800);
        assertFalse(host.isHostingRoom());
        assertTrue(guest.isPlaying());
        assertTrue(host.isPlaying());

        // Disconnect
        guest.disconnect();
        host.disconnect();
    }

    @Test
    public void testSurrender() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("hostSur");
        guest.nick("guestSur");
        wait(500);
        host.host("testmap");
        wait(800);
        guest.join("hostSur");
        wait(800);
        host.surrender();
        wait(600);
        assertFalse(host.isPlaying());
        assertFalse(guest.isPlaying());

        // Disconnect
        host.disconnect();
        guest.disconnect();
    }

    @Test
    public void testEndTurn() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("hostEnd");
        guest.nick("guestEnd");
        wait(400);
        host.host("testmap");
        wait(400);
        assertEquals(-1, host.getGameTurn());
        assertEquals(-1, guest.getGameTurn());
        guest.join("hostEnd");
        wait(800);
        assertEquals(1, host.getGameTurn());
        assertEquals(1, guest.getGameTurn());
        host.endTurn();
        wait(600);
        assertEquals(2, host.getGameTurn());
        assertEquals(2, guest.getGameTurn());
        guest.endTurn();
        wait(600);
        assertEquals(1, host.getGameTurn());
        assertEquals(1, guest.getGameTurn());

        // Disconnect
        host.disconnect();
        guest.disconnect();
    }

    @Test
    public void testMove_Valid() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("hostMvVl");
        guest.nick("guestMvVl");
        wait(400);
        host.host("testmap");
        wait(400);
        guest.join("hostMvVl");
        wait(800);

        assertTrue(host.move(new int[] { 0, 0, 1, 0 }));
        // Disallow another movement from the same Unit during the turn
        assertFalse(host.move(new int[] { 1, 0, 0, 0 }));

        // Disconnect
        host.disconnect();
        guest.disconnect();
    }

    @Test
    public void testMove_Invalid() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("hostMvIn");
        guest.nick("guestMvIn");
        wait(400);
        host.host("testmap");
        wait(400);
        guest.join("hostMvIn");
        wait(800);

        // Important: move() coordinates begin on (0, 0)

        // Move outside of map borders
        assertFalse(host.move(new int[] { 1, -1, 1, 0 }));
        assertFalse(host.move(new int[] { 0, 0, 0, -1 }));
        assertFalse(host.move(new int[] { 0, 0, -1, 0 }));
        assertFalse(host.move(new int[] { 1, 1, 4, 3 }));
        assertFalse(host.move(new int[] { 1, 1, 1, 900 }));

        // Move to same position
        assertFalse(host.move(new int[] { 0, 0, 0, 0 }));

        // Move to occupied Tile
        assertFalse(host.move(new int[] { 0, 0, 1, 1 }));
        assertFalse(host.move(new int[] { 0, 0, 0, 1 }));

        // Move to Tile further than Unit's movement
        assertFalse(host.move(new int[] { 0, 1, 1, 7 }));

        // Move opponent's Unit
        assertFalse(host.move(new int[] { 1, 2, 1, 3 }));

        // Disconnect
        host.disconnect();
        guest.disconnect();
    }

    @Test
    public void testAttack_Valid() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("hostAtkVl");
        guest.nick("guestAtkVl");
        wait(400);
        host.host("testmap");
        wait(400);
        guest.join("hostAtkVl");
        wait(800);

        Assert.assertTrue(host.attack(new int[] { 0, 1, 1, 1 }));

        // Disallow another movement or attack from the same Unit during the
        // turn
        Assert.assertFalse(host.move(new int[] { 0, 1, 1, 0 }));
        Assert.assertFalse(host.attack(new int[] { 0, 1, 1, 1 }));

        // Disconnect
        host.disconnect();
        guest.disconnect();
    }

    @Test
    public void testAttack_Invalid() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("hostAtkIn");
        guest.nick("guestAtkIn");
        wait(400);
        host.host("testmap");
        wait(400);
        guest.join("hostAtkIn");
        wait(800);

        // Important: attack() coordinates begin on (0, 0)

        // Attack with invalid coordinates
        assertFalse(host.attack(new int[] { 0, 0, 0, -1 }));
        assertFalse(host.attack(new int[] { 1, 7, -1, 0 }));
        assertFalse(host.attack(new int[] { 800, -6, 1, 1 }));
        assertFalse(host.attack(new int[] { -1, 0, 1, 1 }));

        // Valid coordinates, empty Tiles
        assertFalse(host.attack(new int[] { 0, 0, 1, 0 }));
        assertFalse(host.attack(new int[] { 0, 2, 0, 1 }));
        assertFalse(host.attack(new int[] { 1, 0, 1, 1 }));

        // Attack Tile further than Unit's range
        assertFalse(host.attack(new int[] { 0, 7, 1, 1 }));

        // Attack a Unit of your team
        assertFalse(host.attack(new int[] { 0, 1, 0, 0 }));

        // Attack using opponent's Unit
        assertFalse(host.attack(new int[] { 1, 1, 0, 1 }));
        assertFalse(host.attack(new int[] { 1, 1, 1, 2 }));

        // Disconnect
        host.disconnect();
        guest.disconnect();
    }

    @Test
    public void testUnitsNextTurn() {
        Client host = new Client("localhost", view);
        Client guest = new Client("localhost", view);
        host.start();
        guest.start();
        host.nick("hostCtrl");
        guest.nick("guestCtrl");
        wait(400);
        host.host("testmap");
        wait(400);
        guest.join("hostCtrl");
        wait(800);

        host.move(new int[] { 0, 0, 1, 0 });
        host.attack(new int[] { 0, 1, 1, 1 });
        host.endTurn();
        wait(600);
        guest.endTurn();
        wait(400);
        assertTrue(host.move(new int[] { 1, 0, 0, 0 }));
        assertTrue(host.attack(new int[] { 0, 1, 1, 1 }));

        // Disconnect
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
