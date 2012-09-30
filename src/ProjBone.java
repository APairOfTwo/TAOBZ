import java.awt.Color;
import java.awt.Graphics2D;


public class ProjBone extends Projectile {

	public ProjBone(float x, float y, float velX, float velY, Object pai) {
		super(x, y, velX, velY, pai);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.setColor(Color.BLACK);
		dbg.fillOval((int)(x-mapX-2), (int)(y-mapY-2), 4, 4);
	}
}
