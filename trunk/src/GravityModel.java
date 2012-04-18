import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GravityModel implements IBouncingBallsModel {
    private final double areaWidth;
    private final double areaHeight;
    private final List<Ball> balls = Collections
            .synchronizedList(new LinkedList<Ball>());
    private boolean collisionHighlighting = false;
    private static final float GRAVITY_ACC = 9.82f/500f;

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
        return addBall(new Ball(1, areaWidth*0.4, areaHeight*0.8, 1, 0, 1));
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
        if (b1 == b2) {
            return false;
        } else {
            Area a1 = new Area(b1.getEllipse());
            Area a2 = new Area(b2.getEllipse());
            a1.intersect(a2);
            return !a1.isEmpty();
        }
    }
    
    /**
     * Checks whether specified ball intersects with any other ball.
     * 
     * @param b
     * @return
     */
    public Ball intersects(Ball b) {
        for (Ball b2 : balls) {
            if (intersects(b, b2)) return b2; 
        } 
        return null;
    }
    
    public List<List<Ball>> getBallCollisions() {
        List<Ball> cBalls = new LinkedList<Ball>(balls);
        ArrayList<List<Ball>> collisions = new ArrayList<List<Ball>>();
        
        boolean collisionsDetected = false;
        while (!cBalls.isEmpty()) {
            Ball b = cBalls.get(0);
            Ball bCol = intersects(b);
            
            if (bCol != null) {
                ArrayList<Ball> c = new ArrayList<Ball>(2);
                c.add(b); c.add(bCol);
                collisions.add(c);
                cBalls.remove(bCol);
                collisionsDetected = true;
            }
            cBalls.remove(0);
        }
        return collisionsDetected ? collisions : null;
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
                //separate balls
                Ball b1 = collision.get(0); Ball b2 = collision.get(1);
                
                b1.vx *= -1; b1.vy *= -1;
                b2.vx *= -1; b2.vy *= -1;
                double theta1 = Math.atan2(b2.y - b1.y, b2.x - b1.x) * 180 / Math.PI;
                double theta2 = Math.atan2(b1.y - b2.y, b1.x - b2.x) * 180 / Math.PI;
                
                // TODO initial speed vector, using some kind of rectToPolar 
                double u1 = 10; double u2 = 12;
                double m1 = 2.5/*b1.mass*/; double m2 = 5.7/*b2.mass*/;
                                
                double I = m1*u1 + m2*u2;
                double R = -1*(u2-u1);
                
                double v1 = (u1*(m1-m2) + 2*m2*u2)/(m1+m2);
                double v2 = (u2*(m2-m1) + 2*m1*u1)/(m1+m2);
                
                System.out.println("V1="+v1+" V2="+v2);
                System.out.println((new Date())+": "+Math.atan2(b2.y - b1.y, b2.x - b1.x) * 180 / Math.PI);//double angle = ;
                //math, send in different directions
            }
        }
        
        // TODO do math here
        for (Ball b : balls) {
            //Gravity
            //System.out.println("X="+b.x+", Y="+b.y+", VX="+b.vx+", VY="+b.vy);
            b.vy += -GRAVITY_ACC;
            //No change in x axis
            b.vx += 0;
            
            if (b.x < b.radius || b.x > areaWidth - b.radius) {
                b.vx *= -1;
                if (b.x < b.radius) {
                    b.x = b.radius;
                } else {
                    b.x = areaWidth - b.radius;
                }
            }
            if (b.y < b.radius /*|| b.y > areaHeight - b.radius*/) {
                b.vy *= -1;
                if (b.y < b.radius) {
                    b.y = b.radius;
                } else {
                    b.y = areaHeight - b.radius;
                }
            }
            
            b.y += b.vy*deltaT;
            b.x += b.vx*deltaT;
            
            
            
            /**System.out.print("X="+b.x+", Y="+b.y+", VX="+b.vx+", VY="+b.vy+" || ");
            double air_friction = 1;
            double x_prev = b.x;
            double y_prev = b.y;
            double vx_prev = b.vx;
            double vy_prev = b.vy;
            
            double ax = -air_friction*vx_prev*vx_prev;
            double ay = -air_friction*vy_prev*vy_prev - GRAVITY_ACC;

            // Update velocity
            b.vx = vx_prev + ax*deltaT;
            b.vy = vy_prev + ay*deltaT;

            // Update position
            b.x = x_prev + vx_prev*deltaT + 0.5*ax*deltaT*deltaT;
            b.y = y_prev + vy_prev*deltaT + 0.5*ay*deltaT*deltaT;
            
            System.out.println("X="+b.x+", Y="+b.y+", VX="+b.vx+", VY="+b.vy);
            if (b.x < b.radius || b.x > areaWidth - b.radius) {
                b.vx *= -1;
                if (b.x < b.radius) {
                    b.x = b.radius;
                } else {
                    b.x = areaWidth - b.radius;
                }
            }
            if (b.y < b.radius || b.y > areaHeight - b.radius) {
                b.vy *= -1;
                if (b.y < b.radius) {
                    b.y = b.radius;
                } else {
                    b.y = areaHeight - b.radius;
                } 
            }*/
            /*if (b.x - b.radius < 0) {
                System.out.println("Hit left wall");
                b.x = b.radius;              // Place ball against edge
                b.vx = -(b.vx * air_friction);
            }
            else if (b.x + b.radius > areaWidth) // Right Wall?
            {
                System.out.println("Hit right wall");
                b.x = areaWidth - b.radius;     // Place ball against edge
                b.vx = -(b.vx * air_friction);
            }
            
            if (b.y - b.radius < 0)              // Bottom Wall?
            {
                System.out.println("Hit bottom wall");
                b.y = b.radius;              // Place ball against edge
                b.vy = -(b.vy * air_friction);
            }
            else if (b.y + b.radius > areaHeight) // Top Wall?
            {
                System.out.println("Hit top wall");
                b.y = areaHeight - b.radius;     // Place ball against edge
                b.vy = -(b.vy * air_friction);
            }

            
            
            //b.x += b.vx * deltaT + GRAVITY_ACC * 0.5 * deltaT * deltaT;
            //b.vx += GRAVITY_ACC * deltaT;

            //b.y += b.vy * deltaT + GRAVITY_ACC * 0.5 * deltaT * deltaT;
            //b.vy += GRAVITY_ACC * deltaT;
            b.x += b.vx * deltaT;
            b.y += b.vy * deltaT;*/
        }
    }
    
    public boolean isCollisionHighlighting() {
        return collisionHighlighting;
    }

    public void setCollisionHighlighting(boolean collisionHighlighting) {
        this.collisionHighlighting = collisionHighlighting;
    }
    
    public void clearBalls() {
        balls.clear();
    }

}
