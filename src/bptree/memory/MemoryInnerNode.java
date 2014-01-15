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

import sun.security.action.GetLongAction;
import bptree.AbstractNode;
import bptree.BPlusTree;
import bptree.InnerNode;
import bptree.Node;

/**
 *
 */
public class MemoryInnerNode<K extends Comparable<K>, V extends RW> extends AbstractNode<K, V> implements InnerNode<K, V> {
//	private Node<K, V> children[];
	
	
	/**
	 * @param maxSlots
	 */
	@SuppressWarnings("unchecked")
	public MemoryInnerNode(BPlusTree bptree, int id, int maxSlots) {
		super(bptree, id, maxSlots);
		
		m_pIdentifier = new int[maxSlots + 1];
		values = (V[]) Array.newInstance(bptree.classInnerData, maxSlots + 1);
	}

	
	public int getChildId(int index) {
		return m_pIdentifier[index];
	}

	public void setChildId(int child, int index) {
		m_pIdentifier[index] = child;
	}
	
	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.InnerNode#insert(K, cherri.bheaven.bplustree.AbstractNode)
	 */
	public void insert(K key, int child) {
		
		int index = getSlots() - 1;
		
		while (index >= 0 && key.compareTo(getKey(index)) < 0) {
			setKey(getKey(index), index + 1);
			setChildId(getChildId(index + 1), index + 2);

			index--;
		}
		
		setKey(key, index + 1);
		setChildId(child, index + 2);
		
		setSlots(getSlots() + 1);
		bptree.writeNode(this);
	}
	
	private InnerNode<K, V> split() {
		checkIsFull();
		
		return new MemoryInnerNode<K, V>(bptree, -1, getMaxSlots());
	}
	
	/*
	 * A very complex method needs documentation. It is used in insertion.
	 */
	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.InnerNode#split(K, cherri.bheaven.bplustree.AbstractNode)
	 */
	public InnerNode<K, V> split(K key, int newNode) {
		InnerNode<K, V> newInnerNode = split();
		int count = getSlots() / 2;
		int right = count - 1;
		int left = getSlots() - 1;
		boolean found = false;
		for (int i = 0; i < count; i++, right--) {
			if(found || key.compareTo(getKey(left)) < 0) {
				newInnerNode.setKey(getKey(left), right);
				newInnerNode.setChildId(getChildId(left + 1), right + 1);
				left--;
			} else {
				newInnerNode.setKey(key, right);
				newInnerNode.setChildId(newNode, right + 1);
				found = true;
			}
		}
		setSlots(getSlots() - count + (found ? 1 : 0));
		newInnerNode.setSlots(count);
		if (!found) {
			insert(key, newNode);
		}
		setSlots(getSlots() - 1);
		newInnerNode.setChildId(getChildId(getSlots() + 1), 0);
		bptree.writeNode(newInnerNode);
		bptree.writeNode(this);
		return newInnerNode;
	}

	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.InnerNode#remove(int)
	 */
	public void remove(int index) {
		bptree.deleteNode(getChildId(index));
		
		for (int i = index; i < getSlots(); i++) {
			if (i < getSlots() - 1) {
				setKey(getKey(i + 1), i);
			}
			setChildId(getChildId(i + 1), i);
		}
		
		setSlots(getSlots() - 1);
		bptree.writeNode(this);
	}
	
	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#hasEnoughSlots()
	 */
	@Override
	public boolean hasEnoughSlots() {
		return getSlots() >= (getMaxSlots() - 1) / 2;
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#canGive()
	 */
	@Override
	public boolean canGiveSlots() {
		return getSlots() - 1 >= (getMaxSlots() - 1) / 2;
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#leftShift(int)
	 */
	@Override
	public void leftShift(int count) {
		for (int i = 0; i < getSlots() - count; i++) {
			setKey(getKey(i + count), i);
			setChildId(getChildId(i + count), i);
		}
		
		setChildId(getChildId(getSlots()), getSlots() - count);
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#rightShift(int)
	 */
	@Override
	public void rightShift(int count) {
		for (int i = getSlots() - 1; i >= 0 ; i--) {
			setKey(getKey(i), i + count);
			setChildId(getChildId(i + 1), i + count + 1);
		}
		
		setChildId(getChildId(0), count);
		
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#copyToLeft(int)
	 */
	@Override
	public void copyToLeft(Node<K, V> node, int count) {
		for (int i = 0; i < count; i++) {
			if(i < getSlots()) {
				node.setKey(getKey(i), node.getSlots() + i + 1);
			}
			((InnerNode<K, V>) node).setChildId(getChildId(i), node.getSlots() + i + 1);
		}
	}

	/* (non-Javadoc)
	 * @see com.cherri.bplustree.Node#copyToRight(int)
	 */
	@Override
	public void copyToRight(Node<K, V> node, int count) {
		for (int i = 0; i < count - 1; i++) {
			node.setKey(getKey(getSlots() - count + i + 1), i);
			((InnerNode<K, V>) node).setChildId(getChildId(getSlots() - count + i + 2), i + 1);
		}
		((InnerNode<K, V>) node).setChildId(getChildId(getSlots() - count + 1), 0);

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
			buffer.append(" children: \n");
		}
		
		for (int i = 0; i < getSlots() + 1; i++) {
			if(i > 0) {
				buffer.append('\n');
			}
			buffer.append(indent);
			if (values[i] == null) buffer.append("null");
			else buffer.append(values[i].toString());
			buffer.append('\n');
			buffer.append((bptree.readNode(getChildId(i))).toString(level + 1));
		}

		return buffer.toString();
	}

}
