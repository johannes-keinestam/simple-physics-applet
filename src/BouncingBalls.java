import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public final class BouncingBalls extends Animator implements MouseListener {

    private static final double PIXELS_PER_METER = 30;

    private IBouncingBallsModel model;
    private double modelHeight;
    private double deltaT;
    private int px, py;

    @Override
    public void init() {
        super.init();
        addMouseListener(this);
        double modelWidth = canvasWidth / PIXELS_PER_METER;
        modelHeight = canvasHeight / PIXELS_PER_METER;
        model = new DummyModel(modelWidth, modelHeight);
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
        g.setColor(Color.RED);
        g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
        g.translate(0, -modelHeight);
        for (Ellipse2D b : balls) {
            g.fill(b);
        }
        if (px != 0 || py != 0) {
            g.fillRect(px, py, 2, 2);
            px = 0;
            py = 0;
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
		model.addBall();
	}

	/** Unused implemented methods */
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
