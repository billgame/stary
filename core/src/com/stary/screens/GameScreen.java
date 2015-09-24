package com.stary.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.AttachmentLoader;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.attachments.SkinnedMeshAttachment;
import com.stary.data.Assets;
import com.stary.data.GameData;
import com.stary.ems.components.CharacterStateComponent;
import com.stary.ems.components.SceneItemComponent;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.ems.components.ZComparator;
import com.stary.ems.components.ZComponent;
import com.stary.ems.systems.SceneControlSystem;
import com.stary.ems.systems.SceneRenderSystem;
import com.stary.ems.systems.SkeletonControlSystem;
import com.stary.ems.systems.SpineRenderSystem;
import com.stary.inputs.InputManager;
import com.stary.utils.AshleyUtil;
import com.stary.utils.Box2dBoundingBoxAttachment;
import com.stary.utils.Box2dUtil;
import com.stary.utils.TMXUtil;

import static com.stary.data.GameData.*;
/**
 * a Odin-like
 * side-scrolling action game
 * 
 * 
 * @author billzhu
 *
 */
public class GameScreen extends BasicScreen{
//	Camera uiCamera;
//	Camera worldCamera;
	float pw=GameData.viewPortPixelWidth, ph=GameData.viewPortPixelHeight;
	float mw=GameData.viewPortMeterWidth;
	float screenRatio=GameData.screenRatio;//1.77777
	float pxToPhy=GameData.pxToPhy;//how much meter per pixel , multiply
	
	Engine ashleyEngine;
	
	PolygonSpriteBatch polygonSpriteBatch;
	
	Box2DDebugRenderer box2dDebugRenderer;
	ShapeRenderer sr=new ShapeRenderer();
	
	SpineRenderSystem spineRenderSystem;
	SceneRenderSystem sceneRenderSystem;

	SkeletonRendererDebug skeletonDebugRenderer;
	SkeletonRenderer skeletonRenderer;
	SkeletonJson json;
	SkeletonBounds skeletonBounds=new  SkeletonBounds();
	
	World box2dWorld;
	
	InputManager inputManager;
//	Array<Skeleton> skeletons;
	
