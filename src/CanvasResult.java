import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CanvasResult extends Canvas {

	public static CanvasResult instance = null;
	private BufferedImage background;
	private BufferedImage savedMsg;
	private GameButton btnContinue, btnSave, btnRetry;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	int numberOfDeaths = 0;
	int numberOfProjectiles = 0;
	boolean saved = false;
	
	public CanvasResult() {
		instance = this;
		GamePanel.app.setCursor(Cursor.getDefaultCursor());
		background = GamePanel.loadImage("backgrounds/score_background.png");
		savedMsg = GamePanel.loadImage("buttons/btnSaved.png");
		btnContinue = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 50, "buttons/btnNextMapOn.png", "buttons/btnNextMapOff.png");
		btnSave = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 100, "buttons/btnSaveOn.png", "buttons/btnSaveOff.png");
		btnRetry = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 150, "buttons/btnRetryOn.png", "buttons/btnRetryOff.png");
		
		numberOfDeaths = CanvasGame.deathCounter;
		numberOfProjectiles = CanvasGame.projectilesCounter;
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btnContinue.isMouseOver(MOUSE_X, MOUSE_Y)){ btnContinue.setState(1); }
		else { btnContinue.setState(0); }
		
		if(!saved) {
			if(btnSave.isMouseOver(MOUSE_X, MOUSE_Y)){ btnSave.setState(1); }
			else { btnSave.setState(0); }
		}
		
		if(btnRetry.isMouseOver(MOUSE_X, MOUSE_Y)){ btnRetry.setState(1); }
		else { btnRetry.setState(0); }
		
		if(MOUSE_PRESSED && btnContinue.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			if(GamePanel.levelId >= 3) {
				GamePanel.canvasActive = new CanvasFinal();
			} else {
				GamePanel.levelId += 1;
				GamePanel.changeMap(GamePanel.levelId);
			}
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnSave.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			SaveGame.save();
			saved = true;
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnRetry.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			CanvasGame.deathCounter = 0;
			CanvasGame.projectilesCounter = 0;
			GamePanel.changeMap(GamePanel.levelId);
			MOUSE_PRESSED = false;
		}
		
		if(saved) {
			btnSave.setBtnOff(savedMsg);
			btnSave.setState(0);
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.drawImage(background, GamePanel.PANEL_WIDTH/2 - 300, GamePanel.PANEL_HEIGHT/2 - 250, GamePanel.PANEL_WIDTH/2 + 300, GamePanel.PANEL_HEIGHT/2 + 250, 0, 0, background.getWidth(), background.getHeight(), null);
		btnContinue.selfDraws(dbg);
		btnSave.selfDraws(dbg);
		btnRetry.selfDraws(dbg);
		dbg.drawString("Number of deaths: " + numberOfDeaths, 325, 200);
		dbg.drawString("Number of projectiles fired: " + numberOfProjectiles, 325, 250);
	}

	@Override
	public void keyPressed(KeyEvent k){ }

	@Override
	public void keyReleased(KeyEvent k){ }
	
	@Override
	public void mouseMoved(MouseEvent m){ 
		MOUSE_X = m.getX();
		MOUSE_Y = m.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e){ }
	
	@Override
	public void mouseReleased(MouseEvent e){
		MOUSE_PRESSED = false;
	}

	@Override
	public void mousePressed(MouseEvent m){
		MOUSE_PRESSED = true;
		MOUSE_CLICK_X = m.getX();
		MOUSE_CLICK_Y = m.getY();
	}
}