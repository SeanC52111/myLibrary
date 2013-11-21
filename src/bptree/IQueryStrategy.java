/**
 * 
 */
package bptree;

/**
 * @author chenqian
 *
 */
public interface IQueryStrategy {
	public void getNextEntry(Node n, int next[], boolean[] hasNext);
}
