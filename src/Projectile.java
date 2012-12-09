import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Projectile extends Sprite {
	float velX = 0;
	float velY = 0;
	int bx, by;
	Object pai = null;
	BufferedImage bmp;
	public static int frameWidth, frameHeight;
	
	public Projectile(float x, float y, float velX, float velY, BufferedImage bmp, Object pai) {
		super();
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		this.pai = pai;
		this.bmp = bmp;
		frameWidth = bmp.getWidth();
		frameHeight = bmp.getHeight();
		CanvasGame.projectilesCounter++;
	}
	
	@Override
	public void selfSimulates(long diffTime){
		if(x<0){
			active = false;
		} else if(x>=(CanvasGame.map.Largura<<4)){
			active = false;
		}
		if(y<0){
			active = false;
		} else if(y>=(CanvasGame.map.Altura<<4)) {
			active = false;
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {}
	
	public boolean circleCollision(Character c, float radius) {
		float dx = (c.x + c.centerX) - x;
		float dy = (c.y + c.centerY) - y;
		float r2 = c.radius + radius;
		r2 = r2*r2;
		if(r2 > ((dx*dx)+(dy*dy))){
			return true;
		}
		return false;
	}
}
