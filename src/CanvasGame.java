import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class CanvasGame extends Canvas {
	public static CanvasGame instance = null;
	static public CharBilly billy;
	static BufferedImage charsetBilly;
	static BufferedImage charsetDemon;
	static BufferedImage charsetBerserker;
	static BufferedImage charsetGargoyle;
	static BufferedImage charsetVegetarian;
	public static TileMap map;
	public static BufferedImage tileset;
	public static int variables[] = new int[100];
	public static ArrayList<Effect> effectsList = new ArrayList<Effect>();
	public static ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();
	Random rand = new Random();
	public static boolean LEFT, RIGHT, JUMP, FIRE;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	public static ArrayList<Character> enemiesList = new ArrayList<Character>();
	public ElementManager elements = new ElementManager();
	
	// variáveis temporárias pra facilitar testes de tiro
	public static boolean projIsBone = true;
	public static boolean projIsMeat = false;
	
	@SuppressWarnings("unused")
	public CanvasGame(){
		instance = this;
		
		charsetBilly = GamePanel.loadImage("Mojo.png");
		charsetDemon = GamePanel.loadImage("spritesheet_demon.png");
		charsetVegetarian = GamePanel.loadImage("spritesheet_vegetarian.png");
		charsetGargoyle = GamePanel.loadImage("spritesheet_gargoyle.png");
		charsetBerserker = GamePanel.loadImage("spritesheet_berserker.png");
		
		setGameLevel(1, "maps/area01_tileset.png", "maps/stage_intro.map", "csv/stage01.csv");
		
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
		} else {	
			billy.respawnCountTime+=diffTime;
			if(billy.respawnCountTime >= 3000) {
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
		for(int i = 0; i < enemiesList.size(); i++) {
			enemiesList.get(i).selfSimulates(diffTime);
			if(!enemiesList.get(i).isAlive){
				enemiesList.remove(i);
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
		
		for(int i = 0; i < projectilesList.size(); i++){
			projectilesList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
		for(int i = 0; i < enemiesList.size(); i++) {
			enemiesList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
		for(int i = 0; i < effectsList.size(); i++){
			effectsList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
		billy.selfDraws(dbg, map.MapX, map.MapY);
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)		{ LEFT = true; }
		if(keyCode == KeyEvent.VK_D)		{ RIGHT = true; }
		if(keyCode == KeyEvent.VK_W)		{ JUMP = true; }
		if(keyCode == KeyEvent.VK_SPACE)	{ FIRE = true; }
		if(keyCode == KeyEvent.VK_S)		{ setGameLevel(1, "maps/hell16.png", "maps/Hell.map", "csv/stage_intro.csv"); }
		if(keyCode == KeyEvent.VK_F1)		{ SaveGame.save(); }
		if(keyCode == KeyEvent.VK_L)		{ LoadGame.load(); }
		if(keyCode == KeyEvent.VK_ESCAPE) {
			if(CanvasPause.instance == null) {
				CanvasPause menu = new CanvasPause();
			}
			GamePanel.canvasActive = CanvasPause.instance;
		}
		
		if(keyCode == KeyEvent.VK_1)		{ projIsBone = true; projIsMeat = false; }
		if(keyCode == KeyEvent.VK_2)		{ projIsBone = false; projIsMeat = true; }
	}

	@Override
	public void keyReleased(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)     { LEFT = false; }
		if(keyCode == KeyEvent.VK_D)     { RIGHT = false; }
		if(keyCode == KeyEvent.VK_W)     { JUMP = false; billy.jumpSpeed = billy.jumpSpeed / 2; }
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
	
	public static void setGameLevel(int levelId, String tilesetSource, String mapSource, String elementsSource) {
		if(levelId == 1) {
			enemiesList.clear();
			projectilesList.clear();
			tileset = GamePanel.loadImage(tilesetSource);
			map = new TileMap(CanvasGame.tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
			map.OpenMap(mapSource);
			ElementManager.decodeElements(elementsSource);
		}
	}

}