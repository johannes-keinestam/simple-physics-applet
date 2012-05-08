import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Ball {
    // x, y: positions
    // vx, vy: speeds/directions
    public double radius, x, y, vx, vy, mass;
    public final Color color;
    private static final double MINIMUM_MASS = 0.1;
    private static final double MAXIMUM_MASS = 5;
    private static final Color HEAVY_COLOR = Color.BLACK;
    private static final Color LIGHT_COLOR = Color.LIGHT_GRAY;

    public Ball(double radius, double x, double y, double vx, double vy, double mass) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = (mass < MAXIMUM_MASS) ? ((mass > MINIMUM_MASS) ? mass : MINIMUM_MASS) : MAXIMUM_MASS;
        
        // From http://www.java2s.com/Code/Java/2D-Graphics-GUI/Commoncolorutilities.htm
        double colorRange = MAXIMUM_MASS - MINIMUM_MASS;
        double relativeMass = this.mass - MINIMUM_MASS;
        float r = (float)relativeMass / (float)colorRange;
        float ir = (float) 1 - r;
        float rgb1[] = new float[3];
        float rgb2[] = new float[3];    
        HEAVY_COLOR.getColorComponents (rgb1);
        LIGHT_COLOR.getColorComponents (rgb2);
        
        this.color = new Color (rgb1[0] * r + rgb2[0] * ir, 
                rgb1[1] * r + rgb2[1] * ir, 
                rgb1[2] * r + rgb2[2] * ir);
    }

    public Ellipse2D getEllipse() {
        return new Ellipse2D.Double(x - radius, y - radius, 2 * radius,
                2 * radius);
    }
}