import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class CharBilly extends Character {
	int fireRate = 800;
	//static float armor = 0.5f;
	//float contador = 0;
	
	public CharBilly(float x, float y, BufferedImage charset, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY);
	}

	@Override
	public void selfSimulates(long diffTime){
		if(CanvasGame.instance.FIRE && fireTimer>fireRate){
			fireTimer = 0;
			float vproj = 1000;
			if(CanvasGame.instance.LEFT || CanvasGame.instance.RIGHT) {
				vproj += speed;
			}
			
			float vx = vproj * moveDirection;
			
			Projectile proj = new ProjBone(x+centerX, y+centerY, vx, 0, this);
			CanvasGame.instance.projectilesList.add(proj);
		}
		
		super.selfSimulates(diffTime);
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
}
