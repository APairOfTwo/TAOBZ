import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class EnemyGargoyle extends Character {
	final float DEFAULT_SPEED = 150;
	final int VISION_RADIUS = 300;
	float oriX, oriY;
	
	public EnemyGargoyle(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 71, 84, 6, 425, 168);
		oriX = x;
		oriY = y;
		speed = DEFAULT_SPEED;
	}

	@Override
	public void selfSimulates(long diffTime){
		oldX = x;
		oldY = y;
		
		double dx = CanvasGame.billy.x - x;
		double dy = CanvasGame.billy.y - y;
		double dist = Math.hypot(dx,dy);
		
		double odx = oriX - x;
		double ody = oriY - y;
		double odist = Math.hypot(odx,ody);
		
		double velx = speed*diffTime/1000.0f;
		double vely = speed*diffTime/1000.0f;
		
		if(dist <= VISION_RADIUS) {
			x += velx*dx/dist;
			y += vely*dy/dist;
		} else if(odist >= 1) {
			x += velx*odx/odist;
			y += vely*ody/odist;
			System.out.println(odist);
		} else if(odist < 1) {
			x = oriX;
			y = oriY;
		}
		
		if(!this.isStunned && !this.isEating) {
			if(dx > 0) {
				animation = 0;
			} else if(dx < 0) {
				animation = 1;
			} else {
				timeAnimating = 0;
			}
		} else {
			if(this.isStunned) {
				System.out.println("Bones");
				y += gravity * diffTime / 1000.0f;
				countTime += diffTime;
				animeSpeed = 300;
				if(moveDirection == 1) {
					animation = 2;
				} else if(moveDirection == -1) {
					animation = 3;
				} else {
					timeAnimating = 0;
				}
				if(countTime >= 5000) {
					isStunned = false;
					animeSpeed = 100;
					speed = DEFAULT_SPEED;
					countTime = 0;
				}
			}
			if(this.isEating){
				System.out.println("Meat");
				countTime += diffTime;
				animeSpeed = 300;
				if(moveDirection == 1) {
					animation = 2;
				} else if(moveDirection == -1) {
					animation = 3;
				} else {
					timeAnimating = 0;
				}
				if(countTime >= 5000) {
					isEating = false;
					animeSpeed = 100;
					speed = DEFAULT_SPEED;
					countTime = 0;
				}
			}
		}
		
		if(hasCollidedWithLayer1((int)((x+15)/16), (int)((x+35)/16), (int)((y+75)/16))) {
			y = oldY;
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}
		
		if(hasCollidedWithLayer1((int)((x+5)/16), (int)((x+70)/16), (int)((y+50)/16))) {
			x = oldX;
		}
		
		if(x < 0) x = oldX;
		if(y < 0) y = oldY;
		if(x >= (CanvasGame.map.Largura << 4) - 75) x = oldX;
		if(y >= (CanvasGame.map.Altura << 4) - 88) y = oldY;
		
		super.selfSimulates(diffTime);
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
	
	@Override
	public void hitByProjectile(Projectile p) {
		super.hitByProjectile(p);
	}
}
