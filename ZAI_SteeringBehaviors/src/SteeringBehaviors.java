//------------------------------------------------------------------------
//
//  Name:   SteeringBehaviors.h
//
//  Desc:   class to encapsulate steering behaviors for a Vehicle
//
//  Author: Mat Buckland 2002 (fup@ai-junkie.com)
//
//------------------------------------------------------------------------


public class SteeringBehaviors {
	//--------------------------- Constants ----------------------------------

	//the radius of the constraining circle for the wander behavior
	final double WanderRad = 1.2;
	//distance the wander circle is projected in front of the agent
	final double WanderDist = 2.0;
	//the maximum amount of displacement along the circle each frame
	final double WanderJitterPerSec = 80.0;
	//used in path following
	final double WaypointSeekDist = 20;                                          

	//------------------------------------------------------------------------

	public enum summing_method{weighted_average, prioritized, dithered};

	enum behavior_type
	{
	    none, seek, flee, arrive, wander, cohesion, separation, allignment,
	    obstacle_avoidance, wall_avoidance, follow_path, pursuit, evade,
	    interpose, hide, flock, offset_pursuit
	};
	
	//a pointer to the owner of this instance
	  Vehicle m_pVehicle;   
	  
	  //the steering force created by the combined effect of all
	  //the selected behaviors
	  Vector2D m_vSteeringForce;
	 
	  //these can be used to keep track of friends, pursuers, or prey
	  Vehicle m_pTargetAgent1;
	  Vehicle m_pTargetAgent2;

	  //the current target
	  Vector2D m_vTarget;

	  //length of the 'detection box' utilized in obstacle avoidance
	  double m_dDBoxLength;

	  //a vertex buffer to contain the feelers rqd for wall avoidance  
	  std::vector<Vector2D> m_Feelers;
	  
	  //the length of the 'feeler/s' used in wall detection
	  double m_dWallDetectionFeelerLength;

	  //the current position on the wander circle the agent is
	  //attempting to steer towards
	  Vector2D m_vWanderTarget; 

	  //explained above
	  double m_dWanderJitter;
	  double m_dWanderRadius;
	  double m_dWanderDistance;

	  //multipliers. These can be adjusted to effect strength of the  
	  //appropriate behavior. Useful to get flocking the way you require
	  //for example.
	  double m_dWeightSeparation;
	  double m_dWeightCohesion;
	  double m_dWeightAlignment;
	  double m_dWeightWander;
	  double m_dWeightObstacleAvoidance;
	  double m_dWeightWallAvoidance;
	  double m_dWeightSeek;
	  double m_dWeightFlee;
	  double m_dWeightArrive;
	  double m_dWeightPursuit;
	  double m_dWeightOffsetPursuit;
	  double m_dWeightInterpose;
	  double m_dWeightHide;
	  double m_dWeightEvade;
	  double m_dWeightFollowPath;

	  //how far the agent can 'see'
	  double m_dViewDistance;

	  //pointer to any current path
	  Path  m_pPath;

	  //the distance (squared) a vehicle has to be from a path waypoint before
	  //it starts seeking to the next waypoint
	  double m_dWaypointSeekDistSq;

	  //any offset used for formations or offset pursuit
	  Vector2D m_vOffset;

	  //binary flags to indicate whether or not a behavior should be active
	  int m_iFlags;

	  //Arrive makes use of these to determine how quickly a vehicle
	  //should decelerate to its target
	  double decel_slow = 1.0;
	  double decel_normal = 2.0;
	  double decel_fast = 3.0;


	  //is cell space partitioning to be used or not?
	  boolean m_bCellSpaceOn;
	 
	  //what type of method is used to sum any active behavior
	  summing_method m_SummingMethod;

