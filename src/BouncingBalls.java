import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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

    private GravityModel model;
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
        
        JOptionPane.showMessageDialog(this.getContentPane(), 
                "<html>" +
                "Controls:<br><br>" +
                "Add new ball - SPACEBAR<br>" +
                "Toggle collision highlighting - C<br>" +
                "Clear all balls - DELETE<br>" +
                "</html>");
    }

    @Override
    protected void drawFrame(Graphics2D g) {
        // Clear the canvas
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        // Update the model
        model.tick(deltaT);
        // Transform balls to fit canvas
        g.setColor(Color.GREEN);
        g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
        g.translate(0, -modelHeight);

        List<Ball> balls = Collections.synchronizedList(model.getBalls());;        
        for (Ball b : balls) {
            if (model.isCollisionHighlighting() && model.intersects(b)) {
                g.setColor(Color.RED);
            }
            g.fill(b.getEllipse());
            g.setColor(Color.GREEN);
        }
    }

    @Override
    protected void setFrameRate(double fps) {
        super.setFrameRate(fps);
        // Update deltaT according to new frame rate
        deltaT = 1 / fps;
    }

}
