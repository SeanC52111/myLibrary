package memoryindex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import apple.laf.JRSUIUtils.Tree;
import IO.RW;


/**
 * A BinaryTree consists of "nodes"--each "node" is itself a BinaryTree.
 * Each node has a parent (unless it is the root), may have a left child,
 * and may have a right child. This class implements loop-free binary trees,
 * allowing shared subtrees.
 * 
 * @author David Matuszek 
 * @version Jan 25, 2004
 *  {@link} 
 *          http://www.cis.upenn.edu/~matuszek/cit594-2012/Code/BinaryTree.java
 *     
 * @revised chenqian
 */
public class BinaryTree<K extends Comparable<K>, V extends RW> implements RW{
    /**
     * The value (data) in this node of the binary tree; may be of
     * any object type.
     */
	public Class classValue;
	public K key;
    public V value;
    private BinaryTree<K, V> leftChild;
    private BinaryTree<K, V> rightChild;

    public BinaryTree(BinaryTree tree) {
    	this.key = (K) tree.key;
    	this.value = (V) tree.value;
    	this.leftChild = tree.leftChild;
    	this.rightChild = tree.rightChild;
    	this.classValue = tree.classValue;
    }
    
    
    /**
     * Constructor for BinaryTree.
     * 
     * @param value The value to be placed in the root.
     * @param leftChild The left child of the root (may be null).
     * @param rightChild The right child of the root (may be null).
     */
    public BinaryTree(K k, V value, BinaryTree<K, V> leftChild, BinaryTree<K, V> rightChild, Class classValue) {
        this.key = k;
    	this.value = value;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.classValue = classValue;
    }

    /**
     * Constructor for a BinaryTree leaf node (that is, with no children).
     * 
     * @param value The value to be placed in the root.
     */
    public BinaryTree(K key, V value, Class classValue) {
        this(key, value, null, null, classValue);
    }

    /**
     * 
     */
    public BinaryTree(Class classValue) {
    	this.classValue = classValue;
    }
    
    /**
     * Getter method for the value in this BinaryTree node.
     * 
     * @return The value in this node.
     */
    public V getValue() {
        return value;
    }
    
    /**
     * Get the key,
     * @return
     */
    public K getKey() {
    	return key;
    }
    
    /**
     * Getter method for left child of this BinaryTree node.
     * 
     * @return The left child (<code>null</code> if no left child).
     */
    public BinaryTree<K, V> getLeftChild() {
        return leftChild;
    }
    
    /**
     * Check whether the left child is empty or not.
     * @return
     */
    public boolean isLeftChildEmpty() {
    	return leftChild == null;
    }
    
    /**
     * Getter method for right child of this BinaryTree node.
     * @return The right child (<code>null</code> if no right child).
     */
    public BinaryTree<K, V> getRightChild() {
        return rightChild;
    }

    /**
     * Check whether the right child is empty or not.
     * @return
     */
    public boolean isRightChildEmpty() {
    	return rightChild == null;
    }
    /**
     * Sets the left child of this BinaryTree node to be the
     * given subtree. If the node previously had a left child,
     * it is discarded. Throws an <code>IllegalArgumentException</code>
     * if the operation would cause a loop in the binary tree.
     * 
     * @param subtree The node to be added as the new left child.
     * @throws IllegalArgumentException If the operation would cause
     *         a loop in the binary tree.
     */
    public void setLeftChildProtected(BinaryTree<K, V> subtree) throws IllegalArgumentException {
        if (contains(subtree, this)) {
            throw new IllegalArgumentException(
                "Subtree " + this +" already contains " + subtree);
        }
        leftChild = subtree;
    }

    /**
     * The difference with the above one is to check whether
     * the subtree is in the tree already, it is not necessary.
     * @param subtree
     */
    public void setLeftChild(BinaryTree<K, V> subtree) {
    	leftChild = subtree;
    }
    
    /**
     * Sets the right child of this BinaryTree node to be the
     * given subtree. If the node previously had a right child,
     * it is discarded. Throws an <code>IllegalArgumentException</code>
     * if the operation would cause a loop in the binary tree.
     * 
     * @param subtree The node to be added as the new right child.
     * @throws IllegalArgumentException If the operation would cause
     *         a loop in the binary tree.
     */
    public void setRightChildProtected(BinaryTree<K, V> subtree) throws IllegalArgumentException {
        if (contains(subtree, this)) {
            throw new IllegalArgumentException(
                    "Subtree " + this +" already contains " + subtree);
        }
        rightChild = subtree;
    }

    public void setRightChild(BinaryTree<K, V> subtree) {
    	rightChild = subtree;
    }
    
    /**
     * Sets the value in this BinaryTree node.
     * 
     * @param value The new value.
     */
    public void setValue(V value) {
        this.value = value;
    }
    
    /**
     * Set the key.
     * @param key
     */
    public void setKey (K key){
    	this.key = key;
    }
    
    public void setKeyValue(K Key, V value) {
    	this.value = value;
    }
    
