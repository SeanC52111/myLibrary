package memoryindex;

import io.RW;
import spatialindex.IShape;

public class QuadEntry {

	private int	id		= -1;
	private IShape	shape	= null;
	private RW		data	= null;
	
	public int getId() {
		return id;
	}
	
	public IShape getShape() {
		return shape;
	}
	
	public RW getData() {
		return data;
	}
	
	public QuadEntry() {
		// TODO Auto-generated constructor stub
	}
	
	public QuadEntry(int _id, IShape _shape, RW _data) {
		id = _id;
		shape = _shape;
		data = _data;
	}

}
