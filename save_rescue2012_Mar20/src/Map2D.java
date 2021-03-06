


public class Map2D {
	public static final int SCALE = 30;
	public static final int COLS = (120/SCALE)+2;
	public static final int ROWS = (90/SCALE)+2;
	
	public static final int WALL = 1;		// Wall
	public static final int PLATFORM = 2;	// Platform
	public static final int CAN = 3;		// Can
	public static final int ROBOT = 4;		// Robot
	
	int[][] grid = new int[COLS][ROWS];
	
	public Map2D() {
		
		reset();
		
	}
	
	public Map2D(Map2D copy) {
		for(int i = 0; i < COLS; i++) {
			for(int j = 0; j < ROWS; j++) {
				this.grid[i][j] = copy.grid[i][j];
			}
		}
		
	}
	
	public void reset() {
		for(int i = 0; i < COLS; i++) {
			for(int j = 0; j < ROWS; j++) {
				if(i == 0 || i == (COLS-1) || j == 0 || j == (ROWS-1)) {
					grid[i][j] = WALL;
				} else {
					grid[i][j] = 0;
				}
			}
		}
	}
	public void run() {
		for(int j = ROWS-1; j >= 0; j--) {
			for(int i = 0; i < COLS; i++) {
				System.out.print(grid[i][j]);
				System.out.print(" ");
			}
			System.out.println("");
		}
	}
	
	static int randRange(int num1, int num2) {
		
	    double rand = Math.random();
	    int range = Math.abs(num2-num1);
	    int count = 0;
	        
	    while (rand > 0.0) {
	      rand -=(1.0/range);
	      count++;
	    }
	    count += num1;
	    return(count);    
	} 
	  
	public static void test_main(String[] args) {
		Map2D array = new Map2D();
		array.run();
		
	}
}
