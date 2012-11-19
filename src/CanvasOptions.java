import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class CanvasOptions extends Canvas {

	public static CanvasOptions instance = null;
	private BufferedImage background;
	private GameButton btnBack, btnMinus, btnPlus;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	private float volume = 1.0f;
	
	public CanvasOptions() {
		instance = this;
		volume = GamePanel.volume;
		background = GamePanel.loadImage("backgrounds/options_background.png");
		btnMinus = new GameButton(GamePanel.PANEL_WIDTH/2 - 100, GamePanel.PANEL_HEIGHT/2 - 80, "buttons/btnMinusOn.png", "buttons/btnMinusOff.png");
		btnPlus = new GameButton(GamePanel.PANEL_WIDTH/2 + 50, GamePanel.PANEL_HEIGHT/2 - 80, "buttons/btnPlusOn.png", "buttons/btnPlusOff.png");
		btnBack = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 200, "buttons/btnBackOn.png", "buttons/btnBackOff.png");
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btnMinus.isMouseOver(MOUSE_X, MOUSE_Y)){ btnMinus.setState(1); }
		else { btnMinus.setState(0); }
		if(btnPlus.isMouseOver(MOUSE_X, MOUSE_Y)){ btnPlus.setState(1); }
		else { btnPlus.setState(0); }
		if(btnBack.isMouseOver(MOUSE_X, MOUSE_Y)){ btnBack.setState(1); }
		else { btnBack.setState(0); }
		
		if(MOUSE_PRESSED && btnMinus.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			if(volume > 0.0f) {
				volume -= 0.1f;
				Audio.setVolume(volume);
			}
			MOUSE_PRESSED = false;
		}
		if(MOUSE_PRESSED && btnPlus.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			if(volume < 1.0f) {
				volume += 0.1f;
				Audio.setVolume(volume);
			}
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
		
		dbg.drawString("Volume:", GamePanel.PANEL_WIDTH/2 - 100, GamePanel.PANEL_HEIGHT/2 - 110);
		dbg.drawString("" + (int)(volume * 100.1f), GamePanel.PANEL_WIDTH/2 - 20, GamePanel.PANEL_HEIGHT/2 - 60);
		
		btnMinus.selfDraws(dbg);
		btnPlus.selfDraws(dbg);
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
