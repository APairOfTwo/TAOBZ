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
	static int mapId;
	static int gameMode;
	static int counter;
	
	public static void load() {
		counter = 0;
		try {
			txtReader = new FileReader(new File("system/SAVE.txt"));
			loadOut = new BufferedReader(txtReader);
			while ((str = loadOut.readLine()) != null) {
				if(counter == 0) {
					mapId = new Integer(str);
					System.out.println(mapId);
				}
				if(counter == 1) {
					gameMode = new Integer(str);
					System.out.println(gameMode);
				}
				counter++;
			}
			loadOut.close();
			loadLastState(mapId, gameMode);
		} catch (Exception e) {
			System.err.println("Erro: " + e.getMessage());
		}
	}
	
	public static void loadLastState(int mapId, int gameMode) {
		GamePanel.canvasActive = new CanvasGame(mapId);
		CanvasGame.setGameLevel(mapId);
		GamePanel.levelId = mapId;
		if(gameMode == 1) {
			CanvasGame.heroes.add(CanvasGame.billy);
			CanvasGame.heroes.add(CanvasGame.zombie);
			GamePanel.isCoop = true;
		} else {
			if(gameMode == 2) {
				CanvasGame.heroes.add(CanvasGame.billy);
				GamePanel.isCoop = false;
			}
			if(gameMode == 3) {
				CanvasGame.heroes.add(CanvasGame.zombie);
				GamePanel.isCoop = false;
			}
		}
	}
}
