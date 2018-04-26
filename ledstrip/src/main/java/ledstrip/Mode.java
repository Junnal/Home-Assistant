package ledstrip;

public enum Mode {
	chase("c"),
	normal("n"),
	stars("s"),
	wave("w");
	
	private String value;
	
	Mode(String value){
		this.value = value;
	}
	
	public String toSerial(){
		return value;
	}
}
