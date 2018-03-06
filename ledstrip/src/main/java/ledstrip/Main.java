package ledstrip;

import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

	public static void main(String[] args) {
		Serial serial = new Serial();
		serial.serialTest();
		
		try {
			Mqtt mqtt = new Mqtt();
			mqtt.connect();
			mqtt.sendMessage("houlouhome/mqttstrip/getpower", "ON");
			
			TimeUnit.SECONDS.sleep(10);
			
			mqtt.sendMessage("houlouhome/mqttstrip/getpower", "OFF");
			
		} catch (MqttException me) {
			printException(me);
			
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		
		System.exit(0);
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
