import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class CanvasCharSelect extends Canvas {

	public static CanvasCharSelect instance = null;
	private BufferedImage background;
	private GameButton btnBilly, btnZombie, btnBack;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	
	public CanvasCharSelect() {
		instance = this;
		background = GamePanel.loadImage("backgrounds/charSelect_background.png");
		btnBilly = new GameButton(GamePanel.PANEL_WIDTH/2 - 250, GamePanel.PANEL_HEIGHT/2 - 150, "buttons/btnBillyOn.png", "buttons/btnBillyOff.png");
		btnZombie = new GameButton(GamePanel.PANEL_WIDTH/2 + 50, GamePanel.PANEL_HEIGHT/2 - 150, "buttons/btnZombieOn.png", "buttons/btnZombieOff.png");
		btnBack = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 150, "buttons/btnBackOn.png", "buttons/btnBackOff.png");
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btnBilly.isMouseOver(MOUSE_X, MOUSE_Y)){ btnBilly.setState(1); }
		else { btnBilly.setState(0); }
		
		if(btnZombie.isMouseOver(MOUSE_X, MOUSE_Y)){ btnZombie.setState(1); }
		else { btnZombie.setState(0); }
		
		if(btnBack.isMouseOver(MOUSE_X, MOUSE_Y)){ btnBack.setState(1); }
		else { btnBack.setState(0); }
		
		if(MOUSE_PRESSED && btnBilly.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.canvasActive = new CanvasGame();
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnZombie.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.canvasActive = new CanvasCharSelect();
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnBack.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.canvasActive = new CanvasGameMode();
			MOUSE_PRESSED = false;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.drawImage(background, 0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT, 0, 0, background.getWidth(), background.getHeight(), null);
		btnBilly.selfDraws(dbg);
		btnZombie.selfDraws(dbg);
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
