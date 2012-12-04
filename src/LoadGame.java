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
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new TextFilter());
		
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				txtReader = new FileReader(file);
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
	}
	
	public static void loadLastState(int mapId, int gameMode) {
		if(gameMode == 1) {
			GamePanel.canvasActive = new CanvasGame(mapId);
			CanvasGame.setGameLevel(mapId);
			CanvasGame.heroes.add(CanvasGame.billy);
			CanvasGame.heroes.add(CanvasGame.zombie);
			GamePanel.isCoop = true;
		} else {
			if(gameMode == 2) {
				GamePanel.canvasActive = new CanvasGame(mapId);
				CanvasGame.setGameLevel(mapId);
				CanvasGame.heroes.add(CanvasGame.billy);
				GamePanel.isCoop = false;
			}
			if(gameMode == 3) {
				GamePanel.canvasActive = new CanvasGame(mapId);
				CanvasGame.setGameLevel(mapId);
				CanvasGame.heroes.add(CanvasGame.zombie);
				GamePanel.isCoop = false;
			}
		}
	}
}
