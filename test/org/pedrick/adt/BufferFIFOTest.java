/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pedrick.adt;

import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author patrick
 */
public class BufferFIFOTest
{
    
    public BufferFIFOTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of isLog method, of class BufferFIFO.
     */
    @Test
    public void testIsLog()
    {
        System.out.println("isLog");
        BufferFIFO instance = new BufferFIFO(10);
        instance.setLog(true);
        boolean expResult = true;
        boolean result = instance.isLog();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLog method, of class BufferFIFO.
     */
    @Test
    public void testSetLog()
    {
        System.out.println("setLog");
        boolean log = true;
        BufferFIFO instance = new BufferFIFO(10);
        instance.setLog(log);
        assertEquals(instance.isLog(), true);
    }

    /**
     * Test of pop method, of class BufferFIFO.
     */
    @Test
    public void testPop()
    {
        System.out.println("pop");
        BufferFIFO<String> instance = new BufferFIFO<String>(10);
        instance.push("test");
        Object expResult = "test";
        Object result = instance.pop();
        assertEquals(expResult, result);
    }

    /**
     * Test of push method, of class BufferFIFO.
     */
    @Test
    public void testPush()
    {
        System.out.println("push");
        String element = "test";
        BufferFIFO<String> instance = new BufferFIFO<String>(10);
        instance.push(element);
        assertEquals(element, instance.pop());
    }

    /**
     * Test of getList method, of class BufferFIFO.
     */
    @Test
    public void testGetList()
    {
        System.out.println("getList");
        BufferFIFO<String> instance = new BufferFIFO<String>(10);
        String element = "test";
        instance.push(element);
        String expResult = instance.getList().getFirst();        
        assertEquals(element, expResult);
    }

    /**
     * Test of getMaxSize method, of class BufferFIFO.
     */
    @Test
    public void testGetMaxSize()
    {
        System.out.println("getMaxSize");
        BufferFIFO instance = new BufferFIFO(10);
        int expResult = 10;
        int result = instance.getMaxSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSize method, of class BufferFIFO.
     */
    @Test
    public void testGetSize()
    {
        System.out.println("getSize");
        BufferFIFO<String> instance = new BufferFIFO<String>(10);
        int expResult = 1;
        instance.push("test");
        int result = instance.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class BufferFIFO.
     */
    @Test
    public void testIsEmpty()
    {
        System.out.println("isEmpty");
        BufferFIFO instance = new BufferFIFO(10);
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of isFull method, of class BufferFIFO.
     */
    @Test
    public void testIsFull()
    {
        System.out.println("isFull");
        BufferFIFO<String> instance = new BufferFIFO<String>(10);
        for (int i = 0; i < 10; i++)
        {
            instance.push("test_" + i);
            
        }
        boolean expResult = true;
        boolean result = instance.isFull();
        assertEquals(expResult, result);
    }
}
