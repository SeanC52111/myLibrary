/**
 * 
 */
package memoryindex;

/**
 * @author chenqian
 *
 */
public interface IQueryStrategy {
	public void getNextEntry(BinaryTree n, BinaryTree[] next, boolean[] hasNext);
}
