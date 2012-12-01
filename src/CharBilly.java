import java.awt.Color;
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
	float spawnX, spawnY;
	
	public CharBilly(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 49, 55, 7);
		this.spawnX = x;
		this.spawnY = y;
	}

	@Override
	public void selfSimulates(long diffTime){
		super.selfSimulates(diffTime);
		
		if(!onTheFloor) {
			y += gravity * diffTime / 1000.0f;
		}
		
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
		
		if(x < 0) x = oldX;
		if(y < 0) y = oldY;
		if(x >= (CanvasGame.map.Largura << 4) - 50) x = oldX;
		if(y >= (CanvasGame.map.Altura << 4) - 48) y = oldY;
		
		if(hasJumped) {
			y -= jumpSpeed * diffTime / 1000.0f;
			jumpSpeed -= 3 * gravity * (diffTime / 1000.0f);
			if(jumpSpeed <= 0) {
				hasJumped = false;
				jumpSpeed = 1100;
			}
		}
		
		if(floorCollision((int)((x+10)/16), (int)((x+25)/16), (int)((x+40)/16), (int)((y+50)/16), (int)((y+49)/16), (int)((y+48)/16))) {
			y = oldY;
			if((int)oldY % 16 != 0) {
				y -= 1;
			}
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}
		
		if(lateralCollision((int)((x+5)/16), (int)((x+45)/16), (int)((y+40)/16), (int)((y+35)/16), (int)((y+35)/16))) {
			x = oldX;
		}
		
		if(topCollision((int)((x+20)/16), (int)((x+25)/16), (int)((x+30)/16), (int)((y+15)/16), (int)((y+20)/16))) {
			y = oldY;
			jumpSpeed = jumpSpeed / 2;
		}
		
		for(Character c : CanvasGame.enemiesList) {
			if(!c.isEating && !c.isStunned) {
				if(this.getBounds().intersects(c.getBounds())) {
					isAlive = false;
				}
			}
		}
		
		for(Element e : CanvasGame.gameElements.elementsList) {
			if(e.itemId == 9) {
				if(this.getBounds().intersects((e.blockX<<4)-CanvasGame.map.MapX, (e.blockY<<4)-CanvasGame.map.MapY, 16, 64)) {
					GamePanel.canvasActive = new CanvasResult();
				}
			}
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		if(isAlive) {
			super.selfDraws(dbg, mapX, mapY);
			dbg.drawString("Bones: "+numShotsBone, 10, 40);
			dbg.setColor(Color.RED);
			dbg.drawRect((int)(x+10)-mapX, (int)(y+48)-mapY, 30, 2);
			dbg.drawRect((int)(x+5)-mapX, (int)(y+35)-mapY, 40, 10);
			dbg.drawRect((int)(x+20)-mapX, (int)(y)-mapY, 10, 16);
		}
	}
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX+10), (int)(y-CanvasGame.map.MapY+5), 25, 45);
		return r;
	}
	
	public void respawn() {
		isAlive = true;
		hasJumped = false;
		numShotsBone = 5;
		if(CanvasGame.checkpoints.size() != 0) {
			for(int i = CanvasGame.checkpoints.size()-1; i >= 0; i--) {
				if(CanvasGame.checkpoints.get(i).isActive) {
					x = CanvasGame.checkpoints.get(i).x;
					y = CanvasGame.checkpoints.get(i).y;
					break;
				} else if(i == 0) {
					x = spawnX;
					y = spawnY;
				}
			}
		} else {
			x = spawnX;
			y = spawnY;
		}
	}
}