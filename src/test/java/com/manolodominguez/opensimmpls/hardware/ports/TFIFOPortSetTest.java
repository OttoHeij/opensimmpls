/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manolodominguez.opensimmpls.hardware.ports;

import com.manolodominguez.opensimmpls.commons.TLongIDGenerator;
import com.manolodominguez.opensimmpls.commons.TSemaphore;
import com.manolodominguez.opensimmpls.gui.simulator.JSimulationPanel;
import com.manolodominguez.opensimmpls.protocols.TAbstractPDU;
import com.manolodominguez.opensimmpls.protocols.TMPLSPDU;
import com.manolodominguez.opensimmpls.scenario.TExternalLink;
import com.manolodominguez.opensimmpls.scenario.TLERNode;
import com.manolodominguez.opensimmpls.scenario.TLSRNode;
import com.manolodominguez.opensimmpls.scenario.TLink;
import com.manolodominguez.opensimmpls.scenario.TScenario;
import com.manolodominguez.opensimmpls.scenario.TTopology;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author manolodd
 */
public class TFIFOPortSetTest {

    public TFIFOPortSetTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of constructor of class TFIFOPortSet.
     */
    @Test
    public void testConstructor() {
        System.out.println("test constructor");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        // We set 8 ports for the ports set and also give the created node as 
        // the parent node.
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        boolean worksFine = true;
        if (instance.getParentNode() != node) { // Web compare the node reference
            worksFine &= false;
        }
        if (instance.getNumberOfPorts() != 8) { // and the specified numer of ports
            worksFine &= false;
        }
        // And the rest of values the constructor sets
        if (instance.getBufferSizeInMBytes() != 1) {
            worksFine &= false;
        }
        if (instance.getPortSetOccupancy() != 0) {
            worksFine &= false;
        }
        if (instance.isCongestedArtificially()) {
            worksFine &= false;
        }
        assertTrue(worksFine);
    }

    /**
     * Test of constructor of class TFIFOPortSet.
     */
    @Test
    public void testConstructorWhenNumberOfPortsNegative() {
        System.out.println("test constructor");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        // We set -1 ports for the ports set and this should throws an exception
        assertThrows(IllegalArgumentException.class, () -> {
            TFIFOPortSet instance = new TFIFOPortSet(-1, node);
        });
    }

    /**
     * Test of constructor of class TFIFOPortSet.
     */
    @Test
    public void testConstructorWhenParentNodeIsNull() {
        System.out.println("test constructor");
        // We set null for the parent node and this should throws an exception
        assertThrows(IllegalArgumentException.class, () -> {
            TFIFOPortSet instance = new TFIFOPortSet(8, null);
        });
    }

// Test of TPortSet superclass
    /**
     * Test of increasePortSetOccupancy method, of class TFIFOPortSet.
     */
    @Test
    public void testIncreasePortSetOccupancy() {
        System.out.println("increasePortSetOccupancy");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // Occupancy is 0 here
        instance.increasePortSetOccupancy(123); // increase port set occupancy in 123 octets
        assertEquals(123, instance.getPortSetOccupancy());
    }

    /**
     * Test of increasePortSetOccupancy method, of class TFIFOPortSet.
     */
    @Test
    public void testIncreasePortSetOccupancyWhenNegativeIncrement() {
        System.out.println("increasePortSetOccupancy");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // Occupancy is 0 here
        assertThrows(IllegalArgumentException.class, () -> {
            instance.increasePortSetOccupancy(-1); // This should thrown an exception
        });
    }

    /**
     * Test of decreasePortSetOccupancy method, of class TFIFOPortSet.
     */
    @Test
    public void testDecreasePortSetOccupancy() {
        System.out.println("test decreasePortSetOccupancy");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // Occupancy is 0 here
        instance.increasePortSetOccupancy(123); // increase port set occupancy in 123 octets
        instance.decreasePortSetOccupancySize(23); // decrease port set occupancy in 23 octets
        assertEquals(100, instance.getPortSetOccupancy());
    }

