package mqtt;

import org.eclipse.paho.client.mqttv3.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is a simple MQTT subscriber that listens to a TOPIC.
 * The BROKER is test.mosquitto.org and the TOPIC is cal-poly/csc/309.
 * (run this and the publisher at the same time)
 *
 * NOTE: Change the file path to CSV before running
 *
 * @author javiergs Adrick Malekian Spencer Perley
 * @version 1.0
 */
public class Subscriber implements MqttCallback {
	public ArrayList<String> lines = new ArrayList<>();
	public int counter = 0;
	private final static String BROKER = "tcp://test.mosquitto.org:1883";
	private final static String TOPIC = "adr-spence";
	private final static String CLIENT_ID = "csv-subscriber";
	
	public static void main(String[] args) {

		try {
			MqttClient client = new MqttClient(BROKER, CLIENT_ID);
			Subscriber subscriber = new Subscriber();
			client.setCallback(subscriber);
			client.connect();
			System.out.println("Connected to BROKER: " + BROKER);
			client.subscribe(TOPIC);
			System.out.println("Subscribed to TOPIC: " + TOPIC);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void connectionLost(Throwable throwable) {
		System.out.println("Connection lost: " + throwable.getMessage());
	}
	
	@Override
	public void messageArrived(String s, MqttMessage mqttMessage) {
		System.out.println("Message arrived. Topic: " + s +
			" Message: " + new String(mqttMessage.getPayload()));
		lines.add(mqttMessage.toString());
		counter++;

		if (counter == 1000) {
			try (FileWriter writer = new FileWriter("/Users/adrickmalekian/Desktop/MQTT/src/main/java/javiergs/mqtt/new.csv")) {
				for (String line : lines) {
					writer.append(line);  // Write the string
					writer.append("\n");  // Add a new line after each string
				}
				System.out.println("CSV file created at: " + "/Users/adrickmalekian/Desktop/MQTT/src/main/java/javiergs/mqtt/new.csv");
			} catch (IOException e) {
				e.printStackTrace();
			}
			lines.clear();
		}
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		System.out.println("Delivered complete: " + iMqttDeliveryToken.getMessageId());
	}

}
