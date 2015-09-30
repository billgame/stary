/**
 * @author billzhu
 */
package com.stary.ems.components.states;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.Skeleton;
import com.stary.data.GameData;
import com.stary.ems.components.CharacterStateComponent;
import com.stary.ems.components.JoystickComponent;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.utils.Box2dUtil;

public enum CharacterState implements State<Entity> {
	JumpState(){

		@Override
		public CharacterState canGotoState(long keys,SkeletonBox2dComponent skeletonComponent
				,CharacterStateComponent state,JoystickComponent joystick) {
//			keys &= JoystickComponent.key_O; //看O键是否按下
			if (joystick.OKey) {//空中角色不论o是长按不放还是短按,都是进入攻击状态
				state.atkNum=2;//2号攻击的招式
				//TODO 根据角色空中位置与攻击角度，计算出velocity
				return AtkState;
			}
			Skeleton skeleton = skeletonComponent.skeleton;
			Float landY=Box2dUtil.getLandY(GameData.instance.land, skeleton.getX());
			Float charY=skeleton.getY();
			if(MathUtils.isEqual(charY, landY)&& state.velocity.y<0/*&&!joystick.justPressedJump*/){//着地
				if (joystick.rightKey||joystick.leftKey) {
				}
				state.velocity.y=0;
				return IdleState;
			}
			return null;
			
		}

		@Override
		public void enter(Entity entity) {
			CharacterStateComponent stateComponent= stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			skeletonComponent.animationState.setAnimation(0, "jump", false);
			stateComponent.jumpTime=1;
			stateComponent.velocity.y+=stateComponent.jumpVelocity;
		}

		@Override
		public void update(Entity entity) {
			CharacterStateComponent stateComponent=stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			JoystickComponent joystick=joystickComponentMapper.get(entity);
			CharacterState newState=canGotoState(0,skeletonComponent, stateComponent, joystick);
			if (newState!=null) {
				stateComponent.stateMachine.changeState(newState);
				return;//状态跳转
			}

			if (joystick.justPressedJump&& stateComponent.jumpTime==1) {
				stateComponent.velocity.y=0;
				stateComponent.velocity.y+=1.2f*stateComponent.jumpVelocity;
				stateComponent.jumpTime++;
				System.out.println("[jumpState.update]jump plus");
			}
			Skeleton skeleton = skeletonComponent.skeleton;
			Float landY=Box2dUtil.getLandY(GameData.instance.land, skeleton.getX());
			Float charY=skeleton.getY();
			System.out.println(stateComponent.velocity.y);
			stateComponent.velocity.y+=Gdx.graphics.getDeltaTime()*GameData.instance.gravity.y;
			if (stateComponent.velocity.y<0) {
				skeletonComponent.animationState.setAnimation(0, "fall", true);
			}
		}//end update

		@Override
		public void exit(Entity entity) {
			// TODO Auto-generated method stub
			super.exit(entity);
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			charStateComponent.jumpTime=0;
//			charStateComponent.velocity.y=0;
		}
		
	},
	WalkState(){
		
		@Override
		public CharacterState canGotoState(long keys,SkeletonBox2dComponent skeletonComponent
				,CharacterStateComponent state,JoystickComponent joystick) {
//			long key=keys&JoystickComponent.key_right;
//			mapper.walkCanGoToState.get(key);
			if (joystick.justPressedJump) {
//				m.changeState();
				return JumpState;
			}
			if ((!joystick.leftKey && !joystick.rightKey)
					||(joystick.leftKey&&joystick.rightKey)) {
				return IdleState;
			}
			if (joystick.OKey&&state.OpressedDuration>=GameData.instance.atkThreshold) {
				//防守状态
				return BlockState;
			}else
			if(state.combo==1){
					return AtkState;
				}
//			if (!joystick.OKey&& state.OpressedDuration!=0 
//					&&state.OpressedDuration<GameData.instance.atkThreshold) {
//				//->攻击状态
//				state.OpressedDuration=0;
//				state.OpressedTimes++;
//				return AtkState;
//			}
			
			return null;
		}

		@Override
		public void enter(Entity entity) {
			CharacterStateComponent stateComponent= stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			skeletonComponent.animationState.setAnimation(0, "walk", true);
			System.out.println("enter walk");
		}

		@Override
		public void update(Entity entity) {
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			JoystickComponent joystick=joystickComponentMapper.get(entity);
			
			CharacterState newState=canGotoState(0,skeletonComponent, charStateComponent, joystick);
			if (newState!=null) {
				charStateComponent.stateMachine.changeState(newState);
				return;//状态跳转
			}
			
			Skeleton skeleton=skeletonComponent.skeleton;
			Vector2 velocity=charStateComponent.velocity;
			if (joystick.rightKey) {
				if (skeleton.getFlipX()) {
					skeleton.setFlipX(false);
					Box2dUtil.updateBody(skeleton);
				}
				velocity.x=1.4f;//TODO 加入加速度x
			}else if(joystick.leftKey){
				if (!skeleton.getFlipX()) {
					skeleton.setFlipX(true);
					Box2dUtil.updateBody(skeleton);
				}
				velocity.x=-1.4f;
			}
		}
		
	},
	AtkState(){

		@Override
		public CharacterState canGotoState(long keys,SkeletonBox2dComponent skeletonComponent
				,CharacterStateComponent stateComponent,JoystickComponent joystick) {
			//TODO 加入攻击被打断处理 			
//			if (skeletonComponent.animationState.getCurrent(0).isComplete()) {
//				return IdleState;
//			}
			Skeleton skeleton = skeletonComponent.skeleton;
			Float landY=Box2dUtil.getLandY(GameData.instance.land, skeleton.getX());
			Float charY=skeleton.getY();
			System.out.println("tracks:"+skeletonComponent.animationState.getTracks().size+" 连击:"+
					stateComponent.combo);
			System.out.println(skeletonComponent.animationState.getCurrent(0));
			System.out.println(skeletonComponent.animationState.getCurrent(0).getNext());
			if (skeletonComponent.animationState.getCurrent(0).isComplete()) {
				stateComponent.combo=0;
			}
			if(MathUtils.isEqual(charY, landY)&&//着地
					skeletonComponent.animationState.getCurrent(0).isComplete()){
				stateComponent.velocity.y=0;
				return IdleState;
			}
			return null;
		}

		@Override
		public void enter(Entity entity) {
			CharacterStateComponent stateComponent= stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			skeletonComponent.animationState.setAnimation(0, "atk1", true);
//			skeletonComponent.animationState.addAnimation(0, "atk2", true, 0);
			if (stateComponent.atkNum==2) {
				skeletonComponent.animationState.setAnimation(0, "atk2", false);
//				te.setTime(10);
//				te.setEndTime(40);
			}
			stateComponent.velocity.x*=0.5f;
			System.out.println("enter atk");
		}

		@Override
		public void update(Entity entity) {
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			JoystickComponent joystick=joystickComponentMapper.get(entity);
			CharacterState newState=canGotoState(0,skeletonComponent, charStateComponent, joystick);
			if (newState!=null) {
				charStateComponent.stateMachine.changeState(newState);
				return;//状态跳转
			}
			Skeleton skeleton = skeletonComponent.skeleton;
			Float landY=Box2dUtil.getLandY(GameData.instance.land, skeleton.getX());
			Float charY=skeleton.getY();
			if(!MathUtils.isEqual(charY, landY)&&//未着地,攻击动作完成了,下降姿势
					skeletonComponent.animationState.getCurrent(0).isComplete()){
				skeletonComponent.animationState.setAnimation(0, "fall", false);
			}
			if(!joystick.OKey&&charStateComponent.OpressedDuration>0
					&&charStateComponent.OpressedDuration <= GameData.instance.atkThreshold
					&& charStateComponent.combo>=1 && GameData.instance.comboTime>0){
				charStateComponent.combo++;//进行连击
				charStateComponent.OpressedDuration=0;//重计有效时间
				GameData.instance.comboTime=GameData.atkComboInterval;
				if (charStateComponent.combo>=2
						&& !skeletonComponent.animationState.getCurrent(0).isComplete()) {
					skeletonComponent.animationState.addAnimation(1, "atk2", false,1);
					System.out.println("next track "+skeletonComponent.animationState.getCurrent(0).getNext());
					System.out.println(charStateComponent.combo+" 连击");
				}
			}else if (!joystick.OKey&&charStateComponent.OpressedDuration>0
					&&charStateComponent.OpressedDuration > GameData.instance.atkThreshold
					&& charStateComponent.combo >=1 && GameData.instance.comboTime<=0) {
				charStateComponent.combo=1;//连击失 效, 重置计数器
				charStateComponent.OpressedDuration=0;//重计有效时间
				GameData.instance.comboTime=GameData.atkComboInterval;
			}
		}//end update
		
	},
	/*防守状态*/
	BlockState(){

		@Override
		public void enter(Entity entity) {
			CharacterStateComponent stateComponent= stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			TrackEntry te=skeletonComponent.animationState.setAnimation(0, "block", false);
			
			stateComponent.combo=0;//清空连击计数
		}

		@Override
		public void update(Entity entity) {
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			JoystickComponent joystick=joystickComponentMapper.get(entity);
			CharacterState newState=canGotoState(0,skeletonComponent, charStateComponent, joystick);
			if (newState!=null) {
				charStateComponent.stateMachine.changeState(newState);
				return;//状态跳转
			}
		}

		@Override
		public CharacterState canGotoState(long keys, SkeletonBox2dComponent skeletonComponent,
				CharacterStateComponent state, JoystickComponent joystick) {
			if (!joystick.OKey) {//放开O键,解除防守
				return IdleState;
			}
			return null;
		}

		@Override
		public void exit(Entity entity) {

			CharacterStateComponent stateComponent=stateComponentMapper.get(entity);
			stateComponent.OpressedDuration=0;//清空按O时长累计
		}
		
		
	},
	/*下蹲状态*/
	SquatState(){

		@Override
		public void enter(Entity entity) {
//			CharacterStateComponent stateComponent= stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			TrackEntry te=skeletonComponent.animationState.setAnimation(0, "down", false);
		}

		@Override
		public void update(Entity entity) {
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			JoystickComponent joystick=joystickComponentMapper.get(entity);
			
			CharacterState newState=canGotoState(0,skeletonComponent, charStateComponent, joystick);
			if (newState!=null) {
				charStateComponent.stateMachine.changeState(newState);
				return;//状态跳转
			}
			Skeleton skeleton = skeletonComponent.skeleton;
			if (joystick.rightKey) {
				if (skeleton.getFlipX()) {
					skeleton.setFlipX(false);
					Box2dUtil.updateBody(skeleton);
				}
			}else if(joystick.leftKey){
				if (!skeleton.getFlipX()) {
					skeleton.setFlipX(true);
					Box2dUtil.updateBody(skeleton);
				}
			}
		}

		@Override
		public CharacterState canGotoState(long keys, SkeletonBox2dComponent skeletonComponent,
				CharacterStateComponent state, JoystickComponent joystick) {
			if (joystick.OKey) {
				return AtkState;//冲刺击
			}else
			if (joystick.jumpKey) {//
				return JumpState;
			}else
			if (joystick.upKey||!joystick.downKey) {
				return IdleState;
			}else if ((joystick.rightKey|joystick.leftKey) 
					&& !(joystick.rightKey&&joystick.leftKey) ) {
				return WalkState;
			}
			return null;
		}
		
	},
	UpState(){


		@Override
		public void enter(Entity entity) {
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			TrackEntry te=skeletonComponent.animationState.setAnimation(0, "up", false);
		}

		@Override
		public void update(Entity entity) {
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			JoystickComponent joystick=joystickComponentMapper.get(entity);
			
			CharacterState newState=canGotoState(0,skeletonComponent, charStateComponent, joystick);
			if (newState!=null) {
				charStateComponent.stateMachine.changeState(newState);
				return;//状态跳转
			}
		}

		@Override
		public CharacterState canGotoState(long keys, SkeletonBox2dComponent skeletonComponent,
				CharacterStateComponent state, JoystickComponent joystick) {
			if (joystick.OKey) {
				return AtkState;//上空击
			}else 
			if (joystick.jumpKey) {
				return JumpState;
			}else
			if (joystick.downKey||!joystick.upKey) {
				return IdleState;
			}else if ((joystick.rightKey|joystick.leftKey) 
					&& !(joystick.rightKey&&joystick.leftKey) ) {
				return WalkState;
			}
			return null;
		}
		
	},
	HitState(){

		@Override
		public void enter(Entity entity) {
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			TrackEntry te=skeletonComponent.animationState.setAnimation(0, "hit", false);
		}

		@Override
		public void update(Entity entity) {
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			JoystickComponent joystick=joystickComponentMapper.get(entity);
			
			CharacterState newState=canGotoState(0,skeletonComponent, charStateComponent, joystick);
			if (newState!=null) {
				charStateComponent.stateMachine.changeState(newState);
				return;//状态跳转
			}
		}

		@Override
		public CharacterState canGotoState(long keys, SkeletonBox2dComponent skeletonComponent,
				CharacterStateComponent state, JoystickComponent joystick) {

			return null;
		}

		
	},
	IdleState(){

		@Override
		public void enter(Entity entity) {

			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			TrackEntry te=skeletonComponent.animationState.setAnimation(0, "idle", false);
			charStateComponent.velocity.x=0;

			System.out.println("enter idle");
		}

		@Override
		public void update(Entity entity) {
			CharacterStateComponent charStateComponent=stateComponentMapper.get(entity);
			SkeletonBox2dComponent skeletonComponent=skeletonBox2dComponentMapper.get(entity);
			JoystickComponent joystick=joystickComponentMapper.get(entity);
//			System.out.println("idle ");
			CharacterState newState=canGotoState(0,skeletonComponent, charStateComponent, joystick);
			if (newState!=null) {
				charStateComponent.stateMachine.changeState(newState);
				return;//状态跳转
			}
		}

		@Override
		public CharacterState canGotoState(long keys, SkeletonBox2dComponent skeletonComponent,
				CharacterStateComponent state, JoystickComponent joystick) {
			if (joystick.downKey) {
				return CharacterState.SquatState;
			}else if(joystick.upKey){
				return UpState;
			}else if ((joystick.rightKey|joystick.leftKey) 
					&& !(joystick.rightKey&&joystick.leftKey) ) {
				return WalkState;
			}else if (joystick.jumpKey) {
				return JumpState;
			}else if (joystick.downKey) {
				return SquatState;
			}else if (state.combo>0) {
				return AtkState;
			}else if (state.OpressedDuration>GameData.instance.atkThreshold) {
				return BlockState;
			}
			return null;
		}
		
	}
	;

