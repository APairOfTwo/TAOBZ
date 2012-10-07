import java.awt.Color;
import java.awt.Graphics2D;


public class ProjMeat extends Projectile {
	float Xspeed, Yspeed;
	float speed = 550;
	double gravity = 700;
	double angle, angle2;
	float oldX, oldY;
	boolean hitTheGround;
	Character shooter;
	
	public ProjMeat(float x, float y, float velX, float velY, Object pai) {
		super(x, y, velX, velY, pai);
		angle = Math.atan2(100, 1);
		angle += Math.PI;
		
		angle2 = angle - (Math.PI / 4) + (Math.PI / 2);
		
		Xspeed = (float)(Math.cos(angle2) * speed);
		Yspeed = (float)(Math.sin(angle2) * speed);
		hitTheGround = false;
		shooter = CanvasGame.billy;
		Xspeed *= shooter.moveDirection;
		radius = 100;
	}
	
	public void selfSimulates(long diffTime) {
		if(!hitTheGround) {
			super.selfSimulates(diffTime);
			oldX = x;
			oldY = y;
			
			bx = (int)((x)/16);
			by = (int)((y)/16);
			
			x += Xspeed * diffTime / 1000;
			y += Yspeed * diffTime / 1000;
			Yspeed += gravity * diffTime / 1000;
			
			
			if(active && CanvasGame.map.mapLayer1[by][bx]>0){
				hitTheGround = true;
				x = oldX;
				y = oldY;
			}
		}
		
		for(int i = 0; i < CanvasGame.instance.enemiesList.size(); i++){
			Character enemy = CanvasGame.instance.enemiesList.get(i);
			if(enemy != pai){
				if(circleCollision(enemy)){
					active = false;
					enemy.hitByProjectile(this);
					break;
				}
			}
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.setColor(Color.RED);
		dbg.fillOval((int)(x-mapX-2), (int)(y-mapY-2), 8, 8);
		dbg.drawOval((int)(x-CanvasGame.map.MapX-radius), (int)(y-CanvasGame.map.MapY-radius), (int)(radius*2),(int)(radius*2));
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