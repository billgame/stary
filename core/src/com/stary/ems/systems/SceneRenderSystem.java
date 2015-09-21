/**
 * @author billzhu
 */
package com.stary.ems.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.stary.data.GameData;
import com.stary.ems.components.SceneItemComponent;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.ems.components.ZComponent;

public class SceneRenderSystem  extends SortedIteratingSystem{

	PolygonSpriteBatch polygonSpriteBatch;
	Batch batch;
	
	SkeletonRenderer skeletonRenderer;//need to render scene animation
	
	SkeletonRendererDebug skeletonDebugRenderer;
//	Box2DDebugRenderer box2dDebugRenderer;
	
	Family family;
	
	Stage phyStage;
	Stage pxStage;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<ZComponent> zConponents ;
    private ComponentMapper<SceneItemComponent> itemConponents ;

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ZComponent zComponent=zConponents.get(entity);
		if (!zComponent.isSpine) {
			SceneItemComponent item=itemConponents.get(entity);
			if(!item.isVisable) return;
			batch.draw(item.region,item.x+item.dx, item.y+item.dy,
					item.originX, item.originY, 
					item.regionWidth,item.regionHeight,
					item.scaleX,item.scaleY,
					-item.rotatioin);
		}
	}
	@Override
	public void update (float deltaTime) {
		batch.setProjectionMatrix(phyStage.getCamera().combined);
//		this.batch=this.polygonSpriteBatch;
		this.batch.begin();
		super.update(deltaTime);
		this.batch.end();
	}
	public SceneRenderSystem(Family family, Comparator<Entity> comparator) {
		super(family, comparator);
		this.phyStage=GameData.instance.phyStage;
		this.pxStage=GameData.instance.pxStage;
		this.batch=phyStage.getBatch();
		this.polygonSpriteBatch=GameData.instance.polygonSpriteBatch;
		zConponents= ComponentMapper.getFor(ZComponent.class);
		itemConponents= ComponentMapper.getFor(SceneItemComponent.class);
		setProcessing(false);
	}
}
