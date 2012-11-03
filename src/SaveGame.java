import java.io.BufferedWriter;
import java.io.FileWriter;


public class SaveGame {
	static FileWriter txtWriter;
	static BufferedWriter saveOut;
	static int posX;
	
	public static void save() {
		try {
		    // Create file
			txtWriter = new FileWriter("Save.txt");
		    saveOut = new BufferedWriter(txtWriter);
		    posX = (int)CanvasGame.billy.x;
		    saveOut.write(Integer.toString(posX));
		    
		    //Close the output stream
		    saveOut.close();
		   } catch (Exception e) {
		    System.err.println("Erro: " + e.getMessage());
		   }
	}
}