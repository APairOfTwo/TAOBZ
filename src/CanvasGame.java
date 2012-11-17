import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class CanvasGame extends Canvas {
	public static CanvasGame instance = null;
	public static ElementManager gameElements = new ElementManager();
	public static CharBilly billy;
	public static TileMap map;
	
	public static BufferedImage charsetBilly;
	public static BufferedImage charsetDemon;
	public static BufferedImage charsetBerserker;
	public static BufferedImage charsetGargoyle;
	public static BufferedImage charsetVegetarian;
	public static BufferedImage tileset;
	
	public static String strMap01 = new String("maps/stage_intro.map");
	public static String strMap02 = new String();
	public static String strMap03 = new String();
	
	public static String strTileset01 = new String("maps/area01_tileset.png");
	public static String strTileset02 = new String();
	public static String strTileset03 = new String();
	
	public static String strElements01 = new String("csv/teste2.csv");
	public static String strElements02 = new String();
	public static String strElements03 = new String();
	
	Random rand = new Random();
	public static int variables[] = new int[100];
	
	public static ArrayList<Character> enemiesList = new ArrayList<Character>();
	public static ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();
	public static ArrayList<Effect> effectsList = new ArrayList<Effect>();
	
	public static boolean projIsBone = true;
	public static boolean projIsMeat = false;
	public static boolean LEFT, RIGHT, JUMP, FIRE;
	public static boolean MOUSE_PRESSED;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	
	public CanvasGame(){
		instance = this;
		
		charsetBilly = GamePanel.loadImage("sprites/mojo.png");
		charsetDemon = GamePanel.loadImage("sprites/spritesheet_demon.png");
		charsetVegetarian = GamePanel.loadImage("sprites/spritesheet_vegetarian.png");
		charsetGargoyle = GamePanel.loadImage("sprites/spritesheet_gargoyle.png");
		charsetBerserker = GamePanel.loadImage("sprites/spritesheet_berserker.png");
		
		MOUSE_X = 0;
		MOUSE_Y = 0;
		MOUSE_CLICK_X = 0;
		MOUSE_CLICK_Y = 0;
		MOUSE_PRESSED = false;
		
		setGameLevel(1);
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
		for(Element e : gameElements.elementsList) {
			if(e.itemId == 8) {
				dbg.setColor(Color.MAGENTA);
				dbg.fillRect((e.blockX<<4)-map.MapX, (e.blockY<<4)-map.MapY, 16, 64);
			}
		}
		
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
		if(keyCode == KeyEvent.VK_S)		{ setGameLevel(1); }
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
	
	public static void setGameLevel(int levelId) {
		if(levelId == 1) {
			enemiesList.clear();
			projectilesList.clear();
			gameElements.elementsList.clear();
			tileset = GamePanel.loadImage(strTileset01);
			map = new TileMap(CanvasGame.tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
			map.OpenMap(strMap01);
			gameElements = new ElementManager(strElements01);
			gameElements.decodeElements();
		}
	}
}