import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JOptionPane;


public class SaveGame {
	static FileWriter txtWriter;
	static BufferedWriter saveOut;
	static int posX;
	static boolean showSaveDialog;
	
	public static void save() {
		int option = JOptionPane.showConfirmDialog(null, "Deseja salvar?");
		
		switch(option) {
		case JOptionPane.YES_OPTION:
			try {
				txtWriter = new FileWriter("Save.txt");
			    saveOut = new BufferedWriter(txtWriter);
			    posX = (int)CanvasGame.billy.x;
			    saveOut.write(Integer.toString(posX));
			    saveOut.close();
			} catch (Exception e) {
				System.err.println("Erro: " + e.getMessage());
			}
			GamePanel.running = false;
			showSaveDialog = false;
			break;

		case JOptionPane.NO_OPTION:
			GamePanel.running = false;
			showSaveDialog = false;
			break;
			
		default:
			showSaveDialog = false;
			break;
		}
	}
}