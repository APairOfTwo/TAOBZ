import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JOptionPane;


public class SaveGame {
	static FileWriter txtWriter;
	static BufferedWriter saveOut;
	static int mapId;
	static int gameMode = 0;
	static boolean showSaveDialog;
	
	public static void save() {
		if(GamePanel.isCoop) {
			gameMode = 1;
		} else {
			if(GamePanel.selectedBilly) {
				gameMode = 2;
			}
			if(GamePanel.selectedZombie) {
				gameMode = 3;
			}
		}
		int option = JOptionPane.showConfirmDialog(null, "Deseja salvar?");
		switch(option) {
		case JOptionPane.YES_OPTION:
			try {
				txtWriter = new FileWriter("system\\SAVE.txt");
			    saveOut = new BufferedWriter(txtWriter);
			    mapId = GamePanel.levelId;
			    saveOut.write(Integer.toString(mapId)+"\n");
			    saveOut.write(Integer.toString(gameMode));
			    saveOut.close();
			} catch (Exception e) {
				System.err.println("Erro: " + e.getMessage());
			}
			showSaveDialog = false;
			break;

		case JOptionPane.NO_OPTION:
			showSaveDialog = false;
			break;
			
		default:
			showSaveDialog = false;
			break;
		}
	}
}