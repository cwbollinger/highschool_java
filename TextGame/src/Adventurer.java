
public class Adventurer {
	int x;
	int y;
	char avatar;
	Adventurer(char c) {
		this.x = RandomCW.diceRoll(10);
		this.y = RandomCW.diceRoll(10);
		avatar = c;
	}
	
	public void left() {
		this.x -= 1;
	}
	public void right() {
		this.x += 1;
	}
	public void up() {
		this.y -= 1;
	}
	public void down() {
		this.y += 1;
	}
}
