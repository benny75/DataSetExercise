package com.benny.exercise;

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

/**
 * A 'forgetting' map should hold associations between a ‘key’ and some ‘content’. 
 * It should hold as many associations as it can, but no more than x associations at any time, 
 * with x being a parameter passed to the constructor. Associations that are least used (in a sense of 'find') 
 * are removed from the map as needed.
 * It should also be thread-safe.
 *
 */
public class ForgettingMap<K,V> extends AbstractMap<K,V>{

	/**
	 * Number of times find method being called
	 */
	private int totalFindRequests;
	
	/**
	 * Numbers of non-null contents
	 */
	private int size = 0;
	
	/**
	 * Stores all the nodes in the instance.
	 */
    private Node<K,V>[] table;
    
    /**
     * Next index to store new node
     */
    private int index = 0;
	
	/**
	 * Nil private constructor to prevent external call.
	 */
    @SuppressWarnings("unused")
	private ForgettingMap(){}
    
    /**
     * Constructor
     * @param x Maximum numbers of content to store
     */
    @SuppressWarnings("unchecked")
	public ForgettingMap(int x){
    	
    	table = (Node<K, V>[]) Array.newInstance(Node.class,x);
    }
    
    /**
     * Add a value to the list. If duplicate key is found, replace with the new value.
     * @param key
     * @param value
     * @return old value if duplicate key, null otherwise.
     */
	public synchronized V add(K key, V value){
		int hash = hash(key);
		
		// Replace value if duplicate key found
		for(int i=0; i<table.length; i++){
			if(table[i] != null){
				if(table[i].hash == hash){
					V oldValue = table[i].getValue();
					table[i].setValue(value);
					return oldValue;
				}
			}
		}
		
		Node<K,V> newNode = new Node<K,V>(hash, key, value, totalFindRequests, null);
		
		// Remove the least used content 
		if(size >= table.length){
			int removeIndex = removeLeastUsed();
			table[removeIndex] = newNode;
		} else{
			table[index++] = newNode;
			size++;
		}
		return null;
	}

	/**
	 * Remove the least used content from the list with the following factor calculation: 
	 * times of this content found / 
	 * (total numbers of contents found - numbers of contents found when the content added)
	 * If there are multiple contents with same value of factor, remove the one on top of the list.
	 * @return the index of content being removed 
	 */
	private int removeLeastUsed() {
		double freqFactor = 0;
		double minFreqFactor = 1;
		int minIndex = 0;

		// Get the index of the content to be removed
		for(int i=0; i<table.length; i++){
			freqFactor = (double)table[i].accessFrequence / (double)(totalFindRequests - table[i].addedSince);
			if (freqFactor<minFreqFactor)  {
				minFreqFactor = freqFactor;
				minIndex = i;
			}
		}
		
		table[minIndex] = null;
		return minIndex;
	}

	/**
	 * Returns the value of the given key
	 * @param key
	 * @return value. Null if no key match.
	 */
	public synchronized V find(K key){
		totalFindRequests++;
		
		for(int i=0; i<table.length; i++){
			if(table[i] != null){
				if(table[i].hash == hash(key)){
					table[i].accessFrequence++;
					return table[i].getValue();
				}
			}
		}
		
		return null;
	}
	
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		synchronized(table){
			return new HashSet<Map.Entry<K,V>>(Arrays.asList(table));
		}
	}

	@Override
    public final String toString() {
		synchronized(table){
			return Arrays.toString(table); 
			}
		}

	
	/**
	 * Basic node with quantities help calculating the least used node
	 */
	static class Node<K,V> implements Map.Entry<K,V>{
	    final int hash;
	    final K key;
	    V value;
	    int addedSince;
	    int accessFrequence;
	    Node<K,V> next;
	
	    Node(int hash, K key, V value, int addedSince, Node<K,V> next) {
	        this.hash = hash;
	        this.key = key;
	        this.value = value;
	        this.addedSince = addedSince;
	        accessFrequence = 1;
	    }
	
	    public final K getKey()        { return key; }
	    public final V getValue()      { return value; }
	    public final String toString() { return key + "=" + value; }
	
	    public final int hashCode() {
	        return Objects.hashCode(key) ^ Objects.hashCode(value);
	    }
	
	    public final V setValue(V newValue) {
	        V oldValue = value;
	        value = newValue;
	        return oldValue;
	    }
	
	    public final boolean equals(Object o) {
	        if (o == this)
	            return true;
	        if (o instanceof Map.Entry) {
	            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
	            if (Objects.equals(key, e.getKey()) &&
	                Objects.equals(value, e.getValue()))
	                return true;
	        }
	        return false;
	    }
	    
	    
	}

	/**
	 * Computes key.hashCode() and spreads (XORs) higher bits of hash
	 * to lower.  Because the table uses power-of-two masking, sets of
	 * hashes that vary only in bits above the current mask will
	 * always collide. (Among known examples are sets of Float keys
	 * holding consecutive whole numbers in small tables.)  So we
	 * apply a transform that spreads the impact of higher bits
	 * downward. There is a tradeoff between speed, utility, and
	 * quality of bit-spreading. Because many common sets of hashes
	 * are already reasonably distributed (so don't benefit from
	 * spreading), and because we use trees to handle large sets of
	 * collisions in bins, we just XOR some shifted bits in the
	 * cheapest possible way to reduce systematic lossage, as well as
	 * to incorporate impact of the highest bits that would otherwise
	 * never be used in index calculations because of table bounds.
	 */
	static final int hash(Object key) {
	    int h;
	    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}
}


