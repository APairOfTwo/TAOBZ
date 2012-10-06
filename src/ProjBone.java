import java.awt.Color;
import java.awt.Graphics2D;


public class ProjBone extends Projectile {

	public ProjBone(float x, float y, float velX, float velY, Object pai) {
		super(x, y, velX, velY, pai);
	}

	@Override
	public void selfSimulates(long diffTime) {
		super.selfSimulates(diffTime);
		x += velX*diffTime/1000.0f;
		y += velY*diffTime/1000.0f;
		bx = (int)((x)/16);
		by = (int)((y)/16);
		
		for(int i = 0; i < CanvasGame.instance.enemiesList.size();i++){
			Character ene = CanvasGame.instance.enemiesList.get(i);
			if(ene!=pai){
				if(rectCollider(ene)) {
					active = false;
					ene.hitByProjectile(this);
					System.out.println("tiro");
					break;
				}
			}
		}
		
		if(CanvasGame.map.mapLayer1[by][bx]>0){
			active = false;
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.setColor(Color.BLACK);
		dbg.fillOval((int)(x-mapX-2), (int)(y-mapY-2), 4, 4);
	}
}
