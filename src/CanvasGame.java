import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import net.java.games.input.Component;
import net.java.games.input.Controller;

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
	
	public static String strElements01 = new String("csv/hell_01.csv");
	public static String strElements02 = new String("csv/hell_02.csv");
	public static String strElements03 = new String("csv/hell_03.csv");
	
	Random rand = new Random();
	
	public static ArrayList<Character> heroes = new ArrayList<Character>();
	public static ArrayList<Character> enemiesList = new ArrayList<Character>();
	public static ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();
	public static ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
	public static ArrayList<Effect> effectsList = new ArrayList<Effect>();
	
	public static boolean B_KEY_LEFT, B_KEY_RIGHT, B_KEY_JUMP, B_KEY_FIRE;
	public static boolean B_JOY_LEFT, B_JOY_RIGHT, B_JOY_JUMP, B_JOY_FIRE;
	public static boolean Z_KEY_LEFT, Z_KEY_RIGHT, Z_KEY_JUMP, Z_KEY_FIRE;
	public static boolean Z_JOY_LEFT, Z_JOY_RIGHT, Z_JOY_JUMP, Z_JOY_FIRE;
	public static boolean MOUSE_PRESSED;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public boolean loading;
	public int loadTime;
	public int mapPositionX;
	public int mapPositionY;
	public int respawnTime = 2000;
	public static int deathCounter = 0;
	public static int projectilesCounter = 0;
	
	public CanvasGame(int levelId) {
		instance = this;
		
		GamePanel.app.setCursor(GamePanel.myCursor);
		
		GamePanel.bgMusic.close();
		
		charsetBilly = GamePanel.loadImage("sprites/billy2.png");
		charsetZombie = GamePanel.loadImage("sprites/zombie4.png");
		charsetDemon = GamePanel.loadImage("sprites/demon.png");
		charsetVegetarian = GamePanel.loadImage("sprites/spritesheet_vegetarian.png");
		charsetGargoyle = GamePanel.loadImage("sprites/gargoyle4.png");
		charsetBerserker = GamePanel.loadImage("sprites/berserker4.png");
		
		MOUSE_X = 0;
		MOUSE_Y = 0;
		MOUSE_CLICK_X = 0;
		MOUSE_CLICK_Y = 0;
		MOUSE_PRESSED = false;
		loading = true;
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		updateGamepads();
		
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
							if(billy.respawnCountTime >= respawnTime) {
								billy.respawnCountTime = 0;
								billy.respawn();
								zombie.respawn();
							}
						}
					} else {
						c.respawnCountTime+=diffTime;
						if(c.respawnCountTime >= respawnTime) {
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
				if(c.respawnBilly && !c.hasBeenActived) {
					billy.respawnCountTime+=diffTime;
					if(billy.respawnCountTime >= 1000) {
						billy.respawnCountTime = 0;
						billy.respawn();
						c.respawnBilly = false;
						c.hasBeenActived = true;
					}
				}
				if(c.respawnZombie && !c.hasBeenActived) {
					zombie.respawnCountTime+=diffTime;
					if(zombie.respawnCountTime >= 1000) {
						zombie.respawnCountTime = 0;
						zombie.respawn();
						c.respawnZombie = false;
						c.hasBeenActived = true;
					}
				}
			}
			for(int i = 0; i < effectsList.size(); i++){
				effectsList.get(i).selfSimulates(diffTime);
				if(effectsList.get(i).active == false){
					effectsList.remove(i);
					i--;
				}
			}
		} else {
			loadTime += diffTime;
			if(loadTime >= 4000) {
				loadTime = 0;
				loading = false;
			}
		}
	}
	
	public void updateGamepads() {
		if(GamePanel.gamepads.size() == 1) {
			GamePanel.gamepads.get(0).poll();
			Component[] comps = GamePanel.gamepads.get(0).getComponents();
			if(GamePanel.isCoop) {
				// 1 controle - coop - zombie
				for(Component comp : comps) {
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 1.0) {
						CanvasGame.Z_JOY_LEFT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0.5) {
						CanvasGame.Z_JOY_RIGHT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0) {
						CanvasGame.Z_JOY_LEFT = false;
						CanvasGame.Z_JOY_RIGHT = false;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 1) {
						CanvasGame.Z_JOY_JUMP = true;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 0) {
						CanvasGame.Z_JOY_JUMP = false;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 1) {
						CanvasGame.Z_JOY_FIRE = true;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 0) {
						CanvasGame.Z_JOY_FIRE = false;
					}
				}
			} else {
				// 1 controle - single - zombie ou billy
				for(Component comp : comps) {
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 1.0) {
						CanvasGame.B_JOY_LEFT = true;
						CanvasGame.Z_JOY_LEFT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0.5) {
						CanvasGame.B_JOY_RIGHT = true;
						CanvasGame.Z_JOY_RIGHT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_LEFT = false;
						CanvasGame.B_JOY_RIGHT = false;
						CanvasGame.Z_JOY_LEFT = false;
						CanvasGame.Z_JOY_RIGHT = false;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 1) {
						CanvasGame.B_JOY_JUMP = true;
						CanvasGame.Z_JOY_JUMP = true;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_JUMP = false;
						CanvasGame.Z_JOY_JUMP = false;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 1) {
						CanvasGame.B_JOY_FIRE = true;
						CanvasGame.Z_JOY_FIRE = true;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_FIRE = false;
						CanvasGame.Z_JOY_FIRE = false;
					}
				}
			}
		}
		
		if(GamePanel.gamepads.size() == 2) {
			GamePanel.gamepads.get(0).poll();
			GamePanel.gamepads.get(1).poll();
			Component[] comps0 = GamePanel.gamepads.get(0).getComponents();
			Component[] comps1 = GamePanel.gamepads.get(1).getComponents();
			if(GamePanel.isCoop) {
				// 2 controles - coop - billy(controle 0) e zombie(controle 1)
				for(Component comp : comps0) {
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 1.0) {
						CanvasGame.B_JOY_LEFT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0.5) {
						CanvasGame.B_JOY_RIGHT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_LEFT = false;
						CanvasGame.B_JOY_RIGHT = false;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 1) {
						CanvasGame.B_JOY_JUMP = true;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_JUMP = false;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 1) {
						CanvasGame.B_JOY_FIRE = true;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_FIRE = false;
					}
				}
				for(Component comp : comps1) {
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 1.0) {
						CanvasGame.Z_JOY_LEFT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0.5) {
						CanvasGame.Z_JOY_RIGHT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0) {
						CanvasGame.Z_JOY_LEFT = false;
						CanvasGame.Z_JOY_RIGHT = false;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 1) {
						CanvasGame.Z_JOY_JUMP = true;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 0) {
						CanvasGame.Z_JOY_JUMP = false;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 1) {
						CanvasGame.Z_JOY_FIRE = true;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 0) {
						CanvasGame.Z_JOY_FIRE = false;
					}
				}
			} else {
				// 2 controles - single - billy(controle 0) ou zombie(controle 0)
				for(Component comp : comps0) {
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 1.0) {
						CanvasGame.B_JOY_LEFT = true;
						CanvasGame.Z_JOY_LEFT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0.5) {
						CanvasGame.B_JOY_RIGHT = true;
						CanvasGame.Z_JOY_RIGHT = true;
					}
					if(comp.getIdentifier().toString() == "pov" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_LEFT = false;
						CanvasGame.B_JOY_RIGHT = false;
						CanvasGame.Z_JOY_LEFT = false;
						CanvasGame.Z_JOY_RIGHT = false;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 1) {
						CanvasGame.B_JOY_JUMP = true;
						CanvasGame.Z_JOY_JUMP = true;
					}
					if(comp.getIdentifier().toString() == "2" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_JUMP = false;
						CanvasGame.Z_JOY_JUMP = false;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 1) {
						CanvasGame.B_JOY_FIRE = true;
						CanvasGame.Z_JOY_FIRE = true;
					}
					if(comp.getIdentifier().toString() == "3" && comp.getPollData() == 0) {
						CanvasGame.B_JOY_FIRE = false;
						CanvasGame.Z_JOY_FIRE = false;
					}
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
		
		if(GamePanel.isCoop) {
			if(!billy.isAlive) {
				
			}
			if(!zombie.isAlive) {
				
			}
		}
		
//		for(Element e : CanvasGame.gameElements.elementsList) {
//			if(e.itemId == 9) {
//				dbg.setColor(Color.CYAN);
//				dbg.fillRect((e.blockX<<4)-map.MapX, (e.blockY<<4)-map.MapY, 16, 64);
//			}
//		}
		
		if(loading) {
			dbg.setColor(Color.BLACK);
			dbg.fillRect(0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
			dbg.drawImage(loadingScreen, 242, 274, 559, 326, 0, 0, loadingScreen.getWidth(), loadingScreen.getHeight(), null);
		}
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)		{ B_KEY_LEFT = true; }
		if(keyCode == KeyEvent.VK_D)		{ B_KEY_RIGHT = true; }
		if(keyCode == KeyEvent.VK_W)		{ B_KEY_JUMP  = true; }
		if(keyCode == KeyEvent.VK_SPACE)	{ B_KEY_FIRE  = true; }
		if(keyCode == KeyEvent.VK_LEFT)		{ Z_KEY_LEFT  = true; }
		if(keyCode == KeyEvent.VK_RIGHT)	{ Z_KEY_RIGHT = true; }
		if(keyCode == KeyEvent.VK_M)		{ billy.haveKey = true; zombie.haveKey = true; }
		if(keyCode == KeyEvent.VK_UP)		{ Z_KEY_JUMP  = true; }
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
		if(keyCode == KeyEvent.VK_A)		{ B_KEY_LEFT  = false; }
		if(keyCode == KeyEvent.VK_D)		{ B_KEY_RIGHT = false; }
		if(keyCode == KeyEvent.VK_W)		{ B_KEY_JUMP  = false; }
		if(keyCode == KeyEvent.VK_SPACE)	{ B_KEY_FIRE  = false; }
		if(keyCode == KeyEvent.VK_LEFT)		{ Z_KEY_LEFT  = false; }
		if(keyCode == KeyEvent.VK_RIGHT)	{ Z_KEY_RIGHT = false; }
		if(keyCode == KeyEvent.VK_M)		{ Z_KEY_RIGHT = false; }
		if(keyCode == KeyEvent.VK_UP)		{ Z_KEY_JUMP  = false; }
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
		Z_KEY_FIRE = true;
	}

	@Override
	public void mouseReleased(MouseEvent m) { 
		MOUSE_PRESSED = false;
		Z_KEY_FIRE = false;
	}
	
	public static void resetControls() {
		B_KEY_LEFT = false;
		B_KEY_RIGHT = false;
		B_KEY_JUMP = false; 
		B_KEY_FIRE = false;
		Z_KEY_LEFT = false;
		Z_KEY_RIGHT = false;
		Z_KEY_JUMP = false; 
		Z_KEY_FIRE = false;
	}
	
	public static void setGameLevel(int levelId) {
		if(levelId == 1) {
			heroes.clear();
			enemiesList.clear();
			projectilesList.clear();
			checkpoints.clear();
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
			checkpoints.clear();
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
			checkpoints.clear();
			gameElements.elementsList.clear();
			tileset = GamePanel.loadImage(strTileset03);
			map = new TileMap(CanvasGame.tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
			map.OpenMap(strMap03);
			gameElements = new ElementManager(strElements03);
			gameElements.decodeElements();
		}
	}
}