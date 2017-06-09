//------------------------------------------------------------------------
//  Name:   Vehicle.h
//
//  Desc:   Definition of a simple vehicle that uses steering behaviors
//
//  Author: Mat Buckland 2002 (fup@ai-junkie.com)
//------------------------------------------------------------------------
import java.util.Vector;

class Vehicle extends MovingEntity
{

  //a pointer to the world data. So a vehicle can access any obstacle,
  //path, wall or agent data
  private GameWorld            m_pWorld;

  //the steering behavior class
private SteeringBehaviors     m_pSteering;


  //some steering behaviors give jerky looking movement. The
  //following members are used to smooth the vehicle's heading
private Smoother<Vector2D>  m_pHeadingSmoother;

  //this vector represents the average of the vehicle's heading
  //vector smoothed over the last few frames
private Vector2D             m_vSmoothedHeading;

  //when true, smoothing is active
private boolean                  m_bSmoothingOn;
  

  //keeps a track of the most recent update time. (some of the
  //steering behaviors make use of this - see Wander)
private double                m_dTimeElapsed;


  //buffer for the vehicle shape
private Vector<Vector2D> m_vecVehicleVB;

//----------------------------- InitializeBuffer -----------------------------
//  fills the vehicle's shape buffer with its vertices
//-----------------------------------------------------------------------------
void InitializeBuffer()
{
  final int NumVehicleVerts = 3;

  Vector2D vehicle[] = new Vector2D[NumVehicleVerts];
  vehicle[0] = new Vector2D(-1.0,0.6);
  vehicle[1] = new Vector2D(1.0,0.0);
  vehicle[2] = new Vector2D(-1.0,-0.6);

  //setup the vertex buffers and calculate the bounding radius
  for (int vtx=0; vtx < NumVehicleVerts; ++vtx)
  {
    m_vecVehicleVB.push_back(vehicle[vtx]);
  }
}
  //disallow the copying of Vehicle types
  //Vehicle( Vehicle&);
  //Vehicle& operator=( Vehicle&);


  // public:

//----------------------------- ctor -------------------------------------
//------------------------------------------------------------------------
Vehicle(GameWorld world, Vector2D position, double rotation, 
		Vector2D velocity, double mass, double max_force, 
		double max_speed, double max_turn_rate, double scale) {  
  super(position, scale, velocity, max_speed, 
		new Vector2D(Math.sin(rotation),-Math.cos(rotation)), mass, 
		new Vector2D(scale,scale), 
		max_turn_rate, max_force);
  m_pWorld = world; 
  m_vSmoothedHeading = new Vector2D(0,0); 
  m_bSmoothingOn = false;
  m_dTimeElapsed = 0.0;
  
  InitializeBuffer();

  //set up the steering behavior class
  m_pSteering = new SteeringBehaviors(this);    

  //set up the smoother
  m_pHeadingSmoother = new Smoother<Vector2D>(Prm.NumSamplesForSmoothing, Vector2D(0.0, 0.0)); 
  
}

/*---------------------------- dtor -------------------------------------
//-----------------------------------------------------------------------
~Vehicle()
{
  delete m_pSteering;
  delete m_pHeadingSmoother;
}

  //updates the vehicle's position and orientation
  void        Update(double time_elapsed);

  void        Render();

*/                                                                          
  //-------------------------------------------accessor methods
  SteeringBehaviors Steering(){return m_pSteering;}
  GameWorld World(){return m_pWorld;} 

  
  Vector2D SmoothedHeading(){return m_vSmoothedHeading;}

  boolean isSmoothingOn(){return m_bSmoothingOn;}
  void SmoothingOn(){m_bSmoothingOn = true;}
  void SmoothingOff(){m_bSmoothingOn = false;}
  void ToggleSmoothing(){m_bSmoothingOn = !m_bSmoothingOn;}
  double TimeElapsed(){return m_dTimeElapsed;}

  //------------------------------ Update ----------------------------------
  //
  //  Updates the vehicle's position from a series of steering behaviors
  //------------------------------------------------------------------------
  void Update(double time_elapsed)
  {    
    //update the time elapsed
    m_dTimeElapsed = time_elapsed;

    //keep a record of its old position so we can update its cell later
    //in this method
    Vector2D OldPos = Pos();

    Vector2D SteeringForce;

    //calculate the combined force from each steering behavior in the 
    //vehicle's list
    SteeringForce = m_pSteering.Calculate();
      
    //Acceleration = Force/Mass
    Vector2D acceleration = SteeringForce.divide(m_dMass);

    //update velocity
    m_vVelocity.add(acceleration.multiply(time_elapsed)); 

    //make sure vehicle does not exceed maximum velocity
    m_vVelocity.Truncate(m_dMaxSpeed);

    //update the position
    m_vPos.add(m_vVelocity.multiply(time_elapsed));

    //update the heading if the vehicle has a non zero velocity
    if (m_vVelocity.LengthSq() > 0.00000001)
    {    
      m_vHeading = Vec2DNormalize(m_vVelocity);

      m_vSide = m_vHeading.Perp();
    }

    //EnforceNonPenetrationConstraint(this, World()->Agents());

    //treat the screen as a toroid
    WrapAround(m_vPos, m_pWorld.cxClient(), m_pWorld.cyClient());

    //update the vehicle's current cell if space partitioning is turned on
    if (Steering().isSpacePartitioningOn())
    {
      World().CellSpace().UpdateEntity(this, OldPos);
    }

    if (isSmoothingOn())
    {
      m_vSmoothedHeading = m_pHeadingSmoother.Update(Heading());
    }
  }

  //-------------------------------- Render -------------------------------------
  //-----------------------------------------------------------------------------
  void Render()
  { 
    //a vector to hold the transformed vertices
    static std::vector<Vector2D>  m_vecVehicleVBTrans;

    //render neighboring vehicles in different colors if requested
    if (m_pWorld.RenderNeighbors())
    {
      if (ID() == 0) gdi.RedPen();
      else if(IsTagged()) gdi.GreenPen();
      else gdi.BluePen();
    }

    else
    {
      gdi.BluePen();
    }

    if (Steering().isInterposeOn())
    {
      gdi.RedPen();
    }

    if (Steering().isHideOn())
    {
      gdi.GreenPen();
    }

    if (isSmoothingOn())
    { 
      m_vecVehicleVBTrans = WorldTransform(m_vecVehicleVB, Pos(), SmoothedHeading(),
                                           SmoothedHeading().Perp(), Scale());
    }

    else
    {
      m_vecVehicleVBTrans = WorldTransform(m_vecVehicleVB, Pos(), Heading(),
                                           Side(), Scale());
    }

    gdi.ClosedShape(m_vecVehicleVBTrans);
   
    //render any visual aids / and or user options
    if (m_pWorld.ViewKeys())
    {
      Steering().RenderAids();
    }
  }




}