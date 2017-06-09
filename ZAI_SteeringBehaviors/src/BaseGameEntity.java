//------------------------------------------------------------------------
//
//  Name: BaseGameEntity.h
//
//  Desc: Base class to define a common interface for all game
//        entities
//
//  Author: Mat Buckland 2002 (fup@ai-junkie.com)
//
//------------------------------------------------------------------------

public abstract class BaseGameEntity
{
  enum EntityType { default_entity_type };

  //each entity has a unique ID
  private int         m_ID;

  //every entity has a type associated with it (health, troll, ammo etc)
  private int         m_EntityType;

  //this is a generic flag. 
  private boolean        m_bTag;

  //used by the constructor to give each entity a unique ID
  static int NextID = 0; 
  
  int NextValidID() {   
	  return NextID++; 
  }


  
  //its location in the environment
  protected Vector2D m_vPos;

  protected Vector2D m_vScale;

  //the length of this object's bounding radius
  protected double    m_dBoundingRadius;

  
  BaseGameEntity() { 
	  m_ID = NextValidID();
                   m_dBoundingRadius = 0.0;
                   m_vPos = new Vector2D();
                   m_vScale = new Vector2D(1.0,1.0);
                   m_EntityType = EntityType.default_entity_type.ordinal();
                   m_bTag = false;
  }

  BaseGameEntity(int entity_type) {
	  m_ID = NextValidID();
                   m_dBoundingRadius = 0.0;
                   m_vPos = new Vector2D();
                   m_vScale = new Vector2D(1.0,1.0);
                   m_EntityType = entity_type;
                   m_bTag = false;
  }
  
  BaseGameEntity(int entity_type, Vector2D pos, double r) {
	  m_vPos = pos;
                                        m_dBoundingRadius = r;
                                        m_ID = NextValidID();
                                        m_vScale = new Vector2D(1.0,1.0);
                                        m_EntityType = entity_type;
                                        m_bTag = false;
                                        
  }

  //this can be used to create an entity with a 'forced' ID. It can be used
  //when a previously created entity has been removed and deleted from the
  //game for some reason. For example, The Raven map editor uses this ctor 
  //in its undo/redo operations. 
  //USE WITH CAUTION!
  BaseGameEntity(int entity_type, int ForcedID) {
	  m_ID = ForcedID;
                   m_dBoundingRadius = 0.0;
                   m_vPos = new Vector2D();
                   m_vScale = new Vector2D(1.0,1.0);
                   m_EntityType = entity_type;
                   m_bTag = false;
  }




  //virtual ~BaseGameEntity(){}

  abstract void Update(double time_elapsed); 

  abstract void Render();

  abstract boolean HandleMessage(Telegram msg);
  
  //entities should be able to read/write their data to a stream
  //abstract void Write(std::ostream&  os);
  //abstract void Read (std::ifstream& is);
  

  Vector2D Pos() {
	  return m_vPos;
  }
  void SetPos(Vector2D new_pos){
	  m_vPos = new_pos;
  }

  double BRadius(){
	  return m_dBoundingRadius;
  }
  void SetBRadius(double r){
	  m_dBoundingRadius = r;
  }
  int ID(){
	  return m_ID;
  }

  boolean IsTagged(){
	  return m_bTag;
  }
  void Tag(){
	  m_bTag = true;
  }
  void UnTag() {
	  m_bTag = false;
  }

  Vector2D Scale(){
	  return m_vScale;
  }
  void SetScale(Vector2D val){
	  m_dBoundingRadius *= Math.max(val.x, val.y)/Math.max(m_vScale.x, m_vScale.y); 
	  m_vScale = val;
  }
  void SetScale(double val){
	  m_dBoundingRadius *= (val/Math.max(m_vScale.x, m_vScale.y)); 
	  m_vScale = new Vector2D(val, val);
  } 

  int EntityType(){
	  return m_EntityType;
  }
  void SetEntityType(int new_type){
	  m_EntityType = new_type;
  }

}