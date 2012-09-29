import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class CanvasGame extends Canvas {
	public static CanvasGame instance = null;
	static public CharBilly billy;
	BufferedImage charset;
	public static TileMap map;
	public BufferedImage tileset;
	public static int variables[] = new int[100];
	public static EventsManager events = new EventsManager();
	public static ArrayList<Effect> effectsList = new ArrayList<Effect>();
	public ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();
	Random rand = new Random();
	boolean LEFT, RIGHT, JUMP, FIRE;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	
	@SuppressWarnings("unused")
	public CanvasGame(){
		instance = this;
		charset = GamePanel.loadImage("Mojo.png");
		billy = new CharBilly(100, 100, charset, 0, 0);
		tileset = GamePanel.loadImage("area01_tileset.png");
		
		map = new TileMap(tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
		map.OpenMap("stage_intro.map");
		events.loadEvents(this.getClass().getResourceAsStream("eventosStageIntro.csv"));
		events.loadSprite();
		MOUSE_X = 0;
		MOUSE_Y = 0;
		MOUSE_CLICK_X = 0;
		MOUSE_CLICK_Y = 0;
		MOUSE_PRESSED = false;
	}
	
	@Override
	public void selfSimulates(long diffTime){
		if(billy.isAlive){
			billy.selfSimulates(diffTime);
			map.Positions((int)billy.x-GamePanel.PANEL_WIDTH/2, (int)billy.y-GamePanel.PANEL_HEIGHT/2);
			events.selfSimulates(diffTime);
		} else {	
			billy.count+=diffTime;
			if(billy.count >= 3000) {
				billy.respawn();
			}
		}
		for(int i = 0; i < projectilesList.size(); i++){
			projectilesList.get(i).selfSimulates(diffTime);
			if(!projectilesList.get(i).active){
				projectilesList.remove(i);
				i--;
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
	public void selfDraws(Graphics2D dbg){
		map.selfDraws(dbg);
		events.selfDraws(dbg, map.MapX, map.MapY);
		billy.selfDraws(dbg, map.MapX, map.MapY);
		for(int i = 0; i < projectilesList.size(); i++){
			projectilesList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
		System.out.println(projectilesList.size());
		for(int i = 0; i < effectsList.size(); i++){
			effectsList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)		{ LEFT = true; }
		if(keyCode == KeyEvent.VK_D)		{ RIGHT = true; }
		if(keyCode == KeyEvent.VK_W)		{ JUMP = true; }
		if(keyCode == KeyEvent.VK_SPACE)	{ FIRE = true; }
	}

	@Override
	public void keyReleased(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)     { LEFT = false; }
		if(keyCode == KeyEvent.VK_D)     { RIGHT = false; }
		if(keyCode == KeyEvent.VK_W)     { JUMP = false; }
		if(keyCode == KeyEvent.VK_SPACE) { FIRE = false; }
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		MOUSE_X = m.getX();
		MOUSE_Y = m.getY();
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		//MOUSE_X = m.getX();
		//MOUSE_Y = m.getY();
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