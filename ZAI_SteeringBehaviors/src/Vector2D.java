//------------------------------------------------------------------------
//
//  Name:   Vector2D.h
//
//  Desc:   2D vector struct
//
//  Author: Mat Buckland (fup@ai-junkie.com)
//
//------------------------------------------------------------------------

//#include "misc/utils.h"

class Vector2D {
	// C++ would use: std::numeric_limits<double>::epsilon()
	public static final double EPSILON = 1.0E-5;

	final int clockwise = 1;
	final int anticlockwise = -1;

	double x;
	double y;

	Vector2D() {
		x = 0.0;
		y = 0.0;
	}

	Vector2D(double a, double b) {
		x = a;
		y = b;
	}

	// sets x and y to zero
	void Zero() {
		x = 0.0;
		y = 0.0;
	}

	// returns true if both x and y are zero
	boolean isZero() {
		return (x * x + y * y) < Double.MIN_VALUE;
	}

	// returns the length of a 2D vector
	double Length() {
		return Math.sqrt(x * x + y * y);
	}

	// returns the squared length of a 2D vector
	double LengthSq() {
		return (x * x + y * y);
	}

	// normalizes a 2D Vector
	void Normalize() {
		double vector_length = this.Length();

		if (vector_length > EPSILON) {
			this.x /= vector_length;
			this.y /= vector_length;
		}
	}

	// calculates the dot product
	double Dot(Vector2D v2) {
		return x * v2.x + y * v2.y;
	}

	// returns positive if v2 is clockwise of this vector,
	// minus if anticlockwise (Y axis pointing down, X axis to right)

	int Sign(Vector2D v2) {
		if (y * v2.x > x * v2.y) {
			return anticlockwise;
		} else {
			return clockwise;
		}
	}

	// Returns a vector perpendicular to this vector
	Vector2D Perp() {
		return new Vector2D(-y, x);
	}

	// truncates a vector so that its length does not exceed max
	void Truncate(double max) {
		if (this.Length() > max) {
			this.Normalize();
			this.multiply(max);
		}
	}

	// calculates the euclidean distance between two vectors
	// ------------------------------------------------------------------------
	double Distance(Vector2D v2) {
		double ySeparation = v2.y - y;
		double xSeparation = v2.x - x;

		return Math.sqrt(ySeparation * ySeparation + xSeparation * xSeparation);
	}

	// calculates the squared euclidean distance squared between two vectors
	double DistanceSq(Vector2D v2) {
		double ySeparation = v2.y - y;
		double xSeparation = v2.x - x;

		return ySeparation * ySeparation + xSeparation * xSeparation;
	}

	// given a normalized vector this method reflects the vector it
	// is operating upon. (like the path of a ball bouncing off a wall)
	void Reflect(Vector2D norm) {
		// C++: this += 2.0 * this.Dot(norm) * norm.GetReverse();
		Vector2D vec = norm.GetReverse();
		vec.multiply(this.Dot(norm));
		vec.multiply(2);
		this.add(vec);
	}

	// returns the vector that is the reverse of this vector
	Vector2D GetReverse() {
		return new Vector2D(-this.x, -this.y);
	}

	// In C++ we would use some overloaded operators; define equivalent member
	// fns here
	Vector2D add(Vector2D rhs) {
		x += rhs.x;
		y += rhs.y;
		return this;
	}

	Vector2D subtract(Vector2D rhs) {
		x -= rhs.x;
		y -= rhs.y;
		return this;
	}

	Vector2D multiply(double rhs) {
		x *= rhs;
		y *= rhs;
		return this;
	}

	Vector2D divide(double rhs) {
		x /= rhs;
		y /= rhs;
		return this;
	}

	boolean isEqual(Vector2D rhs) {
		return ((Double.compare(x, rhs.x) == 0) && (Double.compare(y, rhs.y) == 0));
	}

	// ------------------------------------------------------------------------
	// In C++, these were non-member functions
	// ------------------------------------------------------------------------

	public static Vector2D Vec2DNormalize(Vector2D v) {
		Vector2D vec = v;

		double vector_length = vec.Length();

		if (vector_length > EPSILON) {
			vec.x /= vector_length;
			vec.y /= vector_length;
		}

		return vec;
	}

