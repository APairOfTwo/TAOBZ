
public class Element {
	int itemId;
	int blockX;
	int blockY;
	
	public void decodeElement(String s){
		String strs[] = s.split(";");
		itemId = Integer.parseInt(strs[0]);
		blockX = Integer.parseInt(strs[1]);
		blockY = Integer.parseInt(strs[2]);
	}

}