import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Disease {
	float transmitPercentage = 1.00f;
	Random rand = new Random();
	ArrayList<Bump> bumpArray = new ArrayList<Bump>();
	
	/*
	 * Will transmit disease and write to local txt files the infections
	 * @input: none
	 * @output: none
	 */
	public void transmitDisease(){
		//Open up bump file
		//For each line, create a Bump object and add to BumpArray
			//Save userIDOne
			//Save userIDTwo
		createBumpArray();
		//Open UserID file to check if either are infected
		//If one is, call chanceOfTransmission
		for(Bump b: bumpArray){
			if(isInfected(b.userIDOne) && isInfected(b.userIDTwo)){
			}
			else if(isInfected(b.userIDOne) && isTransmission()){
					infect(b,0);
				}
				else if(isInfected(b.userIDTwo) && isTransmission()){
					infect(b,1);
				}
		}
	}
	
	/*
	 * Will write to Transmissions file and change the boolean from false to true for the appropriate user id 
	 * in the UserID file
	 * @input: Bump bump
	 * @input: int host, 0 if UserIDOne is the carrier of disease and 1 if UserIDTwo is the carrier
	 * @output: none
	 */
	public void infect(Bump bump, int host){
		//Open UserID file and change false to true for both
		//Add bump to transmission file
		String newText ="";
		ArrayList<String> finalText = new ArrayList<String>();
		try{
			FileWriter fw = new FileWriter("/Users/williamstewart/Desktop/Disease/Transmissions",true);
			BufferedReader br = new BufferedReader(new FileReader("/Users/williamstewart/Desktop/Disease/UserID"));

			fw.write(bump.userIDOne + "," + bump.userIDTwo + "," + bump.longitude + "," + bump.latitude + "," + bump.timeStamp + "\n");
			fw.close();
			br.close();
			}
		catch(Exception e){e.printStackTrace();}
		
		try{
			FileWriter fw = new FileWriter("/Users/williamstewart/Desktop/Disease/UserID",true);
			BufferedReader br = new BufferedReader(new FileReader("/Users/williamstewart/Desktop/Disease/UserID"));
			String line;
			ArrayList<String> textArray = new ArrayList<String>();
			while((line = br.readLine()) != null)
            {
				textArray.add(line);
            }
			br.close();
			
			for(String s: textArray){
				String[] infectedID = s.split(",");
				if(infectedID[0].contains(bump.userIDOne) && infectedID[1].contains("false")){
					String replace = bump.userIDOne + "," + "true" + "\n";
					finalText.add(replace);
				}
				else{
					s = s + "\n";
					finalText.add(s);}
			}
			
			PrintWriter writer = new PrintWriter("/Users/williamstewart/Desktop/Disease/UserID");
			writer.print("");
			writer.close();
				for(String str: finalText){
					fw.write(str);
				}
				System.out.println(newText);
			
			fw.close();
			br.close();
			}
		catch(Exception e){e.printStackTrace();}
	}
	
	
	/*
	 * Checks to see if a user is already infected
	 * @input: String userID
	 * @output: boolean true if infected
	 */
	public boolean isInfected(String userID){
		try{
			FileWriter fw = new FileWriter("/Users/williamstewart/Desktop/Disease/UserID",true);
			BufferedReader br = new BufferedReader(new FileReader("/Users/williamstewart/Desktop/Disease/UserID"));
			String line;
			while((line = br.readLine()) != null){
				String[] infectedID = line.split(",");
				if(infectedID[0].contains(userID) && infectedID[1].contains("true")){
					fw.close();
					br.close();
					return true;
				}
			}
			fw.close();
			br.close();
			}
			catch(IOException e){e.printStackTrace();}
		return false;
	}
	
	/*
	 * Calculates percent chance that a bump will result in transmission
	 * @input: none
	 * @output: boolean true if bump will result in transmission
	 */
	public boolean isTransmission(){
		float chance = rand.nextFloat();
		if(chance <= transmitPercentage){
			return true;
		}
		return false;
	}
	
	/*
	 * Creates array of bumps from the Bumps file
	 * @input: none
	 * @output: none
	 */
	public void createBumpArray(){
		try{
		FileWriter fw = new FileWriter("/Users/williamstewart/Desktop/Disease/Bumps",true);
		
		BufferedReader br = new BufferedReader(new FileReader("/Users/williamstewart/Desktop/Disease/Bumps"));
		String line;
		while((line = br.readLine()) != null){
			bumpArray.add(new Bump(line));
		}
		//fw.write("worked");
		fw.close();
		br.close();
		}
		catch(IOException e){System.out.println("Failed");}
	}
	
}
