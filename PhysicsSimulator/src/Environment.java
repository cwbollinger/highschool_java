import java.util.ArrayList;
import java.util.Vector;

public class Environment {
	private ArrayList<Particle> particles;
	private int x;
	private int y;

	public Environment(int x, int y) {
		setSize(x, y);
		particles = new ArrayList<Particle>();
	}

	public void addParticle(Particle p) {
		particles.add(p);
	}

	public ArrayList<Particle> getParticles() {
		return particles;
	}

	public void updateParticles(int t) { // t is the number of ms since last
											// call, used to calculate
											// acceleration and velocity
		int len = particles.size();
		for (int n = 0; n < len; n++) {
			Vector<Double> force = new Vector<Double>();
			for (int i = 0; i < len; i++) {
				Vector<Double> temp = particles.get(n).getExertedForce(
						particles.get(i));
				force.set(0, force.get(0) + temp.get(0));
				force.set(1, force.get(1) + temp.get(1));
			}
			double m = particles.get(n).getM();
			Vector<Double> a = new Vector<Double>();
			a.add(force.get(0) / m);
			a.add(force.get(1) / m);
			Vector<Double> v = new Vector<Double>();
			v.add(a.get(0)*t/1000.0);
			v.add(a.get(1)*t/1000.0);
			particles.get(n).setV(v);
			Vector<Double> p = new Vector<Double>();
			p.add(particles.get(n).getPos().get(0)+v.get(0)*t/1000.0);
			p.add(particles.get(n).getPos().get(1)+v.get(1)*t/1000.0);
			particles.get(n).setPos(p);
		}
	}

	public void setSize(int x, int y) {
		setX(x);
		setY(y);
	}

	public int getX() {
		return x;
	}

	private void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	private void setY(int y) {
		this.y = y;
	}

}
