import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class CanvasGameMode extends Canvas {

	public static CanvasGameMode instance = null;
	private BufferedImage background;
	private GameButton btn1P, btn2P, btnBack;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	
	public CanvasGameMode() {
		instance = this;
		background = GamePanel.loadImage("backgrounds/mainMenu_background.png");
		btn1P = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 0, "buttons/btn1POn.png", "buttons/btn1POff.png");
		btn2P = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 100, "buttons/btn2POn.png", "buttons/btn2POff.png");
		btnBack = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 200, "buttons/btnBackOn.png", "buttons/btnBackOff.png");
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btn1P.isMouseOver(MOUSE_X, MOUSE_Y)){ btn1P.setState(1); }
		else { btn1P.setState(0); }
		
		if(btn2P.isMouseOver(MOUSE_X, MOUSE_Y)){ btn2P.setState(1); }
		else { btn2P.setState(0); }
		
		if(btnBack.isMouseOver(MOUSE_X, MOUSE_Y)){ btnBack.setState(1); }
		else { btnBack.setState(0); }
		
		if(MOUSE_PRESSED && btn1P.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.canvasActive = new CanvasCharSelect();
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btn2P.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.canvasActive = new CanvasGame();
			GamePanel.isCoop = true;
			CanvasGame.heroes.add(CanvasGame.billy);
			CanvasGame.heroes.add(CanvasGame.zombie);
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnBack.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.canvasActive = new CanvasMainMenu();
			MOUSE_PRESSED = false;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.drawImage(background, 0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT, 0, 0, background.getWidth(), background.getHeight(), null);
		btn1P.selfDraws(dbg);
		btn2P.selfDraws(dbg);
		btnBack.selfDraws(dbg);
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
