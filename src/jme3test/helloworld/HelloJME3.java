package jme3test.helloworld;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.MouseInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.asset.TextureKey;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.light.DirectionalLight;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.system.Timer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class HelloJME3 extends SimpleApplication {

    public static void main(String[] args) {
        HelloJME3 app = new HelloJME3();
        app.start();
    }
    
    Material floor_mat;
    Material wall_mat;
    Material wall2_mat;
    Material stone_mat;
    Material corner_mat;
    Material cannon_mat;
    
    private RigidBodyControl    floor_phy;
    private static final Box    floor;
    
    private RigidBodyControl    wall_phy;
    private static final Box    wall;
    private RigidBodyControl    wall2_phy;
    private static final Box    wall2;
    
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
    
    
    private BulletAppState bulletAppState;
    
    static {
        floor = new Box(5f,0.1f,5f);
        floor.scaleTextureCoordinates(new Vector2f(3, 6));
        wall = new Box(0.1f,.3f,5f);
        wall.scaleTextureCoordinates(new Vector2f(3, 6));
        wall2 = new Box(0.1f,.3f,5f);
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
        
    }
    
    Geometry corner1_geo = new Geometry("Corner", corner1);
    Geometry corner2_geo = new Geometry("Corner", corner2);
    Geometry corner3_geo = new Geometry("Corner", corner3);
    Geometry corner4_geo = new Geometry("Corner", corner4);
    
    Geometry cannon1_geo = new Geometry("Cannon", cannon1);
    Geometry cannon2_geo = new Geometry("Cannon", cannon2);
    Geometry cannon3_geo = new Geometry("Cannon", cannon3);
    Geometry cannon4_geo = new Geometry("Cannon", cannon4);
    
    Node n1 = new Node("c1");
    Node n2 = new Node("c2");
    Node n3 = new Node("c3");
    Node n4 = new Node("c4");
    
    
    
    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        rootNode.attachChild(n1);
        rootNode.attachChild(n2);
        rootNode.attachChild(n3);
        rootNode.attachChild(n4);

        rootNode.attachChild(cannon1_geo);
        rootNode.attachChild(cannon2_geo);
        rootNode.attachChild(cannon3_geo);
        rootNode.attachChild(cannon4_geo);
        
        //cam.setLocation(new Vector3f(0,4f,6f));
        //cam.lookAt(new Vector3f(2,2,0), Vector3f.UNIT_Y);
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(10);
        flyCam.setRotationSpeed(10);
        
        
        //inputManager.addMapping("shoot",
            //new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        //inputManager.addListener(actionListener, "shoot");
        /** Initialize the scene, materials, and physics space */
        initMaterials();
        initWall();
        initFloor();
        initCrossHairs();
        initCorners();
        
    }
    
    /*
    private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      
    }
    };
    */
    
    public void makeCannon1(){
        Geometry ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        ball_geo.setLocalTranslation(3.9f,.4f,3.4f);
        rootNode.attachChild(ball_geo);
        Vector3f c1 = new Vector3f(-1f,0f,-1f);
        ball_phy = new RigidBodyControl(5f);
        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        ball_phy.setLinearVelocity(c1.mult(3));
    }
    
    public void makeCannon2(){
        Geometry ball_geo2 = new Geometry("cannon ball", sphere);
        ball_geo2.setMaterial(stone_mat);
        ball_geo2.setLocalTranslation(-3.9f,.4f,3.4f);
        rootNode.attachChild(ball_geo2);
        Vector3f c2 = new Vector3f(1f,0f,-1f);
        ball_phy2 = new RigidBodyControl(5f);
        ball_geo2.addControl(ball_phy2);
        bulletAppState.getPhysicsSpace().add(ball_phy2);
        ball_phy2.setLinearVelocity(c2.mult(3));
    }
    
    public void makeCannon3(){
        Geometry ball_geo3 = new Geometry("cannon ball", sphere);
        ball_geo3.setMaterial(stone_mat);
        ball_geo3.setLocalTranslation(-3.9f,.4f,-3.4f);
        rootNode.attachChild(ball_geo3);
        Vector3f c3 = new Vector3f(1f,0f,1f);
        ball_phy3 = new RigidBodyControl(5f);
        ball_geo3.addControl(ball_phy3);
        bulletAppState.getPhysicsSpace().add(ball_phy3);
        ball_phy3.setLinearVelocity(c3.mult(3));
    }
    
    public void makeCannon4(){
        Geometry ball_geo4 = new Geometry("cannon ball", sphere);
        ball_geo4.setMaterial(stone_mat);
        ball_geo4.setLocalTranslation(3.9f,.4f,-3.4f);
        rootNode.attachChild(ball_geo4);
        Vector3f c4 = new Vector3f(-1f,0f,1f);
        ball_phy4 = new RigidBodyControl(5f);
        ball_geo4.addControl(ball_phy4);
        bulletAppState.getPhysicsSpace().add(ball_phy4);
        ball_phy4.setLinearVelocity(c4.mult(3));
    }
    
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
        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);
        
        corner_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key4 = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg");
        key.setGenerateMips(true);
        Texture tex4 = assetManager.loadTexture(key4);
        corner_mat.setTexture("ColorMap", tex4);
        
        cannon_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        cannon_mat.setColor("Color", ColorRGBA.Blue);
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
        cannon1_geo.setLocalTranslation(4.15f,.4f,3.65f);
        cannon2_geo.setLocalTranslation(-4.15f,.4f,3.65f);
        cannon3_geo.setLocalTranslation(-4.15f,.4f,-3.65f);
        cannon4_geo.setLocalTranslation(4.15f,.4f,-3.65f);
        cannon1_geo.rotate(0f,FastMath.PI/4,0f);       
        cannon2_geo.rotate(0f,3*FastMath.PI/4,0f);       
        cannon3_geo.rotate(0f,FastMath.PI/4,0f);
        cannon4_geo.rotate(0f,3*FastMath.PI/4,0f);
        
        
        rootNode.attachChild(corner1_geo);
        rootNode.attachChild(corner2_geo);
        rootNode.attachChild(corner3_geo);
        rootNode.attachChild(corner4_geo);
        
        rootNode.attachChild(cannon1_geo);
        rootNode.attachChild(cannon2_geo);
        rootNode.attachChild(cannon3_geo);
        rootNode.attachChild(cannon4_geo);
        
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
    }
    
    public void initFloor(){
        Geometry floor_geo = new Geometry("Floor", floor);
        floor_geo.setMaterial(floor_mat);
        floor_geo.setLocalTranslation(0, -0.1f, 0);
        rootNode.attachChild(floor_geo);
        /* Make the floor physical with mass 0.0f! */
        floor_phy = new RigidBodyControl(0.0f);
        floor_geo.addControl(floor_phy);
        bulletAppState.getPhysicsSpace().add(floor_phy);
    }
    
    public void initWall(){
        Geometry wall_geo = new Geometry("Wall", wall);
        Geometry wall2_geo = new Geometry("Wall", wall2);
        wall_geo.setMaterial(wall_mat);
        wall_geo.setLocalTranslation(-4.9f,0.3f,0);
        rootNode.attachChild(wall_geo);
        wall_phy = new RigidBodyControl(0.0f);
        wall_geo.addControl(wall_phy);
        bulletAppState.getPhysicsSpace().add(wall_phy);
        wall2_geo.setMaterial(wall_mat);
        wall2_geo.setLocalTranslation(4.9f,0.3f,0);
        rootNode.attachChild(wall2_geo);
        wall2_phy = new RigidBodyControl(0.0f);
        wall2_geo.addControl(wall2_phy);
        bulletAppState.getPhysicsSpace().add(wall2_phy);
    }
    

    /** A plus sign used as crosshairs to help the player with aiming.*/
    protected void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+");        // fake crosshairs :)
        ch.setLocalTranslation( // center
          settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
          settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
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
    
}
       