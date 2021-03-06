/* 
 * Copyright (C) Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.manolodominguez.opensimmpls.hardware.dmgp;

import com.manolodominguez.opensimmpls.commons.TIPv4AddressGenerator;
import com.manolodominguez.opensimmpls.resources.translations.AvailableBundles;
import java.util.LinkedList;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements an entry that will store data related to a
 * retransmission requested by a node.
 *
 * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
 * @version 2.0
 */
public class TGPSRPRequestEntry implements Comparable<TGPSRPRequestEntry> {

    /**
     * This is the class constructor. Implements a new instance of
     * TGPSRPRequestsEntry.
     *
     * @param arrivalOrder Arrival order. This is a number that must be used to
     * make this entry shorted in a collection in a coherent way.
     * @since 2.0
     */
    public TGPSRPRequestEntry(int arrivalOrder) {
        translations = ResourceBundle.getBundle(AvailableBundles.T_GPSRP_REQUEST_ENTRY.getPath());
        if (arrivalOrder < ZERO) {
            logger.error(translations.getString("argumentOutOfRange"));
            throw new IllegalArgumentException(translations.getString("argumentOutOfRange"));
        }
        this.arrivalOrder = arrivalOrder;
        timeout = DEFAULT_GPSRP_TIMEOUT_NANOSECONDS;
        attempts = DEFAULT_GPSRP_ATTEMPTS;
        globalFlowID = DEFAULT_FLOWID;
        packetGoSGlobalUniqueIdentifier = DEFAULT_PACKET_GOS_GLOBAL_UNIQUE_ID;
        outgoingPortID = DEFAULT_OUTGOING_PORTID;
        crossedNodes = new LinkedList<>();
    }

    /**
     * This method obtains the arrival order to the entry in order to be shorted
     * in a collection.
     *
     * @return Arrival order to the entry.
     * @since 2.0
     */
    public int getArrivalOrder() {
        return arrivalOrder;
    }

    /**
     * This method establishes the flow ID of the flow the entry belongs to.
     *
     * @param globalFlowID The flow ID of the flow the entry belongs to.
     * @since 2.0
     */
    public void setFlowID(int globalFlowID) {
        this.globalFlowID = globalFlowID;
    }

    /**
     * This method obtains the flow ID of the flow the entry belongs to.
     *
     * @return The flow ID of the flow the entry belongs to.
     * @since 2.0
     */
    public int getFlowID() {
        if (globalFlowID == DEFAULT_FLOWID) {
            logger.error(translations.getString("attributeNotInitialized"));
            throw new RuntimeException(translations.getString("attributeNotInitialized"));
        }
        return globalFlowID;
    }

    /**
     * This method establishes the identifier of the packet this entry refers
     * to.
     *
     * @param packetGoSGlobalUniqueIdentifier The packet identifier.
     * @since 2.0
     */
    public void setPacketGoSGlobalUniqueIdentifier(int packetGoSGlobalUniqueIdentifier) {
        this.packetGoSGlobalUniqueIdentifier = packetGoSGlobalUniqueIdentifier;
    }

    /**
     * This method obtains the identifier of the packet this entry refers to.
     *
     * @return The packet identifier.
     * @since 2.0
     */
    public int getPacketGoSGlobalUniqueIdentifier() {
        if (packetGoSGlobalUniqueIdentifier == DEFAULT_PACKET_GOS_GLOBAL_UNIQUE_ID) {
            logger.error(translations.getString("attributeNotInitialized"));
            throw new RuntimeException(translations.getString("attributeNotInitialized"));
        }
        return packetGoSGlobalUniqueIdentifier;
    }

    /**
     * This method establishes the outgoing port ID of the port by where the
     * retransmission request is sent.
     *
     * @param outgoingPortID Outgoing port ID.
     * @since 2.0
     */
    public void setOutgoingPortID(int outgoingPortID) {
        if (outgoingPortID < ZERO) {
            logger.error(translations.getString("argumentOutOfRange"));
            throw new IllegalArgumentException(translations.getString("argumentOutOfRange"));
        }
        this.outgoingPortID = outgoingPortID;
    }

    /**
     * This method obtains the outgoing port ID of the port by where the
     * retransmission request is sent.
     *
     * @return Outgoing port ID.
     * @since 2.0
     */
    public int getOutgoingPortID() {
        if (outgoingPortID == DEFAULT_OUTGOING_PORTID) {
            logger.error(translations.getString("attributeNotInitialized"));
            throw new RuntimeException(translations.getString("attributeNotInitialized"));
        }
        return outgoingPortID;
    }

    /**
     * This method sets the IP address of an active node that will be requested
     * for a packet retransmission.
     *
     * @param crossedNodeIP IP address of a node to be requested for a packet
     * retransmission.
     * @since 2.0
     */
    public void setCrossedNodeIP(String crossedNodeIP) {
        if ((crossedNodeIP == null) || (crossedNodeIP.isEmpty())) {
            logger.error(translations.getString("badArgument"));
            throw new IllegalArgumentException(translations.getString("badArgument"));
        } else {
            TIPv4AddressGenerator ipv4AddresGenerator = new TIPv4AddressGenerator();
            try {
                ipv4AddresGenerator.setIPv4AddressIfGreater(crossedNodeIP);
            } catch (IllegalArgumentException ex) {
                logger.error(translations.getString("argumentOutOfRange"));
                throw new IllegalArgumentException(translations.getString("argumentOutOfRange"));
            }
        }
        crossedNodes.addFirst(crossedNodeIP);
    }

