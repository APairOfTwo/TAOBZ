import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Character extends Sprite {
	BufferedImage charset = null;
	int frame = 0;
	int animation = 0;
	int animeSpeed = 100;
	int timeAnimating = 0;
	int charsetX = 0;
	int charsetY = 0;
	int fireTimer = 0;
	int moveDirection = 1;
	
	int gravity = 500;
	boolean onTheFloor = false;
	boolean hasJumped = false;
	boolean isAlive = true;
	int jumpSpeed = 1100;
	//int jumpReach = 0;
	//int jumpHeight = 150;
	int count;
	int respawnPosX;
	
	boolean destroyBlocks = false;
	
	float radius = 10;
	
	int centerX = 16;
	int centerY = 24;
	
	float oldX = 0;
	float oldY = 0;
	float speed = 300;
	
	int life = 100;
	double angle = 0;
	
	public Character(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		this.x = x;
		this.y = y;
		this.charset = charset;
		this.charsetX = charsetX;
		this.charsetY = charsetY;
	}
	
	@Override
	public void selfSimulates(long diffTime){
		if(this.life <= 0) {
			GamePanel.instance.gameOver = true;
		}
		fireTimer += diffTime;
		timeAnimating += diffTime;
		frame = (timeAnimating / animeSpeed) % 7;
		oldX = x;
		oldY = y;
		y += gravity * diffTime / 1000.0f;
		if(CanvasGame.instance.JUMP && onTheFloor){
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
			jumpSpeed += -1*gravity/15;
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
				CanvasGame.effectsList.add(new Effect(x+26, y+40, vx, vy, 900, 255, 0, 0));
			}
			isAlive = false;
			respawnPosX = blockX*16 - 250;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		int largura = 49;
		int altura = 55;
		int largSpriteSheet = 342;
		int altSpriteSheet = 110;
		dbg.drawImage(charset, (int)(x-mapX), (int)(y-mapY), (int)((x+largura)-mapX), (int)((y+altura)-mapY),
				(largura*frame+charsetX*largSpriteSheet), (altura*animation+charsetY*altSpriteSheet),
				(largura*frame+largura+charsetX*largSpriteSheet), (altura*animation+altura+charsetY*altSpriteSheet), null);
	}
	
	public void respawn() {
		isAlive = true;
		x = respawnPosX;
		y = 100;
		count = 0;
	}
	
	public boolean hasCollidedWithLayer1(int bxi, int bxf, int by) {
		if((CanvasGame.map.mapLayer1[by][bxi]>0) || (CanvasGame.map.mapLayer1[by][bxf]>0)) return true;
		return false;
	}
}