	  SteeringBehaviors(Vehicle agent) { 
          m_pVehicle = agent;
          m_iFlags = 0;
          m_dDBoxLength = Prm.MinDetectionBoxLength;
          m_dWeightCohesion = Prm.CohesionWeight;
          m_dWeightAlignment = Prm.AlignmentWeight;
          m_dWeightSeparation = Prm.SeparationWeight;
          m_dWeightObstacleAvoidance = Prm.ObstacleAvoidanceWeight;
          m_dWeightWander = Prm.WanderWeight;
          m_dWeightWallAvoidance = Prm.WallAvoidanceWeight;
          m_dViewDistance = Prm.ViewDistance;
          m_dWallDetectionFeelerLength = Prm.WallDetectionFeelerLength;
          m_Feelers = 3;
          m_pTargetAgent1 = null;
          m_pTargetAgent2 = null;
          m_dWanderDistance = WanderDist;
          m_dWanderJitter = WanderJitterPerSec;
          m_dWanderRadius = WanderRad;
          m_dWaypointSeekDistSq = WaypointSeekDist*WaypointSeekDist;
          m_dWeightSeek = Prm.SeekWeight;
          m_dWeightFlee = Prm.FleeWeight;
          m_dWeightArrive = Prm.ArriveWeight;
          m_dWeightPursuit = Prm.PursuitWeight;
          m_dWeightOffsetPursuit = Prm.OffsetPursuitWeight;
          m_dWeightInterpose = Prm.InterposeWeight;
          m_dWeightHide = Prm.HideWeight;
          m_dWeightEvade = Prm.EvadeWeight;
          m_dWeightFollowPath = Prm.FollowPathWeight;
          m_bCellSpaceOn = false;
          m_SummingMethod = prioritized;

          //stuff for the wander behavior
          double theta = RandFloat() * TwoPi;

          //create a vector to a target position on the wander circle
          m_vWanderTarget = Vector2D(m_dWanderRadius * cos(theta),
                           m_dWanderRadius * sin(theta));

          //create a Path
          m_pPath = new Path();
          m_pPath.LoopOn();

}
	  
	  //this function tests if a specific bit of m_iFlags is set
	  boolean On(behavior_type bt){return (m_iFlags & bt) == bt;}

	  
	  //--------------------- AccumulateForce ----------------------------------
	  //
	  //  This function calculates how much of its max steering force the 
	  //  vehicle has left to apply and then applies that amount of the
	  //  force to add.
	  //------------------------------------------------------------------------
	  boolean AccumulateForce(Vector2D RunningTot, Vector2D ForceToAdd)
	  {
	  //calculate how much steering force the vehicle has used so far
		  double MagnitudeSoFar = RunningTot.Length();

		  //calculate how much steering force remains to be used by this vehicle
		  double MagnitudeRemaining = m_pVehicle.MaxForce() - MagnitudeSoFar;

		  //return false if there is no more force left to use
		  if (MagnitudeRemaining <= 0.0) return false;

		  //calculate the magnitude of the force we want to add
		  double MagnitudeToAdd = ForceToAdd.Length();
		  
		  //if the magnitude of the sum of ForceToAdd and the running total
		  //does not exceed the maximum force available to this vehicle, just
		  //add together. Otherwise add as much of the ForceToAdd vector is
		  //possible without going over the max.
		  if (MagnitudeToAdd < MagnitudeRemaining)
		  {
		    RunningTot.add(ForceToAdd);
		  }

		  else
		  {
		    //add it to the steering force
		    RunningTot.add((Vec2DNormalize(ForceToAdd) * MagnitudeRemaining)); 
		  }

		  return true;
	  }
	  
