import java.awt.Color;
import java.awt.Graphics2D;

public class Effect extends Sprite {
	int frame = 0;
	int animation = 0;
	int animeSpeed = 500;
	int timeAnimating = 0;
	float velX = 0;
	float velY = 0;
	int duration = 0;
	int timerActive = 0;
	int r = 255;
	int g = 255;
	int b = 255;

	public Effect(float x, float y, float velX, float velY, int duration, int r, int g, int b){
		super();
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		this.duration = duration;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	@Override
	public void selfSimulates(long diffTime){
		timerActive += diffTime;
		timeAnimating += diffTime;
		frame = (timeAnimating / animeSpeed) % 3;
		x += velX * diffTime / 1000.0f;
		y += velY * diffTime / 1000.0f;
		if(timerActive > duration){
			active = false;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapx, int mapY){
		int alpha = (int)((1.0 - (timerActive / (double)duration)) * 255);
		if(alpha > 255){ alpha = 255; }
		dbg.setColor(new Color(r, g, b, alpha));
		dbg.fillRect((int)(x-mapx-1), (int)(y-mapY-1), 5, 5);
	}
}