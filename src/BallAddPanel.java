import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class BallAddPanel extends Panel implements ActionListener {
    private TextField x, y, vx, vy, mass, radius;
    private Label error;
    private GravityModel model;
    
    public BallAddPanel() {
        model = null;
        setBackground(Color.ORANGE);
        setLayout(new FlowLayout());
        
        add(new Label("x:"));
        x = new TextField("", 3);
        add(x);
        
        add(new Label("y:"));
        y = new TextField("", 3);
        add(y);
        
        add(new Label("vx:"));
        vx = new TextField("", 3);
        add(vx);
        
        add(new Label("vy:"));
        vy = new TextField("", 3);
        add(vy);
        
        add(new Label("mass:"));
        mass = new TextField("", 3);
        add(mass);
        
        add(new Label("radius:"));
        radius = new TextField("", 3);
        add(radius);
        
        Button addButton = new Button("Add ball!");
        addButton.addActionListener(this);
        add(addButton);
        
        error = new Label("Could not add ball!");
        error.setForeground(Color.RED);
        error.setVisible(false);
        add(error);
    }
    
    public void setModel(GravityModel model) {
        this.model = model;
    }
    
    public void setCoordinates(double x, double y) {
        this.x.setText(""+x);
        this.y.setText(""+y);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (model == null) {
            return;
        }
        try {
            double dx = Double.parseDouble(x.getText());
            double dy = Double.parseDouble(y.getText());
            double dvx = Double.parseDouble(vx.getText());
            double dvy = Double.parseDouble(vy.getText());
            double dmass = Double.parseDouble(mass.getText());
            double dradius = Double.parseDouble(radius.getText());
            
            Ball b = new Ball(dradius, dx, dy, dvx, dvy, dmass);
            boolean result = model.addBall(b);
            
            if (result) {
                error.setVisible(false);
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            error.setVisible(true);
        }
    }
}