	public static double Vec2DDistance(Vector2D v1, Vector2D v2) {

		double ySeparation = v2.y - v1.y;
		double xSeparation = v2.x - v1.x;

		return Math.sqrt(ySeparation * ySeparation + xSeparation * xSeparation);
	}

	public static double Vec2DDistanceSq(Vector2D v1, Vector2D v2) {

		double ySeparation = v2.y - v1.y;
		double xSeparation = v2.x - v1.x;

		return ySeparation * ySeparation + xSeparation * xSeparation;
	}

	public static double Vec2DLength(Vector2D v) {
		return Math.sqrt(v.x * v.x + v.y * v.y);
	}

	public static double Vec2DLengthSq(Vector2D v) {
		return (v.x * v.x + v.y * v.y);
	}

	/****
	 * public static Vector2D POINTStoVector(POINTS p) { return new
	 * Vector2D(p.x, p.y); }
	 * 
	 * public static Vector2D POINTtoVector(POINT p) { return new
	 * Vector2D((double)p.x, (double)p.y); }
	 * 
	 * public static POINTS VectorToPOINTS(Vector2D v) { POINTS p; p.x =
	 * (short)v.x; p.y = (short)v.y;
	 * 
	 * return p; }
	 * 
	 * public static POINT VectorToPOINT(Vector2D v) { POINT p; p.x = (long)v.x;
	 * p.y = (long)v.y;
	 * 
	 * return p; }
	 ****/

	/****
	 * //----------------------------------------------------------------------
	 * --operator overloads Vector2D operator*(Vector2D &lhs, double rhs) {
	 * Vector2D result(lhs); result *= rhs; return result; }
	 * 
	 * Vector2D operator*(double lhs, Vector2D &rhs) { Vector2D result(rhs);
	 * result *= lhs; return result; }
	 * 
	 * //overload the - operator Vector2D operator-(Vector2D &lhs, Vector2D
	 * &rhs) { Vector2D result(lhs); result.x -= rhs.x; result.y -= rhs.y;
	 * 
	 * return result; }
	 * 
	 * //overload the + operator Vector2D operator+(Vector2D &lhs, Vector2D
	 * &rhs) { Vector2D result(lhs); result.x += rhs.x; result.y += rhs.y;
	 * 
	 * return result; }
	 * 
	 * //overload the / operator Vector2D operator/(Vector2D &lhs, double val) {
	 * Vector2D result(lhs); result.x /= val; result.y /= val;
	 * 
	 * return result; }
	 ****/
	// /////////////////////////////////////////////////////////////////////////////

	// treats a window as a toroid
	public static void WrapAround(Vector2D pos, int MaxX, int MaxY) {
		if (pos.x > MaxX) {
			pos.x = 0.0;
		}

		if (pos.x < 0) {
			pos.x = (double) MaxX;
		}

		if (pos.y < 0) {
			pos.y = (double) MaxY;
		}

		if (pos.y > MaxY) {
			pos.y = 0.0;
		}
	}

	// returns true if the point p is not inside the region defined by top_left
	// and bot_rgt
	public static boolean NotInsideRegion(Vector2D p, Vector2D top_left,
			Vector2D bot_rgt) {
		return (p.x < top_left.x) || (p.x > bot_rgt.x) || (p.y < top_left.y)
				|| (p.y > bot_rgt.y);
	}

	public static boolean InsideRegion(Vector2D p, Vector2D top_left,
			Vector2D bot_rgt) {
		return !((p.x < top_left.x) || (p.x > bot_rgt.x) || (p.y < top_left.y) || (p.y > bot_rgt.y));
	}

	public static boolean InsideRegion(Vector2D p, int left, int top,
			int right, int bottom) {
		return !((p.x < left) || (p.x > right) || (p.y < top) || (p.y > bottom));
	}

	// ------------------ isSecondInFOVOfFirst
	// -------------------------------------
	// returns true if the target position is in the field of view of the entity
	// positioned at posFirst facing in facingFirst
	// -----------------------------------------------------------------------------
	public static boolean isSecondInFOVOfFirst(Vector2D posFirst,
			Vector2D facingFirst, Vector2D posSecond, double fov) {
		Vector2D diff = posSecond;
		diff.subtract(posFirst);

		Vector2D toTarget = Vec2DNormalize(diff);

		return facingFirst.Dot(toTarget) >= Math.cos(fov / 2.0);
	}

}