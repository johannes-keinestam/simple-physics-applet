import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.List;

/**
 * Extends Animator with capability to draw a bouncing balls model.
 * 
 * This class can be left unmodified for the bouncing balls lab. :)
 * 
 * @author Oscar Soderlund
 * 
 */
@SuppressWarnings("serial")
public final class BouncingBalls extends Animator {

    private static final double PIXELS_PER_METER = 30;

    private IBouncingBallsModel model;
    private double modelHeight;
    private double deltaT;

    @Override
    public void init() {
        super.init();
        double modelWidth = canvasWidth / PIXELS_PER_METER;
        modelHeight = canvasHeight / PIXELS_PER_METER;
        // model = new DummyModel(modelWidth, modelHeight);
        model = new GravityModel(modelWidth, modelHeight);
        canvas.addKeyListener(new AddBallKeyListener(model));
    }

    @Override
    protected void drawFrame(Graphics2D g) {
        // Clear the canvas
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        // Update the model
        model.tick(deltaT);
        List<Ellipse2D> balls = model.getBalls();
        // Transform balls to fit canvas
        g.setColor(Color.GREEN);
        g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
        g.translate(0, -modelHeight);
        
        if (balls.size() == 2) {
            Area a1 = new Area(balls.get(0));
            Area a2 = new Area(balls.get(1));
            a1.intersect(a2);
            if (!a1.isEmpty()) {
                g.setColor(Color.RED);
            }
        }
        
        for (Ellipse2D b : balls) {
            g.fill(b);
        }
    }

    @Override
    protected void setFrameRate(double fps) {
        super.setFrameRate(fps);
        // Update deltaT according to new frame rate
        deltaT = 1 / fps;
    }

}
