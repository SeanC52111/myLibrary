/**
 * 
 */
package io;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @author chenqian
 *
 */
public abstract interface RW{

//	public RW() {}
	public abstract void read(DataInputStream ds);
	public abstract void write(DataOutputStream ds);
}
