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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import spatialindex.SpatialIndex;
import storagemanager.DiskStorageManager;
import storagemanager.IBuffer;
import storagemanager.IStorageManager;
import storagemanager.InvalidPageException;
import storagemanager.PropertySet;
import storagemanager.RandomEvictionsBuffer;
import IO.Data;
import IO.RW;
import bptree.memory.MemoryNodeFactory;


/**
 * @param <K>
 *
 */
public /*abstract*/ class BPlusTree<K extends Comparable<K>, V extends RW> /*implements Map<K, V>*/ {
	
	int headerID = -1;
	public Class classLeafData;
	public Class classInnerData;
	IStorageManager m_pStorageManager;
	protected Node<K, V> root = null;
	private NodeFactory<K, V> factory = null;
	
	public BPlusTree() {}
	
	public BPlusTree(BPlusTree<K, V> tree) {
		this.copy(tree);
	}
	
	public void copy(BPlusTree<K, V> tree) {
		this.headerID = tree.headerID;
		this.classLeafData = tree.classLeafData;
		this.classInnerData = tree.classInnerData;
		this.m_pStorageManager = tree.m_pStorageManager;
		this.root = tree.root;
		this.factory = tree.factory;
	}
	/**
	 * @author chenqian
	 * for testing
	 * To create a bptree, we need to first create a node factory, which identifies the capacities
	 * of inner node and leaf node. 
	 * @param args
	 * 
	 * */
	public static void main(String args[]) {
		BPlusTree<Long, Data> tree = null;
		try {
			tree = BPlusTree.createBPTree(new Object[] {"./database/btree", new Integer(5), new Integer(6), Data.class, Data.class});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tree.put(new Long(1), new Data("mon"));
		System.out.println(tree.get(new Long(2)));
		tree.put(new Long(2), new Data("tue"));
		System.out.println(tree.get(new Long(2)));
		tree.put(new Long(3), new Data("wed"));
		tree.put(new Long(4), new Data("thu"));
		tree.put(new Long(5), new Data("fri"));
		tree.put(new Long(6), new Data("sat"));
		tree.put(new Long(7), new Data("sun"));
		tree.remove(new Long(2));
		System.out.println(tree.toString());
		tree.remove(new Long(4));
		System.out.println(tree.toString());
		tree.put(new Long(2), new Data("tue"));
		tree.put(new Long(4), new Data("thu"));
		System.out.println(tree.toString());
		tree.flush();
		
		tree = BPlusTree.loadBPTree(new Object[] {"./database/btree", Data.class, Data.class});
		System.out.println("load from file");
		System.out.println(tree.toString());
	}
	
	

//	/**
//	 * @param order the order of the B+ Tree
//	 * @param records TODO
//	 */
//	public BPlusTree(NodeFactory<K, V> factory) {
//		this.factory = factory;
//	}
	
	
	/**
	 * To load a b+tree from file.
	 * The header is loaded from page 0.
	 * 
	 * @param treeFile
	 * 		the file to be loaded
	 * @return
	 * 		the BPlusTree
	 */
	public static BPlusTree loadBPTree (Object[] args) {
		try{
			PropertySet ps = new PropertySet();
			
			ps.setProperty("FileName", args[0]);
			// .idx and .dat extensions will be added.
			
			IStorageManager diskfile = new DiskStorageManager(ps);
			
			IBuffer file = new RandomEvictionsBuffer(diskfile, 10, false);
			// applies a main memory random buffer on top of the persistent storage manager
			// (LRU buffer, etc can be created the same way).
			
			PropertySet ps2 = new PropertySet();
			
			// If we need to open an existing tree stored in the storage manager, we only
			// have to specify the index identifier as follows
			Integer i = new Integer(0); // INDEX_IDENTIFIER_GOES_HERE (suppose I know that in this case it is equal to 1);
			ps2.setProperty("IndexIdentifier", i);
			// this will try to locate and open an already existing r-tree index from file manager file.
			ps2.setProperty("LeafDataClass", args[1]);
			if(args.length >= 3) ps2.setProperty("InnerDataClass", args[2]);
			
			return new BPlusTree(ps2, file);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param args
	 * @return
	 * @throws SecurityException
	 * @throws NullPointerException
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static BPlusTree createBPTree(Object args[]) throws SecurityException, NullPointerException, FileNotFoundException, IllegalArgumentException, IOException {
		// Create a disk based storage manager.
		PropertySet ps = new PropertySet();

		Boolean b = new Boolean(true);
		ps.setProperty("Overwrite", b);
		//overwrite the file if it exists.

		ps.setProperty("FileName", args[0]);
		// .idx and .dat extensions will be added.

		Integer i = new Integer(4096);
		ps.setProperty("PageSize", i);
		// specify the page size. Since the index may also contain user defined data
		// there is no way to know how big a single node may become. The storage manager
		// will use multiple pages per node if needed. Off course this will slow down performance.

		IStorageManager diskfile = new DiskStorageManager(ps);

		IBuffer file = new RandomEvictionsBuffer(diskfile, 10, false);
		
		PropertySet ps2 = new PropertySet();


		i = (Integer)args[1];
		ps2.setProperty("Order", i);
		i = (Integer)args[2];
		ps2.setProperty("Records", i);
		// Index capacity and leaf capacity may be different.
		ps2.setProperty("LeafDataClass", args[3]);
		if(args.length >= 5) ps2.setProperty("InnerDataClass", args[4]);
		return new BPlusTree(ps2, file);
	}
	
	public BPlusTree(PropertySet ps, IStorageManager sm) {
		m_pStorageManager = sm;
		this.factory = new MemoryNodeFactory<K, V>();
		Object var = ps.getProperty("IndexIdentifier");
		if (var != null) {
			if (! (var instanceof Integer)) throw new IllegalArgumentException("Property IndexIdentifier must an Integer");
			headerID = ((Integer) var).intValue();
			classLeafData = (Class) ps.getProperty("LeafDataClass");
			classInnerData = (Class) ps.getProperty("InnerDataClass");
			try {
				loadHeader();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IllegalStateException("loadHeader failed with IOException");
			}
		} else {
			headerID = m_pStorageManager.storeByteArray(headerID, new byte[1]);
			classLeafData = (Class) ps.getProperty("LeafDataClass");
			classInnerData = (Class) ps.getProperty("InnerDataClass");
			int order = (Integer) ps.getProperty("Order");
			int records = (Integer) ps.getProperty("Records");
			this.factory = new MemoryNodeFactory<K, V>(order, records);
			root = factory.getLeafNode(this, -1);
			this.writeNode(root);
			try {
				storeHeader();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IllegalStateException("storeHeader failed with IOException");
			}
		}
	}
	
	private LeafNode<K, V> findLeafNode(K key) {
		return findLeafNode(key, null);
	}
	
	private LeafNode<K, V> findLeafNode(K key,
			List<Breadcrumb<K, V>> breadcrumbList) {
		if (root == null) {
			return null;
		}
		
		Node<K, V> node = root;
		breadcrumbAdd(breadcrumbList, node, -1);
		
		while (!(node instanceof LeafNode<?, ?>)) {
		
			int index = node.getKeyIndex(key);
			
			if(index < 0) {
				index = -index - 1;
			}
			
			node = this.readNode(((InnerNode)node).getChildId(index));
			breadcrumbAdd(breadcrumbList, node, index);

		}
		
		return (LeafNode<K, V>) node;
	}

	private void breadcrumbAdd(List<Breadcrumb<K, V>> breadcrumbList,
			Node<K, V> node, int index) {
		if(breadcrumbList != null) {
			breadcrumbList.add(new Breadcrumb<K, V>(node, index));
		}
	}
	
	private Node<K, V> getParent(List<Breadcrumb<K, V>> breadcrumbList,
			int position) {
		if (position <= 0) {
			return null;
		} else {
			return breadcrumbList.get(position - 1).getNode();
		}
	}

	private int getIndex(List<Breadcrumb<K, V>> breadcrumbList,
			int position) {
		if (position < 0) {
			return -1;
		} else {
			return breadcrumbList.get(position).getIndex();
		}
	}

	public V get(K key) {
		/*
		   1. Perform a binary search on the search key values in the current
		      node -- recall that the search key values in a node are sorted
		      and that the search starts with the root of the tree. We want to
		      find the key Ki  such that Ki <= K < Ki+1.
		   2. If the current node is an internal node, follow the proper branch
		      associated with the key Ki by loading the disk page corresponding
		      to the node and repeat the search process at that node.
		*/
		LeafNode<K, V> node = findLeafNode(key);
		
		/*
		   3. If the current node is a leaf, then:
		         1. If K = Ki, then the record exists in the table and we can
		            return the record associated with Ki
		         2. Otherwise, K is not found among the search key values at
		            the leaf, we report that there is no record in the table
		            with the value K.
		*/
		
		if (node == null) {
			return null;
		}
		
		int index = node.getKeyIndex(key);
		
		if(index >= 0) {
			return node.getValue(index);
		} else {
			return null;
		}
	}
	
	public void put(K key, V value) {
		if (root == null) {
//			root = this.readNode(rootID);
			throw new IllegalStateException("The root is null.");
		}
		
		/*
		   1.  Follow the path that is traversed as if a Search is being
		       performed on the key of the new record to be inserted.
           2. The leaf page L that is reached is the node where the new record
              is to be indexed.
		*/
		List<Breadcrumb<K, V>> breadcrumbList =
			new ArrayList<Breadcrumb<K, V>>(10);
		LeafNode<K, V> leafNode = findLeafNode(key, breadcrumbList);
		Node<K, V> node = leafNode;
		int position = breadcrumbList.size() - 1;

		/*
           3. If L is not full then an index entry is created that includes the
              search key value of the new row and a reference to where new row
              is in the data file. We are done; this is the easy case!
        */
		if(!leafNode.isFull()) {
			leafNode.insert(key, value);
		} else {
			/*
	           4. If L is full, then a new leaf node Lnew is introduced to the
	              B+-tree as a right sibling of L. The keys in L along with the an
	              index entry for the new record are distributed evenly among L and
	              Lnew. Lnew is inserted in the linked list of leaf nodes just to
	              the right of L. We must now link Lnew to the tree and since Lnew
	              is to be a sibling of L, it will then be pointed to by the
	              parent of L. The smallest key value of Lnew is copied and
	              inserted into the parent of L -- which will also be the parent of
	              Lnew. This entire step is known as commonly referred to as a
	              split of a leaf node.
	        */
			K newKey = key;
			LeafNode<K, V> newLeafNode = leafNode.split(newKey, value);
			
			InnerNode<K, V> parent = 
				(InnerNode<K, V>) getParent(breadcrumbList, position--);
			Node<K, V> newNode = newLeafNode;
			newKey = node.getKey(node.getSlots() - 1);
			
			/*
            a. If the parent P of L is full, then it is split in turn.
               However, this split of an internal node is a bit different.
               The search key values of P and the new inserted key must
               still be distributed evenly among P and the new page
               introduced as a sibling of P. In this split, however, the
               middle key is moved to the node above -- note, that unlike
               splitting a leaf node where the middle key is copied and
               inserted into the parent, when you split an internal node
               the middle key is removed from the node being split and
               inserted into the parent node. This splitting of nodes may
               continue upwards on the tree.
			*/
			while (parent != null && parent.isFull()) {
				InnerNode<K, V> newInnerNode = parent.split(newKey, newNode.getIdentifier());
				newKey = parent.getKey(parent.getSlots());
				node = parent;
				newNode = newInnerNode;
				parent = (InnerNode<K, V>) getParent(breadcrumbList, position--);
				
			}
			
			/*
            b. When a key is added to a full root, then the root splits
               into two and the middle key is promoted to become the new
               root. This is the only way for a B+-tree to increase in
               height -- when split cascades the entire height of the tree
               from the leaf to the root. 
            */
			if (parent == null) {
				parent = factory.getInnerNode(this, -1);
				parent.setChildId(node.getIdentifier(), 0);
				root = parent;
			}
			
			parent.insert(newKey, newNode.getIdentifier());
//			this.writeNode(parent);
		}
	}

	private Node<K, V>[] getSiblings(List<Breadcrumb<K, V>> breadcrumbList,
			int position) {
		InnerNode<K, V> parent =
			(InnerNode<K, V>) getParent(breadcrumbList, position);
		
		int index = getIndex(breadcrumbList, position);
		
		// Should never happen.
		checkIndex(index);
		
		@SuppressWarnings("unchecked")
		Node<K, V> results[] = new Node[2];
		
		if (index > 0) {
			results[0] = this.readNode(parent.getChildId(index - 1));
		}
		
		if (index < parent.getSlots()) {
			results[1] = this.readNode(parent.getChildId(index + 1));
		}

		return results;
	}

	private void checkIndex(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Node is not child of parent!");
		}
	}
	
	private void updateLeafParentKey(Node<K, V> node, int nodeIndex,
			List<Breadcrumb<K, V>> breadcrumbList, int position) {
		InnerNode<K, V> parent = 
			(InnerNode<K, V>) getParent(breadcrumbList, position);
		
		int index = getIndex(breadcrumbList, position) + nodeIndex;
		
		checkIndex(index);
		
		parent.setKey(node.getKey(node.getSlots() - 1), index);
		
		this.writeNode(parent);
	}
	
	private K getParentKey(boolean left, List<Breadcrumb<K, V>> breadcrumbList,
			int position) {
		InnerNode<K, V> parent = 
			(InnerNode<K, V>) getParent(breadcrumbList, position - 1);
		
		int index = getIndex(breadcrumbList, position - 1);
		
		checkIndex(index);
		
		return parent.getKey(left ? index - 1 : index);
	}
	
	private void updateParentKey(int nodeIndex, K key,
			List<Breadcrumb<K, V>> breadcrumbList, int position) {
		InnerNode<K, V> parent = 
			(InnerNode<K, V>) getParent(breadcrumbList, position - 1);
		
		int index = getIndex(breadcrumbList, position - 1) + nodeIndex;
		
		checkIndex(index);

		parent.setKey(key, index);
		
		this.writeNode(parent);
	}
	
	private LeafNode<K, V> getPreviousLeafNode(LeafNode<K, V> leafNode,
			List<Breadcrumb<K, V>> breadcrumbList, int position) {
		LeafNode<K, V> result = null;
		Node<K, V> node = leafNode;
		
		InnerNode<K, V> parent = null;
		int index = -1;
		int loopPosition = position;
		
		do {
			parent  = (InnerNode<K, V>) getParent(breadcrumbList, loopPosition);
			
			index = getIndex(breadcrumbList, loopPosition);
			
			checkIndex(index);
			
			node = parent;
			loopPosition--;
		} while (parent != root && index == 0);

		if (parent != root || index != 0) {
			node = this.readNode(((InnerNode)node).getChildId(index - 1));
			while (node instanceof InnerNode<?, ?>) {
				node = this.readNode(((InnerNode)node).getChildId(node.getSlots()));				
			}
			result = (LeafNode<K, V>) node;
		}
		
		return result;
	}
	
	/**
	 * requires checking
	 * TODO Complex Method
	 */
	@SuppressWarnings("unchecked")
	private void removeParentKey(List<Breadcrumb<K, V>> breadcrumbList,
			int position) {
		InnerNode<K, V> parent =
			(InnerNode<K, V>) getParent(breadcrumbList, position);
		
		if (parent != null) {
			int index = getIndex(breadcrumbList, position);
			
			checkIndex(index);
			
			parent.remove(index);
			
			if (parent != root) {
				if (!parent.hasEnoughSlots()) {
					Node<K, V> siblings[] =
						getSiblings(breadcrumbList, position - 1);
					int siblingIndex = getSiblingIndex(siblings);
					
					if (canGiveSlots(siblings)) {
						int count = (siblings[siblingIndex].getSlots() -
								parent.getSlots()) / 2;
						if (siblingIndex == 0) {
							parent.rightShift(count);
							parent.setKey(getParentKey(true, breadcrumbList, position), count - 1);
							siblings[siblingIndex].copyToRight(parent, count);
						} else {
							parent.setKey(getParentKey(false, breadcrumbList, position), parent.getSlots());
							siblings[siblingIndex].copyToLeft(parent, count);
							siblings[siblingIndex].leftShift(count);
						}
						parent.setSlots(parent.getSlots() + count);
						siblings[siblingIndex].setSlots(siblings[siblingIndex].getSlots() - count);
						if (siblingIndex == 0) {
							updateParentKey(-1,
									siblings[0].getKey(siblings[0].getSlots()),
											breadcrumbList,
											position);
						} else {
							updateParentKey(0,
									parent.getKey(parent.getSlots()),
											breadcrumbList,
											position);
						}
						this.writeNode(siblings[siblingIndex]);
						this.writeNode(parent);
					} else {
				/*
				         b. If both Lleft and Lright have only the minimum number of
				            entries, then L gives its records to one of its siblings
				            and it is removed from the tree. The new leaf will contain
				            no more than the maximum number of entries allowed. This
				            merge process combines two subtrees of the parent, so the
				            separating entry at the parent needs to be removed -- this
				            may in turn cause the parent node to underflow; such an
				            underflow is handled the same way that an underflow of a
				            leaf node.
				*/
						if(siblingIndex == 0) {
							siblings[siblingIndex].setKey(
									getParentKey(true, breadcrumbList, position),
									siblings[siblingIndex].getSlots());
							parent.copyToLeft(siblings[siblingIndex], parent.getSlots() + 1);
						} else {
							siblings[siblingIndex].rightShift(siblings[siblingIndex].getSlots());
							siblings[siblingIndex].setKey(
									getParentKey(false, breadcrumbList, position),
									parent.getSlots());
							parent.copyToRight(siblings[siblingIndex], parent.getSlots() + 1);
						}
						siblings[siblingIndex].setSlots(siblings[siblingIndex].getSlots() + parent.getSlots() + 1);
						this.writeNode(siblings[siblingIndex]);
						removeParentKey(breadcrumbList, position - 1);
						this.deleteNode(parent);
					}
				}
			} else {
				/*
		         c. If the last two children of the root merge together into
		            one node, then this merged node becomes the new root and
		            the tree loses a level. 
				 */
				if(parent.getSlots() == 0) {
					this.deleteNode(root);
					root = this.readNode(parent.getChildId(0));
				}
			}
		}
	}
	
	/*
	 * TODO: Complex Method
	 */
	public void remove(K key) {
		/*
		   1. Perform the search process on the key of the record to be
		      deleted. This search will end at a leaf L.
		*/
		List<Breadcrumb<K, V>> breadcrumbList =
			new ArrayList<Breadcrumb<K, V>>(10);
		LeafNode<K, V> leafNode = findLeafNode(key, breadcrumbList);
		int position = breadcrumbList.size() - 1;

		/*
		   2. If the leaf L contains more than the minimum number of elements
		      (more than m/2 - 1), then the index entry for the record to be
		      removed can be safely deleted from the leaf with no further
		      action.
		*/
		int index = leafNode.getKeyIndex(key);
		
		if (index >= 0) {
			leafNode.remove(index);
		}
		
		if (leafNode != root) {
			if (!leafNode.hasEnoughSlots()) {
	
			/*
			   3. If the leaf contains the minimum number of entries, then the
			      deleted entry is replaced with another entry that can take its
			      place while maintaining the correct order. To find such entries,
			      we inspect the two sibling leaf nodes Lleft and Lright adjacent
			      to L -- at most one of these may not exist.
			*/
				Node<K, V> siblings[] =
					getSiblings(breadcrumbList, position);
				int siblingIndex = getSiblingIndex(siblings);
				
				if (canGiveSlots(siblings)) {
					int count = (siblings[siblingIndex].getSlots() - 
							leafNode.getSlots()) / 2;
					if (siblingIndex == 0) {
						leafNode.rightShift(count);
						siblings[siblingIndex].copyToRight(leafNode, count);
					} else {
						siblings[siblingIndex].copyToLeft(leafNode, count);
						siblings[siblingIndex].leftShift(count);
					}
					leafNode.setSlots(leafNode.getSlots() + count);
					siblings[siblingIndex].setSlots(siblings[siblingIndex].getSlots() - count);
					if (siblingIndex == 0) {
						updateLeafParentKey(siblings[0], -1, breadcrumbList, position);
					} else {
						updateLeafParentKey(leafNode, 0, breadcrumbList, position);
					}
					this.writeNode(siblings[siblingIndex]);
					this.writeNode(leafNode);
				} else {
			/*
			         b. If both Lleft and Lright have only the minimum number of
			            entries, then L gives its records to one of its siblings
			            and it is removed from the tree. The new leaf will contain
			            no more than the maximum number of entries allowed. This
			            merge process combines two subtrees of the parent, so the
			            separating entry at the parent needs to be removed -- this
			            may in turn cause the parent node to underflow; such an
			            underflow is handled the same way that an underflow of a
			            leaf node.
			*/
					if(siblingIndex == 0) {
						leafNode.copyToLeft(siblings[siblingIndex], leafNode.getSlots());
					} else {
						siblings[siblingIndex].rightShift(leafNode.getSlots());
						leafNode.copyToRight(siblings[siblingIndex], leafNode.getSlots());
					}
					siblings[siblingIndex].setSlots(siblings[siblingIndex].getSlots() + leafNode.getSlots());
					this.writeNode(siblings[siblingIndex]);
					if(siblings[0] == null) {
						LeafNode<K, V> previousLeafNode = 
							getPreviousLeafNode(leafNode, breadcrumbList,
									position);
						if (previousLeafNode != null) {
							previousLeafNode.setNextId(leafNode.getNextId());
							this.writeNode(previousLeafNode);
						}
					} else {
						((LeafNode<K, V>) siblings[0]).setNextId(leafNode.getNextId());
						this.writeNode(siblings[0]);
					}
					removeParentKey(breadcrumbList, position);
//					this.deleteNode(leafNode);
				}
			/*
			         c. If the last two children of the root merge together into
			            one node, then this merged node becomes the new root and
			            the tree loses a level. 
			*/
			}
		} else {
//			if (leafNode.getSlots() == 0) {
//				root = null;
//			}
		}
		
	}

	private boolean canGiveSlots(Node<K, V>[] siblings) {
		boolean found = false;
		// TODO optimize
		if (siblings[0] == null) {
			 if (siblings[1].canGiveSlots()) {
				 found = true;
			 }
		} else if (siblings[1] == null) {
			if (siblings[0].canGiveSlots()) {
				found = true;
			}
		} else if (siblings[0].getSlots() > siblings[1].getSlots()) {
			if (siblings[0].canGiveSlots()) {
				found = true;
			}
		} else {
			 if (siblings[1].canGiveSlots()) {
				 found = true;
			 }
		}
		return found;
	}
	
	private int getSiblingIndex(Node<K, V> siblings[]) {
		if (siblings[0] == null) {
			return 1;
		} else if (siblings[1] == null) {
			return 0;
		} else if (siblings[0].getSlots() > siblings[1].getSlots()) {
			return 0;
		} else {
			return 1;
		}
		
	}
	
	/**
	 * @author chenqian
	 * @return int
	 * 			get max capacity of innder node
	 * */
	public int getOrder() {
		return factory.getOrder();
	}
	
	/**
	 * @author chenqian
	 * @return int
	 * 			get max capacity of leaf node
	 * */
	public int getRecords() {
		return factory.getRecords();
	}
	
	/*
	 * Used in unit testing only
	 */
	public Node<K, V> getRoot() {
		return root;
	}
	
	/**
	 * 
	 * @param id
	 * 		node id to be loaded
	 * @return
	 * 		the node read from the disk/buffer
	 */
	public Node<K, V> readNode(int id) {
		byte[] buffer;
		DataInputStream ds = null;
		int nodeType = -1;
		Node n = null;
		try {
			buffer = m_pStorageManager.loadByteArray(id);
			ds = new DataInputStream(new ByteArrayInputStream(buffer));
			nodeType = ds.readInt();
			/**
			 * @check again
			 * */
			if (nodeType == SpatialIndex.PersistentIndex) n = factory.getInnerNode(this, -1);
			else if (nodeType == SpatialIndex.PersistentLeaf) n = factory.getLeafNode(this, -1);
			else throw 
				new IllegalStateException("readNode failed reading the correc node type information!");
			n.setIdentifier(id);
			n.load(buffer);
		} catch (InvalidPageException e) {
			System.err.println(e);
			throw new IllegalStateException("readNode failed with InvalidPageException");
		}
		catch (IOException e) {
			System.err.println(e);
			throw new IllegalStateException("readNode failed with IOException node id: " + id);
		}
		return n;
	}
	
	/**
	 * @para Node
	 * 		write node into bytes
	 * @return int
	 * 		page id
	 * */
	public int writeNode (Node n) {
		byte [] buffer = null;
		try {
			buffer = n.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalStateException("writeNode failed with IOException");
		}
		int page; 
		if (n.getIdentifier() < 0) page = IStorageManager.NewPage;
		else page = n.getIdentifier();
		try {
			page = m_pStorageManager.storeByteArray(page, buffer);
		} catch (InvalidPageException e) {
			System.err.println(e);
			throw new IllegalStateException("writeNode failed with InvalidPageException");
		}
		if (n.getIdentifier() < 0) {
			n.setIdentifier(page);
		}
		return page;
	}
	
	/**
	 * 
	 * @param n
	 * 		Node to be delete
	 */
	public void deleteNode (Node n) {
		try {
//			System.out.println("to delete " + n.getIdentifier());
			m_pStorageManager.deleteByteArray(n.getIdentifier());
		} catch (InvalidPageException e) {
			System.err.println(e);
			throw new IllegalStateException("deleteNode failed with InvalidPageException");
		}
	}
	
	/**
	 * 
	 * @param identifier
	 * 			node id to be delete
	 */
	public void deleteNode (int identifier) {
		try {
//			System.out.println("to delete " + identifier);
			m_pStorageManager.deleteByteArray(identifier);
		} catch (InvalidPageException e) {
			System.err.println(e);
			throw new IllegalStateException("deleteNode failed with InvalidPageException");
		}
	}
	
	/**
	 * Store the header file
	 * @throws IOException
	 */
	private void storeHeader () throws IOException {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);		
		ds.writeInt(factory.getOrder());
		ds.writeInt(factory.getRecords());
		ds.writeInt(root.getIdentifier());
		ds.flush();
		headerID = m_pStorageManager.storeByteArray(headerID, bs.toByteArray());
	}
	
	/**
	 * Load the header file
	 */
	private void loadHeader() throws IOException {
		byte[] data = m_pStorageManager.loadByteArray(headerID);
		DataInputStream ds = new DataInputStream(new ByteArrayInputStream(data));
		factory.setOrder(ds.readInt());
		factory.setRecords(ds.readInt());
		root = this.readNode(ds.readInt());
		ds.close();
	}
	
	/**
	 * Flush the data into the disk
	 * Remember to call flush before close the tree
	 */
	public void flush() {
		try {
			storeHeader();
			m_pStorageManager.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalStateException("flush failed with IOException");
		}
	}
	
	/**
	 * get string of the whole tree
	 * 
	 * */
	public String toString() {
		if (root == null) {
			throw new IllegalStateException("Root is null");
		}
		StringBuffer sb = new StringBuffer("=================== tree structure ==================\n");
		sb.append(root.toString());
		sb.append("\n=====================================================\n");
		return sb.toString();
	}
	
	public void queryStrategy(final IQueryStrategy qs) {
		if (root == null) {
			throw new NullPointerException("The root is pointed to null");
		}
		int[] next = new int[]{root.getIdentifier()};
		while (true) {
			Node n = readNode(next[0]);
			boolean[] hasNext = new boolean[] {false};
			qs.getNextEntry(n, next, hasNext);
			if (hasNext[0] == false) break;
		}
	}
}
