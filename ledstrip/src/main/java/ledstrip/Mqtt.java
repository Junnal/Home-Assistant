package ledstrip;

import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Mqtt{

	int qos             = 0;
	String broker       = "tcp://localhost:1883";
	String clientId     = "ArdulinkMQTT";
	String[] subTopics	= {"houlouhome/mqttstrip/setpower"};

	MemoryPersistence persistence;
	MqttClient client;
	MqttConnectOptions connOpts;

	public Mqtt() throws MqttException {
		persistence = new MemoryPersistence();
		client = new MqttClient(broker, clientId, persistence);
		client.setCallback( new MqttCallbackImplementation() );
		connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
	}

	public void connect(){
		while(!client.isConnected()){
			System.out.println("Connecting to MQTT broker: " + broker);
			
			try {
				client.connect(connOpts);
				
			} catch (MqttException me) {
				me.printStackTrace();
			}
			
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		System.out.println("Connected");
	}

	public void disconnect() throws MqttException{
		System.out.println("Disconnecting from MQTT broker...");
		client.disconnect();
		System.out.println("Disconnected");
	}
	
	public void sendMessage(String topic, String message) throws MqttException{
		System.out.println("Publishing MQTT message: "+message);
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());

		mqttMessage.setQos(qos);

		client.publish(topic, mqttMessage);
		System.out.println("MQTT message published");
	}
	
	public void subscribeAll() throws MqttException{
		for (String topic : subTopics) {
			subscribe(topic);
		}
	}
	
	public void subscribe(String topic) throws MqttException{
		client.subscribe(topic);
		System.out.println("Subscribed to " + topic);
	}


}
