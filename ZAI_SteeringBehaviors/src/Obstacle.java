//------------------------------------------------------------------------
//  Name:   Obstacle.h
//
//  Desc:   Simple obstacle class
//
//  Author: Mat Buckland 2002 (fup@ai-junkie.com)
//------------------------------------------------------------------------
//import Vector2D;

class Obstacle extends BaseGameEntity
{

  public Obstacle(double x,
           double y,
           double r) {
	  super(0, new Vector2D(x,y), r);
  }
 

  public Obstacle(Vector2D pos, double radius) {
	  super(0, pos, radius);
  }

  //public Obstacle(std::ifstream& in){Read(in);}

  //virtual ~Obstacle(){}

  //this is defined as a pure virtual function in BasegameEntity so
  //it must be implemented
  public void Update(double time_elapsed){}

  public void Render() {
	  //gdi->BlackPen();
	  //gdi->Circle(Pos(), BRadius());
  }


@Override
boolean HandleMessage(Telegram msg) {
	// TODO Auto-generated method stub
	return false;
}

  //public void Write(std::ostream& os)const;
  //public void Read(std::ifstream& in);
}