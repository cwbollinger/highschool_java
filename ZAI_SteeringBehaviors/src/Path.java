import java.util.List;

//------------------------------------------------------------------------
//
//  Name:   Path.h
//
//  Desc:   class to define, manage, and traverse a path (defined by a series of 2D vectors)
//          
//
//  Author: Mat Buckland 2003 (fup@ai-junkie.com)
//
//------------------------------------------------------------------------
/*
#include <list>
#include <cassert>

#include "2d/Vector2D.h"
*/

public class Path {
		  
		  List<Vector2D> m_WayPoints;

		  //points to the current waypoint
		  List<Vector2D>::iterator  curWaypoint;

		  //flag to indicate if the path should be looped
		  //(The last waypoint connected to the first)
		  boolean m_bLooped;
		  
		  Path()
		  {
			  m_bLooped = false;
		  }

		  //constructor for creating a path with initial random waypoints. MinX/Y
		  //& MaxX/Y define the bounding box of the path.
		  Path(int NumWaypoints, double MinX, double MinY, double MaxX, double MaxY, boolean looped)
		  {
			m_bLooped = looped;
			
		    CreateRandomPath(NumWaypoints, MinX, MinY, MaxX, MaxY);

		    curWaypoint = m_WayPoints.begin();
		  }


		  //returns the current waypoint
		  Vector2D    CurrentWaypoint(){assert(curWaypoint != NULL); return curWaypoint;}

		  //returns true if the end of the list has been reached
		  boolean     Finished(){return !(curWaypoint != m_WayPoints.end());}
		  
		  //moves the iterator on to the next waypoint in the list
		  void SetNextWaypoint()
			{
			  assert (m_WayPoints.size() > 0);
			    
			  if (++curWaypoint == m_WayPoints.end())
			  {
			    if (m_bLooped)
			    {
			      curWaypoint = m_WayPoints.begin(); 
			    }
			  }
			}  

		  //creates a random path which is bound by rectangle described by
		  //the min/max values
		  List<Vector2D> CreateRandomPath(int NumWaypoints, double MinX, double MinY, 
				  							   double MaxX, double MaxY)
		  {
			  m_WayPoints.clear();

			  double midX = (MaxX+MinX)/2.0;
			  double midY = (MaxY+MinY)/2.0;

			  double smaller = min(midX, midY);

			  double spacing = TwoPi/(double)NumWaypoints;

			  for (int i=0; i<NumWaypoints; ++i)
			  {
				  double RadialDist = RandInRange(smaller*0.2f, smaller);

				  Vector2D temp(RadialDist, 0.0f);

				  Vec2DRotateAroundOrigin(temp, (i*spacing));

				  temp.x += midX; temp.y += midY;
      
				  m_WayPoints.push_back(temp);
                            
			  }

			  curWaypoint = m_WayPoints.begin();

			  return m_WayPoints;
		  }

				  							   


		  void LoopOn(){m_bLooped = true;}
		  void LoopOff(){m_bLooped = false;}
		 
		  //adds a waypoint to the end of the path
		  void AddWayPoint(Vector2D new_point);

		  //methods for setting the path with either another Path or a list of vectors
		  void Set(List<Vector2D> new_path){m_WayPoints = new_path;curWaypoint = m_WayPoints.begin();}
		  void Set(Path path)
		  {
			  m_WayPoints=path.GetPath(); curWaypoint = m_WayPoints.begin();
		  }
		  

		  void Clear(){m_WayPoints.clear();}

		  List<Vector2D> GetPath(){
			  return m_WayPoints;
		  }

		  //renders the path in orange
		  void Render()
		  {
			  /*
			 ListgePen();

			  List<Vector2D>::const_iterator it = m_WayPoints.begin();

			  Vector2D wp = *it++;

			  while (it != m_WayPoints.end())
			  {
			    gdi.Line(wp, *it);

			    wp = *it++;
			  }

			  if (m_bLooped) gdi.Line(*(--it), *m_WayPoints.begin());
			}
			*/
		}

}
