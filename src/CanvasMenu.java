import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class CanvasMenu extends Canvas {

	public static CanvasMenu instance = null;
	
	public CanvasMenu(){
		instance = this;
	}
	
	@Override
	public void selfSimulates(long diffTime){ }

	@Override
	public void selfDrawns(Graphics2D dbg){
		dbg.setColor(Color.blue);
		dbg.fillRect(0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_SPACE){
			GamePanel.canvasActive = CanvasGame.instance;
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