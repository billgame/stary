/**
 * @author billzhu
 */
package com.stary.ems.components;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.stary.data.GameData;

public class SkeletonBox2dComponent implements Component{
//	public Action action=Action.idle;
//	public Action lastAction=Action.idle;
//	public boolean right=true;
//	public float jumpForce;
	public String name;
	public float pxToPhy;
	public float x,y;
	public World box2dWorld;
	public Skeleton skeleton;
	public SkeletonJson skeletonJson;
	public SkeletonData skeletonData;
	public AnimationState animationState;
	public AnimationStateData animationStateData;
	public SkeletonBox2dComponent(String name,float x,float y){
		this.box2dWorld=GameData.box2dWorld;
		this.name=name;
		this.pxToPhy=GameData.pxToPhy;
		this.x=x;
		this.y=y;
	}
}
