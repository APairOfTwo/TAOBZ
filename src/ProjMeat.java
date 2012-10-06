import java.awt.Color;
import java.awt.Graphics2D;


public class ProjMeat extends Projectile {
	
	float flightTime;
	float projectileStartPositionX;
	float projectileStartPositionY;
	float projectileVelocityX = 100;
	float projectileVelocityY = 100;
	float projectileRotation;

	public ProjMeat(float x, float y, float velX, float velY, Object pai) {
		super(x, y, velX, velY, pai);
	}
	
	public void selfSimulates(long diffTime) {
		x += velX*diffTime/1000.0f;
		y += velY*diffTime/1000.0f;
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.setColor(Color.RED);
		dbg.fillOval((int)(x-mapX-2), (int)(y-mapY-2), 8, 8);
	}
}