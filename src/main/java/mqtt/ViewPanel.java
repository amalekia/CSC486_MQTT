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

                // Use the average of both eyes for smoother movement
                float eyeX = (float) ((leftEyeGaze.getDouble("x") + rightEyeGaze.getDouble("x")) / 2.0);
                float eyeY = (float) ((leftEyeGaze.getDouble("y") + rightEyeGaze.getDouble("y")) / 2.0);

                // Extract hand gestures
                boolean leftHandRaised = json.getBoolean("leftHandRaised");
                boolean rightHandRaised = json.getBoolean("rightHandRaised");

                // Extract posture for size adjustments
                boolean leaningForward = json.getBoolean("leanForward");
                boolean standingStraight = json.getBoolean("standUpStraight");

                // Update color based on hand gestures
                Color newColor = circlePanel.getColor();
                if (leftHandRaised) newColor = Color.RED;   // Left hand raised -> Red
                if (rightHandRaised) newColor = Color.BLUE; // Right hand raised -> Blue

                // Update size based on posture
                float newSize = circlePanel.getSizeFactor();
                if (leaningForward) newSize = Math.max(0.5f, newSize - 0.1f); // Shrink
                if (standingStraight) newSize = Math.min(2.0f, newSize + 0.1f); // Expand

                // Move the circle using eye gaze data
                circlePanel.updateCircle(eyeX, eyeY, newColor, newSize);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    // Update the circle's position using normalized coordinates
    private static class CirclePanel extends JPanel {
        private float normalizedX = 0, normalizedY = 0; // Range: -1 to 1
        private float sizeFactor = 1.0f; // Default size scale
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

            // Convert normalized eye gaze (-1 to 1) into pixel coordinates
            int centerX = width / 2;
            int centerY = height / 2;
            int circleX = centerX + (int) (normalizedX * (width / 2)) - (int)(BASE_DIAMETER * sizeFactor) / 2;
            int circleY = centerY - (int) (normalizedY * (height / 2)) - (int)(BASE_DIAMETER * sizeFactor) / 2;

            g.setColor(circleColor);
            g.fillOval(circleX, circleY, (int)(BASE_DIAMETER * sizeFactor), (int)(BASE_DIAMETER * sizeFactor));
        }

        public void updateCircle(float eyeX, float eyeY, Color color, float size) {
            // Normalize eye gaze input (-1 to 1 range)
            this.normalizedX = Math.max(-1, Math.min(1, eyeX));
            this.normalizedY = Math.max(-1, Math.min(1, eyeY));
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
