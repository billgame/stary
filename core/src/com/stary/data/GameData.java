package com.stary.data;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;


public class GameData {
	public final static GameData instance = new GameData();
	public final static float viewPortPixelWidth=1080;
	public final static float viewPortPixelHeight=607.5f;
	public final static float screenRatio=viewPortPixelWidth/viewPortPixelHeight;//1.77777
	public final static float viewPortMeterWidth=8.5f;
	public final static float viewPortMeterHeight=viewPortMeterWidth/screenRatio;
	
	public final static float pxToPhy=viewPortMeterWidth/viewPortPixelWidth;//how much meter per pixel , multiply

	public static Vector2 gravity=new Vector2(0,-10);
	
	public final static Engine ashley = new Engine();
	public final static World box2dWorld = new World(gravity, true);
	
	public float[] land;
	
	public Stage phyStage;
	public Stage pxStage;
	public PolygonSpriteBatch polygonSpriteBatch= new PolygonSpriteBatch();
	public SkeletonRenderer skeletonRenderer = new SkeletonRenderer();
	public SkeletonRendererDebug skeletonDebugRenderer = new SkeletonRendererDebug();
	public Box2DDebugRenderer box2dDebugRenderer=new Box2DDebugRenderer();
//	public PolygonSprite
	
	public static Assets assets=Assets.instance;
	
	public static String testmap="maps/testmap/scene_test.tmx";
	public static String character="characters/";
	public static String bodyType="bodyType";
	public static String staticBody="s";
	public static String dynamicBody="d";
	public static String kinematicBody="k";
	
	public final static int tilesLevel=0;
	public final static int objectsLevel=1;
	public final static int flyLevel=2;
//	public final static int smogLevel=2;
//	public final  World WORLD=new World(gravity, doSleep);
//	public SLGScreen screen;
//	public final static String unitsDB ="scripts/units.jav";
//	public final static String weaponsDB ="scripts/weapons.jav";
//	public final static String characterDB ="scripts/characters.jav";
//	public final static String forcesDB ="scripts/forces.jav";
//	public final static String spellsDB ="scripts/skills.jav";
	
	public boolean isMute;//静音
	public float music=1.0f;
	public float sound=1.0f;
	public float voice=1.0f;
	public long currentCharacter=-1;
	
//	public Map<String,String> forces_db=new HashMap<String,String>();
//	public Map<String,UnitData> units_db=new HashMap<String,UnitData>();
//	public Map<String,WeaponData> weapons_db=new HashMap<String,WeaponData>();
//	public Map<String,SkillData> skills_db=new HashMap<String,SkillData>();
//	public Map<String,CharacterData> characters_db=new HashMap<String,CharacterData>();
}