	  //creates the antenna utilized by the wall avoidance behavior
	  void      CreateFeelers();
	  
	  
	   /* .......................................................

      BEGIN BEHAVIOR DECLARATIONS

.......................................................*/

//------------------------------- Seek -----------------------------------
//
//  Given a target, this behavior returns a steering force which will
//  direct the agent towards the target
//------------------------------------------------------------------------
Vector2D Seek(Vector2D TargetPos)
{
	Vector2D DesiredVelocity = Vec2DNormalize(TargetPos.subtract(m_pVehicle.Pos()))
	                            * m_pVehicle.MaxSpeed();

	return (DesiredVelocity - m_pVehicle.Velocity());
}

//----------------------------- Flee -------------------------------------
//
//Does the opposite of Seek
//------------------------------------------------------------------------
Vector2D Flee(Vector2D TargetPos)
{
	//only flee if the target is within 'panic distance'. Work in distance
	//squared space.
	/* const double PanicDistanceSq = 100.0f * 100.0;
	if (Vec2DDistanceSq(m_pVehicle->Pos(), target) > PanicDistanceSq)
	{
	  return Vector2D(0,0);
	}
	*/
	Vector2D DesiredVelocity = Vec2DNormalize(m_pVehicle.Pos().subtract(TargetPos)) 
	                            * m_pVehicle.MaxSpeed();
	return (DesiredVelocity - m_pVehicle.Velocity());
}

//this behavior is similar to seek but it attempts to arrive 
//at the target position with a zero velocity
Vector2D Arrive(Vector2D TargetPos, double deceleration)
{
	  Vector2D ToTarget = TargetPos.subtract(m_pVehicle.Pos());

	  //calculate the distance to the target
	  double dist = ToTarget.Length();

	  if (dist > 0)
	  {
	    //because Deceleration is enumerated as an int, this value is required
	    //to provide fine tweaking of the deceleration..
	    double DecelerationTweaker = 0.3;

	    //calculate the speed required to reach the target given the desired
	    //deceleration
	    double speed =  dist / (deceleration * DecelerationTweaker);     

	    //make sure the velocity does not exceed the max
	    speed = Math.min(speed, m_pVehicle.MaxSpeed());

	    //from here proceed just like Seek except we don't need to normalize 
	    //the ToTarget vector because we have already gone to the trouble
	    //of calculating its length: dist. 
	    Vector2D DesiredVelocity =  ToTarget.multiply(speed / dist);

	    return (DesiredVelocity.subtract(m_pVehicle.Velocity()));
	  }

	  return new Vector2D(0,0);
	}

//------------------------------ Pursuit ---------------------------------
//
//  this behavior creates a force that steers the agent towards the 
//  evader
//------------------------------------------------------------------------
Vector2D Pursuit( Vehicle evader)
{
	  //if the evader is ahead and facing the agent then we can just seek
	  //for the evader's current position.
	  Vector2D ToEvader = evader.Pos().subtract(m_pVehicle.Pos());

	  double RelativeHeading = m_pVehicle.Heading().Dot(evader.Heading());

	  if ( (ToEvader.Dot(m_pVehicle.Heading()) > 0) &&  
	       (RelativeHeading < -0.95))  //acos(0.95)=18 degs
	  {
	    return Seek(evader.Pos());
	  }

	  //Not considered ahead so we predict where the evader will be.
	 
	  //the lookahead time is propotional to the distance between the evader
	  //and the pursuer; and is inversely proportional to the sum of the
	  //agent's velocities
	  double LookAheadTime = ToEvader.Length() / 
	                        (m_pVehicle.MaxSpeed() + evader.Speed());
	  
	  //now seek to the predicted future position of the evader
	  return Seek(evader.Pos().add(evader.Velocity().multiply(LookAheadTime)));
	}
//this behavior maintains a position, in the direction of offset
//from the target vehicle
Vector2D OffsetPursuit( Vehicle agent,  Vector2D offset);

//----------------------------- Evade ------------------------------------
//
//  similar to pursuit except the agent Flees from the estimated future
//  position of the pursuer
//------------------------------------------------------------------------
Vector2D Evade( Vehicle pursuer)
{
	/* Not necessary to include the check for facing direction this time */

	Vector2D ToPursuer = pursuer.Pos().subtract(m_pVehicle.Pos());

	//uncomment the following two lines to have Evade only consider pursuers 
	//within a 'threat range'
	double ThreatRange = 100.0;
	if (ToPursuer.LengthSq() > ThreatRange * ThreatRange) return new Vector2D();
	
	//the lookahead time is propotional to the distance between the pursuer
	//and the pursuer; and is inversely proportional to the sum of the
	//agents' velocities
	double LookAheadTime = ToPursuer.Length() / 
	                       (m_pVehicle.MaxSpeed() + pursuer.Speed());
	 
	//now flee away from predicted future position of the pursuer
	return Flee(pursuer.Pos().add(pursuer.Velocity()).multiply(LookAheadTime));
}

//--------------------------- Wander -------------------------------------
//
//  This behavior makes the agent wander about randomly
//------------------------------------------------------------------------
Vector2D Wander()
{ 
	  //this behavior is dependent on the update rate, so this line must
	  //be included when using time independent framerate.
	  double JitterThisTimeSlice = m_dWanderJitter * m_pVehicle.TimeElapsed();

	  //first, add a small random vector to the target's position
	  m_vWanderTarget.add(new Vector2D((2*Math.random()-1) * JitterThisTimeSlice,
	                              (2*Math.random()-1) * JitterThisTimeSlice));

	  //reproject this new vector back on to a unit circle
	  m_vWanderTarget.Normalize();

	  //increase the length of the vector to the same as the radius
	  //of the wander circle
	  m_vWanderTarget.multiply(m_dWanderRadius);

	  //move the target into a position WanderDist in front of the agent
	  Vector2D target = m_vWanderTarget.add(new Vector2D(m_dWanderDistance, 0));

	  //project the target into world space
	  Vector2D Target = new PointToWorldSpace(target,
	                                       m_pVehicle.Heading(),
	                                       m_pVehicle.Side(), 
	                                       m_pVehicle.Pos());

	  //and steer towards it
	  return Target - m_pVehicle.Pos(); 
	}
//this returns a steering force which will attempt to keep the agent 
//away from any obstacles it may encounter
Vector2D ObstacleAvoidance( std::vector<BaseGameEntity*>& obstacles);

//this returns a steering force which will keep the agent away from any
//walls it may encounter
Vector2D WallAvoidance( std::vector<Wall2D> &walls);


//given a series of Vector2Ds, this method produces a force that will
//move the agent along the waypoints in order
Vector2D FollowPath();

//this results in a steering force that attempts to steer the vehicle
//to the center of the vector connecting two moving agents.
Vector2D Interpose( Vehicle VehicleA,  Vehicle VehicleB);

//given another agent position to hide from and a list of BaseGameEntitys this
//method attempts to put an obstacle between itself and its opponent
Vector2D Hide( Vehicle hunter,  std::vector<BaseGameEntity*>& obstacles);


// -- Group Behaviors -- //

Vector2D Cohesion( std::vector<Vehicle*> &agents);

Vector2D Separation( std::vector<Vehicle*> &agents);

Vector2D Alignment( std::vector<Vehicle*> &agents);

//the following three are the same as above but they use cell-space
//partitioning to find the neighbors
Vector2D CohesionPlus( std::vector<Vehicle*> &agents);
Vector2D SeparationPlus( std::vector<Vehicle*> &agents);
Vector2D AlignmentPlus( std::vector<Vehicle*> &agents);

/* .......................................................

         END BEHAVIOR DECLARATIONS

.......................................................*/

//---------------------- CalculateWeightedSum ----------------------------
//
//this simply sums up all the active behaviors X their weights and 
//truncates the result to the max available steering force before 
//returning
//------------------------------------------------------------------------
Vector2D CalculateWeightedSum()
{        
if (On(wall_avoidance))
{
  m_vSteeringForce += WallAvoidance(m_pVehicle.World().Walls()) *
                       m_dWeightWallAvoidance;
}
 
if (On(obstacle_avoidance))
{
  m_vSteeringForce += ObstacleAvoidance(m_pVehicle.World().Obstacles()) * 
          m_dWeightObstacleAvoidance;
}

if (On(evade))
{
  assert(m_pTargetAgent1 && "Evade target not assigned");
  
  m_vSteeringForce += Evade(m_pTargetAgent1) * m_dWeightEvade;
}


//these next three can be combined for flocking behavior (wander is
//also a good behavior to add into this mix)
if (!isSpacePartitioningOn())
{
  if (On(separation))
  {
    m_vSteeringForce += Separation(m_pVehicle.World().Agents()) * m_dWeightSeparation;
  }

  if (On(allignment))
  {
    m_vSteeringForce += Alignment(m_pVehicle.World().Agents()) * m_dWeightAlignment;
  }

  if (On(cohesion))
  {
    m_vSteeringForce += Cohesion(m_pVehicle.World().Agents()) * m_dWeightCohesion;
  }
}
else
{
  if (On(separation))
  {
    m_vSteeringForce += SeparationPlus(m_pVehicle.World().Agents()) * m_dWeightSeparation;
  }

  if (On(allignment))
  {
    m_vSteeringForce += AlignmentPlus(m_pVehicle.World().Agents()) * m_dWeightAlignment;
  }

  if (On(cohesion))
  {
    m_vSteeringForce += CohesionPlus(m_pVehicle.World().Agents()) * m_dWeightCohesion;
  }
}


if (On(wander))
{
  m_vSteeringForce += Wander() * m_dWeightWander;
}

if (On(seek))
{
  m_vSteeringForce += Seek(m_pVehicle.World().Crosshair()) * m_dWeightSeek;
}

if (On(flee))
{
  m_vSteeringForce += Flee(m_pVehicle.World().Crosshair()) * m_dWeightFlee;
}

if (On(arrive))
{
  m_vSteeringForce += Arrive(m_pVehicle.World().Crosshair(), m_Deceleration) * m_dWeightArrive;
}

if (On(pursuit))
{
  assert(m_pTargetAgent1 && "pursuit target not assigned");

  m_vSteeringForce += Pursuit(m_pTargetAgent1) * m_dWeightPursuit;
}

if (On(offset_pursuit))
{
  assert (m_pTargetAgent1 && "pursuit target not assigned");
  assert (!m_vOffset.isZero() && "No offset assigned");

  m_vSteeringForce += OffsetPursuit(m_pTargetAgent1, m_vOffset) * m_dWeightOffsetPursuit;
}

if (On(interpose))
{
  assert (m_pTargetAgent1 && m_pTargetAgent2 && "Interpose agents not assigned");

  m_vSteeringForce += Interpose(m_pTargetAgent1, m_pTargetAgent2) * m_dWeightInterpose;
}

if (On(hide))
{
  assert(m_pTargetAgent1 && "Hide target not assigned");

  m_vSteeringForce += Hide(m_pTargetAgent1, m_pVehicle.World().Obstacles()) * m_dWeightHide;
}

if (On(follow_path))
{
  m_vSteeringForce += FollowPath() * m_dWeightFollowPath;
}

m_vSteeringForce.Truncate(m_pVehicle.MaxForce());

return m_vSteeringForce;
}

//---------------------- CalculatePrioritized ----------------------------
//
//  this method calls each active steering behavior in order of priority
//  and acumulates their forces until the max steering force magnitude
//  is reached, at which time the function returns the steering force 
//  accumulated to that  point
//------------------------------------------------------------------------
Vector2D CalculatePrioritized()
{       
	  Vector2D force;
	  
	   if (On(wall_avoidance))
	  {
	    force = WallAvoidance(m_pVehicle.World().Walls()) *
	            m_dWeightWallAvoidance;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }
	   
	  if (On(obstacle_avoidance))
	  {
	    force = ObstacleAvoidance(m_pVehicle.World().Obstacles()) * 
	            m_dWeightObstacleAvoidance;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }

	  if (On(evade))
	  {
	    assert(m_pTargetAgent1 && "Evade target not assigned");
	    
	    force = Evade(m_pTargetAgent1) * m_dWeightEvade;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }

	  
	  if (On(flee))
	  {
	    force = Flee(m_pVehicle.World().Crosshair()) * m_dWeightFlee;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }


	 
	  //these next three can be combined for flocking behavior (wander is
	  //also a good behavior to add into this mix)
	  if (!isSpacePartitioningOn())
	  {
	    if (On(separation))
	    {
	      force = Separation(m_pVehicle.World().Agents()) * m_dWeightSeparation;

	      if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	    }

	    if (On(allignment))
	    {
	      force = Alignment(m_pVehicle.World().Agents()) * m_dWeightAlignment;

	      if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	    }

	    if (On(cohesion))
	    {
	      force = Cohesion(m_pVehicle.World().Agents()) * m_dWeightCohesion;

	      if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	    }
	  }

	  else
	  {

	    if (On(separation))
	    {
	      force = SeparationPlus(m_pVehicle.World().Agents()) * m_dWeightSeparation;

	      if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	    }

	    if (On(allignment))
	    {
	      force = AlignmentPlus(m_pVehicle.World().Agents()) * m_dWeightAlignment;

	      if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	    }

	    if (On(cohesion))
	    {
	      force = CohesionPlus(m_pVehicle.World().Agents()) * m_dWeightCohesion;

	      if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	    }
	  }

	  if (On(seek))
	  {
	    force = Seek(m_pVehicle.World().Crosshair()) * m_dWeightSeek;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }


	  if (On(arrive))
	  {
	    force = Arrive(m_pVehicle.World().Crosshair(), m_Deceleration) * m_dWeightArrive;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }

	  if (On(wander))
	  {
	    force = Wander() * m_dWeightWander;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }

	  if (On(pursuit))
	  {
	    assert(m_pTargetAgent1 && "pursuit target not assigned");

	    force = Pursuit(m_pTargetAgent1) * m_dWeightPursuit;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }

	  if (On(offset_pursuit))
	  {
	    assert (m_pTargetAgent1 && "pursuit target not assigned");
	    assert (!m_vOffset.isZero() && "No offset assigned");

	    force = OffsetPursuit(m_pTargetAgent1, m_vOffset);

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }

	  if (On(interpose))
	  {
	    assert (m_pTargetAgent1 && m_pTargetAgent2 && "Interpose agents not assigned");

	    force = Interpose(m_pTargetAgent1, m_pTargetAgent2) * m_dWeightInterpose;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }

	  if (On(hide))
	  {
	    assert(m_pTargetAgent1 && "Hide target not assigned");

	    force = Hide(m_pTargetAgent1, m_pVehicle.World().Obstacles()) * m_dWeightHide;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }


	  if (On(follow_path))
	  {
	    force = FollowPath() * m_dWeightFollowPath;

	    if (!AccumulateForce(m_vSteeringForce, force)) return m_vSteeringForce;
	  }

	  return m_vSteeringForce;
	}

//---------------------- CalculateDithered ----------------------------
//
//  this method sums up the active behaviors by assigning a probabilty
//  of being calculated to each behavior. It then tests the first priority
//  to see if it should be calcukated this simulation-step. If so, it
//  calculates the steering force resulting from this behavior. If it is
//  more than zero it returns the force. If zero, or if the behavior is
//  skipped it continues onto the next priority, and so on.
//
//  NOTE: Not all of the behaviors have been implemented in this method,
//        just a few, so you get the general idea
//------------------------------------------------------------------------
Vector2D CalculateDithered()
{  
	  //reset the steering force
	   m_vSteeringForce.Zero();

	  if (On(wall_avoidance) && RandFloat() < Prm.prWallAvoidance)
	  {
	    m_vSteeringForce = WallAvoidance(m_pVehicle.World().Walls()) *
	                         m_dWeightWallAvoidance / Prm.prWallAvoidance;

	    if (!m_vSteeringForce.isZero())
	    {
	      m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	      return m_vSteeringForce;
	    }
	  }
	   
	  if (On(obstacle_avoidance) && RandFloat() < Prm.prObstacleAvoidance)
	  {
	    m_vSteeringForce += ObstacleAvoidance(m_pVehicle.World().Obstacles()) * 
	            m_dWeightObstacleAvoidance / Prm.prObstacleAvoidance;

	    if (!m_vSteeringForce.isZero())
	    {
	      m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	      return m_vSteeringForce;
	    }
	  }

	  if (!isSpacePartitioningOn())
	  {
	    if (On(separation) && RandFloat() < Prm.prSeparation)
	    {
	      m_vSteeringForce += Separation(m_pVehicle.World().Agents()) * 
	                          m_dWeightSeparation / Prm.prSeparation;

	      if (!m_vSteeringForce.isZero())
	      {
	        m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	        return m_vSteeringForce;
	      }
	    }
	  }

	  else
	  {
	    if (On(separation) && RandFloat() < Prm.prSeparation)
	    {
	      m_vSteeringForce += SeparationPlus(m_pVehicle.World().Agents()) * 
	                          m_dWeightSeparation / Prm.prSeparation;

	      if (!m_vSteeringForce.isZero())
	      {
	        m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	        return m_vSteeringForce;
	      }
	    }
	  }


	  if (On(flee) && RandFloat() < Prm.prFlee)
	  {
	    m_vSteeringForce += Flee(m_pVehicle.World().Crosshair()) * m_dWeightFlee / Prm.prFlee;

	    if (!m_vSteeringForce.isZero())
	    {
	      m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	      return m_vSteeringForce;
	    }
	  }

	  if (On(evade) && RandFloat() < Prm.prEvade)
	  {
	    assert(m_pTargetAgent1 && "Evade target not assigned");
	    
	    m_vSteeringForce += Evade(m_pTargetAgent1) * m_dWeightEvade / Prm.prEvade;

	    if (!m_vSteeringForce.isZero())
	    {
	      m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	      return m_vSteeringForce;
	    }
	  }


	  if (!isSpacePartitioningOn())
	  {
	    if (On(allignment) && RandFloat() < Prm.prAlignment)
	    {
	      m_vSteeringForce += Alignment(m_pVehicle.World().Agents()) *
	                          m_dWeightAlignment / Prm.prAlignment;

	      if (!m_vSteeringForce.isZero())
	      {
	        m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	        return m_vSteeringForce;
	      }
	    }

	    if (On(cohesion) && RandFloat() < Prm.prCohesion)
	    {
	      m_vSteeringForce += Cohesion(m_pVehicle.World().Agents()) * 
	                          m_dWeightCohesion / Prm.prCohesion;

	      if (!m_vSteeringForce.isZero())
	      {
	        m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	        return m_vSteeringForce;
	      }
	    }
	  }
	  else
	  {
	    if (On(allignment) && RandFloat() < Prm.prAlignment)
	    {
	      m_vSteeringForce += AlignmentPlus(m_pVehicle.World().Agents()) *
	                          m_dWeightAlignment / Prm.prAlignment;

	      if (!m_vSteeringForce.isZero())
	      {
	        m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	        return m_vSteeringForce;
	      }
	    }

	    if (On(cohesion) && RandFloat() < Prm.prCohesion)
	    {
	      m_vSteeringForce += CohesionPlus(m_pVehicle.World().Agents()) *
	                          m_dWeightCohesion / Prm.prCohesion;

	      if (!m_vSteeringForce.isZero())
	      {
	        m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	        return m_vSteeringForce;
	      }
	    }
	  }

	  if (On(wander) && RandFloat() < Prm.prWander)
	  {
	    m_vSteeringForce += Wander() * m_dWeightWander / Prm.prWander;

	    if (!m_vSteeringForce.isZero())
	    {
	      m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	      return m_vSteeringForce;
	    }
	  }

	  if (On(seek) && RandFloat() < Prm.prSeek)
	  {
	    m_vSteeringForce += Seek(m_pVehicle.World().Crosshair()) * m_dWeightSeek / Prm.prSeek;

	    if (!m_vSteeringForce.isZero())
	    {
	      m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	      return m_vSteeringForce;
	    }
	  }

	  if (On(arrive) && RandFloat() < Prm.prArrive)
	  {
	    m_vSteeringForce += Arrive(m_pVehicle.World().Crosshair(), m_Deceleration) * 
	                        m_dWeightArrive / Prm.prArrive;

	    if (!m_vSteeringForce.isZero())
	    {
	      m_vSteeringForce.Truncate(m_pVehicle.MaxForce()); 
	      
	      return m_vSteeringForce;
	    }
	  }
	 
	  return m_vSteeringForce;
	}
//helper method for Hide. Returns a position located on the other
//side of an obstacle to the pursuer
Vector2D GetHidingPosition( Vector2D posOb, double radiusOb, Vector2D posHunter);

//virtual ~SteeringBehaviors();

//calculates and sums the steering forces from any active behaviors
Vector2D Calculate()
{ 
	  //reset the steering force
	  m_vSteeringForce.Zero();

	  //use space partitioning to calculate the neighbours of this vehicle
	  //if switched on. If not, use the standard tagging system
	  if (!isSpacePartitioningOn())
	  {
	    //tag neighbors if any of the following 3 group behaviors are switched on
	    if (On(separation) || On(allignment) || On(cohesion))
	    {
	      m_pVehicle.World().TagVehiclesWithinViewRange(m_pVehicle, m_dViewDistance);
	    }
	  }
	  else
	  {
	    //calculate neighbours in cell-space if any of the following 3 group
	    //behaviors are switched on
	    if (On(separation) || On(allignment) || On(cohesion))
	    {
	      m_pVehicle.World().CellSpace().CalculateNeighbors(m_pVehicle.Pos(), m_dViewDistance);
	    }
	  }

	  switch (m_SummingMethod)
	  {
	  case weighted_average:
	    
	    m_vSteeringForce = CalculateWeightedSum(); break;

	  case prioritized:

	    m_vSteeringForce = CalculatePrioritized(); break;

	  case dithered:
	    
	    m_vSteeringForce = CalculateDithered();break;

	  default:m_vSteeringForce = Vector2D(0,0); 

	  }//end switch

	  return m_vSteeringForce;
	}

