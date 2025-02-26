package mqtt;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class ViewPanel extends JPanel implements PropertyChangeListener {

    private JTextArea textArea;
    private CirclePanel circlePanel;  // Panel for the circle

    public ViewPanel() {
        setLayout(new BorderLayout());

        // Left Panel (Text Area)
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.WEST);

        // Center Panel (Circle)
        circlePanel = new CirclePanel();
        add(circlePanel, BorderLayout.CENTER);
    }

    // Handle property change events
    public void propertyChange(PropertyChangeEvent evt) {
        if (Subscriber.line != null) {
            textArea.append(Subscriber.line + "\n");
            String[] words = Subscriber.line.split(", ");

            // Convert normalized values (-1 to 1) to relative positions
            float x = Float.parseFloat(words[26]);
            float y = Float.parseFloat(words[27]);
            updateCircle(x, y);
        }
    }

    // Update the circle's position using normalized coordinates
    public void updateCircle(float x, float y) {
        circlePanel.updatePosition(x, y);
    }

    // Inner class for the circle panel
    private static class CirclePanel extends JPanel {
        private float normalizedX = 0, normalizedY = 0; // -1 to 1
        private static final int DIAMETER = 50;

        public CirclePanel() {
            setPreferredSize(new Dimension(400, 400));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();

            // Convert normalized (-1 to 1) to pixel coordinates
            int centerX = width / 2;
            int centerY = height / 2;
            int circleX = centerX + (int) (normalizedX * (width / 2)) - DIAMETER / 2;
            int circleY = centerY - (int) (normalizedY * (height / 2)) - DIAMETER / 2;

            g.setColor(Color.BLUE);
            g.fillOval(circleX, circleY, DIAMETER, DIAMETER);
        }

        // Method to update circle position using normalized (-1 to 1) coordinates
        public void updatePosition(float x, float y) {
            this.normalizedX = Math.max(-1, Math.min(1, x)); // Clamp between -1 and 1
            this.normalizedY = Math.max(-1, Math.min(1, y));
            repaint(); // Redraw the panel
        }
    }
}
