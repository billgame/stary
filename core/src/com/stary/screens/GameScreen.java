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
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.ems.systems.SkeletonSystem;
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

	SkeletonRendererDebug debugRenderer;
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
		polygonSpriteBatch = new PolygonSpriteBatch(); // Required to render meshes. SpriteBatch can't render meshes.
		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
		debugRenderer = new SkeletonRendererDebug();
//		debugRenderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
		debugRenderer.setBoundingBoxes(false);
		debugRenderer.setRegionAttachments(false);
		debugRenderer.setMeshTriangles(false);
		
		box2dDebugRenderer=new Box2DDebugRenderer();
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
		
		Family f=Family.all(SkeletonBox2dComponent.class).get();
		SkeletonSystem skeletonSystem= new SkeletonSystem(f);
		spineRenderSystem=new SpineRenderSystem(f);
		ashleyEngine.addEntityListener(Family.all(SkeletonBox2dComponent.class).get(),skeletonSystem);
		ashleyEngine.addSystem(skeletonSystem);
		ashleyEngine.addSystem(spineRenderSystem);
		
		loadMap();
//		System.out.println(tiledMap.getLayers().get("physicalLayer"));
		createPhyLayer(tiledMap.getLayers());
		initEntityBox2dSpine(tiledMap.getLayers());
	}
	@Override
	public void render(float delta) {

		box2dWorld.step(delta, 6, 2);
		
		ashley.update(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		super.render(delta);
		SpriteBatch pxBatch=(SpriteBatch) pxStage.getBatch();
		pxBatch.setProjectionMatrix(pxStage.getCamera().combined);
		pxBatch.begin();
		//draw map layers
		for (int i = 0; i < layers.getCount(); i++) {
			MapLayer layer=layers.get(i);
			//if it has some object ,it's an objectslayer
//			if (!layer.getName().equals("objectLayer_bill_close1"))
//				continue;
			if(layer.getName().startsWith("physicalLayer")){
				continue;
			}
			MapObjects objs=layer.getObjects();
			for (int j = 0; j < objs.getCount(); j++) {
				MapObject obj=objs.get(j);
//				if(j==1)break;
//				if (!(obj.getName().equals("mountain1"))) continue;
				if(!obj.isVisible()) continue;
				if (obj instanceof TextureMapObject) {
					TextureMapObject texObj=(TextureMapObject)obj;
					TextureRegion region=texObj.getTextureRegion();
//					float w=texObj.getProperties().get("width",0.0f,Float.class);//scaled width
//					float h=texObj.getProperties().get("height",0.0f,Float.class);//scaled height
					pxBatch.draw(region,texObj.getX(), texObj.getY(),
							texObj.getOriginX(), texObj.getOriginY(), 
							region.getRegionWidth(),region.getRegionHeight(),
							texObj.getScaleX(),texObj.getScaleY(),
							-texObj.getRotation());
					//same as above...[bill]
//					pxBatch.draw(region,texObj.getX(), texObj.getY(),
//							texObj.getOriginX(), texObj.getOriginY(), 
//							w,h,1,1,-texObj.getRotation());
					//same as above...[bill]
//					Affine2 a=new Affine2();
//					a.translate(texObj.getX(),texObj.getY());//second translates 
//					a.rotate(-texObj.getRotation());	//first rotates at world orgin
//					pxBatch.draw(region, w, h, a);
				}
			}
		}
		pxBatch.end();
		
		spineRenderSystem.update(delta);// render spine
		
	}
	MapLayers layers;
	public void loadMap(){
		tiledMap=new TmxMapLoader().load(testmap);
		layers=tiledMap.getLayers();
//		for (int i = 0; i < layers.getCount(); i++) {
//			MapLayer layer=layers.get(i);
//			System.out.println(layer.getName()+" opacity:"+layer.getOpacity()
//			+" isMaplayer:"+(layer.getClass()==MapLayer.class)+", "
//			+"x objects:"+layer.getObjects().getCount()+" class:"+ layer.getClass().getSimpleName());
//		}
		MapLayer layer=layers.get("objectLayer_bill_close2");
		MapObjects objects=layer.getObjects();
	}
	

	private void initEntityBox2dSpine(MapLayers layers2) {
		MapLayer actorLayer=layers.get("actors");
		for(MapObject actorObj:actorLayer.getObjects()){
			String actorName=actorObj.getName();
			//position in world(meter) [bill]
			Vector2 pos=TMXUtil.getPosition(actorObj, pxToPhy);
			Entity actor=
					AshleyUtil.createActor()
					.add(new SkeletonBox2dComponent(box2dWorld,actorName,pos.x,pos.y,pxToPhy))
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
