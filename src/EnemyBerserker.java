import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class EnemyBerserker extends Character {

	final float DEFAULT_SPEED = 146;
	final int FIELD_OF_VIEW = 300;
	float spawnX, spawnY;
	double projDx, projDy, projDist;
	Projectile proj;
	
	public EnemyBerserker(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 71, 64, 6, 425, 256);
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
		
		if(!this.isEating) {
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
		
		if(this.isEating){
			x += velX * projDx / projDist;
			
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
		
		if(hasCollidedWithLayer1((int)((x+15)/16), (int)((x+35)/16), (int)((y+frameHeight-10)/16))) {
			y = oldY;
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}

		if((x < 0) || (x >= (CanvasGame.map.Largura << 4) - this.frameWidth+1) || hasCollidedWithLayer1((int)((x+10)/16), (int)((x+60)/16), (int)((y+frameHeight-10)/16))) {
			moveDirection *= -1;
		}
		
		int blockX = (int)((x+35)/16);
		int blockY = (int)((y+75)/16);
		
		if(CanvasGame.map.mapLayer2[blockY][blockX]>0) {
			double ang = Math.atan2(100, 1);
			ang+=Math.PI;
			for(int j = 0; j < 20; j++){
				double ang2 = ang - (Math.PI/4)+((Math.PI/2)*Math.random());
				float vel = (float)(100+100*Math.random());
				float vx = (float)(Math.cos(ang2)*vel);
				float vy = (float)(Math.sin(ang2)*vel);
				CanvasGame.effectsList.add(new Effect(x+26, y+40, vx, vy, 900, 255, 0, 0));
			}
			isAlive = false;
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
	
	@Override
	public void hitByProjectile(Projectile p) {
		if(p.getClass() == ProjMeat.class){
			isEating = true;
			proj = p;
		}
	}
}