	TiledMap tiledMap;
	Stage phyStage,pxStage;
	SpriteBatch phyBatch;
	public GameScreen(Game game) {
		super(game);
	}
	@Override
	public void show() {
		polygonSpriteBatch = GameData.instance.polygonSpriteBatch; // Required to render meshes. SpriteBatch can't render meshes.
		skeletonRenderer = GameData.instance.skeletonRenderer;
		skeletonRenderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
		skeletonDebugRenderer = GameData.instance.skeletonDebugRenderer;
//		debugRenderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
		skeletonDebugRenderer.setBoundingBoxes(false);
		skeletonDebugRenderer.setRegionAttachments(false);
		skeletonDebugRenderer.setMeshTriangles(false);
		
		box2dDebugRenderer=GameData.instance.box2dDebugRenderer;
		box2dWorld=GameData.box2dWorld;
		
		//(8.5  5.1)(32, 19.2f)
		phyStage =new Stage(new FitViewport(mw, mw/screenRatio,cameraMap));
		//(800,480)
		pxStage= new Stage(new FitViewport(pw, ph), phyStage.getBatch());
		phyBatch=(SpriteBatch) phyStage.getBatch();
		
		GameData.instance.pxStage= pxStage;
		GameData.instance.phyStage= phyStage;
		
		inputManager=new InputManager(this,pxStage,phyStage);
		super.show();


		ashleyEngine=GameData.ashley;
		
		Family SkeletonBox2dComponentFamily=Family.all(SkeletonBox2dComponent.class).get();
		Family sceneFamily=Family.all(ZComponent.class,SceneItemComponent.class).get();
		
		SkeletonControlSystem skeletonSystem= new SkeletonControlSystem(SkeletonBox2dComponentFamily);
		SceneControlSystem SceneControlSystem=new SceneControlSystem(sceneFamily);
		sceneRenderSystem=new SceneRenderSystem(sceneFamily, new ZComparator());
		spineRenderSystem=new SpineRenderSystem(SkeletonBox2dComponentFamily);
		ashleyEngine.addEntityListener(SkeletonBox2dComponentFamily,skeletonSystem);
		ashleyEngine.addSystem(skeletonSystem);
		ashleyEngine.addSystem(SceneControlSystem);
		ashleyEngine.addSystem(sceneRenderSystem);//not process by ashley
		ashleyEngine.addSystem(spineRenderSystem);//not process by ashley
		
		loadMap();
//		System.out.println(tiledMap.getLayers().get("physicalLayer"));
		createPhyLayer(tiledMap.getLayers());
		initEntityBox2dSpine(tiledMap.getLayers());
	}
	@Override
	public void render(float delta) {

		box2dWorld.step(delta, 6, 2);	//update box2d
		ashley.update(delta);			//update logic system
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		super.render(delta);
		sceneRenderSystem.update(delta);	//render scene
		spineRenderSystem.update(delta);	// render spine
		
	}
	MapLayers layers;
	public void loadMap(){
		tiledMap=new TmxMapLoader().load(testmap);
		layers=tiledMap.getLayers();
		//draw map layers
		for (int i = 0; i < layers.getCount(); i++) {
			MapLayer layer=layers.get(i);
			String z=layer.getProperties().get("z",String.class);
			if (z==null) continue;//fiter other layer ,leave about scene layer
			//-1 is the top layer
			float layerZ=Float.valueOf(layer.getProperties().get("z","-1",String.class));
			MapObjects objs=layer.getObjects();
			for (int j = 0; j < objs.getCount(); j++) {
				MapObject obj=objs.get(j);
//				if(j==1)break;
//				if (!(obj.getName().equals("mountain1"))) continue;
				if(!obj.isVisible()) continue;
				if (obj instanceof TextureMapObject) {
					TextureMapObject texObj=(TextureMapObject)obj;
					TextureRegion region=texObj.getTextureRegion();
					ZComponent zComponent=new ZComponent(layerZ, false);
					SceneItemComponent itemComponent= new SceneItemComponent();
					itemComponent.name= texObj.getName();
					itemComponent.isVisable=texObj.isVisible();
					itemComponent.textureMapObject=texObj;
					itemComponent.region=region;
					itemComponent.x=texObj.getX()*pxToPhy;
					itemComponent.y=texObj.getY()*pxToPhy;
					itemComponent.originX=texObj.getOriginX();
					itemComponent.originY=texObj.getOriginY();
					itemComponent.z=layerZ;
					itemComponent.regionWidth=region.getRegionWidth()*pxToPhy;
					itemComponent.regionHeight=region.getRegionHeight()*pxToPhy;
					itemComponent.scaleX=texObj.getScaleX();
					itemComponent.scaleY=texObj.getScaleY();
					itemComponent.rotatioin=texObj.getRotation();
					Entity itemEntity=AshleyUtil.createSceneItem();
					itemEntity.add(itemComponent).add(zComponent);
					ashley.addEntity(itemEntity);
				}//end if TextureMapObject
			}//end for each objects
		}//end for each layers
	}
	

	private void initEntityBox2dSpine(MapLayers layers2) {
		MapLayer actorLayer=layers.get("actors");
		for(MapObject actorObj:actorLayer.getObjects()){
			String actorName=actorObj.getName();
			//position in world(meter) [bill]
			Vector2 pos=TMXUtil.getPosition(actorObj, pxToPhy);
			Entity actor=
					AshleyUtil.createActor()
					.add(new ZComponent(0.1f,true))
					.add(new SkeletonBox2dComponent(actorName,pos.x,pos.y))
					.add(new CharacterStateComponent());
			ashleyEngine.addEntity(actor);
			GameData.instance.currentCharacter=actor.getId();
		}
	}
	
