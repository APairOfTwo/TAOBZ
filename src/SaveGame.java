import java.io.BufferedWriter;
import java.io.FileWriter;

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
		try {
			txtWriter = new FileWriter("system\\SAVE.txt");
		    saveOut = new BufferedWriter(txtWriter);
		    mapId = GamePanel.levelId;
		    saveOut.write(Integer.toString(mapId)+"\n");
		    saveOut.write(Integer.toString(gameMode));
		    saveOut.close();
		} catch (Exception e) {
				System.err.println("Cannot save the game: " + e.getMessage());
		}
	}
}