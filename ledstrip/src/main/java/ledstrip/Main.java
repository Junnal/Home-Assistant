package ledstrip;

import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

	public static void main(String[] args) {
		Mediator.setSerial(new Serial());
		
		try {
			Mediator.setMqtt(new Mqtt());
			Mediator.getMqtt().connect();
//			Mediator.getMqtt().sendMessage("houlouhome/mqttstrip/getpower", "ON");
//			
//			TimeUnit.SECONDS.sleep(30);
//			
//			Mediator.getMqtt().sendMessage("houlouhome/mqttstrip/getpower", "OFF");
//			Mediator.getMqtt().disconnect();
			
		} catch (MqttException me) {
			printException(me);
			
		}
		
		Mediator.getSerial().serialTest();
		
		loop();
		
		System.exit(0);
	}
	
	private static void loop(){
		while(!Mediator.stopped()){
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
