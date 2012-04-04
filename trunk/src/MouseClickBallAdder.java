import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MouseClickBallAdder implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent event) {
        System.out.println("X:" + event.getX() + ", Y:" + event.getY());
        //py = event.getY();
        //px = event.getX();

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        System.out.println("1");
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        System.out.println("2");
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        System.out.println("3");
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("4");
    }


}
