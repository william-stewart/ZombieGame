import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

/*
 * Will pull data from ZombieB database and check each instance of tracking to see if there are
 * Bumps. A Bump is defined as two users interacting with one another given a certain set of parameters.
 * A Bump will have a certain probability of transmitting the disease.
 */
public class BumpTrack {
	
	private final int timeRange = 60;
	private final double designatedProximity = 100;
	
	/*
	 * Checks for bumps and writes the bumps to the txt file
	 * @input: String[][] two dimensional array of strings 
	 * @output: none
	 */
	public void bumperTracker(String[][] data){
		String userIDOne, userIDNext, longitude, longitudeNext, latitude, latitudeNext, time, timeNext;
		try{
			for(int i = 0;i<data[0].length-1;i++){
				if(i<data[0].length-2){
					userIDOne=data[0][i];
					userIDNext=data[0][i+1];
					longitude=data[1][i];
					longitudeNext=data[1][i+1];
					latitude=data[2][i];
					latitudeNext=data[2][i+1];
					time=data[3][i];
					timeNext=data[3][i+1];
					if(withinTimeRange(time,timeNext)){
						if(checkBump(longitude,latitude,longitudeNext,latitudeNext)){
							writeBumpsToTxt(userIDOne,userIDNext,longitude,latitude,time);
							writeUserIDToTxt(userIDOne);
							writeUserIDToTxt(userIDNext);
						}
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		}
	}
	
	/*
	 * Checks to see if two time ranges are within the timeRange variable
	 * @input: String timeOne, first time stamp
	 * @input: String timeTwo, second time stamp 
	 * @output: boolean true if is within time range
	 */
	public boolean withinTimeRange(String timeOne, String timeTwo){
		try{
		if(timeOne != "" && timeTwo != ""){
			double timeCheckOne = Double.parseDouble(timeOne);
			double timeCheckTwo = Double.parseDouble(timeTwo);
			if(timeCheckTwo-timeCheckOne<=timeRange){
				return true;
			}
		}
		}catch(Exception e){e.printStackTrace();}
		return false;
	}
	
	/*
	 * Checks whether or not two coordinates are within the distance set in designatedProximity
	 * @input: String lngOne, longitude of first user
	 * @input: String latOne, latitude of first user
	 * @input: String lngTwo, longitude of second user
	 * @input: String latTwo, latitude of second user
	 * @output: boolean, true if within the designated range
	 */
	public boolean checkLongLat(String lngOne,String latOne,String lngTwo,String latTwo){
		double longitudeOne = Double.parseDouble(lngOne);
		double latitudeOne = Double.parseDouble(latOne);
		double longitudeTwo = Double.parseDouble(lngTwo);
		double latitudeTwo = Double.parseDouble(latTwo);
		double actualDistance = coordinateCalculator(longitudeOne,latitudeOne,longitudeTwo,latitudeTwo);
		
		if(actualDistance<=designatedProximity){
				return true;
		}
		return false;
	}
	
	/*
	 * Writes the userID to a txt file if it is not already in the file
	 * @input:  String userID, the userID to be written to the file
	 * @output: none
	 */
	public void writeUserIDToTxt(String userID){
		Boolean isInFile = false;
		try{
		FileWriter fw = new FileWriter("/Users/williamstewart/Desktop/Disease/UserID",true);
		
		BufferedReader br = new BufferedReader(new FileReader("/Users/williamstewart/Desktop/Disease/UserID"));
		String line;
		while((line = br.readLine()) != null){
			if(line.contains(userID)){
				isInFile = true;
			}
		}
		if(!isInFile){
			fw.write(userID + ",false" + "\n");
		}
		fw.close();
		}
		catch(IOException e){e.printStackTrace();}
	}
	
	/*
	 * Writes every instance of a Bump to a txt file
	 * @input: String userOne
	 * @input: String userTwo
	 * @input: String lon
	 * @input: String lat
	 * @input: String timeStamp
	 * @output: none
	 */
	public void writeBumpsToTxt(String userOne,String userTwo,String lon,String lat,String timeStamp){
		if(!(userOne.equals(userTwo))){
			Boolean isInFile = false;
			try{
				FileWriter fw = new FileWriter("/Users/williamstewart/Desktop/Disease/Bumps",true);
				String appendedBump = userOne + "," + userTwo + "," + lon + "," + lat + "," + timeStamp;
				//for each line in file, check for inequality, write if not equal
				BufferedReader br = new BufferedReader(new FileReader("/Users/williamstewart/Desktop/Disease/Bumps"));
				String line;
				while((line = br.readLine()) != null){
					if(line.contains(appendedBump)){
						isInFile = true;
				
					}
				}
				if(!isInFile){
					fw.write(appendedBump + "\n");
				}
				fw.close();
			}
			catch(IOException e){System.out.println("Failed");}
		}
	}
	
	/*
	 * Compares two coordinates and returns the distance between the two
	 * @input: Double longitudeOne, longitude of user one
	 * @input: Double latitudeOne, latitude of user one
	 * @input: Double longitudeTwo, longitude of user two
	 * @input: Double latitudeTwo, latitude of user two
	 * @output: Double the distance between the two coordinates
	 */
	public double coordinateCalculator(double longitudeOne,double latitudeOne,double longitudeTwo,double latitudeTwo){
		int radiusOfEarth = 6371000;
		Double latDistance = toRad(latitudeTwo-latitudeOne);
		Double longDistance =toRad(longitudeTwo-longitudeOne);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
                Math.cos(toRad(latitudeOne)) * Math.cos(toRad(latitudeTwo)) * 
                Math.sin(longDistance / 2) * Math.sin(longDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distance = radiusOfEarth * c;

		return distance;
	}
	
	/*
	 * Converts input to radians
	 * @input:  Double val, a value to be converted to radians
	 * @output: double, value in radians
	 */
	private static double toRad(Double val){
		return val * Math.PI / 180;
		
	}
	
	/*
	 * Checks to see if two tracked locations are bumps
	 * @input:  String lngOne, longitude of user one
	 * @input: String latOne, latitude of user one
	 * @input: String lngTwo, longitude of user two
	 * @input: String latTWo, latitude of user two
	 * @output: boolean, true if it is a bump
	 */
	public boolean checkBump(String lngOne, String latOne, String lngTwo, String latTwo){
		checkLongLat(lngOne,latOne,lngTwo,latTwo);
		return true;
	}

}
