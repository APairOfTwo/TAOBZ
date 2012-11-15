import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class GameButton {
	private static final int ON = 1;
	private static final int OFF = 0;
	
	private BufferedImage btnOn;
	private BufferedImage btnOff;
	
	private int x;
    private int y;
    private int buttonWidth;
    private int buttonHeight;
    private int state;

    public GameButton(int x, int y, String imageNameOn, String imageNameOff) {
    	try {
			setBtnOn(ImageIO.read(getClass().getResource(imageNameOn)));
			setBtnOff(ImageIO.read(getClass().getResource(imageNameOff)));
		}
		catch(IOException ex) {
			System.out.println("Load Image error:");
		}
    	this.state = OFF;
        this.x = x;
        this.y = y;
        this.buttonWidth = btnOn.getWidth();
        this.buttonHeight = btnOn.getHeight();
    }
    
    public boolean isMouseOver(double mouseX, double mouseY) {
        return ((mouseX > x)&&(mouseX < x+buttonWidth) && (mouseY > y)&&(mouseY < y+buttonHeight));
    }

    public void selfDraws(Graphics g) {
    	switch(state){
    	case OFF:
    		g.drawImage(btnOff, x, y, null);
    		break;
    	case ON:
    		g.drawImage(btnOn, x, y, null);
    		break;
    	}
    }

	public void setBtnOn(BufferedImage btnOn) {
		this.btnOn = btnOn;
	}

	public void setBtnOff(BufferedImage btnOff) {
		this.btnOff = btnOff;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
}