    /**
     * This method obtains the IP address of the next active node that will be
     * requested for a packet retransmission.
     *
     * @return IP address of the next active node to be requested for a packet
     * retransmission. If there is not a node to be requested, this method
     * return NULL.
     * @since 2.0
     */
    public String getNextNearestCrossedActiveNodeIPv4() {
        if (crossedNodes.isEmpty()) {
            logger.error(translations.getString("attributeNotInitialized"));
            throw new RuntimeException(translations.getString("attributeNotInitialized"));
        }
        return crossedNodes.removeFirst();
    }

    /**
     * This method decreases the retransmission TimeOut.
     *
     * @param nanosecondsToDecrease Number of nanoseconds to decrease from the
     * timeout.
     * @since 2.0
     */
    public void decreaseTimeout(int nanosecondsToDecrease) {
        if (nanosecondsToDecrease < ZERO) {
            logger.error(translations.getString("argumentOutOfRange"));
            throw new IllegalArgumentException(translations.getString("argumentOutOfRange"));
        }
        timeout -= nanosecondsToDecrease;
        if (timeout < ZERO) {
            timeout = ZERO;
        }
    }

    /**
     * This method restores the retransmission TimeOut to its original value.
     *
     * @since 2.0
     */
    public void resetTimeoutAndDecreaseAttempts() {
        if (timeout == ZERO) {
            if (attempts > ZERO) {
                timeout = DEFAULT_GPSRP_TIMEOUT_NANOSECONDS;
                attempts--;
            }
        }
    }

    /**
     * This method forces the TimeOut restoration to its original value and also
     * increases the number of expired retransmission attempts.
     *
     * @since 2.0
     */
    public void forceTimeoutReset() {
        timeout = DEFAULT_GPSRP_TIMEOUT_NANOSECONDS;
        attempts--;
        if (attempts < ZERO) {
            attempts = ZERO;
            timeout = ZERO;
        }
    }

    /**
     * This method ckeck whether the retransmission request must be retried
     * again or not.
     *
     * @return TRUE, if the retransmission must be retried. Otherwise, FALSE.
     * @since 2.0
     */
    public boolean isRetriable() {
        if (attempts > ZERO) {
            if (timeout == ZERO) {
                if (!crossedNodes.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method check whether the entry must be removed from the table
     * (because retransmission is not going to be retried) or not.
     *
     * @return TRUE, if the entry must be removed. Otherwise, FALSE.
     * @since 2.0
     */
    public boolean canBePurged() {
        if (crossedNodes.isEmpty()) {
            return true;
        }
        if (attempts == ZERO) {
            if (timeout == ZERO) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method compares the current instance with another of the same type
     * passed as an argument to know the order to be inserted in a collection.
     *
     * @param anotherTGPSRPRequestEntry Instancia con la que se va a comparar la
     * actual.
     * @return -1, 0, 1, depending on wheter the curren instance is lower,
     * equal, or greater than the one passed as an argument. In terms of
     * shorting.
     * @since 2.0
     */
    @Override
    public int compareTo(TGPSRPRequestEntry anotherTGPSRPRequestEntry) {
        if (anotherTGPSRPRequestEntry == null) {
            logger.error(translations.getString("badArgument"));
            throw new IllegalArgumentException(translations.getString("badArgument"));
        }
        if (arrivalOrder < anotherTGPSRPRequestEntry.getArrivalOrder()) {
            return TGPSRPRequestEntry.THIS_LOWER;
        }
        if (arrivalOrder > anotherTGPSRPRequestEntry.getArrivalOrder()) {
            return TGPSRPRequestEntry.THIS_GREATER;
        }
        return TGPSRPRequestEntry.THIS_EQUAL;
    }

    private static final int THIS_LOWER = -1;
    private static final int THIS_EQUAL = 0;
    private static final int THIS_GREATER = 1;

    private static final int DEFAULT_FLOWID = 0;
    private static final int DEFAULT_PACKET_GOS_GLOBAL_UNIQUE_ID = 0;
    private static final int DEFAULT_OUTGOING_PORTID = -1;

    private static final int DEFAULT_GPSRP_TIMEOUT_NANOSECONDS = 50000;
    private static final int DEFAULT_GPSRP_ATTEMPTS = 8;

    private static final int ZERO = 0;

    private int timeout;
    private int globalFlowID;
    private int packetGoSGlobalUniqueIdentifier;
    private int outgoingPortID;
    private final LinkedList<String> crossedNodes;
    private final int arrivalOrder;
    private int attempts;
    private final ResourceBundle translations;
    private final Logger logger = LoggerFactory.getLogger(TGPSRPRequestEntry.class);
}
