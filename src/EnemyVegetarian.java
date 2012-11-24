import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class EnemyVegetarian extends Character {
	final float DEFAULT_SPEED = 55;
	private int changeDirectionRate = 200;
	Projectile proj;
	
	public EnemyVegetarian(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 62, 81, 12);
		speed = DEFAULT_SPEED;
		animeSpeed = 150;
	}

	@Override
	public void selfSimulates(long diffTime){	
		super.selfSimulates(diffTime);
		
		oldX = x;
		oldY = y;
		y += gravity * diffTime / 1000.0f;
		
		if(!this.isStunned) {
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
				//animeSpeed = 300;
				if(moveDirection == 1) {
					animation = 2;
				} else if(moveDirection == -1) {
					animation = 3;
				} else {
					timeAnimating = 0;
				}
				if(countTime >= 5000) {
					isStunned = false;
					animeSpeed = 150;
					speed = DEFAULT_SPEED;
					countTime = 0;
				}
			}
		}	
		
//		if(hasCollidedWithLayer1((int)((x+15)/16), (int)((x+35)/16), (int)((y+frameHeight-5)/16))) {
//			y = oldY;
////			if((int)oldY % 16 != 0) {
////				y -= 1;
////			}
//			onTheFloor = true;
//		} else {
//			onTheFloor = false;
//		}

//		if((x < 0) || (x >= (CanvasGame.map.Largura << 4) - this.frameWidth+1) || hasCollidedWithLayer1((int)((x+10)/16), (int)((x+60)/16), (int)((y+frameHeight-5)/16))) {
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
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX+16), (int)(y-CanvasGame.map.MapY+21), 25, 54);
		return r;
	}
	
	@Override
	public void hitByProjectile(Projectile p) {
		if(p.getClass() == ProjBone.class){
			isStunned = true;
			proj = p;
		}
	}
}
