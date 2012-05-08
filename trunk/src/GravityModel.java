import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GravityModel implements IBouncingBallsModel {
    private final double areaWidth;
    private final double areaHeight;
    private final List<Ball> balls = Collections
            .synchronizedList(new LinkedList<Ball>());
    private boolean collisionHighlighting = false;
    private boolean roofEnabled = true;
    private static final float GRAVITY_ACC = 9.82f / 60f;

    public GravityModel(double width, double height) {
        this.areaWidth = width;
        this.areaHeight = height;

        addBall();
        // vx = 2.3;
        // vy = 1;
    }

    /**
     * Returns a list of shape representations of the balls. Used by the
     * BouncingBalls class to draw the balls.
     * 
     * @return the balls as shape objects
     */
    public List<Ball> getBalls() {
        return new LinkedList<Ball>(balls);
    }

    public boolean addBall() {
        return addBall(new Ball(1, areaWidth * 0.4, areaHeight * 0.8, 1, 0, 1));
    }

    public boolean addBall(Ball b) {
        if (intersects(b) == null) {
            balls.add(b);
            System.out.println("Ball added");
            return true;
        } else {
            System.err.println("Could not add ball!");
            return false;
        }
    }

    /**
     * Checks whether the two specified balls intersect.
     * 
     * @param b1
     * @param b2
     * @return
     */
    public boolean intersects(Ball b1, Ball b2) {
        // TODO b1.radius + b2.radius >= dist between balls x,y ?
        if (b1 == b2) {
            return false;
        } else {
            double distance = Math.sqrt(Math.pow(b1.x - b2.x, 2) + Math.pow(b1.y - b2.y, 2));
            return (distance <= b1.radius + b2.radius); 
            /*Area a1 = new Area(b1.getEllipse());
            Area a2 = new Area(b2.getEllipse());
            a1.intersect(a2);
            return !a1.isEmpty();*/
        }
    }

    /**
     * Checks whether specified ball intersects with any other ball.
     * 
     * @param b
     * @return
     */
    public Ball intersects(Ball b) {
        // fetches list to get copy; avoids concurrentmodificationexception
        for (Ball b2 : getBalls()) {
            if (intersects(b, b2))
                return b2;
        }
        return null;
    }

    public List<List<Ball>> getBallCollisions() {
        List<Ball> cBalls = Collections.synchronizedList(new LinkedList<Ball>(
                balls));
        ArrayList<List<Ball>> collisions = new ArrayList<List<Ball>>();

        boolean collisionsDetected = false;
        while (!cBalls.isEmpty()) {
            Ball b = cBalls.remove(0);
            Ball bCol = intersects(b);

            if (bCol != null) {
                ArrayList<Ball> c = new ArrayList<Ball>(2);
                c.add(b);
                c.add(bCol);
                collisions.add(c);
                cBalls.remove(bCol);
                collisionsDetected = true;
            }
        }
        return collisionsDetected ? collisions : null;
    }
    
    public void releaseRandomBalls(int amount) {
        balls.clear();
        
        int releasedBalls = 0;
        Random randomGen = new Random();
        
        while (releasedBalls != amount) {
            double radius = 0.3+(Math.max(randomGen.nextDouble(), randomGen.nextDouble()))*3.0;
            double x = areaWidth*randomGen.nextDouble();
            double y = areaHeight*randomGen.nextDouble();
            double vx = 10*Math.max(randomGen.nextDouble(), randomGen.nextDouble()) - 5;
            double vy = 10*Math.max(randomGen.nextDouble(), randomGen.nextDouble()) - 5;
            double mass = 0.1+randomGen.nextDouble()*5;
            
            if (x < radius) x = radius;
            else if (x > areaWidth-radius) x = areaWidth-radius;
            if (y < radius) y = radius;
            else if (y > areaHeight-radius) y = areaHeight-radius;
            
            Ball randomBall = new Ball(radius, x, y, vx, vy, mass);
            if (addBall(randomBall)) releasedBalls++;
        }
    }

    /**
     * Changes the state of the model using the Euler method by simulating
     * deltaT units of time.
     * 
     * @param deltaT
     *            the amount of time to simulate
     */
    public void tick(double deltaT) {
        List<List<Ball>> collisions = getBallCollisions();
        if (collisions != null) {
            for (List<Ball> collision : collisions) {
                Ball b1 = collision.get(0);
                Ball b2 = collision.get(1);

                // http://en.wikipedia.org/wiki/Rotation_matrix#In_two_dimensions
                
                // calculate angle of impact for balls b1 and b2
                double theta = Math.atan2(b1.y - b2.y, b1.x - b2.x);

                // rotate velocities from x, y to new coordinate system p, q
                double vp1 = b1.vx * Math.cos(theta) - b1.vy * Math.sin(theta);
                double vp2 = b2.vx * Math.cos(theta) - b2.vy * Math.sin(theta);
                double vq1 = b1.vx * Math.sin(theta) + b1.vy * Math.cos(theta);
                double vq2 = b2.vx * Math.sin(theta) + b2.vy * Math.cos(theta);
                double m1 = b1.mass;
                double m2 = b2.mass;

                // calculate new velocities from elastic collision in new coordinate system
                double up1 = (vp1 * (m1 - m2) + 2 * m2 * vp2) / (m1 + m2);
                double up2 = (vp2 * (m2 - m1) + 2 * m1 * vp1) / (m1 + m2);

                // rotate new velocity back from p, q to x, y
                double vx1 = up1 * Math.cos(-theta) - vq1 * Math.sin(-theta);
                double vx2 = up2 * Math.cos(-theta) - vq2 * Math.sin(-theta);
                double vy1 = up1 * Math.sin(-theta) + vq1 * Math.cos(-theta);
                double vy2 = up2 * Math.cos(-theta) + vq2 * Math.sin(-theta);
                
                b1.vy = vy1;
                b1.vx = vx1;
                b2.vy = vy2;
                b2.vx = vx2;

                // separate balls correctly
                separateBalls(b1, b2, deltaT);
            }
        }

        for (Ball b : balls) {
            // Gravity
            // System.out.println("X="+b.x+", Y="+b.y+", VX="+b.vx+", VY="+b.vy);
            b.vy += -GRAVITY_ACC;
            // No change in x axis
            b.vx += 0;

            if (b.x < b.radius || b.x > areaWidth - b.radius) {
                b.vx *= -1;
                if (b.x < b.radius) {
                    b.x = b.radius;
                } else {
                    b.x = areaWidth - b.radius;
                }
            }
            if (b.y < b.radius) {
                b.vy *= -1;
                b.y = b.radius;
            } else if (roofEnabled && b.y > areaHeight - b.radius) {
                b.vy *= -1;
                b.y = areaHeight - b.radius;
            }

            b.y += b.vy * deltaT;
            b.x += b.vx * deltaT;
        }
    }
    
    private void separateBalls(Ball b1, Ball b2, double deltaT) {
        while (intersects(b1, b2)) {
            b1.y += b1.vy * deltaT;
            b1.x += b1.vx * deltaT;

            b2.y += b2.vy * deltaT;
            b2.x += b2.vx * deltaT;
        }
        
        Ball newCollision1 = intersects(b1);
        Ball newCollision2 = intersects(b2);
        if (newCollision1 != null) separateBalls(b1, newCollision1, deltaT);
        if (newCollision2 != null) separateBalls(b2, newCollision2, deltaT);
    }

    public boolean isCollisionHighlighting() {
        return collisionHighlighting;
    }

    public void setCollisionHighlighting(boolean collisionHighlighting) {
        this.collisionHighlighting = collisionHighlighting;
    }

    public boolean isRoofEnabled() {
        return roofEnabled;
    }

    public void setRoofEnabled(boolean roofEnabled) {
        this.roofEnabled = roofEnabled;
    }

    public void clearBalls() {
        balls.clear();
    }

}
