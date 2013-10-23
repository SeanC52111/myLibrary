/*
 * Copyright 2010 Moustapha Cherri
 * 
 * This file is part of bheaven.
 * 
 * bheaven is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * bheaven is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with bheaven.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package bptree;

import java.io.IOException;

public interface Node<K, V> {

	/**
	 * @return the key
	 */
	K getKey(int index);

	/**
	 * @param key the key to set
	 */
	void setKey(K key, int index);

	/**
	 * @return the slots
	 */
	int getSlots();

	/**
	 * @param slots the slots to set
	 */
	void setSlots(int slots);

	int getMaxSlots();
	
	boolean isEmpty();
	
	boolean isFull();
	
	boolean hasEnoughSlots();
		
	int getKeyIndex(K key);
	
	boolean canGiveSlots();

	void leftShift(int count);
	
	void rightShift(int count);
	
	void copyToLeft(Node<K, V> node, int count);
	
	void copyToRight(Node<K, V> node, int count);
	
	boolean isLeafNode ();
	
	boolean isInnerNode ();
	
	int getChildId(int index);
	/**
	 * @author chenqian
	 * */
	
	void load(byte[] data) throws IOException ;
	
	byte[] store () throws IOException ;
	
	int getIdentifier ();
	
	void setIdentifier(int m_identifier);

	String toString(int i);
	

}