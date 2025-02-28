package mqtt;

import org.json.JSONObject;

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

            try {
                // Parse the JSON string
                JSONObject json = new JSONObject(Subscriber.line);

                // Extract eye gaze data
                JSONObject leftEyeGaze = json.getJSONObject("leftEyeGaze");
                JSONObject rightEyeGaze = json.getJSONObject("rightEyeGaze");

                float eyeX = (float) leftEyeGaze.getDouble("x");
                float eyeY = (float) leftEyeGaze.getDouble("y");

                // Extract head movement data
                JSONObject head = json.getJSONObject("head");
                float headX = (float) head.getDouble("x");
                float headY = (float) head.getDouble("y");

                // Extract arm movement
                boolean leftHandRaised = json.getBoolean("leftHandRaised");
                boolean rightHandRaised = json.getBoolean("rightHandRaised");

                // Extract posture
                boolean leaningForward = json.getBoolean("leanForward");
                boolean standingStraight = json.getBoolean("standUpStraight");

                // Move the circle based on gaze
                float moveX = 0, moveY = 0;
                if (headX < -0.5) moveX = -0.1f;  // Look left
                if (headX > 0.5) moveX = 0.1f;   // Look right
                if (headY > 0.5) moveY = 0.1f;   // Look up
                if (headY < -0.5) moveY = -0.1f; // Look down

                // Change color based on hand raise
                Color newColor = circlePanel.getColor();
                if (leftHandRaised) newColor = Color.RED;   // Raise left hand -> Red
                if (rightHandRaised) newColor = Color.BLUE; // Raise right hand -> Blue

                // Change size based on posture
                float newSize = circlePanel.getSizeFactor();
                if (leaningForward) newSize = Math.max(0.5f, newSize - 0.1f); // Shrink
                if (standingStraight) newSize = Math.min(2.0f, newSize + 0.1f); // Expand

                // Update the circle properties
                circlePanel.updateCircle(moveX, moveY, newColor, newSize);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Update the circle's position using normalized coordinates
    private static class CirclePanel extends JPanel {
        private float normalizedX = 0, normalizedY = 0; // -1 to 1
        private float sizeFactor = 1.0f; // Scale factor for size
        private Color circleColor = Color.BLUE; // Default color
        private static final int BASE_DIAMETER = 50;

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
            int circleX = centerX + (int) (normalizedX * (width / 2)) - (int)(BASE_DIAMETER * sizeFactor) / 2;
            int circleY = centerY - (int) (normalizedY * (height / 2)) - (int)(BASE_DIAMETER * sizeFactor) / 2;

            g.setColor(circleColor);
            g.fillOval(circleX, circleY, (int)(BASE_DIAMETER * sizeFactor), (int)(BASE_DIAMETER * sizeFactor));
        }

        public void updateCircle(float deltaX, float deltaY, Color color, float size) {
            this.normalizedX = Math.max(-1, Math.min(1, normalizedX + deltaX));
            this.normalizedY = Math.max(-1, Math.min(1, normalizedY + deltaY));
            this.circleColor = color;
            this.sizeFactor = size;
            repaint();
        }

        public Color getColor() {
            return circleColor;
        }

        public float getSizeFactor() {
            return sizeFactor;
        }
    }

}
