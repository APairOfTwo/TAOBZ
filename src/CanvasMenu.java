import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class CanvasMenu extends Canvas {

	public static CanvasMenu instance = null;
	
	public CanvasMenu() {
		instance = this;
	}
	
	@Override
	public void selfSimulates(long diffTime) { }

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.setColor(Color.blue);
		dbg.fillRect(GamePanel.PANEL_WIDTH/2 - 200, GamePanel.PANEL_HEIGHT/2 - 200, 400, 400);
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_ESCAPE){
			GamePanel.canvasActive = CanvasGame.instance;
			CanvasGame.LEFT = false;
			CanvasGame.RIGHT = false;
			CanvasGame.JUMP = false; 
			CanvasGame.FIRE = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent k){ }
	@Override
	public void mouseMoved(MouseEvent m){ }
	@Override
	public void mouseDragged(MouseEvent e){ }
	@Override
	public void mouseReleased(MouseEvent e){ }

	@Override
	public void mousePressed(MouseEvent e){ }
}