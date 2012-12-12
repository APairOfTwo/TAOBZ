import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class CanvasFinal extends Canvas {

	public static CanvasFinal instance = null;
	private BufferedImage background;
	private GameButton btnMainMenu, btnExit;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	
	public CanvasFinal() {
		instance = this;
		background = GamePanel.loadImage("backgrounds/final_background.png");
		btnMainMenu = new GameButton(GamePanel.PANEL_WIDTH/2 - 40, GamePanel.PANEL_HEIGHT/2 + 175, "buttons/btnMainMenuOn.png", "buttons/btnMainMenuOff.png");
		btnExit = new GameButton(GamePanel.PANEL_WIDTH/2 - 40, GamePanel.PANEL_HEIGHT/2 + 225, "buttons/btnExitOn.png", "buttons/btnExitOff.png");
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btnMainMenu.isMouseOver(MOUSE_X, MOUSE_Y)){ btnMainMenu.setState(1); }
		else { btnMainMenu.setState(0); }
		
		if(btnExit.isMouseOver(MOUSE_X, MOUSE_Y)){ btnExit.setState(1); }
		else { btnExit.setState(0); }
		
		if(MOUSE_PRESSED && btnMainMenu.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			CanvasGame.heroes.clear();
			CanvasGame.deathCounter = 0;
			CanvasGame.projectilesCounter = 0;
			GamePanel.canvasActive = new CanvasMainMenu();
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnExit.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			System.exit(0);
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.drawImage(background, 0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT, 0, 0, background.getWidth(), background.getHeight(), null);
		btnMainMenu.selfDraws(dbg);
		btnExit.selfDraws(dbg);
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
