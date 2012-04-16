import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AddBallKeyListener implements KeyListener {
    /** The model to add the balls to */
    private GravityModel model;
    /** Minimum time between adding balls, in milliseconds */
    private static final long ADD_BALL_LIMIT_MS = 1000;
    /** The time of the previously added ball */
    private long lastBallTime = System.currentTimeMillis() - ADD_BALL_LIMIT_MS;

    public AddBallKeyListener(GravityModel model) {
        this.model = model;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        long currTime = System.currentTimeMillis();
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE
                && currTime > (lastBallTime + ADD_BALL_LIMIT_MS)) {
            lastBallTime = currTime;
            model.addBall();
        } else if (key == KeyEvent.VK_C) {
            model.setCollisionHighlighting(!model.isCollisionHighlighting());
        } else if (key == KeyEvent.VK_DELETE) {
            model.clearBalls();
        }
    }

    // Unused overridden methods
    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

}
