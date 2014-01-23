/**
 * 
 */
package memoryindex;

/**
 * @author chenqian
 *
 */
public interface IQueryStrategyQT {
	public void getNextEntry(QuadTree n, QuadTree[] next, boolean[] hasNext);
}
