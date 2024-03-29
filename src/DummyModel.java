import java.util.LinkedList;
import java.util.List;

public class DummyModel implements IBouncingBallsModel {

    private final double areaWidth;
    private final double areaHeight;

    private double x, y, vx, vy, r;

    public DummyModel(double width, double height) {
        this.areaWidth = width;
        this.areaHeight = height;
        x = 1;
        y = 1;
        vx = 2.3;
        vy = 1;
        r = 1;
    }

    @Override
    public void tick(double deltaT) {
        if (x < r || x > areaWidth - r) {
            vx *= -1;
        }
        if (y < r || y > areaHeight - r) {
            vy *= -1;
        }
        x += vx * deltaT;
        y += vy * deltaT;
    }

    @Override
    public List<Ball> getBalls() {
        List<Ball> balls = new LinkedList<Ball>();
        Ball b = new Ball(r, x, y, vx, vy, 1);
        balls.add(b);
        return balls;
    }

    @Override
    public boolean addBall() {
        return false;
    }
}
