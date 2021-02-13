package jme3test.helloworld;

import HelloJME3.GUI;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState.*;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.light.DirectionalLight;
import com.jme3.font.BitmapText;
import com.jme3.system.Timer;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.KeyInput;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class HelloJME3 extends SimpleApplication implements PhysicsCollisionListener{

    public static void main(String[] args) {
        //HelloJME3 app = new HelloJME3();
        GUI interfaz = new GUI("Player 1");
    }
    
    private PhysicsSpace getPhysicsSpace(){
        return bulletAppState.getPhysicsSpace();
    }
    
    private boolean isRunning = true;
    
    Material floor_mat;
    Material wall_mat;
    Material wall2_mat;
    Material stone_mat;
    Material corner_mat;
    Material cannon_mat;
    Material puck_mat;
    Material porteria_mat;
    
    private RigidBodyControl    floor_phy;
    private static final Box    floor;
    
    private RigidBodyControl    wall_phy;
    private static final Box    wall;
    private RigidBodyControl    wall2_phy;
    private static final Box    wall2;
    
    private static final Box    porteria_h;
    private static final Box    porteria_v;
    //Me encantaria saber por que tengo que declarar 4 fisicas distintas para un
    //mismo comportamiento en 4 lugares distintos, pero alright then, keep your secrets jmonkey
    private RigidBodyControl    porteria1_phy;
    private RigidBodyControl    porteria2_phy;
    private RigidBodyControl    porteria3_phy;
    private RigidBodyControl    porteria4_phy;
    
    private RigidBodyControl    ball_phy;
    private RigidBodyControl    ball_phy2;
    private RigidBodyControl    ball_phy3;
    private RigidBodyControl    ball_phy4;
    private static final Sphere sphere;
    
    private RigidBodyControl    corner1_phy;
    private RigidBodyControl    corner2_phy;
    private RigidBodyControl    corner3_phy;
    private RigidBodyControl    corner4_phy;
    private static final Cylinder    corner1;
    private static final Cylinder    corner2;
    private static final Cylinder    corner3;
    private static final Cylinder    corner4;
    
    private static final Cylinder    cannon1;
    private static final Cylinder    cannon2;
    private static final Cylinder    cannon3;
    private static final Cylinder    cannon4;
    private RigidBodyControl    cannon1_phy;
    private RigidBodyControl    cannon2_phy;
    private RigidBodyControl    cannon3_phy;
    private RigidBodyControl    cannon4_phy;
    private Spatial             puck,puck2;
    private RigidBodyControl    puck_physics,puck_phy2;
    private Geometry ball_geo;
    private Geometry ball_geo2;
    private Geometry ball_geo3;
    private Geometry ball_geo4;
    
    private player player1,player2;
    
    private BulletAppState bulletAppState;
    
    static {
        floor = new Box(5f,0.1f,5f);
        floor.scaleTextureCoordinates(new Vector2f(3, 6));
        wall = new Box(0.1f,.3f,3.5f);
        wall.scaleTextureCoordinates(new Vector2f(3, 6));
        wall2 = new Box(0.1f,.3f,3.5f);
        wall2.scaleTextureCoordinates(new Vector2f(3, 6));
        sphere = new Sphere(32, 32, 0.1f, true, false);
        sphere.setTextureMode(TextureMode.Projected);
        corner1 = new Cylinder(64,64,1f,1f,true,false);
        corner2 = new Cylinder(64,64,1f,1f,true,false);
        corner3 = new Cylinder(64,64,1f,1f,true,false);
        corner4 = new Cylinder(64,64,1f,1f,true,false);
        cannon1 = new Cylinder(64,64,.25f,.5f,true,false);
        cannon2 = new Cylinder(64,64,.25f,.5f,true,false);
        cannon3 = new Cylinder(64,64,.25f,.5f,true,false);
        cannon4 = new Cylinder(64,64,.25f,.5f,true,false);
        corner1.scaleTextureCoordinates(new Vector2f(3,6));
        corner2.scaleTextureCoordinates(new Vector2f(3,6));
        corner3.scaleTextureCoordinates(new Vector2f(3,6));
        corner4.scaleTextureCoordinates(new Vector2f(3,6));
        porteria_h = new Box(4.12f,.25f,.1f);
        porteria_v = new Box(.1f,.25f,4.12f);
    }
    
    public void initPuck(){
        puck  = assetManager.loadModel("Models/Circle.mesh.xml");
        puck2 = assetManager.loadModel("Models/Circle.mesh.xml");
        puck.setLocalTranslation(0f, 0f, 4f);
        puck2.setLocalTranslation(0f,0f,-4f);
        puck2.setName("player2");
        puck.setName("player1");
        rootNode.attachChild(puck);
        rootNode.attachChild(puck2);
        //A esto me refiero, lo coloqué pero nunca uso el objeto, tengo que reconfigurar como usarlo
        player1 = new player(puck, puck_physics, p1,ColorRGBA.Red,"ElPatoMacho",1);
        player2 = new player(puck2,puck_physics,p2,ColorRGBA.Blue,"equisde",1);
    }
    
    Geometry corner1_geo = new Geometry("Corner", corner1);
    Geometry corner2_geo = new Geometry("Corner", corner2);
    Geometry corner3_geo = new Geometry("Corner", corner3);
    Geometry corner4_geo = new Geometry("Corner", corner4);
    
    Geometry cannon1_geo = new Geometry("Cannon", cannon1);
    Geometry cannon2_geo = new Geometry("Cannon", cannon2);
    Geometry cannon3_geo = new Geometry("Cannon", cannon3);
    Geometry cannon4_geo = new Geometry("Cannon", cannon4);

    
    
    ScheduledThreadPoolExecutor executor;
    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        //bulletAppState.setDebugEnabled(true);
        
        setDisplayStatView(false);
        setDisplayFps(false);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
        
        cam.setLocation(new Vector3f(0f,5f,10f));
        cam.lookAt(new Vector3f(0,1.5f,5), Vector3f.UNIT_Y);
        flyCam.setEnabled(false);
        
        //Probando Multithreads
        executor = new ScheduledThreadPoolExecutor(4);
        
        
        //inputManager.addListener(actionListener, "shoot");
        /** Initialize the scene, materials, and physics space */
        initMaterials();
        initWall();
        initFloor();
        initCorners();
        initPuck();
        initCol();
        initPorterias();
        initHUD();
        initKeys();
        
        /*
        flyCam.setEnabled(false);
        //create the camera Node
        camNode = new CameraNode("Camera Node", cam);
        //This mode means that camera copies the movements of the target:
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        
        //Move camNode, e.g. behind and above the target:
        camNode.setLocalTranslation(new Vector3f(0, 1, -1));
        //Rotate the camNode to look at the target:
        camNode.lookAt(puck.getLocalTranslation(), Vector3f.UNIT_Y);*/
        
        getPhysicsSpace().addCollisionListener(this);
        
        //initKeys();

    }
    
    @Override
    public void destroy() {
        super.destroy();
        executor.shutdown();
    }
    
    
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(name.equals("Pause") && !keyPressed)
                isRunning = !isRunning;
        }
    };
    
    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf){
            if (isRunning){
                if (name.equals("Derecha")){
                    Vector3f v = puck.getLocalTranslation();
                    v.x+=value*speed*3;
                    puck_physics.setPhysicsLocation(v);
                }
                if (name.equals("Izquierda")){
                    Vector3f v = puck.getLocalTranslation();
                    v.x-=value*speed*3;
                    puck_physics.setPhysicsLocation(v);
                }
            }  else {
                System.out.println("PAUSA");
            }
        }
    };
    
    
    public void makeCannon1(){
        Geometry ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        ball_geo.setLocalTranslation(3.9f,.25f,3.4f);
        Vector3f c1 = new Vector3f(-1f,0f,-1f);
        ball_phy = new RigidBodyControl(.01f);
        ball_geo.addControl(ball_phy);
        ball_phy.setRestitution(1f);
        ball_phy.setCcdSweptSphereRadius(.1f);
        ball_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        ball_phy.setLinearVelocity(c1.mult(3));
        rootNode.attachChild(ball_geo);
    }
    
    public void makeCannon2(){
        Geometry ball_geo2 = new Geometry("cannon ball", sphere);
        ball_geo2.setMaterial(stone_mat);
        ball_geo2.setLocalTranslation(-3.9f,.25f,3.4f);
        Vector3f c2 = new Vector3f(1f,0f,-1f);
        ball_phy2 = new RigidBodyControl(.01f);
        ball_geo2.addControl(ball_phy2);
        ball_phy2.setRestitution(1f);
        ball_phy2.setCcdSweptSphereRadius(.1f);
        ball_phy2.setCcdMotionThreshold(Float.MIN_VALUE);
        bulletAppState.getPhysicsSpace().add(ball_phy2);
        ball_phy2.setLinearVelocity(c2.mult(3));
        rootNode.attachChild(ball_geo2);
    }
    
    
    public void makeCannon3(){
        Geometry ball_geo3 = new Geometry("cannon ball", sphere);
        ball_geo3.setMaterial(stone_mat);
        ball_geo3.setLocalTranslation(-3.9f,.25f,-3.4f);
        Vector3f c3 = new Vector3f(1f,0f,1f);
        ball_phy3 = new RigidBodyControl(.01f);
        ball_geo3.addControl(ball_phy3);
        ball_phy3.setRestitution(1f);
        ball_phy3.setCcdMotionThreshold(Float.MIN_VALUE);
        ball_phy3.setCcdSweptSphereRadius(.1f);
        bulletAppState.getPhysicsSpace().add(ball_phy3);
        ball_phy3.setLinearVelocity(c3.mult(3));
        rootNode.attachChild(ball_geo3);
    }
    
    public void makeCannon4(){
        Geometry ball_geo4 = new Geometry("cannon ball", sphere);
        ball_geo4.setMaterial(stone_mat);
        ball_geo4.setLocalTranslation(3.9f,.25f,-3.4f);
        Vector3f c4 = new Vector3f(-1f,0f,1f);
        ball_phy4 = new RigidBodyControl(.01f);
        ball_geo4.addControl(ball_phy4);
        ball_phy4.setRestitution(1f);
        ball_phy4.setCcdSweptSphereRadius(.1f);
        ball_phy4.setCcdMotionThreshold(Float.MIN_VALUE);
        bulletAppState.getPhysicsSpace().add(ball_phy4);
        ball_phy4.setLinearVelocity(c4.mult(3));
        rootNode.attachChild(ball_geo4);
    }
    
    Material player1_mat,player2_mat;
    
    public void initMaterials(){
        
        wall_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wall2_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg");
        key.setGenerateMips(true);
        Texture tex = assetManager.loadTexture(key);
        wall_mat.setTexture("ColorMap", tex);
        wall2_mat.setTexture("ColorMap", tex);
    
        floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
        key3.setGenerateMips(true);
        Texture tex3 = assetManager.loadTexture(key3);
        tex3.setWrap(WrapMode.Repeat);
        floor_mat.setTexture("ColorMap", tex3);
    
        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");       
        stone_mat.setColor("Color", ColorRGBA.Gray);
        
        corner_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key4 = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg");
        key.setGenerateMips(true);
        Texture tex4 = assetManager.loadTexture(key4);
        corner_mat.setTexture("ColorMap", tex4);
        
        cannon_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        cannon_mat.setColor("Color", ColorRGBA.Blue);
        
        player2_mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        player2_mat.setColor("Color", ColorRGBA.Blue);
        
        player1_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        player1_mat.setColor("Color",ColorRGBA.Red);
        
        porteria_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        ColorRGBA transparente = new ColorRGBA(1,1,1,0);
        porteria_mat.setColor("Color",transparente);
        porteria_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        
    }
    
    public void initCorners(){
        corner1_geo.setMaterial(corner_mat);
        corner2_geo.setMaterial(corner_mat);
        corner3_geo.setMaterial(corner_mat);
        corner4_geo.setMaterial(corner_mat);
        corner1_geo.setLocalTranslation(5f,0.4f,4.5f);
        corner2_geo.setLocalTranslation(-5f,0.4f,4.5f);
        corner3_geo.setLocalTranslation(-5f,0.4f,-4.5f);
        corner4_geo.setLocalTranslation(5f,0.4f,-4.5f);
        corner1_geo.rotate(FastMath.PI/2,0f,0f);
        corner2_geo.rotate(FastMath.PI/2,0f,0f);
        corner3_geo.rotate(FastMath.PI/2,0f,0f);
        corner4_geo.rotate(FastMath.PI/2,0f,0f);
        
        cannon1_geo.setMaterial(cannon_mat);
        cannon2_geo.setMaterial(cannon_mat);
        cannon3_geo.setMaterial(cannon_mat);
        cannon4_geo.setMaterial(cannon_mat);
        cannon1_geo.setLocalTranslation(4.15f,.25f,3.65f);
        cannon2_geo.setLocalTranslation(-4.15f,.25f,3.65f);
        cannon3_geo.setLocalTranslation(-4.15f,.25f,-3.65f);
        cannon4_geo.setLocalTranslation(4.15f,.25f,-3.65f);
        cannon1_geo.rotate(0f,FastMath.PI/4,0f);       
        cannon2_geo.rotate(0f,3*FastMath.PI/4,0f);       
        cannon3_geo.rotate(0f,FastMath.PI/4,0f);
        cannon4_geo.rotate(0f,3*FastMath.PI/4,0f);
        
        corner1_phy = new RigidBodyControl(0f);
        corner2_phy = new RigidBodyControl(0f);
        corner3_phy = new RigidBodyControl(0f);
        corner4_phy = new RigidBodyControl(0f);
        corner1_geo.addControl(corner1_phy);
        corner2_geo.addControl(corner2_phy);
        corner3_geo.addControl(corner3_phy);
        corner4_geo.addControl(corner4_phy);
        bulletAppState.getPhysicsSpace().add(corner1_phy);
        bulletAppState.getPhysicsSpace().add(corner2_phy);
        bulletAppState.getPhysicsSpace().add(corner3_phy);
        bulletAppState.getPhysicsSpace().add(corner4_phy);
        
        cannon1_phy = new RigidBodyControl(0f);
        cannon2_phy = new RigidBodyControl(0f);
        cannon3_phy = new RigidBodyControl(0f);
        cannon4_phy = new RigidBodyControl(0f);
        cannon1_geo.addControl(cannon1_phy);
        cannon2_geo.addControl(cannon2_phy);
        cannon3_geo.addControl(cannon3_phy);
        cannon4_geo.addControl(cannon4_phy);
        bulletAppState.getPhysicsSpace().add(cannon1_phy);
        bulletAppState.getPhysicsSpace().add(cannon2_phy);
        bulletAppState.getPhysicsSpace().add(cannon3_phy);
        bulletAppState.getPhysicsSpace().add(cannon4_phy);
        
        corner1_phy.setRestitution(.7f);
        corner2_phy.setRestitution(.7f);
        corner3_phy.setRestitution(.7f);
        corner4_phy.setRestitution(.7f);
        corner1_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        corner2_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        corner3_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        corner4_phy.setCcdMotionThreshold(Float.MIN_VALUE);
       
        cannon1_phy.setRestitution(.7f);
        cannon2_phy.setRestitution(.7f);
        cannon3_phy.setRestitution(.7f);
        cannon4_phy.setRestitution(.7f);
        cannon1_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        cannon2_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        cannon3_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        cannon4_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        
        rootNode.attachChild(corner1_geo);
        rootNode.attachChild(corner2_geo);
        rootNode.attachChild(corner3_geo);
        rootNode.attachChild(corner4_geo);
        
        rootNode.attachChild(cannon1_geo);
        rootNode.attachChild(cannon2_geo);
        rootNode.attachChild(cannon3_geo);
        rootNode.attachChild(cannon4_geo); 
    }
    
    public void initFloor(){
        Geometry floor_geo = new Geometry("Floor", floor);
        floor_geo.setMaterial(floor_mat);
        floor_geo.setLocalTranslation(0, -0.1f, 0);
        rootNode.attachChild(floor_geo);
        /* Make the floor physical with mass 0.0f! */
        floor_phy = new RigidBodyControl(0.0f);
        floor_geo.addControl(floor_phy);
        floor_phy.setFriction(0.6f);
        bulletAppState.getPhysicsSpace().add(floor_phy);
    }
    
    public void initWall(){
        Geometry wall_geo = new Geometry("Wall", wall);
        Geometry wall2_geo = new Geometry("Wall", wall2);
        wall_geo.setMaterial(wall_mat);
        wall_geo.setLocalTranslation(-4.9f,0.3f,0f);
        rootNode.attachChild(wall_geo);
        wall_phy = new RigidBodyControl(0.0f);
        wall_geo.addControl(wall_phy);
        wall_phy.setRestitution(.8f);
        bulletAppState.getPhysicsSpace().add(wall_phy);
        wall2_geo.setMaterial(wall_mat);
        wall2_geo.setLocalTranslation(4.9f,0.3f,0f);
        rootNode.attachChild(wall2_geo);
        wall2_phy = new RigidBodyControl(0.0f);
        wall2_geo.addControl(wall2_phy);
        wall2_phy.setRestitution(.8f);
        wall_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        wall2_phy.setCcdMotionThreshold(Float.MIN_VALUE);
        bulletAppState.getPhysicsSpace().add(wall2_phy);
    }
    
    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Izquierda",   new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Derecha",  new KeyTrigger(KeyInput.KEY_L));
                                        
        // Add the names to the action listener.
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(analogListener, new String[]{"Izquierda", "Derecha"});

    }
    
    @Override
    public void simpleUpdate(float tpf){
        int cannon;
        Timer peloticastime=getTimer();
        if(peloticastime.getTimeInSeconds()>3){
            cannon = ThreadLocalRandom.current().nextInt(1,5);
            if (cannon==1)
                makeCannon1();
            if (cannon==2)
                makeCannon2();
            if (cannon==3)
                makeCannon3();
            if (cannon==4)
                makeCannon4(); 
            peloticastime.reset();
        } 
    }
    
    private CollisionShape puckCol,puckCol2;
    
    public void initCol(){
        puckCol = CollisionShapeFactory.createMeshShape((Node) puck);
        puck_physics = new RigidBodyControl(puckCol, 0f);
        puck_physics.setRestitution(1.5f);
        puck.addControl(puck_physics); 
        puck_physics.setCcdMotionThreshold(Float.MIN_VALUE);
        bulletAppState.getPhysicsSpace().add(puck_physics);
        
        puckCol = CollisionShapeFactory.createMeshShape((Node)puck2);
        puck_phy2 = new RigidBodyControl(puckCol,0f);
        puck_phy2.setRestitution(1.5f);
        puck2.addControl(puck_phy2);
        puck_phy2.setCcdMotionThreshold(Float.MIN_VALUE);
        bulletAppState.getPhysicsSpace().add(puck_phy2);
        
       
    }
    
    Geometry p1 = new Geometry("porteria",porteria_h);
    Geometry p2 = new Geometry("porteria",porteria_h);
    Geometry p3 = new Geometry("porteria",porteria_v);
    Geometry p4 = new Geometry("porteria",porteria_v);
    
    //Luego convertiré esto en 3 funciones, una para dos porterías y otras dos para las dos faltantes
    public void initPorterias(){
        p1.setMaterial(porteria_mat);
        p2.setMaterial(porteria_mat);
        p3.setMaterial(porteria_mat);
        p4.setMaterial(porteria_mat);
        
        p1.setLocalTranslation(0,0f,5.2f);
        p2.setLocalTranslation(0,0f,-5.2f);
        p3.setLocalTranslation(5.2f,0f,0);
        p4.setLocalTranslation(-5.2f,0f,0);
        rootNode.attachChild(p1);
        rootNode.attachChild(p2);
        rootNode.attachChild(p3);
        rootNode.attachChild(p4);
        
        porteria1_phy = new RigidBodyControl(0f);
        porteria2_phy = new RigidBodyControl(0f);
        porteria3_phy = new RigidBodyControl(0f);
        porteria4_phy = new RigidBodyControl(0f);
        p1.addControl(porteria1_phy);
        p2.addControl(porteria2_phy);
        p3.addControl(porteria3_phy);
        p4.addControl(porteria4_phy);
        
        //p1.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        
        
        bulletAppState.getPhysicsSpace().add(porteria1_phy);
        bulletAppState.getPhysicsSpace().add(porteria2_phy);
        bulletAppState.getPhysicsSpace().add(porteria3_phy);
        bulletAppState.getPhysicsSpace().add(porteria4_phy);
    }
    
    private Integer peloticas_killed=0;
    
    public BitmapText name;
    public BitmapText count;
    
    public void initHUD(){
        name = new BitmapText(guiFont, false);
        name.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        name.setColor(ColorRGBA.White);                             // font color
        name.setText("PeloticaKiller");             // the text
        name.setLocalTranslation(30, settings.getHeight()-name.getLineHeight(), 0); // position
        guiNode.attachChild(name);
        count = new BitmapText(guiFont, false);
        count.setSize(guiFont.getCharSet().getRenderedSize());
        count.setColor(ColorRGBA.White);
        if(peloticas_killed==0)
            count.setText(peloticas_killed.toString());
        count.setLocalTranslation(name.getLineWidth(), 
                settings.getHeight()-count.getLineHeight()-name.getLineHeight(), 0);
        guiNode.attachChild(count);
        
    }
    
    
    @Override
    public void collision(PhysicsCollisionEvent event) {
        if("cannon ball".equals(event.getNodeA().getName()) || "cannon ball".equals(event.getNodeB().getName() )){
            if ("player1".equals(event.getNodeA().getName()) || "player1".equals(event.getNodeB().getName() ) ){
                if("cannon ball".equals(event.getNodeA().getName())){
                    event.getNodeA().setMaterial(player1_mat);
                }
                else {
                    event.getNodeB().setMaterial(player1_mat);
                }
            }
        
            if ("porteria".equals(event.getNodeA().getName()) || "porteria".equals(event.getNodeB().getName())){
                if("cannon ball".equals(event.getNodeA().getName())){
                    //Imagínate hacer todo este desastre para poder obtener el color
                    //Imagínate hacer un juego en Java <8^D
                    ColorRGBA rgb=(ColorRGBA)((Geometry)event.getNodeA()).getMaterial().getParam("Color").getValue();
                    if (rgb.equals(ColorRGBA.Red)){
                        peloticas_killed++;
                        count.setText(peloticas_killed.toString());
                        rootNode.detachChild(event.getNodeA());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeA());
                    }
                }
                
                if("cannon ball".equals(event.getNodeB().getName())){
                    //Auxilio...
                    ColorRGBA rgb=(ColorRGBA)((Geometry)event.getNodeB()).getMaterial().getParam("Color").getValue();
                    if (rgb.equals(ColorRGBA.Red)){
                        peloticas_killed++;
                        count.setText(peloticas_killed.toString());
                        rootNode.detachChild(event.getNodeB());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeB());  
                    }
                }
            }
        }
    }
    
    //TODO audio
    //los demás modelos de los pucks
    //los demás jugadores (clase players)
    //la GUI de los otros jugadores
    //Ajustar la cámara
    //la función que ajusta cada cuanto debe dispararse la pelotica
    //la función que determina la velocidad de la pelotica
    //poner bonitico el código porque nawebona de asco que da esto
}
       
