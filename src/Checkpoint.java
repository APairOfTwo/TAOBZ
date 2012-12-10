import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Checkpoint extends Sprite {
	int x, y;
	boolean isActive;
	boolean hasBeenActived = false;
	boolean respawnBilly, respawnZombie;
	BufferedImage checkpointOff = GamePanel.loadImage("sprites/checkpointOff.png");
	BufferedImage checkpointOn = GamePanel.loadImage("sprites/checkpointOn.png");
	BufferedImage checkpointMsg = GamePanel.loadImage("sprites/checkpointMsg.png");
	int msgX, msgY, msgWidth, msgHeight;
	boolean showMsg = false;
	long msgTime = 0;
	int collidedCounter = 0;
	
	public Checkpoint(int x, int y) {
		this.x = x;
		this.y = y;
		this.isActive = false;
		this.respawnBilly = false;
		this.respawnZombie = false;
		this.msgX = (x - (checkpointMsg.getWidth()/2)) + (checkpointOn.getWidth()/2);
		this.msgY = y - (checkpointOn.getHeight());
		this.msgWidth = (msgX + checkpointMsg.getWidth());
		this.msgHeight = (msgY + checkpointMsg.getHeight());
	}

	@Override
	public void selfSimulates(long diffTime) {
		for(Character c : CanvasGame.heroes) {
			if(c.getBounds().intersects(x-CanvasGame.map.MapX, y-CanvasGame.map.MapY, 16, 64)) {
				collidedCounter = 1;
				if(!isActive && collidedCounter == 1) {
					showMsg = true;
					collidedCounter = 0;
				}
				isActive = true;
				if(GamePanel.isCoop) {
					if(!CanvasGame.billy.isAlive) {
						respawnBilly = true;
					} else if(!CanvasGame.zombie.isAlive) {
						respawnZombie = true;
					}
				}
			}
		}
		if(showMsg) {
			msgTime += diffTime;
			msgX -= 0.02;
			msgY -= 0.02;
			msgWidth -= 0.02;
			msgHeight -= 0.02;
		}
		if(msgTime >= 2000) {
			showMsg = false;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		if(this.isActive) {
			dbg.drawImage(checkpointOn, (int)(x-mapX), (int)(y-mapY), (int)((x+checkpointOn.getWidth())-mapX), (int)((y+checkpointOn.getHeight())-mapY), 0, 0, checkpointOn.getWidth(), checkpointOn.getHeight(), null);
		} else {
			dbg.drawImage(checkpointOff, (int)(x-mapX), (int)(y-mapY), (int)((x+checkpointOff.getWidth())-mapX), (int)((y+checkpointOff.getHeight())-mapY), 0, 0, checkpointOff.getWidth(), checkpointOff.getHeight(), null);
		}
		if(showMsg) {
			dbg.drawImage(checkpointMsg, (int)(msgX-mapX), (int)(msgY-mapY), (int)(msgWidth-mapX), (int)(msgHeight-mapY), 0, 0, checkpointMsg.getWidth(), checkpointMsg.getHeight(), null);
		}
	}
}