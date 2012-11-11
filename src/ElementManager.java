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
	
	public void loadElements(InputStream input){
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
}