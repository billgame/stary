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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
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
import com.stary.ems.components.Action;
import com.stary.ems.components.CharacterStateComponent;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.ems.components.CharacterStateComponent.State;
import com.stary.ems.components.JoystickComponent;
import com.stary.utils.Box2dBoundingBoxAttachment;
import com.stary.utils.Box2dUtil;

public class SkeletonControlSystem extends IteratingSystem implements EntityListener{
	
	ComponentMapper<SkeletonBox2dComponent> skeletonBox2dComponentMapper=ComponentMapper.getFor(SkeletonBox2dComponent.class);
	ComponentMapper<CharacterStateComponent> stateComponentMapper=ComponentMapper.getFor(CharacterStateComponent.class);
	ComponentMapper<JoystickComponent> joystickComponentMapper=ComponentMapper.getFor(JoystickComponent.class);
	public SkeletonControlSystem(Family family) {
		super(family);
	}

	@Override
	public void entityAdded(Entity entity) {
		SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
		String name=skeletonComponent.name;
		TextureAtlas atlas=new TextureAtlas(character+name+".atlas");
		AttachmentLoader al=new AtlasAttachmentLoader(atlas) {
			@Override
			public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
				return new Box2dBoundingBoxAttachment(name);
			}
		};
		SkeletonJson skeletonJson = new SkeletonJson(al);
		skeletonJson.setScale(skeletonComponent.pxToPhy);
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(character+name+".json"));
		Skeleton skeleton=new Skeleton(skeletonData);
		skeleton.setPosition(skeletonComponent.x, skeletonComponent.y);
		skeleton.setSkin("goblin");
//		skeleton.setFlipX(true);		//水平翻转
//		skeleton.setFlipY(true);		//垂直翻转
		
		boolean flipx=skeleton.getFlipX();
		boolean flipy=skeleton.getFlipY();

		//FIXME box2d body not correct
		skeleton.updateWorldTransform();
		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
		stateData.setMix("walk", "idle", 0.2f);stateData.setMix("idle", "walk", 0.2f);
		stateData.setMix("idle", "atk", 0.2f);stateData.setMix("atk", "idle", 0.2f);
		stateData.setMix("walk", "atk", 0.2f);stateData.setMix("atk", "walk", 0.2f);
		stateData.setMix("idle", "jump", 0.2f);stateData.setMix("jump", "idle", 0.2f);
		stateData.setMix("fall", "jump", 0.2f);stateData.setMix("jump", "fall", 0.2f);
		stateData.setMix("idle", "fall", 0.2f);stateData.setMix("fall", "idle", 0.2f);
		
//		stateData.setMix("jump", "run", 0.2f);
		AnimationState state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
//		state.setTimeScale(0.5f); // Slow all animations down to 50% speed.
		// Queue animations on track 0.
		state.setAnimation(0, "idle", true);
//		state.addAnimation(1, "walk", false, 5);//idle完后,5秒后walk

		skeletonComponent.skeletonJson=skeletonJson;
		skeletonComponent.skeletonData=skeletonData;
		skeletonComponent.skeleton=skeleton;
		skeletonComponent.animationStateData=stateData;
		skeletonComponent.animationState=state;
		
		//create box2d part 
		
		for(int s=0;s<skeleton.getSlots().size;s++){
			Slot slot=skeleton.getSlots().get(s);
			Attachment attachment = slot.getAttachment();
			if (! (attachment instanceof Box2dBoundingBoxAttachment)) continue;
			Box2dBoundingBoxAttachment bbba=(Box2dBoundingBoxAttachment)attachment;
//			float[] worldVertices=new float[bba.getVertices().length];
//			bba.computeWorldVertices(slot.getBone(), worldVertices);
			//3 <= count && count <= 8
			//vertices wouldn't more than 8 in polygonShape
			PolygonShape shape=new PolygonShape(); 
//			ChainShape shape=new ChainShape();
//			shape.set(worldVertices);
			float[] worldVertices=new float[bbba.getVertices().length];
			float[] newVertices=new float[worldVertices.length];
			float[] localVertices = bbba.getVertices(); //BoundingBoxAttachment
			for (int i = 0; i < bbba.getVertices().length; i++) {
	            for (int v = 0 ; v < localVertices.length; v += 2) {
	                float px = localVertices[v];
	                float py = localVertices[v + 1];
	                newVertices[v]=flipx?-px:px;
	                newVertices[v+1]=flipy?-py:py;
	             }
			}
			shape.set(newVertices);
//			shape.set(bbba.getVertices());
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
			float x = skeleton.getX() + slot.getBone().getWorldX();
			float y = skeleton.getY() + slot.getBone().getWorldY();
			float rotation = slot.getBone().getWorldRotation();
			rotation=flipx?-rotation:rotation;
			rotation=flipy?-rotation:rotation;
//			System.out.println("body "+x+" "+y);
			actorComponentBody.setTransform(x,y,rotation*MathUtils.degRad);
			bbba.setBody(actorComponentBody);//setup body to attachment
		}//for each slots

		skeleton.updateWorldTransform();	
