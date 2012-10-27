import java.awt.Color;
import java.awt.Graphics2D;


public class ProjBone extends Projectile {

	public ProjBone(float x, float y, float velX, float velY, Object pai) {
		super(x, y, velX, velY, pai);
	}

	@Override
	public void selfSimulates(long diffTime) {
		super.selfSimulates(diffTime);
		
		if(CanvasGame.billy.x <= GamePanel.PANEL_WIDTH/2) {
			if(x > GamePanel.PANEL_WIDTH) {
				active = false;
			}
		} else if(CanvasGame.billy.x >= CanvasGame.map.Largura*16 - GamePanel.PANEL_WIDTH/2) {
			if(x < CanvasGame.map.Largura*16 - GamePanel.PANEL_WIDTH) {
				active = false;
			}
		} else {
			if((x < CanvasGame.billy.x - GamePanel.PANEL_WIDTH/2) || (x > CanvasGame.billy.x + GamePanel.PANEL_WIDTH/2)) {
				active = false;
			}
		}
		
		x += velX*diffTime/1000.0f;
		y += velY*diffTime/1000.0f;
		bx = (int)((x)/16);
		by = (int)((y)/16);
		
		for(int i = 0; i < CanvasGame.instance.enemiesList.size();i++){
			Character ene = CanvasGame.instance.enemiesList.get(i);
			if(ene!=pai){
				if(circleCollision(ene)) {
					active = false;
					ene.hitByProjectile(this);
					break;
				}
			}
		}
		if(bx < 256) {
			if(CanvasGame.map.mapLayer1[by][bx]>0){
				active = false;
			}
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.setColor(Color.BLACK);
		dbg.fillOval((int)(x-mapX-2), (int)(y-mapY-2), 4, 4);
	}
}