    /**
     * Tests whether this node is a leaf node.
     * 
     * @return <code>true</code> if this BinaryTree node has no children.
     */
    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }
    
    /**
     * Tests whether this BinaryTree is equal to the given object.
     * To be considered equal, the object must be a BinaryTree,
     * and the two binary trees must have equal values in their
     * roots, equal left subtrees, and equal right subtrees.
     * 
     * @return <code>true</code> if the binary trees are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BinaryTree)) {
            return false;
        }
        BinaryTree<K, V> otherTree = (BinaryTree<K, V>) o;
        return equals(value, otherTree.value)
            && equals(leftChild, otherTree.getLeftChild())
            && equals(rightChild, otherTree.getRightChild());
    }
    
    /**
     * Tests whether its two arguments are equal.
     * This method simply checks for <code>null</code> before
     * calling <code>equals(Object)</code> so as to avoid possible
     * <code>NullPointerException</code>s.
     * 
     * @param x The first object to be tested.
     * @param y The second object to be tested.
     * @return <code>true</code> if the two objects are equal.
     */
    private boolean equals(Object x, Object y) {
        if (x == null) return y == null;
        return x.equals(y);
    }

    /**
     * Tests whether the <code>tree</code> argument contains within
     * itself the <code>targetNode</code> argument.
     * 
     * @param tree The root of the binary tree to search.
     * @param targetNode The node to be searched for.
     * @return <code>true</code> if the <code>targetNode</code> argument can
     *        be found within the binary tree rooted at <code>tree</code>.
     */
    protected boolean contains(BinaryTree<K, V> tree, BinaryTree<K, V> targetNode) {
        if (tree == null)
            return false;
        if (tree == targetNode)
            return true;
        return contains(targetNode, tree.getLeftChild())
            || contains(targetNode, tree.getRightChild());
    }
    
    /**
     * Returns a String representation of this BinaryTree.
     * 
     * @see java.lang.Object#toString()
     * @return A String representation of this BinaryTree.
     */
    public String toString() {
        if (isLeaf()) {
            return value.toString();
        }
        else {
        	StringBuffer sb = new StringBuffer();
        	if (value == null) sb.append("null (");
        	else sb.append(value.toString());
            String left = "null", right = "null";
            if (getLeftChild() != null) {
                left = getLeftChild().toString();
            }
            if (getRightChild() != null) {
                right = getRightChild().toString();
            }
            sb.append(left + ", " + right + ")");
            return sb.toString();
        }
    }
    
    /**
     * Computes a hash code for the complete binary tree rooted
     * at this BinaryTree node.
     * 
     * @return A hash code for the binary tree with this root.
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int result = 0;
        if(value != null) result = value.hashCode();
        if (leftChild != null) {
            result += 3 * leftChild.hashCode();
        }
        if (rightChild != null) {
            result += 7 * leftChild.hashCode();
        }
        return result;
    }
    
    /**
     * Prints the binary tree rooted at this BinaryTree node.
     */
    public void print() {
        print(this, 0);
    }
    
    private void print(BinaryTree<K, V> root, int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("   ");
        }
        if (root == null) {
            System.out.println("null");
            return;
        }
        System.out.println(root.value);
        if (root.isLeaf()) return;
        print(root.leftChild, indent + 1);
        print(root.rightChild, indent + 1);
    }

    /**
     * Get the not empty children
     * @return
     */
    public BinaryTree<K, V>[] getChildren() {
    	ArrayList<BinaryTree<K, V>> trees = new ArrayList<>();
    	if (!isLeftChildEmpty()) trees.add(getLeftChild());
    	if (!isRightChildEmpty()) trees.add(getRightChild());
    	return trees.toArray(new BinaryTree[]{});
    }
    
    /**
     * '<=' enters into leftNode, and '>' enters into right
     * @param k
     * @return
     */
    public V get(K k) {
    	if (this.isLeaf()) {
    		if (key.compareTo(k) == 0) return getValue();
    		else return null;
    	}
    	if (key.compareTo(k) <= 0) {
    		return getLeftChild().get(k);
    	} else {
    		if (!isRightChildEmpty())
    			return getLeftChild().get(k);
    	}
    	return null;
    }


	@Override
	public void read(DataInputStream ds) {
		// TODO Auto-generated method stub
		try {
			key = (K) new Integer(ds.readInt());
			value = (V) classValue.newInstance();
			value.read(ds);
			if (ds.readBoolean()) {
				leftChild = new BinaryTree(this.classValue);
				leftChild.read(ds);
			}
			if (ds.readBoolean()) {
				rightChild = new BinaryTree(this.classValue);
				rightChild.read(ds);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public void write(DataOutputStream ds) {
		// TODO Auto-generated method stub
		try {
			if(key != null) ds.writeInt((Integer)key);
			else ds.writeInt(0);
			value.write(ds);
			if (isLeftChildEmpty()) ds.writeBoolean(false);
			else {
				ds.writeBoolean(true);
				leftChild.write(ds);
			}
			if (isRightChildEmpty()) ds.writeBoolean(false);
			else {
				ds.writeBoolean(true);
				rightChild.write(ds);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void loadBytes(byte[] data) {
		// TODO Auto-generated method stub
		DataInputStream ds = new DataInputStream(new ByteArrayInputStream(data));
		read(ds);
	}


	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(bs);
		write(ds);
		return bs.toByteArray();
	}
	
	public Class getClassValue() {
		return classValue;
	}
	
	public void queryStrategy(final IQueryStrategy qs) {
		BinaryTree[] next = new BinaryTree[]{this};
		while (true) {
			BinaryTree n = next[0];
			boolean[] hasNext = new boolean[] {false};
			qs.getNextEntry(n, next, hasNext);
			if (hasNext[0] == false) break;
		}
	}
}
