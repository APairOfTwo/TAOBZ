import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class EnemyGargoyle extends Character {
	final float DEFAULT_SPEED = 109;
	final int FIELD_OF_VIEW = 300;
	float spawnX, spawnY;
	double projDx, projDy, projDist;
	double dx, dy, dist;
	double velX, velY;
	Projectile proj;
	
	public EnemyGargoyle(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 87, 77, 6);
		spawnX = x;
		spawnY = y;
		speed = DEFAULT_SPEED;
	}

	@Override
	public void selfSimulates(long diffTime) {
		super.selfSimulates(diffTime);
		oldX = x;
		oldY = y;
		
		if((x < 5)) { x = 5; }
		if((y < 5)) { y = 5; }
		if((x+frameWidth > (CanvasGame.map.Largura << 4)-5)) { x = (((CanvasGame.map.Largura << 4)-5)-frameWidth); }
		if((y+frameHeight > (CanvasGame.map.Altura << 4)-5)) { isAlive = false; }
		
		velX = speed * diffTime / 1000.0f;
		velY = speed * diffTime / 1000.0f;
		
		if(CanvasGame.heroes.size() == 1) {
			dx = (CanvasGame.heroes.get(0).x + CanvasGame.heroes.get(0).centerX) - (x + centerX);
			dy = CanvasGame.heroes.get(0).y - (y + centerY);
			dist = Math.hypot(dx, dy);
		} else if(CanvasGame.heroes.size() == 2) {
			double dx1 = (CanvasGame.heroes.get(0).x + CanvasGame.heroes.get(0).centerX) - (x + centerX);
			double dy1 = CanvasGame.heroes.get(0).y - (y + centerY);
			double dist1 = Math.hypot(dx1, dy1);
			
			double dx2 = (CanvasGame.heroes.get(1).x + CanvasGame.heroes.get(1).centerX) - (x + centerX);
			double dy2 = CanvasGame.heroes.get(1).y - (y + centerY);
			double dist2 = Math.hypot(dx2, dy2);
			
			if(dist1 <= dist2) {
				dx = dx1;
				dy = dy1;
				dist = dist1;
			} else {
				dx = dx2;
				dy = dy2;
				dist = dist2;
			}
		}
		
		double spawnDx = spawnX - x;
		double spawnDy = spawnY - y;
		double spawnDist = Math.hypot(spawnDx, spawnDy);
		
		if(proj != null) {
			projDx = proj.x - (x + centerX);
			projDy = proj.y - (y + centerY);
			projDist = Math.hypot(projDx, projDy);
		}
		
		if(!this.isStunned && !this.isEating && !this.isFollowing) {
			if(dist <= FIELD_OF_VIEW) {
				x += velX * dx / dist;
				y += velY * dy / dist;
				if(dx >= 0) {
					animation = 0;
					moveDirection = 1;
				} else if(dx < 0) {
					animation = 1;
					moveDirection = -1;
				}
			} else {
				if(spawnDist >= 1) {
					x += velX * spawnDx / spawnDist;
					y += velY * spawnDy / spawnDist;
					if((x - spawnX) >= 0) {
						animation = 1;
					} else {
						animation = 0;
					}
				} 
				if(spawnDist < 1) {
					x = spawnX;
					y = spawnY;
					if(moveDirection == 1) {
						animation = 0;
					} else if(moveDirection == -1) {
						animation = 1;
					}
				}
			}
		}
		if(this.isStunned) {
			y += gravity * diffTime / 1000.0f;
			countTime += diffTime;
			animeSpeed = 300;
			if(moveDirection == 1) {
				animation = 2;
			} else if(moveDirection == -1) {
				animation = 3;
			}
			if(countTime >= 5000) {
				isStunned = false;
				animeSpeed = 100;
				speed = DEFAULT_SPEED;
				countTime = 0;
			}
		}
		if(this.isFollowing && proj.active) {
			x += velX * projDx / projDist;
			y += velY * projDy / projDist;
			
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
		
		if(this.isStunned) {
			if(floorCollision((int)((x+30)/16), (int)((x+40)/16), (int)((x+50)/16), (int)((y+70)/16), (int)((y+70)/16), (int)((y+70)/16))) {
				y = oldY;
				onTheFloor = true;
			} else {
				onTheFloor = false;
			}
		}
		
		if(this.isStunned || this.isFollowing || this.isEating) {
			if(spykeCollision((int)((x+25)/16), (int)((x+55)/16), (int)((y+20)/16), (int)((y+65)/16))) {
				bloodAngle = Math.atan2(100, 1);
				bloodAngle += Math.PI;
				for(int i = 0; i < 20; i++) {
					bloodAuxAngle = bloodAngle - (Math.PI/4) + ((Math.PI/2) * Math.random());
					vel = (float)(50 + 50 * Math.random());
					vX = (float)(Math.cos(bloodAuxAngle) * vel);
					vY = (float)(Math.sin(bloodAuxAngle) * vel);
					CanvasGame.effectsList.add(new Effect(x+frameWidth/2, y+frameHeight/2, vX, vY, 600, 255, 0, 0));
				}
				isAlive = false;
			}
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX+28), (int)(y-CanvasGame.map.MapY+30), 28, 40);
		return r;
	}
	
	@Override
	public void hitByProjectile(Projectile p) {
		super.hitByProjectile(p);
		proj = p;
	}
}
