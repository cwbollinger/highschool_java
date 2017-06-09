import java.awt.event.KeyEvent;


public class ArrowKeyTest {

	public void keyPressed(KeyEvent e) {
	    int keyCode = e.getKeyCode();
	    
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            System.out.println("up"); 
	            break;
	        case KeyEvent.VK_DOWN:
	            System.out.println("down"); 
	            break;
	        case KeyEvent.VK_LEFT:
	            System.out.println("left"); 
	            break;
	        case KeyEvent.VK_RIGHT :
	            System.out.println("right"); 
	            break;
	    }
	} 
	
	public static void main(String[] args)
	{
		
	}
}
