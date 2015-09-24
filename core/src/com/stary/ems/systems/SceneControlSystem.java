/**
 * @author billzhu
 */
package com.stary.ems.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.stary.data.GameData;
import com.stary.ems.components.SceneItemComponent;
import com.stary.ems.components.ZComponent;

public class SceneControlSystem extends IteratingSystem {
	private ComponentMapper<ZComponent> zm = ComponentMapper.getFor(ZComponent.class);
	private ComponentMapper<SceneItemComponent> im = ComponentMapper.getFor(SceneItemComponent.class);
	public float factorX=0.25f,factorY=0.25f;
	public SceneControlSystem(Family family) {
		super(family);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Entity c=this.getEngine().getEntity(GameData.instance.currentCharacter);
		ZComponent zc=zm.get(entity);
		SceneItemComponent ic=im.get(entity);
		float itemX=ic.x;
		float itemY=ic.y;
		float z=zc.z;//0~-1
		if (z==0) return;// needn't to prcoess perspective
//		System.out.println(ic.name+" "+itemY);
		long id=GameData.instance.currentCharacter;
		Camera camera=GameData.instance.phyStage.getCamera();
		float cameraX=camera.position.x;
		float dx=(cameraX-itemX)*z*factorX;
		ic.dx=dx;
		float cameraY=camera.position.y;
		float dy=(cameraY-itemY)*z*factorY;
		ic.dy=dy;
		
	}

}
