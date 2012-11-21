import java.awt.Color;
import java.awt.Graphics2D;


public class Checkpoint extends Sprite {
	int x, y;
	boolean isActive;
	
	public Checkpoint(int x, int y) {
		this.x = x;
		this.y = y;
		this.isActive = false;
	}

	@Override
	public void selfSimulates(long diffTime) {
		for(Character c : CanvasGame.heroes) {
			if(c.getBounds().intersects(x-CanvasGame.map.MapX, y-CanvasGame.map.MapY, 16, 64)) {
				isActive = true;
			}
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		if(this.isActive) {
			dbg.setColor(Color.GREEN);
			dbg.fillRect(x-CanvasGame.map.MapX, y-CanvasGame.map.MapY, 16, 64);
		} else {
			dbg.setColor(Color.YELLOW);
			dbg.fillRect(x-CanvasGame.map.MapX, y-CanvasGame.map.MapY, 16, 64);
		}
	}
}