import java.awt.*;
import java.util.Vector;

public class Particle {

	private Vector<Double> pos; // units of distance are in m
	private Vector<Double> v; // units of speed are in m/sec
	private double m; // units of mass are in kg
	private double q; // units of charge are in coulombs
	private Rectangle size;

	private boolean fixed;

	public Particle(double x, double y, double m, Rectangle size, boolean fixed) {
		pos = new Vector<Double>();
		pos.add(x);
		pos.add(y);

		setM(m);
		setSize(size);
		setFixed(fixed);

	}

	public Particle(double x, double y, double m, Rectangle size) {
		pos = new Vector<Double>();
		pos.add(x);
		pos.add(y);

		setM(m);
		setSize(size);
		setFixed(false);

	}

	public Particle(double x, double y) {
		pos.add(x);
		pos.add(y);

		setM(5.0);
		setSize(new Rectangle(5, 5));
		setFixed(false);
	}

	public Vector<Double> getExertedForce(Particle p) {
		Vector<Double> g = getExertedGravitationalForce(p);
		Vector<Double> q = getExertedElectricalForce(p);
		Vector<Double> f = new Vector<Double>();
		f.add(g.get(0)+q.get(0));
		f.add(g.get(1)+q.get(1));
		return f;
	}
	
	public Vector<Double> getExertedGravitationalForce(Particle p) {
		double magnitude = (Const.G * p.m * this.m)
				/ (Math.pow(getDistanceTo(p), 2));
		double angle = Math.atan2((pos.get(1) - p.pos.get(1)),
				(pos.get(0) - p.pos.get(0)));
		Vector<Double> f = new Vector<Double>();
		f.add(Math.cos(angle) * magnitude);
		f.add(Math.sin(angle) * magnitude);
		return f;
	}
	
	public Vector<Double> getExertedElectricalForce(Particle p) {
		double magnitude = (Const.C * p.q * this.q)
				/ (Math.pow(getDistanceTo(p), 2));
		double angle = Math.atan2((pos.get(1) - p.pos.get(1)),
				(pos.get(0) - p.pos.get(0)));
		Vector<Double> f = new Vector<Double>();
		f.add(Math.cos(angle) * magnitude);
		f.add(Math.sin(angle) * magnitude);
		return f;
	}


	public double getDistanceTo(Particle p) {
		return Point.distance(pos.get(0), pos.get(1), p.pos.get(0),
				p.pos.get(1));
	}

	public Vector<Double> getPos() {
		return pos;
	}

	public void setPos(Vector<Double> pos) {
		this.pos = pos;
	}

	public Vector<Double> getV() {
		return v;
	}

	public void setV(Vector<Double> v) {
		this.v = v;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public Rectangle getSize() {
		return size;
	}

	public void setSize(Rectangle size) {
		this.size = size;
	}

	private boolean isFixed() {
		return fixed;
	}

	private void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
}