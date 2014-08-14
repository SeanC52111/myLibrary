/**
 * 
 */

/**
 * @author chenqian
 *
 */
public class Algorithm {

	
	public static void swap(int[] a, int l, int r) {
		int tmp = a[l];
		a[l] = a[r];
		a[r] = tmp;
	}

	static int select(int[] a, int l, int r, int k) {
		if (l == r)
			return a[l];
		int val = a[l], p = l, q = r;
		for (int i = l; i <= q;) {
			if (a[i] < val) {
				swap(a, p++, i++);
			} else if (a[i] > val) {
				swap(a, q--, i);
			} else {
				i++;
			}
		}
		if (p <= k + l && q >= k + l) return val;
		else if (k + l < p)
			return select(a, l, p, k);
		else
			return select(a, q, r, k - (p-l+1));
	}
	
	/**
	 * k is 0 based
	 * @param a
	 * @param k
	 * @return
	 */
	public static int quickSelect(int[] a, int k) {
		if (a == null || k >= a.length) {
			throw new IllegalStateException("a = null or k is larger than a.length-1");
		}
		return select(a, 0, a.length-1, k);
	}
	
	/**
	 * 
	 */
	public Algorithm() {
		// TODO Auto-generated constructor stub
	}

}
