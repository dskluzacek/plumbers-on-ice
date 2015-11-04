
public class Rectangle {
	
	private int x;
	private int y;
	private int w;
	private int h;

	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
 
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getW() {
		return w;
	}
	
	public int getH() {
		return h;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setW(int w) {
		this.w = w;
	}
	
	public void setH(int h) {
		this.h = h;
	}

	public boolean intersects(Rectangle other) {
		return ( this.x + this.w < other.x || other.x + other.w < this.x
			    || this.y + this.h < other.y || other.y + other.h < this.y );
	}
}
