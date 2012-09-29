import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Event {
	BufferedImage sprite;
	int type;
	int posX;
	int posY;
	int Pvar;
	int Pop;
	int Pval;
	int Avar;
	int Aop;
	int Aval;
	int codMapDest;
	int destX;
	int destY;
	boolean active = false;
	static boolean collidedRight = false;
	static boolean collidedLeft = false;
	static boolean collidedUp = false;
	
	public void selfSimulates(long diffTime){	
		active = false;
		colision(CanvasGame.billy);
		if(type == 1){
			if(collidedRight && !collidedUp) { CanvasGame.billy.x = CanvasGame.billy.oldX; }
			if(collidedLeft && !collidedUp)  { CanvasGame.billy.x = CanvasGame.billy.oldX; }
		}
		if(Pvar != -1){
			switch (Pop) {
			case 0:
				if(CanvasGame.variables[Pvar] == Pval){
					active = true;
					return;
				}
				break;
			case 1:
				if(CanvasGame.variables[Pvar] > Pval){
					active = true;
					return;
				}
				break;
			case 2:
				if(CanvasGame.variables[Pvar] < Pval){
					active = true;
					return;
				}
				break;
			}
		} else{
			active = true;
		}
	}
	
	public void selfDraws(Graphics2D dbg, int mapX, int mapY){
		if(active) {
			if(sprite != null) {
				dbg.drawImage(sprite, ((posX<<4)-mapX), ((posY<<4)-mapY), ((posX<<4)+sprite.getWidth())-mapX, ((posY<<4)+sprite.getHeight())-mapY, 0, 0, sprite.getWidth(), sprite.getHeight(), null);
			} else {
				dbg.setColor(Color.yellow);
				dbg.fillRect((int)((posX<<4)-mapX), (int)((posY<<4)-mapY), 32,32);
			}
		}
	}
	
	public void decodeEvent(String s){
		String strs[] = s.split(";");
		type = Integer.parseInt(strs[0]);
		posX = Integer.parseInt(strs[1]);
		posY = Integer.parseInt(strs[2]);
		Pvar = Integer.parseInt(strs[3]);
		Pop = Integer.parseInt(strs[4]);
		Pval = Integer.parseInt(strs[5]);	
		Avar = Integer.parseInt(strs[6]);
		Aop = Integer.parseInt(strs[7]);
		Aval = Integer.parseInt(strs[8]);
		codMapDest = Integer.parseInt(strs[9]);
		destX = Integer.parseInt(strs[10]);
		destY = Integer.parseInt(strs[11]);
	}
	
	public void executeAction(){
		if(Avar != -1){
			switch (Aop){
			case 0:
				CanvasGame.variables[Avar] = Aval;
				break;
			case 1:
				CanvasGame.variables[Avar] += Aval;
				break;
			case 2:
				CanvasGame.variables[Avar] -= Aval;
				break;
			}
		}
	}
	
	public void colision(Character chart) {
		int bx = (posX << 4);
		int by = (posY << 4);
		
		int x = (int)chart.x+13;
		int y = (int)chart.y+5;
		int width = x+20;
		int height = y+42;

		if(width >= bx && width <= bx + 32) { collidedRight = true; }
		else { collidedRight = false; }
		
		if(x <= bx + 32 && x >= bx) { collidedLeft = true; }
		else { collidedLeft = false; }
		
		if(height >= by && height <= by + 32) { collidedUp = true; }
		else { collidedUp = false; }
	}
}