	private CharacterState() {
	}
	Mapper mapper=new Mapper();
	long condiction=0;
//	ArrayMap<Long,CharacterState> jumpCanGoToState=new ArrayMap<Long,CharacterState>();
//	ArrayMap<Long,CharacterState> walkCanGoToState=new ArrayMap<Long,CharacterState>();
	public ComponentMapper<SkeletonBox2dComponent> skeletonBox2dComponentMapper=ComponentMapper.getFor(SkeletonBox2dComponent.class);
	public ComponentMapper<CharacterStateComponent> stateComponentMapper=ComponentMapper.getFor(CharacterStateComponent.class);
	public ComponentMapper<JoystickComponent> joystickComponentMapper=ComponentMapper.getFor(JoystickComponent.class);
	public SkeletonBox2dComponent getSkeletonBox2dComponent(Entity entity){
		return skeletonBox2dComponentMapper.get(entity);
	}
	@Override
	public abstract void enter(Entity entity) ;

	@Override
	public abstract void update(Entity entity);

	@Override
	public void exit(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}
	public abstract CharacterState canGotoState(long keys,SkeletonBox2dComponent skeletonComponent,CharacterStateComponent state,JoystickComponent joystick);
	class Mapper{
		ArrayMap<Long,CharacterState> jumpCanGoToState=new ArrayMap<Long,CharacterState>();
		ArrayMap<Long,CharacterState> walkCanGoToState=new ArrayMap<Long,CharacterState>();
		ArrayMap<Long,CharacterState> idleCanGoToState=new ArrayMap<Long,CharacterState>();
		ArrayMap<Long,CharacterState> atkCanGoToState=new ArrayMap<Long,CharacterState>();
		ArrayMap<Long,CharacterState> blockCanGoToState=new ArrayMap<Long,CharacterState>();
		ArrayMap<Long,CharacterState> downCanGoToState=new ArrayMap<Long,CharacterState>();
		Mapper(){
			/*跳*/
			long condiction=JoystickComponent.key_O;
			jumpCanGoToState.put(condiction, AtkState);//跳攻  跳攻（攻键不放）落地后变防守
//			condiction=JoystickComponent.key_jump|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
//			condiction=JoystickComponent.key_jump|JoystickComponent.key_right|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
//			condiction=JoystickComponent.key_jump|JoystickComponent.key_left|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
//			condiction=JoystickComponent.key_jump|JoystickComponent.key_up|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
//			condiction=JoystickComponent.key_jump|JoystickComponent.key_down|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
//			condiction=JoystickComponent.key_right|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
//			condiction=JoystickComponent.key_left|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
//			condiction=JoystickComponent.key_up|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
//			condiction=JoystickComponent.key_down|JoystickComponent.key_O;
//			jumpCanGoToState.put(condiction, AtkState);
			/*Idle*/ //监听左，右，跳，下，上
			condiction=JoystickComponent.key_left;
			idleCanGoToState.put(condiction,WalkState);//idle to walk
			condiction=JoystickComponent.key_right;
			idleCanGoToState.put(condiction,WalkState);//idle to walk
			condiction=JoystickComponent.key_jump;
			idleCanGoToState.put(condiction,JumpState);//idle to jump
			condiction=JoystickComponent.key_down;
			idleCanGoToState.put(condiction,SquatState);//idle to down
			condiction=JoystickComponent.key_up;
			idleCanGoToState.put(condiction,UpState);//idle to down
//			condiction=JoystickComponent.key_O;
//			idleCanGoToState.put(condiction,AtkState);//攻/守  运行中处理		
			/*Walk*/
			condiction=0;
			walkCanGoToState.put(condiction, IdleState);
			condiction=JoystickComponent.key_left|JoystickComponent.key_right;
			walkCanGoToState.put(condiction, IdleState);//
			condiction=JoystickComponent.key_jump;// just pressed
			walkCanGoToState.put(condiction, JumpState);
//			condiction=JoystickComponent.key_O;
//			walkCanGoToState.put(condiction, JumpState); //攻/守  运行中处理  防守只有在地面时才能进入			

			/*Block*/
//			condiction=JoystickComponent.key_left;
//			atkCanGoToState.put(condiction,IdleState);
			condiction=0;//松开O键->idle
			blockCanGoToState.put(condiction, IdleState);//防守中只能缓慢移动，防守状态中检查移动键
			
			/*Down*/
			condiction=0;//松开down键->idle  监听上，攻，跳      
			downCanGoToState.put(condiction, IdleState);
			condiction=JoystickComponent.key_up;//按了up键->idle
			downCanGoToState.put(condiction, IdleState);
			condiction=JoystickComponent.key_jump;//按了jump键->jump
			downCanGoToState.put(condiction, JumpState);//
			condiction=JoystickComponent.key_O;//按了atk键->atk
			downCanGoToState.put(condiction, AtkState);//下攻击
			
			
		}
	}
}
