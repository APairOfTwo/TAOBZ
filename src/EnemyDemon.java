import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class EnemyDemon extends Character {
	float velX;
	float speed = 150;
	
	public EnemyDemon(float x, float y, BufferedImage charset, int charsetX, int charsetY, float velX) {
		super(x, y, charset, charsetX, charsetY, 71, 84, 6, 425, 168);
		this.velX = velX;
	}

	@Override
	public void selfSimulates(long diffTime){	
		super.selfSimulates(diffTime);
		
		oldX = x;
		oldY = y;
		y += gravity * diffTime / 1000.0f;
		
		if(moveDirection == 1) {
			animation = 0;
			x += speed * diffTime / 1000.0f;
		} else if(moveDirection == -1) {
			animation = 1;
			x -= speed * diffTime / 1000.0f;
		} else {
			timeAnimating = 0;
		}
		
		if(hasCollidedWithLayer1((int)((y+75)/16))) {
			y = oldY;
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}

		if((x < 0) || (x >= (CanvasGame.map.Largura << 4) - 50) || hasCollidedWithLayer1((int)((x+10)/16), (int)((x+60)/16), (int)((y+75)/16))) {
			moveDirection *= -1;
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
}
