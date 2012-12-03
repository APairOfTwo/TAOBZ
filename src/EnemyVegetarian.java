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
		
		if(!onTheFloor) {
			y += gravity * diffTime / 1000.0f;
		}
		
		oldX = x;
		oldY = y;
		
		if((x < 5)) {
			x = 5;
			if(fireTimer > changeDirectionRate) {
				fireTimer = 0;
				moveDirection *= -1;
			}
		}
		if((x+frameWidth > (CanvasGame.map.Largura << 4)-5)) {
			x = (CanvasGame.map.Largura << 4)-5;
			if(fireTimer > changeDirectionRate) {
				fireTimer = 0;
				moveDirection *= -1;
			}
		}
		if((y < 5)) { y = 5; }
		if((y+frameHeight > (CanvasGame.map.Altura << 4)-5)) { isAlive = false; }
		
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
		
		if(floorCollision((int)((x+25)/16), (int)((x+30)/16), (int)((x+35)/16), (int)((y+80)/16), (int)((y+75)/16), (int)((y+70)/16))) {
			y = oldY;
			if((int)oldY % 16 != 0) {
				y -= 1;
			}
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}
		
		if(lateralCollision((int)((x+20)/16), (int)((x+40)/16), (int)((y+30)/16), (int)((y+40)/16), (int)((y+50)/16))) {
			x = oldX;
			if(fireTimer > changeDirectionRate) {
				fireTimer = 0;
				moveDirection *= -1;
			}
		}
		
		if(spykeCollision((int)((x+20)/16), (int)((x+40)/16), (int)((y+15)/16), (int)((y+75)/16))) {
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