    /**
     * Test of decreasePortSetOccupancy method, of class TFIFOPortSet.
     */
    @Test
    public void testDecreasePortSetOccupancyWhenNegativeDecrease() {
        System.out.println("test decreasePortSetOccupancy");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // Occupancy is 0 here
        instance.increasePortSetOccupancy(123); // increase port set occupancy in 123 octets
        assertThrows(IllegalArgumentException.class, () -> {
            instance.decreasePortSetOccupancySize(-1); // This should thrown an exception
        });
    }

    /**
     * Test of setPortSetOccupancySize method, of class TFIFOPortSet.
     */
    @Test
    public void testSetPortSetOccupancySize() {
        System.out.println("test setPortSetOccupancySize");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // Occupancy is 0 here
        instance.setPortSetOccupancySize(123); // sets the port set occupancy in 123 octets
        assertEquals(123, instance.getPortSetOccupancy());
    }

    /**
     * Test of setPortSetOccupancySize method, of class TFIFOPortSet.
     */
    @Test
    public void testSetPortSetOccupancySizeWhenNegative() {
        System.out.println("test setPortSetOccupancySize");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // Occupancy is 0 here
        assertThrows(IllegalArgumentException.class, () -> {
            instance.setPortSetOccupancySize(-1); // This should thrown an exception
        });
    }

    /**
     * Test of getPortSetOccupancy method, of class TFIFOPortSet.
     */
    @Test
    public void testGetPortSetOccupancy() {
        System.out.println("test getPortSetOccupancy");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // Occupancy is 0 here
        instance.setPortSetOccupancySize(100); //sets occupancy to 100 octets
        assertEquals(100, instance.getPortSetOccupancy());
    }

    /**
     * Test of isCongestedArtificially method, of class TFIFOPortSet.
     */
    @Test
    public void testIsCongestedArtificially() {
        System.out.println("test isCongestedArtificially");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // Occupancy is 0 here
        instance.setPortSetOccupancySize(100); //sets occupancy to 100 octets
        boolean worksFine = true;
        if (instance.isCongestedArtificially()) { // By default is not congested
            worksFine &= false;
        }
        instance.setArtificiallyCongested(true); // sets artificially congested
        if (!instance.isCongestedArtificially()) { // Should be congested artificially
            worksFine &= false;
        }
        assertTrue(worksFine);
    }

    /**
     * Test of getNumberOfPorts method, of class TFIFOPortSet.
     */
    @Test
    public void testGetNumberOfPorts() {
        System.out.println("test getNumberOfPorts");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(5, node); // sets 5 ports
        assertEquals(5, instance.getNumberOfPorts());
    }

    /**
     * Test of getParentNode method, of class TFIFOPortSet.
     */
    @Test
    public void testGetParentNode() {
        System.out.println("test getParentNode");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertTrue(node == instance.getParentNode()); // Both references should be the same
    }

