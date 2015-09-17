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

public class SkeletonBox2dComponent implements Component{
	public String name;
	public float pxToPhy;
	public float x,y;
	public World box2dWorld;
	public Skeleton skeleton;
	public SkeletonJson skeletonJson;
	public SkeletonData skeletonData;
	public AnimationState animationState;
	public AnimationStateData animationStateData;
	public SkeletonBox2dComponent(World box2dWorld,String name,float x,float y,float scale){
		this.box2dWorld=box2dWorld;
		this.name=name;
		this.pxToPhy=scale;
		this.x=x;
		this.y=y;
	}
}