//		Box2dUtil.flipXBody(skeleton);		//body水平翻转

		//spine 2 box2d 按spine skeleton所有boundingbox的位置，角度给与box2d的body
//		Box2dUtil.spineToBox2d(skeleton.getSlots());  //position box2d bodies
	}

	@Override
	public void entityRemoved(Entity entity) {
	}
//	private void doState(){
//		
//	}
	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
		CharacterStateComponent stateComponent=stateComponentMapper.get(entity);
		JoystickComponent joystickComponent=joystickComponentMapper.get(entity);

		Skeleton skeleton=skeletonComponent.skeleton;
		AnimationState animationState= skeletonComponent.animationState;
		Array<TrackEntry> tracks=animationState.getTracks();
		TrackEntry te=animationState.getCurrent(0);

		Vector2 velocity=stateComponent.velocity;
		State lastState=stateComponent.state;
		
		stateComponent.stateMachine.update();
		
		if (GameData.instance.comboTime>0) {
			GameData.instance.comboTime-=deltaTime;
		}else {
			
		}
		
		if (joystickComponent.OKey 
				&& stateComponent.OpressedDuration < GameData.instance.atkThreshold) {
			stateComponent.OpressedDuration+=deltaTime;
		}else if (joystickComponent.OKey 
				&& stateComponent.OpressedDuration > GameData.instance.atkThreshold) {
//			stateComponent.OpressedDuration=0;  //符合防守状态
//			stateComponent.OpressedTimes=0;//
		}else if (!joystickComponent.OKey 
				&& stateComponent.OpressedDuration > GameData.instance.atkThreshold) {
			stateComponent.OpressedDuration=0;  //解除防守状态
			stateComponent.combo=0;//
		}else if (!joystickComponent.OKey&&stateComponent.OpressedDuration>0
					&&stateComponent.OpressedDuration < GameData.instance.atkThreshold
					&& stateComponent.combo==0) {
			stateComponent.combo=1;//首次攻击
			stateComponent.OpressedDuration=0;
			GameData.instance.comboTime=GameData.atkComboInterval;
		}
//		else if(!joystickComponent.OKey&&stateComponent.OpressedDuration>0
//					&&stateComponent.OpressedDuration <= GameData.instance.atkComboInterval
//					&& stateComponent.OpressedTimes!=0){
//			stateComponent.OpressedTimes++;//进行连击
//			stateComponent.OpressedDuration=0;//重计有效时间
//		}
		
//		System.out.println(stateComponent.jumpTime);
//		System.out.println(stateComponent.state);
		animationState.update(deltaTime);
		animationState.apply(skeleton);
		float x=skeleton.getX()+velocity.x*deltaTime;
		float y=skeleton.getY()+velocity.y*deltaTime;
		
		if(GameData.instance.land!=null){
			Float landY=Box2dUtil.getLandY(GameData.instance.land, x);
//			System.out.println(y+" "+landY+" "+velocity.y);
			if (landY==null) {
				x=skeleton.getX();
//				System.out.println(1);
//				this.getEngine().removeEntity(entity);
			}else if (y<landY) {
				y=landY;
//				velocity.y=0;
//				System.out.println(2);
			}else if(y>landY){
				velocity.y+=GameData.gravity.y*deltaTime;
//				System.out.println(3);
			}
			skeleton.setPosition(x, y);
		}
		if (GameData.instance.currentCharacter==entity.getId()) {
			GameData.instance.phyStage.getCamera().position.x=skeleton.getX();
			GameData.instance.phyStage.getCamera().position.y=Math.min(skeleton.getY()+2f, 3);
		}
		GameData.instance.phyStage.getCamera().update();
//		TrackEntry te=animationState.getCurrent(0);
		skeletonComponent.skeleton.updateWorldTransform();
		
		//spine 2 box2d 按spine skeleton所有boundingbox的位置，角度给与box2d的body
		Box2dUtil.spineToBox2d(skeleton);
//		Box2dUtil.box2dToSpine(skeletonComponent.skeleton.getSlots());
		joystickComponent.reset();//重要
	}
	public static void main(String[] args) {
		int a=1;
		switch (a) {
		case 1:
			a=3;
			System.out.println(1);
		case 2:
			if (a==2) {
				System.out.println(2);
			}
		case 3:

			System.out.println(3);

		default:
			System.out.println("defalut");
			break;
		}
	}

}
