import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EventsManager {
	ArrayList<Event> eventsList;
	int bx;
	int by;
	
	public EventsManager(){
		eventsList = new ArrayList<Event>();
	}
	
	public void selfSimulates(long diffTime){	
		for(int i = 0; i < eventsList.size(); i++){
			eventsList.get(i).selfSimulates(diffTime);
			bx = eventsList.get(i).posX << 4;
			by = eventsList.get(i).posY << 4;
			if(CanvasGame.instance.mojo.destroyBlocks && eventsList.get(i).type == 1 && intercept(CanvasGame.MOUSE_X, CanvasGame.MOUSE_Y, bx-CanvasGame.map.MapX, by-CanvasGame.map.MapY, (bx+32)-CanvasGame.map.MapX, (by+32)-CanvasGame.map.MapY) && CanvasGame.MOUSE_PRESSED){
				eventsList.get(i).active = false;
			}
			if(!eventsList.get(i).active){
				eventsList.remove(i);
			}
		}
	}
	
	public void selfDrawns(Graphics2D dbg, int mapX, int mapY){
		for(int i = 0; i < eventsList.size(); i++){ 
			eventsList.get(i).selfDraws(dbg, mapX, mapY);
			bx = eventsList.get(i).posX << 4;
			by = eventsList.get(i).posY << 4;
			if(eventsList.get(i).type == 1 && intercept(CanvasGame.MOUSE_X, CanvasGame.MOUSE_Y, bx-mapX, by-mapY, (bx+32)-mapX, (by+32)-mapY)){
				dbg.setColor(Color.RED);
				dbg.drawRect(bx-mapX, by-mapY, 32, 32);
			}
		}
	}
	
	public void loadEvents(InputStream input){
		InputStreamReader inputReader = new InputStreamReader(input);
		BufferedReader bufferReader = new BufferedReader(inputReader);		
		String string = null;
		
		try {
			while((string = bufferReader.readLine()) != null){
				if(string.charAt(0) != '#'){
					Event event = new Event();
					event.decodeEvent(string);
					eventsList.add(event);
				}
			}
		} catch (IOException e) {
			System.out.println("problems in reading the events");
			e.printStackTrace();
		}
	}
	
	public Event checkEvent(int blockX, int blockY){
		for(int i = 0; i < eventsList.size(); i++){
			Event event = eventsList.get(i);
			if(event.active){
				if(event.posX == blockX && event.posY == blockY){
					return event;
				}
			}
		}
		return null;
	}
	
	public void loadSprite() {
		for (int i = 0; i < eventsList.size(); i++){ 
			if(eventsList.get(i).type == 3) {
				eventsList.get(i).sprite = GamePanel.loadImage("chestClosed.png");
			}
			else if(eventsList.get(i).type == 2) {
				eventsList.get(i).sprite = GamePanel.loadImage("lever.png");
			}
		}
	}
	
	public boolean intercept(int objX, int objY, int x, int y, int width, int height){
		if(objX > x && objX < width && objY > y && objY < height){
			return true;
		}
		return false;
	}
}