import java.awt.Color;
import java.awt.Cursor;
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
	public static CharZombie zombie;
	public static TileMap map;
	
	public static BufferedImage charsetBilly;
	public static BufferedImage charsetZombie;
	public static BufferedImage charsetDemon;
	public static BufferedImage charsetBerserker;
	public static BufferedImage charsetGargoyle;
	public static BufferedImage charsetVegetarian;
	public static BufferedImage tileset;
	public BufferedImage loadingScreen = GamePanel.loadImage("backgrounds/loading_background.png");
	
	public static String strMap01 = new String("maps/hell_01.map");
	public static String strMap02 = new String("maps/hell_02.map");
	public static String strMap03 = new String("maps/hell_03.map");
	
	public static String strTileset01 = new String("maps/hell_tileset.png");
	public static String strTileset02 = new String("maps/hell_tileset2.png");
	public static String strTileset03 = new String("maps/hell_tileset3.png");
	
	public static String strElements01 = new String("csv/demon.csv");
	public static String strElements02 = new String("csv/hell_02.csv");
	public static String strElements03 = new String("csv/hell_03.csv");
	
	Random rand = new Random();
	
	public static ArrayList<Character> heroes = new ArrayList<Character>();
	public static ArrayList<Character> enemiesList = new ArrayList<Character>();
	public static ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();
	public static ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
	public static ArrayList<Effect> effectsList = new ArrayList<Effect>();
	
	public static boolean B_LEFT, B_RIGHT, B_JUMP, B_FIRE;
	public static boolean Z_LEFT, Z_RIGHT, Z_JUMP, Z_FIRE;
	public static boolean MOUSE_PRESSED;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public boolean loading;
	public int mapPositionX;
	public int mapPositionY;	
	
	public CanvasGame(int levelId) {
		instance = this;
		
		GamePanel.app.setCursor(GamePanel.myCursor);
		
		GamePanel.bgMusic.close();
		
		charsetBilly = GamePanel.loadImage("sprites/billy.png");
		charsetZombie = GamePanel.loadImage("sprites/zombie.png");
		charsetDemon = GamePanel.loadImage("sprites/spritesheet_demon.png");
		charsetVegetarian = GamePanel.loadImage("sprites/spritesheet_vegetarian.png");
		charsetGargoyle = GamePanel.loadImage("sprites/spritesheet_gargoyle.png");
		charsetBerserker = GamePanel.loadImage("sprites/spritesheet_berserker.png");
		
		MOUSE_X = 0;
		MOUSE_Y = 0;
		MOUSE_CLICK_X = 0;
		MOUSE_CLICK_Y = 0;
		MOUSE_PRESSED = false;
		loading = true;
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(!loading) {
			for(Character c : heroes) {
				if(c.isAlive) {
					c.selfSimulates(diffTime);
					if(GamePanel.isCoop) {
						if(billy.isAlive && zombie.isAlive) {
							if(billy.x < zombie.x) {
								mapPositionX = (int)billy.x;
							} else {
								mapPositionX = (int)zombie.x;
							}
							if(billy.y > zombie.y) {
								mapPositionY = (int)billy.y;
							} else {
								mapPositionY = (int)zombie.y;
							}
						} else {
							if(billy.isAlive) {
								mapPositionX = (int)billy.x;
								mapPositionY = (int)billy.y;
							}
							if(zombie.isAlive) {
								mapPositionX = (int)zombie.x;
								mapPositionY = (int)zombie.y;
							}
						}
					} else {
						mapPositionX = (int)c.x;
						mapPositionY = (int)c.y;
					}
					map.Positions(mapPositionX-GamePanel.PANEL_WIDTH/2, mapPositionY-GamePanel.PANEL_HEIGHT/2);
					
				} else {
					if(GamePanel.isCoop) {
						if(!billy.isAlive && !zombie.isAlive) {
							billy.respawnCountTime+=diffTime;
							if(billy.respawnCountTime >= 3000) {
								billy.respawnCountTime = 0;
								billy.respawn();
								zombie.respawn();
							}
						}
					} else {
						c.respawnCountTime+=diffTime;
						if(c.respawnCountTime >= 3000) {
							c.respawnCountTime = 0;
							c.respawn();
						}
					}
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
			for(Checkpoint c : checkpoints) {
				c.selfSimulates(diffTime);
			}
			for(int i = 0; i < effectsList.size(); i++){
				effectsList.get(i).selfSimulates(diffTime);
				if(effectsList.get(i).active == false){
					effectsList.remove(i);
					i--;
				}
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
		for(Checkpoint c : checkpoints) {
			c.selfDraws(dbg, map.MapX, map.MapY);
		}
		for(int i = 0; i < effectsList.size(); i++){
			effectsList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
		for(Character c : heroes) {
			c.selfDraws(dbg, map.MapX, map.MapY);
		}
		
		for(Element e : CanvasGame.gameElements.elementsList) {
			if(e.itemId == 9) {
				dbg.setColor(Color.CYAN);
				dbg.fillRect((e.blockX<<4)-map.MapX, (e.blockY<<4)-map.MapY, 16, 64);
			}
		}
		
		if(loading) {
			dbg.setColor(Color.BLACK);
			dbg.fillRect(0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
			dbg.drawImage(loadingScreen, 242, 274, 559, 326, 0, 0, loadingScreen.getWidth(), loadingScreen.getHeight(), null);
		}
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)		{ B_LEFT  = true; }
		if(keyCode == KeyEvent.VK_D)		{ B_RIGHT = true; }
		if(keyCode == KeyEvent.VK_W)		{ B_JUMP  = true; }
		if(keyCode == KeyEvent.VK_SPACE)	{ B_FIRE  = true; }
		if(keyCode == KeyEvent.VK_LEFT)		{ Z_LEFT  = true; }
		if(keyCode == KeyEvent.VK_RIGHT)	{ Z_RIGHT = true; }
		if(keyCode == KeyEvent.VK_M)		{ Z_RIGHT = true; }
		if(keyCode == KeyEvent.VK_UP)		{ Z_JUMP  = true; }
		if(keyCode == KeyEvent.VK_S)		{ loading = false; }
		if(keyCode == KeyEvent.VK_F1)		{ SaveGame.save(); }
		if(keyCode == KeyEvent.VK_L)		{ LoadGame.load(); }
		if(keyCode == KeyEvent.VK_ESCAPE) {
			GamePanel.app.setCursor(Cursor.getDefaultCursor());
			if(CanvasPause.instance == null) {
				CanvasPause pause = new CanvasPause();
			}
			GamePanel.canvasActive = CanvasPause.instance;
		}
	}

	@Override
	public void keyReleased(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)		{ B_LEFT  = false; }
		if(keyCode == KeyEvent.VK_D)		{ B_RIGHT = false; }
		if(keyCode == KeyEvent.VK_W)		{ B_JUMP  = false; billy.jumpSpeed = billy.jumpSpeed / 2; }
		if(keyCode == KeyEvent.VK_SPACE)	{ B_FIRE  = false; }
		if(keyCode == KeyEvent.VK_LEFT)		{ Z_LEFT  = false; }
		if(keyCode == KeyEvent.VK_RIGHT)	{ Z_RIGHT = false; }
		if(keyCode == KeyEvent.VK_M)		{ Z_RIGHT = false; }
		if(keyCode == KeyEvent.VK_UP)		{ Z_JUMP  = false; zombie.jumpSpeed = zombie.jumpSpeed / 2; }
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		MOUSE_X = m.getX();
		MOUSE_Y = m.getY();
	}

	@Override
	public void mouseDragged(MouseEvent m) { }
	
	@Override
	public void mousePressed(MouseEvent m) {
		MOUSE_PRESSED = true;
		MOUSE_CLICK_X = m.getX();
		MOUSE_CLICK_Y = m.getY();
		Z_FIRE = true;
	}

	@Override
	public void mouseReleased(MouseEvent m) { 
		MOUSE_PRESSED = false;
		Z_FIRE = false;
	}
	
	public static void resetControls() {
		B_LEFT = false;
		B_RIGHT = false;
		B_JUMP = false; 
		B_FIRE = false;
		Z_LEFT = false;
		Z_RIGHT = false;
		Z_JUMP = false; 
		Z_FIRE = false;
	}
	
	public static void setGameLevel(int levelId) {
		if(levelId == 1) {
			heroes.clear();
			enemiesList.clear();
			projectilesList.clear();
			gameElements.elementsList.clear();
			tileset = GamePanel.loadImage(strTileset01);
			map = new TileMap(CanvasGame.tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
			map.OpenMap(strMap01);
			gameElements = new ElementManager(strElements01);
			gameElements.decodeElements();
		}
		if(levelId == 2) {
			heroes.clear();
			enemiesList.clear();
			projectilesList.clear();
			gameElements.elementsList.clear();
			tileset = GamePanel.loadImage(strTileset02);
			map = new TileMap(CanvasGame.tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
			map.OpenMap(strMap02);
			gameElements = new ElementManager(strElements02);
			gameElements.decodeElements();
		}
		if(levelId == 3) {
			heroes.clear();
			enemiesList.clear();
			projectilesList.clear();
			gameElements.elementsList.clear();
			tileset = GamePanel.loadImage(strTileset03);
			map = new TileMap(CanvasGame.tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
			map.OpenMap(strMap03);
			gameElements = new ElementManager(strElements03);
			gameElements.decodeElements();
		}
	}
}