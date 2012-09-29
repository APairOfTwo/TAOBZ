import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Canvas {
	
	public abstract void selfSimulates(long diffTime);
	public abstract void selfDraws(Graphics2D dbg);
		
	public abstract void keyPressed(KeyEvent k);
	public abstract void keyReleased(KeyEvent k);
	public abstract void mouseMoved(MouseEvent m);
	public abstract void mouseDragged(MouseEvent m); 
	public abstract void mousePressed(MouseEvent m);
	public abstract void mouseReleased(MouseEvent m);
}
