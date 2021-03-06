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
package bptree.memory; 


import io.RW;

import java.lang.reflect.Array;

import bptree.AbstractNode;
import bptree.BPlusTree;
import bptree.LeafNode;
import bptree.Node;


public class MemoryLeafNode<K extends Comparable<K>, V extends RW> extends AbstractNode<K, V> implements LeafNode<K, V> {
		
	
	/**
	 * @param keys
	 * @param maxSlots
	 * @param next
	 */
	@SuppressWarnings("unchecked")
	public MemoryLeafNode(BPlusTree bptree, int id, int maxSlots, int n_identifier) {
		super(bptree, id, maxSlots);

		this.n_identifier = n_identifier;
		values = (V[]) Array.newInstance(bptree.classLeafData, maxSlots);
	}
	

	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.LeafNode#getNext()
	 */
	public int getNextId() {
		return n_identifier;
	}

	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.LeafNode#setNext(cherri.bheaven.bplustree.Node)
	 */
	public void setNextId(int n_identifier) {
		this.n_identifier = n_identifier;
	}
	
	
	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.LeafNode#insert(K, V)
	 */
	public void insert(K key, V value) {
		
		int index = getSlots() - 1;
		
		while (index >= 0 && key.compareTo(getKey(index)) < 0) {
			setKey(getKey(index), index + 1);
			setValue(getValue(index), index + 1);

			index--;
		}
		
		setKey(key, index + 1);
		setValue(value, index + 1);
		
		setSlots(getSlots() + 1);
		bptree.writeNode(this);
	}
	
	private LeafNode<K, V> split() {
		checkIsFull();
		
		return new MemoryLeafNode<K, V>(bptree, -1, getMaxSlots(), -1);
	}
	
	/*
	 * A very complex method needs documentation. It is used in insertion.
	 */
	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.LeafNode#split(K, V)
	 */
	public LeafNode<K, V> split(K key, V value) {
		LeafNode<K, V> newLeafNode = split();
		int count = (getSlots() + 1) / 2;
		int right = count - 1;
		int left = getSlots() - 1;
		boolean found = false;
		for (int i = 0; i < count; i++, right--) {
			if(found || key.compareTo(getKey(left)) < 0) {
				newLeafNode.setKey(getKey(left), right);
				newLeafNode.setValue(getValue(left), right);
				left--;
			} else {
				newLeafNode.setKey(key, right);
				newLeafNode.setValue(value, right);
				found = true;
			}
		}
		setSlots(getSlots() - count + (found ? 1 : 0));
		newLeafNode.setSlots(count);
		if (!found) {
			insert(key, value);
		}
		bptree.writeNode(newLeafNode);
		setNextId(newLeafNode.getIdentifier());
		bptree.writeNode(this);
		return newLeafNode;
	}

	
	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.LeafNode#remove(int)
	 */
	public void remove(int index) {
		
		for (int i = index; i < getSlots() - 1; i++) {
			setKey(getKey(i + 1), i);
			setValue(getValue(i + 1), i);
		}
		
		setSlots(getSlots() - 1);
		bptree.writeNode(this);
	}
	
	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#hasEnoughSlots()
	 */
	@Override
	public boolean hasEnoughSlots() {
		return getSlots() >= (getMaxSlots() + 1) / 2;
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#canGive()
	 */
	@Override
	public boolean canGiveSlots() {
		return getSlots() - 1 >= (getMaxSlots() + 1) / 2;
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#leftShift(int)
	 */
	@Override
	public void leftShift(int count) {
		for (int i = 0; i < getSlots() - count; i++) {
			setKey(getKey(i + count), i);
			setValue(getValue(i + count), i);
		}
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#rightShift(int)
	 */
	@Override
	public void rightShift(int count) {
		for (int i = getSlots() - 1; i >= 0 ; i--) {
			setKey(getKey(i), i + count);
			setValue(getValue(i), i + count);
		}
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#copyToLeft(int)
	 */
	@Override
	public void copyToLeft(Node<K, V> node, int count) {
		for (int i = 0; i < count; i++) {
			node.setKey(getKey(i), node.getSlots() + i);
			((LeafNode<K, V>) node).setValue(getValue(i), node.getSlots() + i);
		}
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#copyToRight(int)
	 */
	@Override
	public void copyToRight(Node<K, V> node, int count) {
		for (int i = 0; i < count; i++) {
			node.setKey(getKey(getSlots() - count + i), i);
			((LeafNode<K, V>) node).setValue(getValue(getSlots() - count + i), i);
		}
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#toString(int)
	 */
	@Override
	public String toString(int level) {
		StringBuffer buffer = new StringBuffer(super.toString(level));
		StringBuffer indent = getIndent(level);
		buffer.append('\n');
		
		if (getSlots() > 0) {
			buffer.append(indent);
			buffer.append(" values: \n");
		}
		
		for (int i = 0; i < getSlots(); i++) {
			if(i > 0) {
				buffer.append('\n');
			}
			buffer.append("  ");
			buffer.append(indent);
			buffer.append(((V)values[i]).toString());
		}
		
		buffer.append('\n');
		buffer.append(indent);
		buffer.append(" next: id = " + n_identifier + ", key = " );
		buffer.append(n_identifier == -1 ? "null" : bptree.readNode(n_identifier).getKey(0));
		
		return buffer.toString();
	}

//	@Override
//	public int getChildId(int index) {
//		// TODO Auto-generated method stub
////		return m_pIdentifier[index];
//		return -1;
//	}

}
