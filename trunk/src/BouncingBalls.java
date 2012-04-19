import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

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
public final class BouncingBalls extends Animator implements MouseListener {

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
        ballAddPanel.setModel(model);
        canvas.addKeyListener(new AddBallKeyListener(model));
        canvas.addMouseListener(this);
        
        /*JOptionPane.showMessageDialog(this.getContentPane(), 
                "<html>" +
                "Controls:<br><br>" +
                "Add new ball - SPACEBAR<br>" +
                "Toggle collision highlighting - C<br>" +
                "Clear all balls - DELETE<br>" +
                "</html>");*/
    }

    @Override
    protected void drawFrame(Graphics2D g) {
        // Clear the canvas
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        // Update the model
        model.tick(deltaT);
        // Transform balls to fit canvas
        g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
        g.translate(0, -modelHeight);

        List<Ball> balls = Collections.synchronizedList(model.getBalls());;        
        try {
            for (Ball b : balls) {
                if (model.isCollisionHighlighting() && model.intersects(b) != null) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(b.color);
                }
                g.fill(b.getEllipse());
                g.setColor(Color.GREEN);
            }   
        } catch (ConcurrentModificationException cme) {
            // Thread crash, doesn't really matter
            System.err.println("Concurrent modification!");
        }
    }

    @Override
    protected void setFrameRate(double fps) {
        super.setFrameRate(fps);
        // Update deltaT according to new frame rate
        deltaT = 1 / fps;
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        ballAddPanel.setCoordinates(e.getX()/PIXELS_PER_METER, e.getY()/PIXELS_PER_METER);
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

}
