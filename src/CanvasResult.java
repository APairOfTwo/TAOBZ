import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CanvasResult extends Canvas {

	public static CanvasResult instance = null;
	private BufferedImage background;
	private GameButton btnContinue, btnRetry;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	int numberOfDeaths = 0;
	int numberOfProjectiles = 0;
	
	public CanvasResult() {
		instance = this;
		GamePanel.app.setCursor(Cursor.getDefaultCursor());
		background = GamePanel.loadImage("backgrounds/score_background.png");
		btnContinue = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 100, "buttons/btnNextMapOn.png", "buttons/btnNextMapOff.png");
		btnRetry = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 150, "buttons/btnRetryOn.png", "buttons/btnRetryOff.png");
		
		numberOfDeaths = CanvasGame.deathCounter;
		numberOfProjectiles = CanvasGame.projectilesCounter;
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btnContinue.isMouseOver(MOUSE_X, MOUSE_Y)){ btnContinue.setState(1); }
		else { btnContinue.setState(0); }
		
		if(btnRetry.isMouseOver(MOUSE_X, MOUSE_Y)){ btnRetry.setState(1); }
		else { btnRetry.setState(0); }
		
		if(MOUSE_PRESSED && btnContinue.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.levelId += 1;
			GamePanel.changeMap(GamePanel.levelId);
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnRetry.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.changeMap(GamePanel.levelId);
			MOUSE_PRESSED = false;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.drawImage(background, GamePanel.PANEL_WIDTH/2 - 300, GamePanel.PANEL_HEIGHT/2 - 250, GamePanel.PANEL_WIDTH/2 + 300, GamePanel.PANEL_HEIGHT/2 + 250, 0, 0, background.getWidth(), background.getHeight(), null);
		btnContinue.selfDraws(dbg);
		btnRetry.selfDraws(dbg);
		dbg.drawString("Number of deaths: " + numberOfDeaths, 325, 200);
		dbg.drawString("Number of projectiles fired: " + numberOfProjectiles, 325, 250);
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_ESCAPE){
			GamePanel.changeMap(GamePanel.levelId);
		}
	}

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