	//calculates the component of the steering force that is parallel
	//with the vehicle heading
	double ForwardComponent()
	{
	  return m_pVehicle.Heading().Dot(m_vSteeringForce);
	}
	//calculates the component of the steering force that is perpendicuar
	//with the vehicle heading
	double SideComponent()
	{
	  return m_pVehicle.Side().Dot(m_vSteeringForce);
	}


//renders visual aids and info for seeing how each behavior is
//calculated
void RenderAids();

void SetTarget( Vector2D t){m_vTarget = t;}

void SetTargetAgent1(Vehicle Agent){m_pTargetAgent1 = Agent;}
void SetTargetAgent2(Vehicle Agent){m_pTargetAgent2 = Agent;}

void SetOffset( Vector2D offset){m_vOffset = offset;}
Vector2D GetOffset(){return m_vOffset;}

void SetPath(std::list<Vector2D> new_path){m_pPath->Set(new_path);}
void CreateRandomPath(int num_waypoints, int mx, int my, int cx, int cy)
          {m_pPath.CreateRandomPath(num_waypoints, mx, my, cx, cy);}

Vector2D Force(){return m_vSteeringForce;}

void ToggleSpacePartitioningOnOff(){m_bCellSpaceOn = !m_bCellSpaceOn;}
boolean isSpacePartitioningOn(){return m_bCellSpaceOn;}

void SetSummingMethod(summing_method sm){m_SummingMethod = sm;}

void FleeOn(){m_iFlags |= flee;}
void SeekOn(){m_iFlags |= seek;}
void ArriveOn(){m_iFlags |= arrive;}
void WanderOn(){m_iFlags |= wander;}
void PursuitOn(Vehicle v){m_iFlags |= pursuit; m_pTargetAgent1 = v;}
void EvadeOn(Vehicle v){m_iFlags |= evade; m_pTargetAgent1 = v;}
void CohesionOn(){m_iFlags |= cohesion;}
void SeparationOn(){m_iFlags |= separation;}
void AlignmentOn(){m_iFlags |= allignment;}
void ObstacleAvoidanceOn(){m_iFlags |= obstacle_avoidance;}
void WallAvoidanceOn(){m_iFlags |= wall_avoidance;}
void FollowPathOn(){m_iFlags |= follow_path;}
void InterposeOn(Vehicle v1, Vehicle v2){m_iFlags |= interpose; m_pTargetAgent1 = v1; m_pTargetAgent2 = v2;}
void HideOn(Vehicle v){m_iFlags |= hide; m_pTargetAgent1 = v;}
void OffsetPursuitOn(Vehicle v1,  Vector2D offset){m_iFlags |= offset_pursuit; m_vOffset = offset; m_pTargetAgent1 = v1;}  
void FlockingOn(){CohesionOn(); AlignmentOn(); SeparationOn(); WanderOn();}

void FleeOff()  {if(On(flee))   m_iFlags ^=flee;}
void SeekOff()  {if(On(seek))   m_iFlags ^=seek;}
void ArriveOff(){if(On(arrive)) m_iFlags ^=arrive;}
void WanderOff(){if(On(wander)) m_iFlags ^=wander;}
void PursuitOff(){if(On(pursuit)) m_iFlags ^=pursuit;}
void EvadeOff(){if(On(evade)) m_iFlags ^=evade;}
void CohesionOff(){if(On(cohesion)) m_iFlags ^=cohesion;}
void SeparationOff(){if(On(separation)) m_iFlags ^=separation;}
void AlignmentOff(){if(On(allignment)) m_iFlags ^=allignment;}
void ObstacleAvoidanceOff(){if(On(obstacle_avoidance)) m_iFlags ^=obstacle_avoidance;}
void WallAvoidanceOff(){if(On(wall_avoidance)) m_iFlags ^=wall_avoidance;}
void FollowPathOff(){if(On(follow_path)) m_iFlags ^=follow_path;}
void InterposeOff(){if(On(interpose)) m_iFlags ^=interpose;}
void HideOff(){if(On(hide)) m_iFlags ^=hide;}
void OffsetPursuitOff(){if(On(offset_pursuit)) m_iFlags ^=offset_pursuit;}
void FlockingOff(){CohesionOff(); AlignmentOff(); SeparationOff(); WanderOff();}

boolean isFleeOn(){return On(flee);}
boolean isSeekOn(){return On(seek);}
boolean isArriveOn(){return On(arrive);}
boolean isWanderOn(){return On(wander);}
boolean isPursuitOn(){return On(pursuit);}
boolean isEvadeOn(){return On(evade);}
boolean isCohesionOn(){return On(cohesion);}
boolean isSeparationOn(){return On(separation);}
boolean isAlignmentOn(){return On(allignment);}
boolean isObstacleAvoidanceOn(){return On(obstacle_avoidance);}
boolean isWallAvoidanceOn(){return On(wall_avoidance);}
boolean isFollowPathOn(){return On(follow_path);}
boolean isInterposeOn(){return On(interpose);}
boolean isHideOn(){return On(hide);}
boolean isOffsetPursuitOn(){return On(offset_pursuit);}

double DBoxLength(){return m_dDBoxLength;}
 std::vector<Vector2D>& GetFeelers(){return m_Feelers;}

double WanderJitter(){return m_dWanderJitter;}
double WanderDistance(){return m_dWanderDistance;}
double WanderRadius(){return m_dWanderRadius;}

double SeparationWeight(){return m_dWeightSeparation;}
double AlignmentWeight(){return m_dWeightAlignment;}
double CohesionWeight(){return m_dWeightCohesion;}
	  	  
}
