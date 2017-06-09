import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

public class Guard {

	public static final double baseSpeed = 2.0;

	private double x;
	private double y;
	public double heading;
	Rectangle rect;
	Image guardStandImage;
	
	private Item heldItem;
	
	AffineTransform affineTransform = new AffineTransform(); 



	public Guard(int x, int y) {
		this.x = x;
		this.y = y;
		guardStandImage = new ImageIcon("Images/Player_Stand.png").getImage();

		rect = new Rectangle(x-6, y-6, 23, 23);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void facePoint(Point p) {
		double angle = HeistPanel.findAngle(getX(),getY(),p.x,p.y); 
		heading = angle;
		affineTransform.setToTranslation(getX(), getY());
		affineTransform.rotate(angle+(Math.PI/2), 11, 7);
	}
	

	public void moveUp(double n) {
		y -= n;
		rect.setLocation((int) Math.round(x), (int) (Math.round(y)-6));
	}

	public void moveUpRight(double n) {
		double dist = n / Math.sqrt(2);
		y -= dist;
		x += dist;
		rect.setLocation((int) Math.round(x), (int) (Math.round(y)-6));
	}

	public void moveRight(double n) {
		x += n;
		rect.setLocation((int) Math.round(x), (int) (Math.round(y)-6));
	}

	public void moveDownRight(double n) {
		double dist = n / Math.sqrt(2);
		y += dist;
		x += dist;
		rect.setLocation((int) Math.round(x), (int) (Math.round(y)-6));
	}

	public void moveDown(double n) {
		y += n;
		rect.setLocation((int) Math.round(x), (int) (Math.round(y)-6));
	}

	public void moveDownLeft(double n) {
		double dist = n / Math.sqrt(2);
		y += dist;
		x -= dist;
		rect.setLocation((int) Math.round(x), (int) (Math.round(y)-6));
	}

	public void moveLeft(double n) {
		x -= n;
		rect.setLocation((int) Math.round(x), (int) (Math.round(y)-6));
	}

	public void moveUpLeft(double n) {
		double dist = n / Math.sqrt(2);
		y -= dist;
		x -= dist;
		rect.setLocation((int) Math.round(x), (int) (Math.round(y)-6));
	}

	public void draw(Graphics2D g) {
		g.drawImage(guardStandImage, affineTransform, HeistCore.heist);
		g.drawImage(heldItem.itemImage, affineTransform, HeistCore.heist);
	}

	public boolean collisionCheck(Wall w) {
		if (rect.intersects(w.rect)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean collisionCheck(Door d) {
		if (rect.intersects(d.rect)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void movePlayer(int mousex, int mousey){
		double angle = HeistPanel.findAngle(getX(),getY(),mousex,mousey); 
		heading = angle;
		affineTransform.setToTranslation(getX(), getY());
		affineTransform.rotate(angle+(Math.PI/2), 11, 7); 
		
	}

	public Item getHeldItem() {
		return heldItem;
	}

	public void setHeldItem(Item heldItem) {
		this.heldItem = heldItem;
	}
	
	public Bullet shoot() {
		double bulletX = x + 11.0;
		double bulletY = y + 7.0;
		if(heldItem.getID() == 2) {
			return (new Bullet(bulletX, bulletY, heading, 3));
		} else {
			return null;
		}
	}
}