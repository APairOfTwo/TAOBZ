import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;


public class LoadGame {
	static FileReader txtReader;
	static BufferedReader loadOut;
	static String str;
	static int posX;
	
	public static void load() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new TextFilter());
		
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
			    // Create file
				txtReader = new FileReader(file);
				loadOut = new BufferedReader(txtReader);
				str = loadOut.readLine();
				posX = new Integer(str);
				System.out.println(posX);
			    
			    //Close the output stream
			    loadOut.close();
			   } catch (Exception e) {
			    System.err.println("Erro: " + e.getMessage());
			   }
		}
	}
}
