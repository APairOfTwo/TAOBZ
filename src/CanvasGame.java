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
	BufferedImage charsetDemon;
	BufferedImage charsetBerserker;
	BufferedImage charsetGargoyle;
	BufferedImage charsetVegetarian;
	public static TileMap map;
	public BufferedImage tileset;
	public static int variables[] = new int[100];
	public static EventsManager events = new EventsManager();
	public static ArrayList<Effect> effectsList = new ArrayList<Effect>();
	public ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();
	Random rand = new Random();
	public static boolean LEFT, RIGHT, JUMP, FIRE;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	public ArrayList<Character> enemiesList = new ArrayList<Character>();
	
	// variáveis temporárias pra facilitar testes de tiro
	public static boolean projIsBone = true;
	public static boolean projIsMeat = false;
	
	@SuppressWarnings("unused")
	public CanvasGame(){
		instance = this;
		charset = GamePanel.loadImage("Mojo.png");
		billy = new CharBilly(100, 100, charset, 0, 0);
		
//		charsetDemon = GamePanel.loadImage("spritesheet_demon.png");
//		Character demon = new EnemyDemon(800, 200, charsetDemon, 0, 0);
//		enemiesList.add(demon);
//		charsetVegetarian = GamePanel.loadImage("spritesheet_vegetarian.png");
//		Character vegetarian = new EnemyVegetarian(500, 200, charsetVegetarian, 0, 0);
//		enemiesList.add(vegetarian);
//		charsetGargoyle = GamePanel.loadImage("spritesheet_gargoyle.png");
//		Character gargoyle = new EnemyGargoyle(800, 100, charsetGargoyle, 0, 0);
//		enemiesList.add(gargoyle);
//		charsetBerserker = GamePanel.loadImage("spritesheet_berserker.png");
//		Character berserker = new EnemyBerserker(3500, 100, charsetBerserker, 0, 0);
//		enemiesList.add(berserker);
		
		tileset = GamePanel.loadImage("maps/hell16.png");
		map = new TileMap(tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
		map.OpenMap("maps/Hell.map");
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
		events.selfDraws(dbg, map.MapX, map.MapY);
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
		if(keyCode == KeyEvent.VK_S)	{ SaveGame.save(); }
		if(keyCode == KeyEvent.VK_L)	{ LoadGame.load(); }
		if(keyCode == KeyEvent.VK_ESCAPE){
			if(CanvasMenu.instance == null) {
				CanvasMenu menu = new CanvasMenu();
			}
			GamePanel.canvasActive = CanvasMenu.instance;
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

}