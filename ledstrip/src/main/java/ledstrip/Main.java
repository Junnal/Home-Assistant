package ledstrip;

import java.io.IOException;

import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {

	public static void main(String[] args) {
		
		Link link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=COM3&baudrate=9600&pingprobe=false"));
		
		 try {
			link.sendCustomMessage("1");
		} catch (IOException e) {
			System.out.println("can't send custom message");
			e.printStackTrace();
		}
		 
		 String topic        = "houlouhome/";
         String content      = "Message from MqttPublishSample";
         int qos             = 2;
         String broker       = "tcp://localhost:1883";
         String clientId     = "ArdulinkMQTT";
         MemoryPersistence persistence = new MemoryPersistence();

         try {
             MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
             MqttConnectOptions connOpts = new MqttConnectOptions();
             connOpts.setCleanSession(true);
             System.out.println("Connecting to broker: "+broker);
             sampleClient.connect(connOpts);
             System.out.println("Connected");
             System.out.println("Publishing message: "+content);
             MqttMessage message = new MqttMessage(content.getBytes());
             message.setQos(qos);
             sampleClient.publish(topic, message);
             System.out.println("Message published");
             sampleClient.disconnect();
             System.out.println("Disconnected");
             System.exit(0);
         } catch(MqttException me) {
             System.out.println("reason "+me.getReasonCode());
             System.out.println("msg "+me.getMessage());
             System.out.println("loc "+me.getLocalizedMessage());
             System.out.println("cause "+me.getCause());
             System.out.println("excep "+me);
             me.printStackTrace();
         }
	}
	
}
