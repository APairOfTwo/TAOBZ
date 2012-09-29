import java.awt.Graphics2D;

abstract public class Sprite {
	
	public float x;
	public float y;
	public boolean active = true;
	
	public abstract void selfSimulates(long diffTime);
	public abstract void selfDraws(Graphics2D dbg, int mapX, int mapY);
}
