import java.awt.geom.Ellipse2D;

public class Ball {
    private final Ellipse2D ellipse;
    // x, y: positions
    // vx, vy: speeds/directions
    // r: ball radius
    private final double radius, x, y, vx, vy;

    public Ball(double radius, double x, double y) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.vx = 2.3;
		this.vy = 1;
        
        ellipse = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);
    }
    
    public Ellipse2D getEllipse() {
        return ellipse;
    }
}