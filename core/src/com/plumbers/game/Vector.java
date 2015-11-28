public class Vector {
	private double x;
	private double y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void add(Vector r) {
		this.x = this.x + r.getX();
		this.y = this.y + r.getY();
	}

	public void subtract(Vector r) {
		this.x = this.x - r.getX();
		this.y = this.y - r.getY();
	}

	public void multiply(int i) {
		this.x = this.x * i;
		this.y = this.y * i;
	}

	public Vector sum(Vector r) {
		double a = this.x + r.getX();
		double b = this.y + r.getY();
		return new Vector(a, b);

	}
}
