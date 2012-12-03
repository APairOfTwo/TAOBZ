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
	int fireAnim;
	
	public CharBilly(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 59, 60, 4);
		this.spawnX = x;
		this.spawnY = y;
	}

	@Override
	public void selfSimulates(long diffTime){
		super.selfSimulates(diffTime);
		
		if(!onTheFloor) {
			y += gravity * diffTime / 1000.0f;
		}
		
		oldX = x;
		oldY = y;
		
		if((x < 5)) { x = 5; }
		if((y < 5)) { y = 5; }
		if((x+frameWidth > (CanvasGame.map.Largura << 4)-5)) { x = (CanvasGame.map.Largura << 4)-5; }
		if((y+frameHeight > (CanvasGame.map.Altura << 4)-5)) { isAlive = false; }
		
		if(numShotsBone <= 0) {
			isAlive = false;
		}
		
		if(CanvasGame.B_FIRE && fireTimer > fireRate){
			fireAnim += diffTime;
			
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
		
		if(CanvasGame.B_JUMP && onTheFloor) {
			animeSpeed = 100;
			jumpSpeed = 1100;
			hasJumped = true;
			if(moveDirection == 1) animation = 0;
			if(moveDirection == -1) animation = 1;
		}
		if(CanvasGame.B_RIGHT) {
			animeSpeed = 100;
			x += speed * diffTime / 1000.0f;
			animation = 2;
			moveDirection = 1;
		} else if(CanvasGame.B_LEFT) {
			animeSpeed = 100;
			x -= speed * diffTime / 1000.0f;
			animation = 3;
			moveDirection = -1;
		} else {
			if(moveDirection == 1) {
				animeSpeed = 200;
				animation = 0;
			} else if(moveDirection == -1) {
				animeSpeed = 200;
				animation = 1;
			}
		}
		
		// Animacao de atirando
		if(fireAnim != 0) {
			fireAnim += diffTime;
			animeSpeed = 100;
			if(fireAnim >= 420) {
				fireAnim = 0;
			}
			if(moveDirection == 1) {
				animation = 4;
			} else {
				animation = 5;
			}
		}
		
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
		
		if(spykeCollision((int)((x+15)/16), (int)((x+35)/16), (int)((y+10)/16), (int)((y+45)/16))) {
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
		
		for(Character c : CanvasGame.enemiesList) {
			if(!c.isEating && !c.isStunned) {
				if(this.getBounds().intersects(c.getBounds())) {
					isAlive = false;
					bloodAngle = Math.atan2(100, 1);
					bloodAngle += Math.PI;
					for(int i = 0; i < 20; i++) {
						bloodAuxAngle = bloodAngle - (Math.PI/4) + ((Math.PI/2) * Math.random());
						vel = (float)(50 + 50 * Math.random());
						vX = (float)(Math.cos(bloodAuxAngle) * vel);
						vY = (float)(Math.sin(bloodAuxAngle) * vel);
						CanvasGame.effectsList.add(new Effect(x+frameWidth/2, y+frameHeight/2, vX, vY, 600, 255, 0, 0));
					}
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