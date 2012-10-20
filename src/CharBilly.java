import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class CharBilly extends Character {
	int fireRate = 800;
	int life = 100;
	int respawnCountTime;
	float speed = 220;
	int numShotsBone = 5;
	Projectile proj;
	
	public CharBilly(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 49, 55, 7, 342, 110);
	}

	@Override
	public void selfSimulates(long diffTime){
		
		if(numShotsBone <= 0) {
			isAlive = false;
		}
		if(CanvasGame.instance.FIRE && fireTimer>fireRate){
			fireTimer = 0;
			float vproj = 1000;
			if(CanvasGame.instance.LEFT || CanvasGame.instance.RIGHT) {
				vproj += speed;
			}
			
			float vx = vproj * moveDirection;
			
			// TODO desfazer seleção de projétil após implementar os 2 heróis
			if(CanvasGame.projIsBone) {
				proj = new ProjBone(x+centerX, y+centerY, vx/2, 0, this);
			} else {
				proj = new ProjMeat(x+centerX, y+centerY, vx/2, 0, this);
			}
			CanvasGame.instance.projectilesList.add(proj);
			numShotsBone--;
		}
		
		oldX = x;
		oldY = y;
		y += gravity * diffTime / 1000.0f;
		if(CanvasGame.instance.JUMP && onTheFloor){
			jumpSpeed = 1100;
			hasJumped = true;
			if(moveDirection == 1) animation = 0;
			if(moveDirection == -1) animation = 1;
		}
		if(CanvasGame.instance.RIGHT){
			x += speed * diffTime / 1000.0f;
			animation = 0;
			moveDirection = 1;
		} else if(CanvasGame.instance.LEFT){
			x -= speed * diffTime / 1000.0f;
			animation = 1;
			moveDirection = -1;
		}else{
			timeAnimating = 0;
		}
		
		if(hasJumped){
			y -= jumpSpeed * diffTime / 1000.0f;
			jumpSpeed -= 3*gravity*(diffTime / 1000.0f);
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
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}
		
		if(hasCollidedWithLayer1((int)((x+10)/16), (int)((x+40)/16), (int)((y+35)/16))) {
			x = oldX;
		}
		
		int blockX = (int)((x+35)/16);
		int blockY = (int)((y+42)/16);
		
		if(CanvasGame.map.mapLayer2[blockY][blockX]>0) {
			double ang = Math.atan2(100, 1);
			ang+=Math.PI;
			for(int j = 0; j < 20; j++){
				double ang2 = ang - (Math.PI/4)+((Math.PI/2)*Math.random());
				float vel = (float)(100+100*Math.random());
				float vx = (float)(Math.cos(ang2)*vel);
				float vy = (float)(Math.sin(ang2)*vel);
				CanvasGame.effectsList.add(new Effect(x, y, vx, vy, 900, 255, 0, 0));
			}
			isAlive = false;
		}
		
		for(int i = 0; i < CanvasGame.instance.enemiesList.size(); i++) {
			Character c = CanvasGame.instance.enemiesList.get(i);
			if(!c.isEating && !c.isStunned) {
				if(rectCollider(c)) {
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
	
	public void respawn() {
		isAlive = true;
		hasJumped = false;
		numShotsBone = 5;
		x = 100;
		y = 100;
		respawnCountTime = 0;
	}
}
