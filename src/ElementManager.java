import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ElementManager {
	ArrayList<Element> elementsList;
	int bx;
	int by;
	
	public ElementManager(){
		elementsList = new ArrayList<Element>();
	}
	
	public ElementManager(String elementsSource){
		elementsList = new ArrayList<Element>();
		loadElements(this.getClass().getResourceAsStream(elementsSource));
	}
	
	public void loadElements(InputStream input) {
		InputStreamReader inputReader = new InputStreamReader(input);
		BufferedReader bufferReader = new BufferedReader(inputReader);		
		String str = null;
		
		try {
			while((str = bufferReader.readLine()) != null){
				if(str.charAt(0) != '#'){
					Element ele = new Element();
					ele.decodeElement(str);
					elementsList.add(ele);
				}
			}
		} catch (IOException e) {
			System.out.println("problems in reading the elements");
			e.printStackTrace();
		}
	}
	
	public void decodeElements() {
		for(Element ele : this.elementsList) {
			switch (ele.itemId) {
			case 1:
				CanvasGame.billy = new CharBilly(ele.blockX<<4, ele.blockY<<4, CanvasGame.charsetBilly, 0, 0);
				break;
			case 2:
				CanvasGame.zombie = new CharZombie(ele.blockX<<4, ele.blockY<<4, CanvasGame.charsetZombie, 0, 0);
				break;
			case 3:
				Checkpoint check = new Checkpoint(ele.blockX<<4, ele.blockY<<4);
				CanvasGame.checkpoints.add(check);
				break;
			case 4:
				Character demon = new EnemyDemon(ele.blockX<<4, ele.blockY<<4, CanvasGame.charsetDemon, 0, 0);
				CanvasGame.enemiesList.add(demon);
				break;
			case 5:
				Character gargoyle = new EnemyGargoyle(ele.blockX<<4, ele.blockY<<4, CanvasGame.charsetGargoyle, 0, 0);
				CanvasGame.enemiesList.add(gargoyle);
				break;
			case 6:
				Character vegetarian = new EnemyVegetarian(ele.blockX<<4, ele.blockY<<4, CanvasGame.charsetVegetarian, 0, 0);
				CanvasGame.enemiesList.add(vegetarian);
				break;
			case 7:
				Character berserker = new EnemyBerserker(ele.blockX<<4, ele.blockY<<4, CanvasGame.charsetBerserker, 0, 0);
				CanvasGame.enemiesList.add(berserker);
				break;
			case 8:
				break;
			}
		}
	}
}