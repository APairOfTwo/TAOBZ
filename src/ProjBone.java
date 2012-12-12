import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class ProjBone extends Projectile {
	float boneRadius = 10;

	public ProjBone(float x, float y, float velX, float velY, BufferedImage bmp, Object pai) {
		super(x, y, velX, velY, bmp, pai);
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
				if(circleCollision(ene, boneRadius)) {
					active = false;
					ene.hitByProjectile(this);
					break;
				}
			}
		}
		if(bx > 0 && bx < CanvasGame.map.Largura) {
			if(CanvasGame.map.mapLayer1[by][bx]>0){
				active = false;
			}
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		//dbg.setColor(Color.BLACK);
		dbg.drawImage(bmp, (int)(x-mapX), (int)(y-mapY), (int)((x+frameWidth)-mapX), (int)((y+frameHeight)-mapY), 0, 0, frameWidth, frameHeight, null);
	}
}