	AnimationState state;
	public Skeleton initSpineActor(MapObject actor){
		String name=actor.getName();
		TextureAtlas atlas=new TextureAtlas(character+name+".atlas");
		AttachmentLoader al=new AtlasAttachmentLoader(atlas) {
			@Override
			public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
				return new Box2dBoundingBoxAttachment(name);
			}
		};
		json = new SkeletonJson(al);
		json.setScale(pxToPhy);
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(character+name+".json"));
		Skeleton skeleton=new Skeleton(skeletonData);
		Vector2 pos=TMXUtil.getPosition(actor,pxToPhy);//get position(in meter) 
		skeleton.setPosition(pos.x, pos.y);
		skeleton.setSkin("goblin");//TODO change to var 
//		skeleton.setToSetupPose();
		skeleton.updateWorldTransform();
		
		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
//		stateData.setMix("run", "jump", 0.2f);
//		stateData.setMix("jump", "run", 0.2f);

		state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
		state.setTimeScale(0.5f); // Slow all animations down to 50% speed.

		// Queue animations on track 0.
		state.setAnimation(0, "walk", true);
		
//		skeletonBounds.update(skeleton, false);
//		skeletonBounds.getPolygons();
		return skeleton;
//		for(FloatArray a:skeletonBounds.getPolygons()){
//			BodyDef boxBodyDef = new BodyDef();
//			boxBodyDef.type = BodyType.StaticBody;
//			attachment.body = world.createBody(boxBodyDef);
//			attachment.body.createFixture(boxPoly, 1);
//
//			boxPoly.dispose();
//		}
//		for (Slot slot : skeleton.getSlots()) {
//			if (!(slot.getAttachment() instanceof BoundingBoxAttachment)) continue;
//			BoundingBoxAttachment attachment = (BoundingBoxAttachment)slot.getAttachment();
//
//			PolygonShape boxPoly = new PolygonShape();
//			boxPoly.setAsBox(attachment.getWidth() / 2 * attachment.getScaleX(),
//				attachment.getHeight() / 2 * attachment.getScaleY(), vector.set(attachment.getX(), attachment.getY()),
//				attachment.getRotation() * MathUtils.degRad);
//		}
		
	}
	public void createPhyLayer(MapLayers layers){
		MapLayer box2dLayer=layers.get("physicalLayer");
		for(MapObject obj:box2dLayer.getObjects()){
			if (obj instanceof TextureMapObject) continue;
			Shape shape = null;
			BodyDef bodyDef=new BodyDef();
			String type=
					obj.getProperties().get(bodyType,staticBody, String.class);//TODO staticBody emun.valueOf()
			if(staticBody.equals(type)){
				bodyDef.type=BodyDef.BodyType.StaticBody;
			}else if(dynamicBody.equals(type)){
				bodyDef.type=BodyDef.BodyType.DynamicBody;
			}else{
				bodyDef.type=BodyDef.BodyType.KinematicBody;
			}
			shape=Box2dUtil.getShape(obj, pxToPhy);
			if ("physicalLand".equals(obj.getName())) {
				GameData.instance.land=Box2dUtil.getVertices(obj, pxToPhy);
			}
			/*
			 * [bill]
			 * TODO terrian material will be settingin TMX map file(poperty terrian：xxx)
			 * for example: terrian:mountain terrian:snow terrian:green terrian:sand
			 */
			FixtureDef fixtureDef=new FixtureDef();
			fixtureDef.shape=shape;
			//TODO [bill]terrian density,friction...will be setting in database manually
			fixtureDef.density=0.8f;
			fixtureDef.friction=0.8f;
			fixtureDef.restitution=0.15f;
			Body body=box2dWorld.createBody(bodyDef);
			body.createFixture(fixtureDef);
//			bodies.add(body);
			fixtureDef.shape=null;
			shape.dispose();
		}//end for
		
	}
}
