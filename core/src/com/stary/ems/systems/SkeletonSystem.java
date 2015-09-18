/**
 * @author billzhu
 */
package com.stary.ems.systems;

import static com.stary.data.GameData.character;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.AttachmentLoader;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.stary.data.GameData;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.utils.Box2dBoundingBoxAttachment;
import com.stary.utils.Box2dUtil;

public class SkeletonSystem extends IteratingSystem implements EntityListener{
	
	ComponentMapper<SkeletonBox2dComponent> skeletonBox2dComponent=ComponentMapper.getFor(SkeletonBox2dComponent.class);
	
	public SkeletonSystem(Family family) {
		super(family);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void entityAdded(Entity entity) {
		SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponent.get(entity);
		String name=skeletonComponent.name;
		TextureAtlas atlas=new TextureAtlas(character+name+".atlas");
		AttachmentLoader al=new AtlasAttachmentLoader(atlas) {
			@Override
			public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
				return new Box2dBoundingBoxAttachment(name);
			}
		};
		SkeletonJson sklJson = new SkeletonJson(al);
		sklJson.setScale(skeletonComponent.pxToPhy);
		SkeletonData skeletonData = sklJson.readSkeletonData(Gdx.files.internal(character+name+".json"));
		Skeleton skeleton=new Skeleton(skeletonData);
		skeleton.setPosition(skeletonComponent.x, skeletonComponent.y);
		skeleton.setSkin("goblin");
//		skeleton.setFlipX(true);		//FIXME box2d body not correct
		skeleton.updateWorldTransform();
		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
//		stateData.setMix("run", "jump", 0.2f);
//		stateData.setMix("jump", "run", 0.2f);
		AnimationState state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
//		state.setTimeScale(0.5f); // Slow all animations down to 50% speed.
		// Queue animations on track 0.
		state.setAnimation(0, "walk", true);

		skeletonComponent.skeletonJson=sklJson;
		skeletonComponent.skeletonData=skeletonData;
		skeletonComponent.skeleton=skeleton;
		skeletonComponent.animationStateData=stateData;
		skeletonComponent.animationState=state;
		
		//create box2d part 
		
		for(int s=0;s<skeleton.getSlots().size;s++){
			Slot slot=skeleton.getSlots().get(s);
			Attachment attachment = slot.getAttachment();
			if (! (attachment instanceof Box2dBoundingBoxAttachment)) continue;
			Box2dBoundingBoxAttachment bba=(Box2dBoundingBoxAttachment)attachment;
			float[] worldVertices=new float[bba.getVertices().length];
			bba.computeWorldVertices(slot.getBone(), worldVertices);
			//3 <= count && count <= 8
			//vertices wouldn't more than 8 in polygonShape
			PolygonShape shape=new PolygonShape(); 
//			ChainShape shape=new ChainShape();
//			shape.set(worldVertices);
			shape.set(bba.getVertices());
//			shape.createLoop(worldVertices);
			BodyDef bodyDef=new BodyDef();
			bodyDef.type=BodyType.DynamicBody;
			FixtureDef actorFixtureDef=new FixtureDef();
			actorFixtureDef.shape=shape;
			//TODO [bill]terrian density,friction...will be setting in database manually
			actorFixtureDef.density=1f;
			actorFixtureDef.friction=0.81f;
			actorFixtureDef.restitution=0.2f;
			Body actorComponentBody=skeletonComponent.box2dWorld.createBody(bodyDef);
			actorComponentBody.createFixture(actorFixtureDef);
			actorFixtureDef.shape=null;
			shape.dispose();
			bba.setBody(actorComponentBody);//setup body to attachment
		}//for each slots

		skeleton.updateWorldTransform();
//		Box2dUtil.updateBody(skeleton);

		//spine 2 box2d 按spine skeleton所有boundingbox的位置，角度给与box2d的body
		Box2dUtil.spineToBox2d(skeleton.getSlots());  //position box2d bodies
	}

	@Override
	public void entityRemoved(Entity entity) {
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponent.get(entity);
		AnimationState animationState=skeletonComponent.animationState;

		animationState.update(deltaTime);
		animationState.apply(skeletonComponent.skeleton);
		skeletonComponent.skeleton.updateWorldTransform();
		
		if(GameData.instance.land!=null){
			Skeleton skl=skeletonComponent.skeleton;
			float x=skl.getX();
			float y1=skl.getY()+deltaTime*GameData.instance.gravity.y/5;
			float y=Box2dUtil.getLandY(GameData.instance.land, x);
			if (y1<y) {
				y1=y;
			}
			skl.setPosition(skl.getX()+0.01f, y1);
		}
		
		//spine 2 box2d 按spine skeleton所有boundingbox的位置，角度给与box2d的body
		Box2dUtil.spineToBox2d(skeletonComponent.skeleton.getSlots());
//		Box2dUtil.box2dToSpine(skeletonComponent.skeleton.getSlots());
		
	}
	

}
