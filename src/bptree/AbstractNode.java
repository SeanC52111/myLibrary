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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import spatialindex.SpatialIndex;
import IO.Data;
import IO.DataIO;
import IO.RW;


/**
 *
 */
public abstract class AbstractNode<K, V extends RW> implements Node<K, V> /*implements Comparable<Node<K, V>>*/ {

	protected BPlusTree		bptree 			= null;
	protected int 			m_identifier 	= -1;
	protected int 			slots			= 0;
	protected int 			m_pIdentifier[]	= null;
	protected K 			keys[]			= null;
	protected V 			values[]		= null;
	protected int		 	n_identifier	= -1; // next pointer to neighbor node
	
	/*private LeafNode<K, V> previous;*/
	
	/**
	 * @param maxSlots
	 */
	@SuppressWarnings("unchecked")
	public AbstractNode(BPlusTree bptree, int id, int maxSlots) {
		this.bptree = bptree;
		m_identifier = id;
		slots = 0;
		keys = (K[])new Comparable[maxSlots];
		values = (V[]) new RW[maxSlots]; 
	}

	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.Node#getKey(int)
	 */
	public K getKey(int index) {
		return keys[index];
	}

	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.Node#setKey(K, int)
	 */
	public void setKey(K key, int index) {
		keys[index] = key;
	}
	
	public int getKeyIndex(K key) {
		return Arrays.binarySearch(keys, 0, slots, key, null);
	}

	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.Node#getSlots()
	 */
	public int getSlots() {
		return slots;
	}

	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.Node#setSlots(int)
	 */
	public void setSlots(int slots) {
		this.slots = slots;
	}
	
	/* (non-Javadoc)
	 * @see cherri.bheaven.bplustree.Node#getMaxSlots()
	 */
	public int getMaxSlots() {
		return keys.length;
	}

	public boolean isEmpty() {
		return getSlots() == 0;
	}
	
	public boolean isFull() {
		return getSlots() == keys.length;
	}
	
	/**
	 * Split the current node if it is full and return the new node. 
	 */
	//public abstract Node<K, V> split();

	protected void checkIsFull() {
		if (!isFull()) {
			throw new IllegalStateException("Cannot split a non full node.");
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(0);
	}

	public String toString(int level) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer indent = getIndent(level);
		
		buffer.append(indent);
		buffer.append(getClass().getName());
		buffer.append(" id = ");
//		buffer.append(hashCode());
		buffer.append(getIdentifier());
		if (slots > 0) {
			buffer.append('\n');
			buffer.append(indent);
			buffer.append(" keys: \n");
		}

		for (int i = 0; i < slots; i++) {
			if(i > 0) {
				buffer.append('\n');
			}
			buffer.append("  ");
			buffer.append(indent);
			buffer.append(keys[i].toString());
		}
		
		return buffer.toString();
	}

	protected StringBuffer getIndent(int level) {
		StringBuffer indent = new StringBuffer();
		for (int i = 0; i < level; i++) {
			indent.append("  ");
		}
		return indent;
	}
	
	
	/**
	 * @author chenqian
	 * @param byte[]
	 * 			data to be loaded
	 * @throws IOException 
	 * 
	 * */
	public void load(byte[] data) throws IOException {
		DataInputStream ds = 
				new DataInputStream(new ByteArrayInputStream(data));
		ds.readInt(); // read Type, just skip 
//		m_identifier 	= ds.readInt();
		slots 			= ds.readInt();
		if (this instanceof InnerNode) {
			m_pIdentifier 	= DataIO.readIntArrays(ds);
		}
		for (int i = 0; i < slots; i ++) {
			keys[i] = (K) new Long(ds.readLong());
		}
		for (int i = 0; i < slots; i ++) {
			boolean isNull = ds.readBoolean();
			if (isNull == false) {
				try {
					values[i] = (V) bptree.classV.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((V)values[i]).read(DataIO.readBytes(ds));
			}
		}
		if (this instanceof LeafNode) {
			n_identifier 	= ds.readInt();
		}
		ds.close();
	}
	
	/**
	 * @author chenqian
	 * @return byte[]
	 * 			return the bytes to store
	 * @throws IOException 
	 * */
	public byte[] store() throws IOException {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);
		if (this instanceof InnerNode) {
			ds.writeInt(SpatialIndex.PersistentIndex);
		} else {
			ds.writeInt(SpatialIndex.PersistentLeaf);
		}
//		ds.writeInt(m_identifier);
		ds.writeInt(slots);
		if (this instanceof InnerNode) {
			DataIO.writeIntArrays(ds, m_pIdentifier);
		}
		for (int i = 0; i < slots; i ++) {
			ds.writeLong((Long) keys[i]);
		}
		for (int i = 0; i < slots; i ++) {
//				DataIO.writeBytes(ds, (byte[])values[i]);
			if (values[i] == null) {
				ds.writeBoolean(true);
			} else {
				ds.writeBoolean(false);
				DataIO.writeBytes(ds, values[i].write());
			}
//				DataIO.writeString(ds, values[i].toString());
		}
		if (this instanceof LeafNode) {
			ds.writeInt(n_identifier);
		}
		ds.flush();
		return bs.toByteArray();
	}
	
	
	/**
	 * @author chenqian
	 * @return boolean
	 * */
	public boolean isLeafNode() {
		return this instanceof LeafNode;
	}
	
	/**
	 * @author chenqian 
	 * @return boolean
	 * 
	 * */
	public boolean isInnerNode() {
		return this instanceof InnerNode;
	}
	
	/**
	 * @return Identifier
	 * */
	public int getIdentifier () {
		return m_identifier;
	}
	
	/**
	 * @param int
	 * 			set Indentifer
	 * */
	public void setIdentifier(int m_identifier ) {
		this.m_identifier = m_identifier;
	}
	
}
