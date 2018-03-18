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

	public Mqtt(){
		persistence = new MemoryPersistence();
		
		try {
			client = new MqttClient(broker, clientId, persistence);
		} catch (MqttException me) {
			printException(me);
		}
		
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

	public void disconnect(){
		System.out.println("Disconnecting from MQTT broker...");
		try {
			client.disconnect();
		} catch (MqttException me) {
			me.printStackTrace();
		}
		System.out.println("Disconnected");
	}
	
	public void sendMessage(String topic, String message){
		if(client.isConnected()){
			System.out.println("Publishing MQTT message: "+message);
			MqttMessage mqttMessage = new MqttMessage(message.getBytes());

			mqttMessage.setQos(qos);

			try {
				client.publish(topic, mqttMessage);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException me) {
				me.printStackTrace();
			}
			
			System.out.println("MQTT message published");
		}
		else System.out.println("Can't publish MQTT message: client disconnected");
	}
	
	public void subscribeAll(){
		for (String topic : subTopics) {
			subscribe(topic);
		}
	}
	
	public void subscribe(String topic){
		try {
			client.subscribe(topic);
			System.out.println("Subscribed to " + topic);
		} catch (MqttException me) {
			printException(me);
		}
	}

	private static void printException(MqttException me){
		System.out.println("reason "+me.getReasonCode());
		System.out.println("msg "+me.getMessage());
		System.out.println("loc "+me.getLocalizedMessage());
		System.out.println("cause "+me.getCause());
		System.out.println("excep "+me);
		me.printStackTrace();
	}
}
