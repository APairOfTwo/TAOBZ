import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class CanvasMenu extends Canvas {

	public static CanvasMenu instance = null;
	private GameButton btnResume, btnExit;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	
	public CanvasMenu() {
		instance = this;
		btnResume = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 - 100, "buttons/btnResumeOn.png", "buttons/btnResumeOff.png");
		btnExit = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 100, "buttons/btnExitOn.png", "buttons/btnExitOff.png");
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btnResume.isMouseOver(MOUSE_X, MOUSE_Y)){ btnResume.setState(1); }
		else { btnResume.setState(0); }
		
		if(btnExit.isMouseOver(MOUSE_X, MOUSE_Y)){ btnExit.setState(1); }
		else { btnExit.setState(0); }
		
		if(MOUSE_PRESSED && btnResume.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			closeThisCanvas();
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnExit.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			System.exit(0);
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.setColor(Color.BLACK);
		dbg.fillRect(GamePanel.PANEL_WIDTH/2 - 200, GamePanel.PANEL_HEIGHT/2 - 200, 400, 400);
		btnResume.selfDraws(dbg);
		btnExit.selfDraws(dbg);
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_ESCAPE){
			closeThisCanvas();
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
	
	public void closeThisCanvas() {
		GamePanel.canvasActive = CanvasGame.instance;
		CanvasGame.LEFT = false;
		CanvasGame.RIGHT = false;
		CanvasGame.JUMP = false; 
		CanvasGame.FIRE = false;
	}
}