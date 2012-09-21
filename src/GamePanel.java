import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable {
	
	//mapa 4096 de largura / 16 = 256 tiles
	//mapa 640 de altura / 16 = 40 tiles
	
	// OLA
	
	public static final int PANEL_WIDTH = 1024;
	public static final int PANEL_HEIGHT = 640;
	public static GamePanel instance;
	private Thread gameThread;
	private boolean running = false;
	private BufferedImage buffImage;
	private Graphics2D dbg;
	public static int fps, sfps, fpscount, seconds;
	long diffTime, previousTime;
	public static Canvas canvasActive = null;

	public GamePanel(){
		instance = this;
		setBackground(Color.white);
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setFocusable(true);
		requestFocus();
		
		if (buffImage == null){
			buffImage = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			if (buffImage == null){
				System.out.println("buffered image is null");
				return;
			} else{
				dbg = (Graphics2D)buffImage.getGraphics();
			}
		}

		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent k){
				if(canvasActive != null) {
					canvasActive.keyPressed(k);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent k){
				if(canvasActive != null){
					canvasActive.keyReleased(k);
				}
			}
		});

		addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseMoved(MouseEvent m){
				if(canvasActive != null) {
					canvasActive.mouseMoved(m);
				}
			}

			@Override
			public void mouseDragged(MouseEvent m){
				if(canvasActive != null) {
					canvasActive.mouseDragged(m);
				}
			}
		});

		addMouseListener(new MouseListener(){
			@Override
			public void mouseReleased(MouseEvent m){
				if(canvasActive != null) {
					canvasActive.mouseReleased(m);
				}
			}

			@Override
			public void mousePressed(MouseEvent m){
				if(canvasActive != null) {
					canvasActive.mousePressed(m);
				}
			}

			@Override
			public void mouseExited(MouseEvent m){	}

			@Override
			public void mouseEntered(MouseEvent m){ }

			@Override
			public void mouseClicked(MouseEvent m){ }
		});
		canvasActive = new CanvasGame();
	}

	public void addNotify() {
		super.addNotify();
		startGame();
	}

	private void startGame() {
		if (gameThread == null || !running){
			gameThread = new Thread(this);
			gameThread.start();
		}
	}

	public void stopGame() {
		running = false;
	}

	public void run(){
		running = true;
		seconds = 0;
		diffTime = 0;
		previousTime = System.currentTimeMillis();

		while (running) {
			gameUpdate(diffTime);
			gameRender();
			paintImmediately(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
			try {
				Thread.sleep(0);
			} catch (InterruptedException ex){ }
			diffTime = System.currentTimeMillis() - previousTime;
			previousTime = System.currentTimeMillis();
			if(seconds != ((int) (previousTime / 1000))){
				fps = sfps;
				sfps = 1;
				seconds = ((int) (previousTime / 1000));
			} else{
				sfps++;
			}
		}
		System.exit(0);
	}

	//int timerfps = 0;

	private void gameUpdate(long diffTime){
		if(canvasActive != null) {
			canvasActive.selfSimulates(diffTime);
		}
	}

	private void gameRender(){
		if(canvasActive != null) {
			canvasActive.selfDrawns(dbg);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(buffImage != null){
			g.drawImage(buffImage, 0, 0, null);
		}
	}

	public static void main(String args[]){
		GamePanel ttPanel = new GamePanel();
		JFrame app = new JFrame("Mojo The Treasure Hunter");
		app.getContentPane().add(ttPanel, BorderLayout.CENTER);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.pack();
		app.setResizable(true);
		app.setVisible(true);
	}

	public static BufferedImage loadImage(String source){
		BufferedImage image = null;
		try {
			BufferedImage tmp = ImageIO.read(GamePanel.instance.getClass().getResource(source));
			image = new BufferedImage(tmp.getWidth(), tmp.getHeight(), BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(tmp, 0, 0, null);
			tmp = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}