package com.benny.exercise;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class ForgettingMapTest 
{

    /**
     * Base case
     */
    public void testConstructor()
    {
    	ForgettingMap<String, String> f = new ForgettingMap<String, String>(1);
        assertNotNull(f);
    }
    
    /**
     * Try simple add and find
     */
    @Test
    public void testAdd1(){
    	ForgettingMap<String, String> f = new ForgettingMap<String, String>(1);
    	String key = "keyy";
    	String value = "valuee";
    	
    	f.add(key, value);
    	assertEquals(f.find(key), value);
    }
    
    /**
     * Try to add more
     */
    @Test
    public void testAdd2(){
    	ForgettingMap<Integer, String> f = new ForgettingMap<Integer, String>(10);
    	int key1 = 1;    	String value1 = "valuee1";
    	int key2 = 3;    	String value2 = "valuee2";
    	int key3 = 5;    	String value3 = "valuee2";
    	
    	f.add(key1, value1);
    	f.add(key2, value2);
    	f.add(key3, value3);
    	assertEquals(f.find(key2), value2);
    	assertEquals(f.find(key1), value1);
    	assertEquals(f.find(key3), value3);
    }
    
    /**
     * Test to fill up the map
     */
    @Test
    public void testAdd3(){
    	ForgettingMap<Integer, Character> f = new ForgettingMap<Integer, Character>(3);
    	int key1 = 1;    	char value1 = 'a';
    	int key2 = 3;    	char value2 = 'b';
    	int key3 = 5;    	char value3 = 'c';
    	
    	f.add(key1, value1);
    	f.add(key2, value2);
    	f.add(key3, value3);
    	assertEquals((char)f.find(key2), value2);
    	assertEquals((char)f.find(key1), value1);
    	assertEquals((char)f.find(key3), value3);
    }
    
    /**
     * Test to add with duplicate key
     */
    @Test
    public void testAdd4(){
    	ForgettingMap<Double, Character> f = new ForgettingMap<Double, Character>(3);
    	double key1 = 1.1;    	char value1 = 'a';
    	double key2 = 2.2;    	char value2 = 'b';
    	double key3 = 2.2;    	char value3 = 'c';
    	
    	f.add(key1, value1);
    	f.add(key2, value2);
    	f.add(key3, value3);
    	assertEquals((char)f.find(key2), value3);
    	assertEquals((char)f.find(key1), value1);
    	assertEquals((char)f.find(key3), value3);
    }

    /**
     * Test to add new content exceeding the size of the list
     */
    @Test
    public void testAdd5(){
    	ForgettingMap<Integer, String> f = new ForgettingMap<Integer, String>(7);
    	int key1 = 1;    	String value1 = "valuee1";
    	int key2 = 2;    	String value2 = "valuee2";
    	int key3 = 3;    	String value3 = "valuee3";
    	int key4 = 4;    	String value4 = "valuee4";
    	int key5 = 5;    	String value5 = "valuee5";
    	int key6 = 6;    	String value6 = "valuee6";
    	int key7 = 7;    	String value7 = "valuee7";
    	int key8 = 8;    	String value8 = "valuee8";
    	
    	f.add(key1, value1);
    	f.add(key2, value2);
    	f.add(key3, value3);
    	f.add(key4, value4);
    	f.add(key5, value5);
    	f.add(key6, value6);
    	f.add(key7, value7);

    	// Leave key3 unused
    	for(int i=0; i<4; i++){
        	f.find(key1);
        	f.find(key2);
        	f.find(key4);
        	f.find(key5);
        	f.find(key6);
        	f.find(key7);
    	}

    	assertNotNull(f.find(key3));
    	f.add(key8, value8);
    	assertNull(f.find(key3));
    }
    
    /**
     * Test to add new content exceeding the size of the list without any find operation executed
     */
    @Test
    public void testAdd6(){
    	ForgettingMap<Integer, String> f = new ForgettingMap<Integer, String>(7);
    	int key1 = 1;    	String value1 = "valuee1";
    	int key2 = 2;    	String value2 = "valuee2";
    	int key3 = 3;    	String value3 = "valuee3";
    	int key4 = 4;    	String value4 = "valuee4";
    	int key5 = 5;    	String value5 = "valuee5";
    	int key6 = 6;    	String value6 = "valuee6";
    	int key7 = 7;    	String value7 = "valuee7";
    	int key8 = 8;    	String value8 = "valuee8";
    	
    	f.add(key1, value1);
    	f.add(key2, value2);
    	f.add(key3, value3);
    	f.add(key4, value4);
    	f.add(key5, value5);
    	f.add(key6, value6);
    	f.add(key7, value7);
    	f.add(key8, value8);
    	assertNull(f.find(key1));
    }
    
    /**
     * Load test
     */
    @Test
    public void testLoad(){
    	int noOfContents = 1000;
    	// Has to be large enough
    	int noOfRandomLoad = 1000000;
    	int noOfLaterAdd = 10;
    	ForgettingMap<Integer, Integer> f = new ForgettingMap<Integer, Integer>(noOfContents);
    	
    	// Fill up the list
    	for(int i=0; i<noOfContents; i++){
    		f.add(i, i*1000);
    	}

    	// Randomly find contents and store the number of executions
    	Random rnd = new Random();
    	int[] freq = new int[noOfContents];
    	for(int i=0; i<noOfRandomLoad; i++){
    		int key = rnd.nextInt(noOfContents);
    		freq[key]++;
    		f.find(key);
    	}

    	// Add contents and check is the expected content removed
    	for(int i=1; i<=noOfLaterAdd; i++){
	    	int smallest = findSmallest(freq);
	    	f.add(noOfContents+i, (noOfContents+i)*1000);
	    	// Omit if already used in previous tests
	    	freq[smallest]=Integer.MAX_VALUE;
	    	assertNull(f.find(smallest));
    	}
    	
    }

	private int findSmallest(int[] freq) {
    	int smallest = 0;
        for(int i=1; i< freq.length; i++)
        {
            if (freq[i] < freq[smallest])
                    smallest = i;
        }
        return smallest;
	}
	
	
	/**
	 * Test for multi-thread
	 */
    @Test
	public void testMultiThread(){
    	int noOfContents = 10000;
    	ForgettingMap<Integer, Integer> f = new ForgettingMap<Integer, Integer>(noOfContents);

    	ArrayList<Thread> threadList = new ArrayList<Thread>();
    	for(int i=0; i<noOfContents; i++){
    		threadList.add(new TestingAddThread(i, f));
    	}
    	for(Thread t : threadList){
    		t.start();
    	}
    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {e.printStackTrace();}
    	
    	for(int i=0; i<noOfContents; i++){
    		assertEquals((int)f.find(i), i);
    	}
		
	}
    
    class TestingAddThread extends Thread{
    	private int value;
    	private ForgettingMap<Integer, Integer> f;
    	Random rnd = new Random();
    	public TestingAddThread(int value, ForgettingMap<Integer, Integer> f){
    		this.value = value;
    		this.f = f;
    	}
    	public void run(){
    		try {
				sleep(new Long(rnd.nextInt(100)));
			} catch (InterruptedException e) {e.printStackTrace();}
    		f.add(value, value);
    	}
    }
}
