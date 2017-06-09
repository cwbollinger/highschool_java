
public class TextMap {
	protected int mapsize;
	protected char map[][];
	
	TextMap(int a) {
		mapsize = a;
		map = new char [mapsize][mapsize];
	}
	
	/* This is the old version of drawMap, still works for terminals
	public void drawMap(char[][] map)
	{
		for(int y = 0; y < mapsize; y++) {
			for(int x = 0; x < mapsize; x++) {
				System.out.print(map[x][y]);
			}
			System.out.println();
		}
	}
  	*/
	
	public String drawMap()
	{
		StringBuffer temp = new StringBuffer((mapsize*mapsize)+mapsize);
		for(int y = 0; y < mapsize; y++) {
			for(int x = 0; x < mapsize; x++) {
				temp.append(map[x][y]);
			}
			temp.append('\n');
		}
		return temp.toString();
		
	}
  	
	
	public void initializeRandomMap()
	{
		double rand;
		for(int y = 0; y < mapsize; y++) {
			for(int x = 0; x < mapsize; x++) {
				rand = Math.random();
				if (rand >= 0.65){
					map[x][y] = 'M';
				} else {
					map[x][y] = '~';
				}
			}
	    }  
	}
  
	public void initializeFlatMap()
	{ 
		for(int y = 0; y < mapsize; y++) {
			for(int x = 0; x < mapsize; x++) {
				map[x][y] = '+';
			}
		} 
	} 	
}
