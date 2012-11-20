import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class CharBilly extends Character {
	int fireRate = 800;
	int life = 100;
	int respawnCountTime;
	float speed = 220;
	int numShotsBone = 5;
	Projectile proj;
	boolean positionsMap = false;
	
	public CharBilly(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 49, 55, 7);
	}

	@Override
	public void selfSimulates(long diffTime){
		
		if(numShotsBone <= 0) {
			isAlive = false;
		}
		if(CanvasGame.B_FIRE && fireTimer > fireRate){
			fireTimer = 0;
			float vproj = 1000;
			if(CanvasGame.B_LEFT || CanvasGame.B_RIGHT) {
				vproj += speed;
			}
			
			float vx = vproj * moveDirection;
			proj = new ProjBone(x+centerX, y+centerY, vx/2, 0, this);
			CanvasGame.projectilesList.add(proj);
			numShotsBone--;
		}
		
		oldX = x;
		oldY = y;
		y += gravity * diffTime / 1000.0f;
		
		if(CanvasGame.B_JUMP && onTheFloor) {
			jumpSpeed = 1100;
			hasJumped = true;
			if(moveDirection == 1) animation = 0;
			if(moveDirection == -1) animation = 1;
		}
		if(CanvasGame.B_RIGHT) {
			x += speed * diffTime / 1000.0f;
			animation = 0;
			moveDirection = 1;
		} else if(CanvasGame.B_LEFT) {
			x -= speed * diffTime / 1000.0f;
			animation = 1;
			moveDirection = -1;
		} else {
			timeAnimating = 0;
		}
		
		if(hasJumped) {
			y -= jumpSpeed * diffTime / 1000.0f;
			jumpSpeed -= 3 * gravity * (diffTime / 1000.0f);
			if(jumpSpeed <= 0) {
				hasJumped = false;
				jumpSpeed = 1100;
			}
		}
		
		if(x < 0) x = oldX;
		if(y < 0) y = oldY;
		if(x >= (CanvasGame.map.Largura << 4) - 50) x = oldX;
		if(y >= (CanvasGame.map.Altura << 4) - 48) y = oldY;
		
		if(hasCollidedWithLayer1((int)((x+15)/16), (int)((x+35)/16), (int)((y+44)/16))) {
			y = oldY;
			if((int)oldY % 16 != 0) {
				y -= 1;
			}
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}
		
		if(hasCollidedWithLayer1((int)((x+10)/16), (int)((x+40)/16), (int)((y+35)/16))) {
			x = oldX;
		}
		
		if(hasCollidedWithLayer1((int)((x+15)/16), (int)((x+35)/16), (int)((y)/16))) {
			y = oldY;
			CanvasGame.billy.jumpSpeed = CanvasGame.billy.jumpSpeed / 2;
		}
		
		int blockX = (int)((x+35)/16);
		int blockY = (int)((y+42)/16);
		
		for(Character c : CanvasGame.enemiesList) {
			if(!c.isEating && !c.isStunned) {
				if(this.getBounds().intersects(c.getBounds())) {
					isAlive = false;
				}
			}
		}
		
		super.selfSimulates(diffTime);
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
		dbg.drawString("Bones: "+numShotsBone, 10, 40);
	}
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX+10), (int)(y-CanvasGame.map.MapY+5), 25, 45);
		return r;
	}
	
	public void respawn() {
		isAlive = true;
		hasJumped = false;
		numShotsBone = 5;
		x = 100;
		y = 100;
	}
	
	public void checkMapPositions() {
		double dx = (CanvasGame.zombie.x + CanvasGame.zombie.centerX) - (x + centerX);
		double dy = CanvasGame.zombie.y - (y + centerY);
		double dist = Math.hypot(dx, dy);
		System.out.println(dist);
		
//		if(dist < 400) {
//			return true;
//		}
//		return false;
	}
}