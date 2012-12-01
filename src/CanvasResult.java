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
	
	public CanvasResult() {
		instance = this;
		background = GamePanel.loadImage("backgrounds/score_background.png");
		btnContinue = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 100, "buttons/btnNextMapOn.png", "buttons/btnNextMapOff.png");
		btnRetry = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 150, "buttons/btnRetryOn.png", "buttons/btnRetryOff.png");
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btnContinue.isMouseOver(MOUSE_X, MOUSE_Y)){ btnContinue.setState(1); }
		else { btnContinue.setState(0); }
		
		if(btnRetry.isMouseOver(MOUSE_X, MOUSE_Y)){ btnRetry.setState(1); }
		else { btnRetry.setState(0); }
		
		if(MOUSE_PRESSED && btnContinue.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.levelId += 1;
			changeMap(GamePanel.levelId);
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnRetry.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			changeMap(GamePanel.levelId);
			MOUSE_PRESSED = false;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.drawImage(background, GamePanel.PANEL_WIDTH/2 - 300, GamePanel.PANEL_HEIGHT/2 - 250, GamePanel.PANEL_WIDTH/2 + 300, GamePanel.PANEL_HEIGHT/2 + 250, 0, 0, background.getWidth(), background.getHeight(), null);
		btnContinue.selfDraws(dbg);
		btnRetry.selfDraws(dbg);
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_ESCAPE){
			changeMap(GamePanel.levelId);
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
	
	public void changeMap(int levelId) {
		GamePanel.canvasActive = new CanvasGame(levelId);
		CanvasGame.setGameLevel(levelId);
		CanvasGame.resetControls();
		if(GamePanel.isCoop) {
			CanvasGame.heroes.add(CanvasGame.billy);
			CanvasGame.heroes.add(CanvasGame.zombie);
		} else {
			if(GamePanel.selectedBilly) {
				CanvasGame.heroes.add(CanvasGame.billy);
			}
			if(GamePanel.selectedZombie) {
				CanvasGame.heroes.add(CanvasGame.zombie);
			}
		}
	}
}