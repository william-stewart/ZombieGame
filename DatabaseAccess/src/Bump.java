
public class Bump {
	String userIDOne;
	String userIDTwo;
	String longitude;
	String latitude;
	String timeStamp;
	
	public Bump(String individualLine){
		String[] splitFields = individualLine.split(",");
		userIDOne = splitFields[0];
		userIDTwo = splitFields[1];
		longitude = splitFields[2];
		latitude = splitFields[3];
		timeStamp = splitFields[4];
	}
	
}
