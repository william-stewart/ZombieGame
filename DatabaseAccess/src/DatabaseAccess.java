import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


import com.mysql.jdbc.ResultSetMetaData;


/*
 * Establishes a connection with a mySQL, ZombieB database in order to query that database.
 * This database holds all of the data for the Zombie game. You need to have MySQL connector jar
 * file in the lib folder in order for this class to connect to a MySQL database.
 */
public class DatabaseAccess {
	private Connection con = null;
	private Statement state = null;
	
	BumpTrack bump = new BumpTrack();
	
	
	//Updates database with instance of new virus transmission
	private String updateQuery = "INSERT INTO ZombieB (UserID,Longitude,Latitude,Time) VALUES ( "; 
	
	//Database access information
	final private String url="jdbc:mysql://cs.furman.edu:3306/wstewart";
	final private String user = "wstewart";
	final private String password = "William0416";
	
	/*
	 * Queries database in order to update it
	 * @input: String[] 
	 * ex: String[] dataValues = {userID,Longitude,Latitude,Time}
	 * @output: none
	 */
	public  void writeToDatabase(String[] dataValues) throws Exception{
		String userID = dataValues[0];
		String Longitude = dataValues[1];
		String Latitude = dataValues[2];
		String Time = dataValues[3];
		
		//Concatenate new inputed information to query string
		updateQuery = updateQuery + "'" + userID + "','" + Longitude + "','" + Latitude + "','" + Time + "');";
		
		try{
		//Connect to database
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con=DriverManager.getConnection(url,user,password);
		
		//Execute query
		state = con.createStatement();
		state.executeUpdate(updateQuery);
		}
		catch(Exception e){
			System.out.println(e);
			throw e;
		}
	}
	
	/*
	 * Pulls all data from the ZombieB database and creates a two dimensional array
	 * of that data.
	 * @input: none
	 * @output: none
	 */
	public void retrieveData() throws Exception{
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con=DriverManager.getConnection(url,user,password);
		
		String retrieveQuery = "SELECT * FROM ZombieB";
		
		//Execute query
		state = con.createStatement();
		ResultSet r1 = state.executeQuery(retrieveQuery);
		
		ResultSetMetaData rsmd = (ResultSetMetaData) r1.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		int rowsNumber = 0;
		//Counts rows
		while(r1.next()){++rowsNumber;}
		
		String[][] dataTable = new String[columnsNumber][rowsNumber];
		int rowCounter = 0;
		r1.beforeFirst();
		//Save data to two dimensional array
		while (r1.next()) {
		    for (int i = 1; i <= columnsNumber; i++) {
		        if (i > 1) ;
		        dataTable[i-1][rowCounter]=r1.getString(i);
		    }
		    rowCounter++;
		}
		bump.bumperTracker(dataTable);
		
		
	}
	
	public static void main(String[] args){
		DatabaseAccess db = new DatabaseAccess();
		Disease disease = new Disease();
		try{
			//pull data from database
			db.retrieveData();
			//calculate bumps and transmissions
			disease.transmitDisease();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		

	}

}