import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class EnemyBerserker extends Character {

	final float DEFAULT_SPEED = 146;
	final int FIELD_OF_VIEW = 300;
	float spawnX, spawnY;
	double projDx, projDy, projDist;
	Projectile proj;
	
	public EnemyBerserker(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 71, 64, 6);
		spawnX = x;
		spawnY = y;
		speed = DEFAULT_SPEED;
		moveDirection = -1;
	}

	@Override
	public void selfSimulates(long diffTime){	
		super.selfSimulates(diffTime);
		
		oldX = x;
		oldY = y;
		y += gravity * diffTime / 1000.0f;
		
		double dx = (CanvasGame.billy.x + CanvasGame.billy.centerX) - (x + centerX);
		double dy = CanvasGame.billy.y - (y + centerY);
		double dist = Math.hypot(dx, dy);
		
		double spawnDx = spawnX - x;
		double spawnDist = Math.hypot(spawnDx, 0);
		
		double velX = speed * diffTime / 1000.0f;
		
		if(proj != null) {
			projDx = proj.x - (x + centerX);
			projDy = proj.y - (y + centerY);
			projDist = Math.hypot(projDx, projDy);
		}
		
		if(!this.isEating && !this.isFollowing) {
			if(dist <= FIELD_OF_VIEW) {					// herói dentro do campo de visão, início da perseguição
				x += velX * dx / dist;
				if(dx >= 0) {
					animation = 0;
					moveDirection = 1;
				} else if(dx < 0) {
					animation = 1;
					moveDirection = -1;
				}
			} else {
				if(spawnDist >= 1) {					// herói saiu do campo de visão, início da volta para spawn point
					x += velX * spawnDx / spawnDist;
					if((x - spawnX) >= 0) {
						animation = 1;
					} else {
						animation = 0;
					}
				} 
				if(spawnDist < 1) {						// this está no spawn point
					x = spawnX;
					if(moveDirection == 1) {
						animation = 2;
					} else if(moveDirection == -1) {
						animation = 3;
					}
				}
			}
		}
		
		if(this.isFollowing && proj.active) {
			x += velX * projDx / projDist;
			
			if(projDx >= 0) {
				animation = 0;
				moveDirection = 1;
			} else if(projDx < 0) {
				animation = 1;
				moveDirection = -1;
			}
		}
		if(this.isEating){
			countTime += diffTime;
			animeSpeed = 300;
			if(moveDirection == 1) {
				animation = 2;
			} else if(moveDirection == -1) {
				animation = 3;
			}
			if(countTime >= 5000) {
				proj.active = false;
				animeSpeed = 100;
				speed = DEFAULT_SPEED;
				countTime = 0;
			}
		}
		
		if(hasCollidedWithLayer1((int)((x+15)/16), (int)((x+35)/16), (int)((y+frameHeight-10)/16))) {
			y = oldY;
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}

		if((x < 0) || (x >= (CanvasGame.map.Largura << 4) - this.frameWidth+1) || hasCollidedWithLayer1((int)((x+10)/16), (int)((x+60)/16), (int)((y+frameHeight-10)/16))) {
			moveDirection *= -1;
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
	
	//retangulo delimitador
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX+20), (int)(y-CanvasGame.map.MapY+23), 30, 34);
		return r;
	}
	
	@Override
	public void hitByProjectile(Projectile p) {
		if(p.getClass() == ProjMeat.class){
			isFollowing = true;
			proj = p;
		}
	}
}
