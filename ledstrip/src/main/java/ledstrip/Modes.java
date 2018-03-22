package ledstrip;

public enum Modes {
	chase("c"),
	normal("n");
	
	private String value;
	
	Modes(String value){
		this.value = value;
	}
	
	public String toSerial(){
		return value;
	}
}
