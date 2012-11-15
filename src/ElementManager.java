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
	
	public static void decodeElements(String elementsSource) {
		ElementManager elements = new ElementManager();
		elements.loadElements(elements.getClass().getResourceAsStream(elementsSource));
		
		for(Element ele : elements.elementsList) {
			switch (ele.itemId) {
			case 1:
				CanvasGame.billy = new CharBilly(ele.blockX<<4, ele.blockY<<4, CanvasGame.charsetBilly, 0, 0);
				break;
			case 2:
				// TODO - Instanciar o Z
				break;
			case 3:
				// TODO - Checkpoints
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
			}
		}
	}
}