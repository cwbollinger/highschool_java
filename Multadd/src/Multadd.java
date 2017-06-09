
public class Multadd {
	static double multadd(double a, double b, double c)
	{
		return((a*b)+c);
	}
	static void yikes(double x)
	{
		multadd(x, Math.exp(-x), Math.sqrt(multadd(-1, Math.exp(-x), 1 )));
	}
	public static void main(String[] args) {
		System.out.println(multadd(Math.cos(Math.PI/4),0.5,Math.sin(Math.PI/4)));
	}
}
