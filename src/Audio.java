import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javazoom.jl.player.Player;


public class Audio {
    private String filename;
    private Player player;

    public Audio(String filename) {
        this.filename = filename;
    }

    public void close() {
    	if (player != null) {
    		player.close();
    	}
    }

    public void play() {
        try {
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        new Thread() {
            public void run() {
                try {
                	player.play();
                } catch (Exception e) {
                	System.out.println(e);
                }
            }
        }.start();
    }
    
    public static void setVolume(float ctrl) {          
    	try {  
    		Mixer.Info[] infos = AudioSystem.getMixerInfo();    
    		for (Mixer.Info info: infos) {    
    			Mixer mixer = AudioSystem.getMixer(info);  
    			if (mixer.isLineSupported(Port.Info.SPEAKER)) {    
    				Port port = (Port)mixer.getLine(Port.Info.SPEAKER);    
    				port.open();    
    				if (port.isControlSupported(FloatControl.Type.VOLUME)) {    
    					FloatControl volume =  (FloatControl)port.getControl(FloatControl.Type.VOLUME);                    
    					volume.setValue(ctrl);  
    				}    
    				port.close();    
    			}    
    		}    
    	} catch (Exception e) {   
    		System.out.println(e);  
    	}  
    }
}