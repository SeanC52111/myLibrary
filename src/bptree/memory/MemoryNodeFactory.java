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

import bptree.BPlusTree;
import bptree.InnerNode;
import bptree.LeafNode;
import bptree.NodeFactory;

/**
 *
 */
public class MemoryNodeFactory<K extends Comparable<K>, V> implements NodeFactory<K, V> {

	private int order;
	private int records;
	
	/**
	 * @param order the order of the B+ Tree
	 * @param records TODO
	 */
	public MemoryNodeFactory(int order, int records) {
		this.order = order;
		this.records = records;
	}


	/**
	 * @param order the order of the B+ Tree
	 * @param records TODO
	 */
	public MemoryNodeFactory() {
	}
		
	@Override
	public InnerNode<K, V> getInnerNode(BPlusTree bptree, int id) {
		return new MemoryInnerNode<K, V>(bptree, id, order - 1);
	}

	@Override
	public LeafNode<K, V> getLeafNode(BPlusTree bptree, int id) {
		return new MemoryLeafNode<K, V>(bptree, id, records, -1);
	}
	
	public int getOrder() {
		return order;
	}
	
	public int getRecords() {
		return records;
	}
	
	public int setOrder(int order) {
		return this.order = order;
	}
	
	public int setRecords(int records) {
		return this.records = records;
	}

}
