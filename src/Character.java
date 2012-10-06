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
	
	int gravity = 500;
	boolean onTheFloor = false;
	boolean hasJumped = false;
	boolean isAlive = true;
	int jumpSpeed = 1100;
	
	boolean destroyBlocks = false;
	
	float radius = 10;
	
	int centerX = 16;
	int centerY = 24;
	
	float oldX = 0;
	float oldY = 0;
	
	boolean isStunned = false;
	int stunCountTime = 0;
	
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
		dbg.drawOval((int)(x-CanvasGame.map.MapX-radius+centerX), (int)(y-CanvasGame.map.MapY-radius+centerY), (int)(radius*2),(int)(radius*2));
		dbg.drawRect((int)(x-CanvasGame.map.MapX), (int)(y-CanvasGame.map.MapY), this.frameWidth, this.frameHeight);
	}
	
	public boolean hasCollidedWithLayer1(int by) {
		if(CanvasGame.map.mapLayer1[by][0]>0) return true;
		return false;
	}
	
	public boolean hasCollidedWithLayer1(int bxi, int bxf, int by) {
		if((CanvasGame.map.mapLayer1[by][bxi]>0) || (CanvasGame.map.mapLayer1[by][bxf]>0)) return true;
		return false;
	}

	public boolean rectCollider(Character c) {
		if(x + frameWidth < c.x) return false;
        if(x > c.x + c.frameWidth) return false;
        if(y + frameHeight < c.y) return false;
        if(y > c.y + c.frameHeight) return false;
        return true;
	}
	
	public void hitByProjectile(Projectile p) {
		System.out.println(p.getClass());
	}
	
}