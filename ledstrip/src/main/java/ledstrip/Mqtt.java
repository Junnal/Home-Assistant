package ledstrip;

import java.util.concurrent.TimeUnit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Mqtt{

	final private int QOS				= 0;
	final private String BROKER			= "tcp://localhost:1883";
	final private String CLIENT_ID		= "ArdulinkMQTT";
	final private String TOPIC_HEADER	= "houlouhome/mqttstrip/";
	final private String FILENAME = "data/password.txt";
	
	MemoryPersistence persistence;
	MqttClient client;
	MqttConnectOptions connOpts;

	public Mqtt(){
		String password = "";
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String pw;
			if((pw = br.readLine()) != null) password = pw;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		persistence = new MemoryPersistence();
		
		try {
			client = new MqttClient(BROKER, CLIENT_ID, persistence);
		} catch (MqttException me) {
			printException(me);
		}
		
		client.setCallback( new MqttCallbackImplementation() );
		connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(CLIENT_ID);
		connOpts.setPassword(password.toCharArray());
	}

	public void connect(){
		while(!client.isConnected()){
			System.out.println("Connecting to MQTT broker: " + BROKER);
			
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
			System.out.println("Publishing MQTT message:\n\t" + message);
			MqttMessage mqttMessage = new MqttMessage(message.getBytes());

			mqttMessage.setQos(QOS);

			try {
				client.publish(topic, mqttMessage);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException me) {
				me.printStackTrace();
			}
			
			System.out.println("MQTT message published"   + '\n');
		}
		else System.out.println("Can't publish MQTT message: client disconnected");
	}
	
	public void subscribeAll(){
		for (SubTopics subTopic : SubTopics.values()) {
			subscribe(subTopic.toString());
		}
	}
	
	public void subscribe(String topic){
		try {
			client.subscribe(TOPIC_HEADER + "set" + topic);
			System.out.println("Subscribed to " + topic);
		} catch (MqttException me) {
			printException(me);
		}
	}
	
	public void processMessage(SubTopics subTopic, String toTranslate) {
		boolean isValid = false;
		String message = "";
		
		switch(subTopic){
			case power:
				if(toTranslate.equals("ON")){
					message = "1";
					isValid = true;
				}
				else if(toTranslate.equals("OFF")){
					message = "0";
					isValid = true;
				}
				break;
				
			case rgb:
				String[] colors = toTranslate.split(",");
				boolean inRange = true;
				for(String color : colors){
					int value = Integer.parseInt(color);
					if(value < 0 || value > 255) inRange = false;
				}
				if(colors.length == 3 && inRange){
					message = "C";
					for(String color : colors) message += "_" + color;
					isValid = true;
				}
				break;
			
			case brightness:
				int value = Integer.parseInt(toTranslate);
				if(value <= 255 && value >= 0) {
					isValid = true;
					message = "B_" + toTranslate;
				}
				break;
				
			case mode:
				for(Mode mode : Mode.values()){
					if(toTranslate.equals(mode.name())){
						isValid = true;
						message = mode.toSerial();
					}
				}
				break;
				
			default:
				break;
		}
		
		if(isValid) Mediator.getSerial().sendMessage(message);
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
