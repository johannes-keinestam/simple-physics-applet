import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSlider;
import javax.swing.SwingConstants;


@SuppressWarnings("serial")
public class BallAddPanel extends Panel implements ActionListener {
    private TextField x, y;
    private JSlider mass, radius, vx, vy;
    private Label error;
    private GravityModel model;
    
    public BallAddPanel() {
        model = null;
        setBackground(Color.ORANGE);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        add(new Label("x:"));
        x = new TextField("", 3);
        add(x);
        
        add(new Label("y:"));
        y = new TextField("", 3);
        add(y);
        
        add(new Label("vx:"));
        vx = new JSlider(SwingConstants.HORIZONTAL, -200, 200, 0);
        vx.setPreferredSize(new Dimension(100, 18));
        vx.setOpaque(false);
        add(vx);
        
        add(new Label("vy:"));
        vy = new JSlider(SwingConstants.HORIZONTAL, -200, 200, 0);
        vy.setPreferredSize(new Dimension(100, 18));
        vy.setOpaque(false);
        add(vy);
        
        add(new Label("mass:"));
        mass = new JSlider(SwingConstants.HORIZONTAL, 1, 50, 10);
        mass.setPreferredSize(new Dimension(100, 18));
        mass.setOpaque(false);
        add(mass);
        
        add(new Label("radius:"));
        radius = new JSlider(SwingConstants.HORIZONTAL, 3, 30, 10);
        radius.setPreferredSize(new Dimension(100, 18));
        radius.setOpaque(false);
        add(radius);
        
        Button addButton = new Button("Add ball!");
        addButton.addActionListener(this);
        add(addButton);
        
        error = new Label("Could not add ball!");
        error.setForeground(Color.RED);
        add(error);
        error.setVisible(false);
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
            double dvx = vx.getValue()/10.0;
            double dvy = vy.getValue()/10.0;
            double dmass = mass.getValue()/10.0;
            double dradius = radius.getValue()/10.0;
            
            Ball b = new Ball(dradius, dx, dy, dvx, dvy, dmass);
            boolean result = model.addBall(b);
            
            if (result) {
                error.setVisible(false);
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            error.setVisible(true);
            revalidate();
        }
    }
}
