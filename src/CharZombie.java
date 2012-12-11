import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class CharZombie extends Character {
	int fireRate = 800;
	int respawnCountTime;
	float speed = 220;
	boolean haveKey = false;
	int numShotsMeat = 3;
	Projectile proj;
	float spawnX, spawnY;
	int fireAnim;
	int animeLine = 0;
	public static BufferedImage hudProjMeat = GamePanel.loadImage("sprites/hud_projMeat.png");
	BufferedImage deadMsg = GamePanel.loadImage("sprites/msgDeathZ.png");
	public static BufferedImage bmpMeat;
	
	public CharZombie(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 60, 60, 4);
		this.spawnX = x;
		this.spawnY = y;
		bmpMeat = GamePanel.loadImage("sprites/meat.png");
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
		if((x+frameWidth > (CanvasGame.map.Largura << 4)-5)) { x = (((CanvasGame.map.Largura << 4)-5)-frameWidth); }
		if((y+frameHeight > (CanvasGame.map.Altura << 4)-5)) { isAlive = false; }
		
		if(numShotsMeat <= 0) {
			isHeadless = true;
		}
		
		if(!isHeadless) {
			if((CanvasGame.Z_KEY_FIRE || CanvasGame.Z_JOY_FIRE) && fireTimer > fireRate){
				fireAnim += diffTime;
				fireTimer = 0;
				float vproj = 1000;
				float vx = vproj * moveDirection;
				proj = new ProjMeat(x+centerX, y+centerY, vx/2, 0, bmpMeat, this);
				CanvasGame.projectilesList.add(proj);
				numShotsMeat--;
			}

			if((CanvasGame.Z_KEY_JUMP || CanvasGame.Z_JOY_JUMP) && onTheFloor) {
				jumpSpeed = 1100;
				hasJumped = true;
				if(moveDirection == 1) animation = 0 + animeLine;
				if(moveDirection == -1) animation = 1 + animeLine;
			}
			if(!CanvasGame.Z_KEY_JUMP && !CanvasGame.Z_JOY_JUMP) {
				jumpSpeed = jumpSpeed / 2;
			}
			if(CanvasGame.Z_KEY_RIGHT || CanvasGame.Z_JOY_RIGHT) {
				x += speed * diffTime / 1000.0f;
				animation = 2 + animeLine;
				moveDirection = 1;
			} else if(CanvasGame.Z_KEY_LEFT || CanvasGame.Z_JOY_LEFT) {
				x -= speed * diffTime / 1000.0f;
				animation = 3 + animeLine;
				moveDirection = -1;
			} else {
				if(moveDirection == 1) {
					animeSpeed = 200;
					animation = 0 + animeLine;
				} else if(moveDirection == -1) {
					animeSpeed = 200;
					animation = 1 + animeLine;
				}
			}

			if(fireAnim != 0) {
				fireAnim += diffTime;
				animeSpeed = 100;
				if(fireAnim >= 420) {
					fireAnim = 0;
				}
				if(moveDirection == 1) {
					animation = 5 + animeLine;
				} else {
					animation = 4 + animeLine;
				}
			}

			if(numShotsMeat == 2) {
				animeLine = 6;
			}
			if(numShotsMeat == 1) {
				animeLine = 12;
			}
			if(numShotsMeat <= 0) {
				animeLine = 14;
			}

			if(hasJumped) {
				y -= jumpSpeed * diffTime / 1000.0f;
				jumpSpeed -= 3 * gravity * (diffTime / 1000.0f);
				if(jumpSpeed <= 0) {
					hasJumped = false;
					jumpSpeed = 1100;
				}
			}

		} else {
			if(moveDirection == 1) {
				animation = 4 + animeLine;
			} else {
				animation = 5 + animeLine;
			}
			headlessCounter += diffTime;
			if(headlessCounter >= headlessTime) {
				headlessCounter = 0;
				CanvasGame.deathCounter++;
				isAlive = false;
			}
		}
		
		if(floorCollision((int)((x+10)/16), (int)((x+25)/16), (int)((x+40)/16), (int)((y+50)/16), (int)((y+50)/16), (int)((y+45)/16))) {
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
			CanvasGame.deathCounter++;
			isAlive = false;
		}
		
		for(Character c : CanvasGame.enemiesList) {
			if(!c.isEating && !c.isStunned) {
				if(this.getBounds().intersects(c.getBounds())) {
					CanvasGame.deathCounter++;
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
				if(GamePanel.levelId == 1 && haveKey) {
					if(this.getBounds().intersects((e.blockX<<4)-CanvasGame.map.MapX, (e.blockY<<4)-CanvasGame.map.MapY, 16, 64)) {
						GamePanel.canvasActive = new CanvasResult();
					}
				}
				if(GamePanel.levelId != 1) {
					if(this.getBounds().intersects((e.blockX<<4)-CanvasGame.map.MapX, (e.blockY<<4)-CanvasGame.map.MapY, 16, 64)) {
						GamePanel.canvasActive = new CanvasResult();
					}
				}
			}
		}
	
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		if(isAlive) {
			super.selfDraws(dbg, mapX, mapY);
			for(int i = 0; i < numShotsMeat; i++) {
				int x = 100 + (i * 13);
				dbg.drawImage(hudProjMeat, (int)(x), (int)(25), (int)(x+19), (int)(25+40), 0, 0, hudProjMeat.getWidth(), hudProjMeat.getHeight(), null);
			}
		} else {
			dbg.setColor(Color.BLACK);
			dbg.fillRect(0, GamePanel.PANEL_HEIGHT-35, deadMsg.getWidth()+10, deadMsg.getHeight()+10);
			dbg.drawImage(deadMsg, 5, (int)(GamePanel.PANEL_HEIGHT - 30), (int)(5+deadMsg.getWidth()), (int)((GamePanel.PANEL_HEIGHT - 30)+deadMsg.getHeight()), 0, 0, deadMsg.getWidth(), deadMsg.getHeight(), null);
		}

	}
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX+10), (int)(y-CanvasGame.map.MapY+5), 40, 50);
		return r;
	}
	
	public void respawn() {
		isAlive = true;
		isHeadless = false;
		hasJumped = false;
		numShotsMeat = 3;
		animeLine = 0;
		
		if(CanvasGame.checkpoints.size() != 0) {
			for(int i = CanvasGame.checkpoints.size()-1; i >= 0; i--) {
				if(CanvasGame.checkpoints.get(i).isActive) {
					x = CanvasGame.checkpoints.get(i).x;
					y = CanvasGame.checkpoints.get(i).y-16;
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