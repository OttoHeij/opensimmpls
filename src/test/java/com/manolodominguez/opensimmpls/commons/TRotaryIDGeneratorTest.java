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
package com.manolodominguez.opensimmpls.commons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
 */
public class TRotaryIDGeneratorTest {

    public TRotaryIDGeneratorTest() {
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
     * Test of constructor, of class TLongIDGenerator.
     */
    @Test
    public void testConstructor() {
        System.out.println("Testing TRotaryIDGenerator constructor");
        TRotaryIDGenerator instance = new TRotaryIDGenerator();
        assertEquals(1, instance.getNextIdentifier()); // should return 1.
    }

    /**
     * Test of reset method, of class TRotaryIDGenerator.
     */
    @Test
    public void testReset() {
        System.out.println("Testing reset()");
        TRotaryIDGenerator instance = new TRotaryIDGenerator();
        instance.getNextIdentifier(); // should return 1. Next 2
        instance.getNextIdentifier(); // should return 2. Next 3
        instance.getNextIdentifier(); // should return 3. Next 4
        instance.getNextIdentifier(); // should return 4. Next 5
        instance.reset(); //reset to 0
        assertEquals(1, instance.getNextIdentifier()); // should return 1.
    }

    /**
     * Test of getNextIdentifier method, of class TRotaryIDGenerator.
     */
    @Test
    public void testGetNextIdentifier() {
        System.out.println("testing getNextIdentifier()");
        TRotaryIDGenerator instance = new TRotaryIDGenerator();
        instance.getNextIdentifier(); // should return 1. Next 2
        instance.getNextIdentifier(); // should return 2. Next 3
        instance.getNextIdentifier(); // should return 3. Next 4
        instance.getNextIdentifier(); // should return 4. Next 5
        assertEquals(5, instance.getNextIdentifier());
    }

    /**
     * Test of getNextIdentifier method, of class TRotaryIDGenerator.
     */
    @Test
    public void testGetNextIdentifierRestart() {
        System.out.println("testing getNextIdentifier() restart");
        TRotaryIDGenerator instance = new TRotaryIDGenerator();
        instance.setIdentifier(Integer.MAX_VALUE); // Next value will make the ID generator to rotate
        assertEquals(0, instance.getNextIdentifier());
    }

    /**
     * Test of setIdentifier method, of class TRotaryIDGenerator.
     */
    @Test
    public void testSetIdentifierWhenLower() {
        System.out.println("testing setIdentifier() when argument is lower than current value");
        TRotaryIDGenerator instance = new TRotaryIDGenerator();
        instance.getNextIdentifier(); // should return 1. Next 2
        instance.getNextIdentifier(); // should return 2. Next 3
        instance.getNextIdentifier(); // should return 3. Next 4
        instance.getNextIdentifier(); // should return 4. Next 5
        instance.setIdentifier(2); // Sets 2. Next should be 3
        assertEquals(3, instance.getNextIdentifier());
    }

    /**
     * Test of setIdentifier method, of class TRotaryIDGenerator.
     */
    @Test
    public void testSetIdentifierWhenGreater() {
        System.out.println("testing setIdentifier() when argument is greater than current value");
        TRotaryIDGenerator instance = new TRotaryIDGenerator();
        instance.getNextIdentifier(); // should return 1. Next 2
        instance.getNextIdentifier(); // should return 2. Next 3
        instance.getNextIdentifier(); // should return 3. Next 4
        instance.getNextIdentifier(); // should return 4. Next 5
        instance.setIdentifier(10); // Sets 10. Next should be 11
        assertEquals(11, instance.getNextIdentifier());
    }

    /**
     * Test of setIdentifier method, of class TRotaryIDGenerator.
     */
    @Test
    public void testSetIdentifierWhenNegative() {
        System.out.println("testing setIdentifier() when argument is negative");
        assertThrows(IllegalArgumentException.class, () -> {
            TRotaryIDGenerator instance = new TRotaryIDGenerator();
            instance.setIdentifier(-2); // This is lower than DEFAULT_ID and should throws an exception
        });
    }
}
