import java.io.BufferedReader;
import java.io.FileReader;


public class LoadGame {
	static FileReader txtReader;
	static BufferedReader loadOut;
	static String str;
	static int posX;
	
	public static void load() {
		try {
		    // Create file
			txtReader = new FileReader("Save.txt");
			loadOut = new BufferedReader(txtReader);
			str = loadOut.readLine();
			posX = new Integer(str);
			CanvasGame.billy.x = posX;
		    
		    //Close the output stream
		    loadOut.close();
		   } catch (Exception e) {
		    System.err.println("Erro: " + e.getMessage());
		   }
	}
	
}
