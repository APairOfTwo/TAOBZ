import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class EnemyDemon extends Character {
	final float DEFAULT_SPEED = 150;
	double projDx, projDy, projDist;
	Projectile proj;
	
	public EnemyDemon(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 71, 84, 6, 425, 168);
		speed = DEFAULT_SPEED;
	}

	@Override
	public void selfSimulates(long diffTime){	
		super.selfSimulates(diffTime);
		
		oldX = x;
		oldY = y;
		y += gravity * diffTime / 1000.0f;
		
		double velX = speed * diffTime / 1000.0f;
		double velY = speed * diffTime / 1000.0f;
		
		if(proj != null) {
			projDx = proj.x - x;
			projDy = proj.y - y;
			projDist = Math.hypot(projDx, projDy);
		}
		
		if(!this.isStunned && !this.isEating) {
			if(moveDirection == 1) {
				animation = 0;
				x += speed * diffTime / 1000.0f;
			} else if(moveDirection == -1) {
				animation = 1;
				x -= speed * diffTime / 1000.0f;
			} else {
				timeAnimating = 0;
			}
		} else {
			if(this.isStunned){
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
				x += velX * projDx / projDist;
				y += velY * projDy / projDist;
				
				if(projDx >= 0) {
					animation = 0;
					moveDirection = 1;
				} else if(projDx < 0) {
					animation = 1;
					moveDirection = -1;
				}
				countTime += diffTime;
				animeSpeed = 300;
				if(moveDirection == 1) {
					animation = 2;
				} else if(moveDirection == -1) {
					animation = 3;
				}
				if(countTime >= 5000) {
					proj.active = false;
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

		if((x < 0) || (x >= (CanvasGame.map.Largura << 4) - 50) || hasCollidedWithLayer1((int)((x+10)/16), (int)((x+60)/16), (int)((y+75)/16))) {
			moveDirection *= -1;
		}
		
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
	
	@Override
	public void hitByProjectile(Projectile p) {
		super.hitByProjectile(p);
		proj = p;
	}
}
