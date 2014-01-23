/**
 * 
 */
package memoryindex;

/**
 * @author chenqian
 *
 */
public interface IQueryStrategyBT {
	public void getNextEntry(BinaryTree n, BinaryTree[] next, boolean[] hasNext);
}
