import java.awt.geom.Ellipse2D;

public class Ball {
    // x, y: positions
    // vx, vy: speeds/directions
    // r: ball radius
    public double radius, x, y, vx, vy, x0, y0, mass;

    public Ball(double radius, double x, double y, double vx, double vy, double mass) {
        this.radius = radius;
        this.x = this.x0 = x;
        this.y = this.y0 = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
    }

    public Ellipse2D getEllipse() {
        return new Ellipse2D.Double(x - radius, y - radius, 2 * radius,
                2 * radius);
    }
}