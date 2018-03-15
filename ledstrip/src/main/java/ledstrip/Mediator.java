package ledstrip;

final class Mediator {
	private static Serial serial;
	private static Mqtt mqtt;
	private static boolean stop = false;
	
	public static Mqtt getMqtt() {
		return mqtt;
	}
	
	public static void setMqtt(Mqtt mqtt) {
		Mediator.mqtt = mqtt;
	}
	
	public static Serial getSerial() {
		return serial;
	}
	
	public static void setSerial(Serial serial) {
		Mediator.serial = serial;
	}
	
	public static boolean stopped(){
		return Mediator.stop;
	}
}
