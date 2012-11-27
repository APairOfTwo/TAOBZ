import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class EnemyDemon extends Character {
	final float DEFAULT_SPEED = 109;
	double projDx, projDy, projDist;
	private int changeDirectionRate = 200;
	Projectile proj;
	
	public EnemyDemon(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 71, 85, 6);
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
			projDx = proj.x - (x + centerX);
			projDy = proj.y - (y + centerY);
			projDist = Math.hypot(projDx, projDy);
		}
		
		if(!this.isStunned && !this.isEating && !this.isFollowing) {
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
			if(this.isFollowing && proj.active){
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
		}
		
//		if(floorCollision((int)((x+15)/16), (int)((x+35)/16), (int)((y+frameHeight-10)/16), (int)((y+frameHeight-15)/16), (int)((y+frameHeight-20)/16))) {
//			y = oldY;
//			if((int)oldY % 16 != 0) {
//				y -= 1;
//			}
//			onTheFloor = true;
//		} else {
//			onTheFloor = false;
//		}
//
//		if((x < 0) || (x >= (CanvasGame.map.Largura << 4) - this.frameWidth+1) || sideAndTopCollision((int)((x+10)/16), (int)((x+60)/16), (int)((y+frameHeight-10)/16))) {
//			if(fireTimer > changeDirectionRate) {
//				fireTimer = 0;
//				moveDirection *= -1;
//			}
//		}
		
		for(Element e : CanvasGame.gameElements.elementsList) {
			if(e.itemId == 8) {
				if(this.getBounds().intersects((e.blockX<<4)-CanvasGame.map.MapX, (e.blockY<<4)-CanvasGame.map.MapY, 16, 64)) {
					if(fireTimer > changeDirectionRate) {
						fireTimer = 0;
						moveDirection *= -1;
					}
				}
			}
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
	
	//retangulo delimitador
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX+20), (int)(y-CanvasGame.map.MapY+30), 30, 50);
		return r;
	}
	
	@Override
	public void hitByProjectile(Projectile p) {
		super.hitByProjectile(p);
		proj = p;
	}
}
