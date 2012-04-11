import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class GravityModel implements IBouncingBallsModel {
    private final double areaWidth;
    private final double areaHeight;
    private final List<Ball> balls = new LinkedList<Ball>();

    // x, y: positions
    // vx, vy: speeds/directions
    // r: ball radius
    private double x, y, vx, vy, r;

    public GravityModel(double width, double height) {
        this.areaWidth = width;
        this.areaHeight = height;

        addBall(new Ball(1, 1, 1));
        // vx = 2.3;
        // vy = 1;
    }

    /**
     * Returns a list of shape representations of the balls. Used by the
     * BouncingBalls class to draw the balls.
     * 
     * @return the balls as shape objects
     */
    public List<Ellipse2D> getBalls() {
        List<Ellipse2D> l = new LinkedList<Ellipse2D>();
        for (Ball b : balls) {
            l.add(b.getEllipse());
        }
        return l;
    }
    
	public void addBall() {
		addBall(new Ball(1, 2.3, 1));
	}
    
    public void addBall(Ball b) {
        balls.add(b);
    }

    /**
     * Changes the state of the model using the Euler method by simulating
     * deltaT units of time.
     * 
     * @param deltaT
     *            the amount of time to simulate
     */
    public void tick(double deltaT) {
        // for (Ball b : balls) {

        // }
    }

}
