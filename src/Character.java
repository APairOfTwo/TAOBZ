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
	}
	
	public boolean hasCollidedWithLayer1(int by) {
		if(CanvasGame.map.mapLayer1[by][0]>0) return true;
		return false;
	}
	
	public boolean hasCollidedWithLayer1(int bxi, int bxf) {
		if((CanvasGame.map.mapLayer1[0][bxi]>0) || (CanvasGame.map.mapLayer1[0][bxf]>0)) return true;
		return false;
	}
	
	public boolean hasCollidedWithLayer1(int bxi, int bxf, int by) {
		if((CanvasGame.map.mapLayer1[by][bxi]>0) || (CanvasGame.map.mapLayer1[by][bxf]>0)) return true;
		return false;
	}
}