    /**
     * Test of setUnlimitedBuffer method, of class TFIFOPortSet.
     */
    @Test
    public void testSetUnlimitedBuffer() {
        System.out.println("test setUnlimitedBuffer");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // By default, it is not unlimited
        boolean worksFine = true;
        for (int i = 0; i < instance.getNumberOfPorts(); i++) {
            if (instance.getPort(i).isUnlimitedBuffer()) {
                worksFine &= false;
            }
        }
        instance.setUnlimitedBuffer(true); // all ports are defined as unlimited
        for (int i = 0; i < instance.getNumberOfPorts(); i++) {
            if (!instance.getPort(i).isUnlimitedBuffer()) {
                worksFine &= false;
            }
        }
        assertTrue(worksFine);
    }

// Test of TFIFOPortSet subclass
    /**
     * Test of getPort method, of class TFIFOPortSet.
     */
    @Test
    public void testGetPort() {
        System.out.println("test getPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // By default, it is not unlimited
        assertTrue(instance.getPort(3) instanceof TPort);
    }

    /**
     * Test of getPort method, of class TFIFOPortSet.
     */
    @Test
    public void testGetPortWhenOutOfRange1() {
        System.out.println("test getPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // By default, it is not unlimited
        assertThrows(IllegalArgumentException.class, () -> {
            instance.getPort(-1); // This should thrown an exception
        });
    }

    /**
     * Test of getPort method, of class TFIFOPortSet.
     */
    @Test
    public void testGetPortWhenOutOfRange2() {
        System.out.println("test getPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node); // By default, it is not unlimited
        assertThrows(IllegalArgumentException.class, () -> {
            instance.getPort(8); // There are 8 ports (0 to 7) so, this causes an exception
        });
    }

    /**
     * Test of setBufferSizeInMB method, of class TFIFOPortSet.
     */
    @Test
    public void testSetBufferSizeInMB() {
        System.out.println("test setBufferSizeInMB");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        instance.setBufferSizeInMB(5); // we set 5 MB buffer size
        assertEquals(5, instance.getBufferSizeInMBytes());
    }

    /**
     * Test of setBufferSizeInMB method, of class TFIFOPortSet.
     */
    @Test
    public void testSetBufferSizeInMBWhenNegative() {
        System.out.println("test setBufferSizeInMB");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.setBufferSizeInMB(-1); // This causes an exception
        });
    }

    /**
     * Test of getBufferSizeInMBytes method, of class TFIFOPortSet.
     */
    @Test
    public void testGetBufferSizeInMBytes() {
        System.out.println("getBufferSizeInMBytes");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLSRNode node = new TLSRNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        boolean worksFine = true;
        if (instance.getBufferSizeInMBytes() != 1) { // By default buffer size is 1 MB
            worksFine &= false;
        }
        instance.setBufferSizeInMB(5); // we set 5 MB buffer size
        if (instance.getBufferSizeInMBytes() != 5) {
            worksFine &= false;
        }
        assertTrue(worksFine);
    }

    /**
     * Test of isAvailable method, of class TFIFOPortSet.
     */
    @Test
    public void testIsAvailable() {
        System.out.println("test isAvailable");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        TExternalLink link = new TExternalLink(1, new TLongIDGenerator(), topology);
        boolean worksFine = true;
        if (!instance.getPort(3).isAvailable()) { // By default all ports ara available
            worksFine &= false;
        }
        instance.getPort(3).setLink(link); // Now, port 3 is not available
        if (instance.getPort(3).isAvailable()) {
            worksFine &= false;
        }
        assertTrue(worksFine);
    }

    /**
     * Test of isAvailable method, of class TFIFOPortSet.
     */
    @Test
    public void testIsAvailableWhenOutOfRange1() {
        System.out.println("test isAvailable");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.isAvailable(-1); // There are 8 ports (0 to 7) so, this causes an exception
        });
    }

    /**
     * Test of isAvailable method, of class TFIFOPortSet.
     */
    @Test
    public void testIsAvailableWhenOutOfRange2() {
        System.out.println("test isAvailable");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.isAvailable(8); // There are 8 ports (0 to 7) so, this causes an exception
        });
    }

    /**
     * Test of hasAvailablePorts method, of class TFIFOPortSet.
     */
    @Test
    public void testHasAvailablePorts() {
        System.out.println("test hasAvailablePorts");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        TExternalLink link = new TExternalLink(1, new TLongIDGenerator(), topology);
        boolean worksFine = true;
        if (!instance.hasAvailablePorts()) { // By default all ports are available
            worksFine &= false;
        }
        for (int i = 0; i < instance.getNumberOfPorts(); i++) {
            instance.getPort(i).setLink(link); // set all ports as unavailable
        }
        if (instance.hasAvailablePorts()) {
            worksFine &= false;
        }
        assertTrue(worksFine);
    }

    /**
     * Test of connectLinkToPort method, of class TFIFOPortSet.
     */
    @Test
    public void testConnectLinkToPort() {
        System.out.println("test connectLinkToPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        TExternalLink link = new TExternalLink(1, new TLongIDGenerator(), topology);
        boolean worksFine = true;
        if (!instance.hasAvailablePorts()) { // By default all ports are available
            worksFine &= false;
        }
        for (int i = 0; i < instance.getNumberOfPorts(); i++) {
            instance.connectLinkToPort(link, i); // set all ports as unavailable
        }
        if (instance.hasAvailablePorts()) {
            worksFine &= false;
        }
        assertTrue(worksFine);
    }

    /**
     * Test of connectLinkToPort method, of class TFIFOPortSet.
     */
    @Test
    public void testConnectLinkToPortWhenOutOfRange1() {
        System.out.println("test connectLinkToPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        TExternalLink link = new TExternalLink(1, new TLongIDGenerator(), topology);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.connectLinkToPort(link, -1); // There are 8 ports (0 to 7) so, this causes an exception
        });
    }

    /**
     * Test of connectLinkToPort method, of class TFIFOPortSet.
     */
    @Test
    public void testConnectLinkToPortWhenOutOfRange2() {
        System.out.println("test connectLinkToPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        TExternalLink link = new TExternalLink(1, new TLongIDGenerator(), topology);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.connectLinkToPort(link, 8); // There are 8 ports (0 to 7) so, this causes an exception
        });
    }

    /**
     * Test of connectLinkToPort method, of class TFIFOPortSet.
     */
    @Test
    public void testConnectLinkToPortWhenLinkIsNull() {
        System.out.println("test connectLinkToPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.connectLinkToPort(null, 5); // link == null causes an exception
        });
    }

    /**
     * Test of getLinkConnectedToPort method, of class TFIFOPortSet.
     */
    @Test
    public void testGetLinkConnectedToPort() {
        System.out.println("test getLinkConnectedToPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        TExternalLink link = new TExternalLink(1, new TLongIDGenerator(), topology);
        boolean worksFine = true;
        for (int i = 0; i < instance.getNumberOfPorts(); i++) {
            instance.connectLinkToPort(link, i); // set all ports as unavailable
        }
        for (int i = 0; i < instance.getNumberOfPorts(); i++) {
            if (instance.getLinkConnectedToPort(i) != link) { // compares references 
                worksFine &= false;
            }
        }
        assertTrue(worksFine);
    }

    /**
     * Test of getLinkConnectedToPort method, of class TFIFOPortSet.
     */
    @Test
    public void testGetLinkConnectedToPortWhnOutOfRange1() {
        System.out.println("test getLinkConnectedToPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.getLinkConnectedToPort(-1); // should cause an exception
        });
    }

    /**
     * Test of getLinkConnectedToPort method, of class TFIFOPortSet.
     */
    @Test
    public void testGetLinkConnectedToPortWhnOutOfRange2() {
        System.out.println("test getLinkConnectedToPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.getLinkConnectedToPort(8); // should cause an exception
        });
    }

    /**
     * Test of disconnectLinkFromPort method, of class TFIFOPortSet.
     */
    @Test
    public void testDisconnectLinkFromPort() {
        System.out.println("test disconnectLinkFromPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        TExternalLink link = new TExternalLink(1, new TLongIDGenerator(), topology);
        boolean worksFine = true;
        for (int i = 0; i < instance.getNumberOfPorts(); i++) {
            instance.connectLinkToPort(link, i); // set all ports as unavailable
        }
        for (int i = 0; i < instance.getNumberOfPorts(); i++) {
            instance.disconnectLinkFromPort(i); // set all port as available
            if (!instance.getPort(i).isAvailable()) {
                worksFine &= false;
            }
        }
        assertTrue(worksFine);
    }

    /**
     * Test of disconnectLinkFromPort method, of class TFIFOPortSet.
     */
    @Test
    public void testDisconnectLinkFromPortWhenOutOfRange1() {
        System.out.println("test disconnectLinkFromPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.disconnectLinkFromPort(8); // should cause an exception
        });
    }

    /**
     * Test of disconnectLinkFromPort method, of class TFIFOPortSet.
     */
    @Test
    public void testDisconnectLinkFromPortWhenOutOfRange2() {
        System.out.println("test disconnectLinkFromPort");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertThrows(IllegalArgumentException.class, () -> {
            instance.disconnectLinkFromPort(8); // should cause an exception
        });
    }

    /**
     * Test of getNextPacket method, of class TFIFOPortSet.
     */
    @Test
    public void testGetNextPacket() {
        System.out.println("getNextPacket");
        TScenario scenario = new TScenario();  //Creates an scenario
        TTopology topology = new TTopology(scenario); //Creates a topology
        TLERNode tailEndNode = new TLERNode(2, "10.0.0.2", new TLongIDGenerator(), topology); //Creates a node
        tailEndNode.setName("Dummy tail end node name");
        topology.addNode(tailEndNode); // Adds tail end node to the topology
        JSimulationPanel simulationPanel = new JSimulationPanel();
        tailEndNode.simulationEventsListener.setSimulationPanel(simulationPanel);
        //Creates a new MPLS packet directed to tail end node.
        boolean worksFine = true;
        TMPLSPDU mplsPacket = new TMPLSPDU(1, "10.0.0.1", "10.0.0.2", 1024);
        for (int i = 0; i < tailEndNode.getPorts().getNumberOfPorts(); i++) {
            tailEndNode.getPorts().getPort(i).reEnqueuePacket(mplsPacket); // put a packet in each port
            if (tailEndNode.getPorts().getPort(i).getNumberOfPackets() != 1) { // check that packets are there
                worksFine &= false;
            }
        }
        for (int i = 0; i < tailEndNode.getPorts().getNumberOfPorts(); i++) {
            if (!(tailEndNode.getPorts().getNextPacket() instanceof TAbstractPDU)) { // check that all packets are there and can be retrieved
                worksFine &= false;
            }
        }
        if (tailEndNode.getPorts().getNextPacket() != null) { // Next call to getNextPacket should return null
            worksFine &= false;
        }
        assertTrue(worksFine);
    }

    /**
     * Test of getNextPacket method, of class TFIFOPortSet.
     */
    @Test
    public void testGetNextPacketWhenNoPacketAwaiting() {
        System.out.println("getNextPacket");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertNull(instance.getNextPacket()); // No packet awaiting, so should be null
    }

    /**
     * Test of isThereAnyPacketToSwitch method, of class TFIFOPortSet.
     */
    @Test
    public void testIsThereAnyPacketToSwitch() {
        System.out.println("isThereAnyPacketToSwitch");
        TScenario scenario = new TScenario();  //Creates an scenario
        TTopology topology = new TTopology(scenario); //Creates a topology
        TLERNode tailEndNode = new TLERNode(2, "10.0.0.2", new TLongIDGenerator(), topology); //Creates a node
        tailEndNode.setName("Dummy tail end node name");
        topology.addNode(tailEndNode); // Adds tail end node to the topology
        JSimulationPanel simulationPanel = new JSimulationPanel();
        tailEndNode.simulationEventsListener.setSimulationPanel(simulationPanel);
        //Creates a new MPLS packet directed to tail end node.
        TMPLSPDU mplsPacket = new TMPLSPDU(1, "10.0.0.1", "10.0.0.2", 1024);
        for (int i = 0; i < tailEndNode.getPorts().getNumberOfPorts(); i++) {
            tailEndNode.getPorts().getPort(i).reEnqueuePacket(mplsPacket); // put a packet in each port
        }
        assertTrue(tailEndNode.getPorts().isThereAnyPacketToSwitch());
    }

    /**
     * Test of isThereAnyPacketToSwitch method, of class TFIFOPortSet.
     */
    @Test
    public void testIsThereAnyPacketToSwitchWhenThereIsNotAPacket() {
        System.out.println("isThereAnyPacketToSwitch");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertFalse(instance.isThereAnyPacketToSwitch());
    }

    /**
     * Test of isThereAnyPacketToRoute method, of class TFIFOPortSet.
     */
    @Test
    public void testIsThereAnyPacketToRoute() {
        System.out.println("isThereAnyPacketToRoute");
        TScenario scenario = new TScenario();  //Creates an scenario
        TTopology topology = new TTopology(scenario); //Creates a topology
        TLERNode tailEndNode = new TLERNode(2, "10.0.0.2", new TLongIDGenerator(), topology); //Creates a node
        tailEndNode.setName("Dummy tail end node name");
        topology.addNode(tailEndNode); // Adds tail end node to the topology
        JSimulationPanel simulationPanel = new JSimulationPanel();
        tailEndNode.simulationEventsListener.setSimulationPanel(simulationPanel);
        //Creates a new MPLS packet directed to tail end node.
        TMPLSPDU mplsPacket = new TMPLSPDU(1, "10.0.0.1", "10.0.0.2", 1024);
        for (int i = 0; i < tailEndNode.getPorts().getNumberOfPorts(); i++) {
            tailEndNode.getPorts().getPort(i).reEnqueuePacket(mplsPacket); // put a packet in each port
        }
        assertTrue(tailEndNode.getPorts().isThereAnyPacketToRoute());
    }

    /**
     * Test of isThereAnyPacketToRoute method, of class TFIFOPortSet.
     */
    @Test
    public void testIsThereAnyPacketToRouteWhenThereIsNotAPacket() {
        System.out.println("isThereAnyPacketToRoute");
        TScenario scenario = new TScenario();
        TTopology topology = new TTopology(scenario);
        TLERNode node = new TLERNode(1, "10.0.0.1", new TLongIDGenerator(), topology);
        TFIFOPortSet instance = new TFIFOPortSet(8, node);
        assertFalse(instance.isThereAnyPacketToRoute());
    }

    /**
     * Test of canSwitchPacket method, of class TFIFOPortSet.
     */
    @Test
    public void testCanSwitchPacket() {
        System.out.println("canSwitchPacket");
        int maxSwitchableOctects = 0;
        TFIFOPortSet instance = null;
        boolean expResult = false;
        boolean result = instance.canSwitchPacket(maxSwitchableOctects);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of skipPort method, of class TFIFOPortSet.
     */
    @Test
    public void testSkipPort() {
        System.out.println("skipPort");
        TFIFOPortSet instance = null;
        instance.skipPort();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReadPort method, of class TFIFOPortSet.
     */
    @Test
    public void testGetReadPort() {
        System.out.println("getReadPort");
        TFIFOPortSet instance = null;
        int expResult = 0;
        int result = instance.getReadPort();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocalPortConnectedToANodeWithIPv4Address method, of class
     * TFIFOPortSet.
     */
    @Test
    public void testGetLocalPortConnectedToANodeWithIPv4Address() {
        System.out.println("getLocalPortConnectedToANodeWithIPv4Address");
        String adjacentNodeIPv4Address = "";
        TFIFOPortSet instance = null;
        TPort expResult = null;
        TPort result = instance.getLocalPortConnectedToANodeWithIPv4Address(adjacentNodeIPv4Address);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIPv4OfNodeLinkedTo method, of class TFIFOPortSet.
     */
    @Test
    public void testGetIPv4OfNodeLinkedTo() {
        System.out.println("getIPv4OfNodeLinkedTo");
        int portID = 0;
        TFIFOPortSet instance = null;
        String expResult = "";
        String result = instance.getIPv4OfNodeLinkedTo(portID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCongestionLevel method, of class TFIFOPortSet.
     */
    @Test
    public void testGetCongestionLevel() {
        System.out.println("getCongestionLevel");
        TFIFOPortSet instance = null;
        long expResult = 0L;
        long result = instance.getCongestionLevel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class TFIFOPortSet.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        TFIFOPortSet instance = null;
        instance.reset();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setArtificiallyCongested method, of class TFIFOPortSet.
     */
    @Test
    public void testSetArtificiallyCongested() {
        System.out.println("setArtificiallyCongested");
        boolean congestArtificially = false;
        TFIFOPortSet instance = null;
        instance.setArtificiallyCongested(congestArtificially);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}