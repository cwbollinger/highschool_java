import java.awt.*;

public class LCARSComponent {

	private int xSize;
	private int ySize;

	private Type type;
	private String name;
	
	private LCARSButton btn;

	public enum Type {
		FRAME, BUTTON
	};

	public LCARSComponent(int xSize, int ySize, String name, Type type) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.type = type;
		this.name = name;

		if (type == Type.FRAME) {

		} else if (type == Type.BUTTON) {
			btn = new LCARSButton(name);
			btn.setPreferredSize(new Dimension(xSize,ySize));
		}
	}
	
	public LCARSButton getButton() {
		if(type == Type.BUTTON){
			return btn;
		} else {
			System.err.println("ERROR: Not Button Component!");
			return null;
		}
	}

}
