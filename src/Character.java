import java.awt.Color;
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
	int frameWidth;
	int frameHeight;
	int charsetWidth;
	int charsetHeight;
	int numberOfFrames;
	float speed;
	
	int gravity = 500;
	boolean onTheFloor = false;
	boolean hasJumped = false;
	boolean isAlive = true;
	int jumpSpeed = 1100;
	
	boolean destroyBlocks = false;
	
	float radius = 24;
	
	int centerX;
	int centerY;
	
	float oldX = 0;
	float oldY = 0;
	
	boolean isStunned = false;
	boolean isEating = false;
	int countTime = 0;
	
	public Character(float x, float y, BufferedImage charset, int charsetX, int charsetY, int frameWidth, 
						int frameHeight, int numberOfFrames, int charsetWidth, int charsetHeight) {
		this.x = x;
		this.y = y;
		this.charset = charset;
		this.charsetX = charsetX;
		this.charsetY = charsetY;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.charsetWidth = charsetWidth;
		this.charsetHeight = charsetHeight;
		this.numberOfFrames = numberOfFrames;
		this.centerX = frameWidth / 2;
		this.centerY = (frameHeight / 2) + 10;
	}
	
	@Override
	public void selfSimulates(long diffTime){
		fireTimer += diffTime;
		timeAnimating += diffTime;
		frame = (timeAnimating / animeSpeed) % numberOfFrames;
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.drawImage(charset, (int)(x-mapX), (int)(y-mapY), (int)((x+frameWidth)-mapX), (int)((y+frameHeight)-mapY),
				(frameWidth*frame+charsetX*charsetWidth), (frameHeight*animation+charsetY*charsetHeight),
				(frameWidth*frame+frameWidth+charsetX*charsetWidth), (frameHeight*animation+frameHeight+charsetY*charsetHeight), null);
		
		dbg.setColor(Color.RED);
		//dbg.drawOval((int)(x-CanvasGame.map.MapX-radius+centerX), (int)(y-CanvasGame.map.MapY-radius+centerY), (int)(radius*2),(int)(radius*2));
		//dbg.drawRect((int)(x-CanvasGame.map.MapX+10), (int)(y-CanvasGame.map.MapY+10), this.frameWidth-20, this.frameHeight-10);
	}
	
	public boolean hasCollidedWithLayer1(int bxi, int bxf, int by) {
		if((CanvasGame.map.mapLayer1[by][bxi]>0) || (CanvasGame.map.mapLayer1[by][bxf]>0)) return true;
		return false;
	}

	public boolean rectCollider(Character c) {
		if(x + (frameWidth-20) < c.x) return false;
        if(x > c.x + (c.frameWidth-20)) return false;
        if(y + (frameHeight-10) < c.y) return false;
        if(y > c.y + (c.frameHeight-10)) return false;
        return true;
	}
	
	public void hitByProjectile(Projectile p) {
		if(p.getClass() == ProjMeat.class){
			isEating = true;
		}
		if(p.getClass() == ProjBone.class){
			isStunned = true;
			speed = 0;
		}
	}
	
}