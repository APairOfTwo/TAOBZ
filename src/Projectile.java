import java.awt.Color;
import java.awt.Graphics2D;

public class Projectile extends Sprite {
	float velX = 0;
	float velY = 0;
	int bx, by;
	Object pai = null;
	float radius = 2;
	
	public Projectile(float x, float y, float velX, float velY, Object pai) {
		super();
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		this.pai = pai;
	}
	
	@Override
	public void selfSimulates(long diffTime){
		if(x<0){
			active = false;
		}else if(y<0){
			active = false;
		}else if(x>=(CanvasGame.map.Largura<<4)-1){
			active = false;
		}
		
		if(CanvasGame.billy.x <= GamePanel.PANEL_WIDTH/2) {
			if(x > GamePanel.PANEL_WIDTH) {
				active = false;
			}
		} else if(CanvasGame.billy.x >= CanvasGame.map.Largura*16 - GamePanel.PANEL_WIDTH/2) {
			if(x < CanvasGame.map.Largura*16 - GamePanel.PANEL_WIDTH) {
				active = false;
			}
		} else {
			if((x < CanvasGame.billy.x - GamePanel.PANEL_WIDTH/2) || (x > CanvasGame.billy.x + GamePanel.PANEL_WIDTH/2)) {
				active = false;
			}
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.setColor(Color.WHITE);
		dbg.fillOval((int)(x-mapX-2), (int)(y-mapY-2), (int)radius*2, (int)radius*2);
	}
	
	public boolean circleCollision(Character c) {
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
