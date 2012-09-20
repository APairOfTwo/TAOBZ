import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class CanvasStage01 extends Canvas {
	public static CanvasStage01 instance = null;
	public Character mojo;
	BufferedImage charset;
	public static TileMap map;
	public BufferedImage tileset;
	public static int variables[] = new int[100];
	public static EventsManager events = new EventsManager();
	public static ArrayList<Effect> effectsList = new ArrayList<Effect>();
	Random rand = new Random();
	boolean LEFT, RIGHT, UP, JUMP, TELEPORT;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	
	@SuppressWarnings("unused")
	public CanvasStage01(){
		instance = this;
		charset = GamePanel.loadImage("Mojo.png");
		mojo = new Character(10, 10, charset, 0, 0);
		tileset = GamePanel.loadImage("area02_tileset.png");
		
		map = new TileMap(tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
		map.OpenMap("stage01.map");
		//events.loadEvents(this.getClass().getResourceAsStream("eventos.csv"));
		//events.loadSprite();
		MOUSE_X = 0;
		MOUSE_Y = 0;
		MOUSE_CLICK_X = 0;
		MOUSE_CLICK_Y = 0;
		MOUSE_PRESSED = false;
	}
	
	@Override
	public void selfSimulates(long diffTime){
		if(mojo.isAlive){
			mojo.selfSimulates(diffTime);
			map.Positions((int)mojo.x-GamePanel.PANEL_WIDTH/2,(int)mojo.y-GamePanel.PANEL_HEIGHT/2);
			events.selfSimulates(diffTime);
		} else {	
			mojo.count+=diffTime;
			if(mojo.count >= 3000) {
				mojo.respawn();
			}
		}
		for(int i = 0; i < effectsList.size(); i++){
			effectsList.get(i).selfSimulates(diffTime);
			if(effectsList.get(i).active == false){
				effectsList.remove(i);
				i--;
			}
		}
	}

	@Override
	public void selfDrawns(Graphics2D dbg){
		map.selfDrawns(dbg);
		
		dbg.setColor(Color.GRAY);
		dbg.setFont(new Font("JFRockOutcrop", Font.PLAIN, 48));
		dbg.drawString("STAGE 01", (int)((22 << 4) - map.MapX), (int)((7 << 4) - map.MapY));
		
		events.selfDrawns(dbg, map.MapX, map.MapY);
		
		mojo.selfDrawns(dbg, map.MapX, map.MapY);
		
		for(int i = 0; i < effectsList.size(); i++){
			effectsList.get(i).selfDrawns(dbg, map.MapX, map.MapY);
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)     { LEFT = true; }
		if(keyCode == KeyEvent.VK_D)     { RIGHT = true; }
		if(keyCode == KeyEvent.VK_W)     { UP = true; }
		if(keyCode == KeyEvent.VK_SPACE) { JUMP = true; }
		if(keyCode == KeyEvent.VK_1) { TELEPORT = true; }
	}

	@Override
	public void keyReleased(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)     { LEFT = false; }
		if(keyCode == KeyEvent.VK_D)     { RIGHT = false; }
		if(keyCode == KeyEvent.VK_W)     { UP = false; }
		if(keyCode == KeyEvent.VK_SPACE) { JUMP = false; }
		if(keyCode == KeyEvent.VK_SHIFT) { TELEPORT = false; }
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		MOUSE_X = m.getX();
		MOUSE_Y = m.getY();
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		MOUSE_X = m.getX();
		MOUSE_Y = m.getY();
	}

	@Override
	public void mouseReleased(MouseEvent m) { 
		MOUSE_PRESSED = false;
	}
	@Override
	public void mousePressed(MouseEvent m) {
		MOUSE_PRESSED = true;
		MOUSE_CLICK_X = m.getX();
		MOUSE_CLICK_Y = m.getY();
	}

}