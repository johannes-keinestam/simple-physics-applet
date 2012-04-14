import java.awt.geom.Ellipse2D;

public class Ball {
	// x, y: positions
	// vx, vy: speeds/directions
	// r: ball radius
	public double radius, x, y, vx, vy;

	public Ball(double radius, double x, double y) {
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.vx = 2.3;
		this.vy = 1;
	}

	public Ellipse2D getEllipse() {
		return new Ellipse2D.Double(x - radius, y - radius, 2 * radius,
				2 * radius);
	}
}