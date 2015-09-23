<<<<<<< HEAD
/**
 * @author billzhu
 */
package com.stary.ems.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.stary.data.GameData;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.utils.Box2dUtil;

/*no need process by ashley*/
public class SpineRenderSystem extends EntitySystem {
	PolygonSpriteBatch polygonSpriteBatch;
	
	SkeletonRenderer skeletonRenderer;
	
	SkeletonRendererDebug skeletonDebugRenderer;
	Box2DDebugRenderer box2dDebugRenderer;
	
	Family family;
	
	Stage phyStage;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<SkeletonBox2dComponent> skeletonBox2dComponents = ComponentMapper.getFor(SkeletonBox2dComponent.class);
    
	public SpineRenderSystem(Family family) {
		setProcessing(false);
		
		this.family=family;
		
		phyStage=GameData.instance.phyStage;
		
		polygonSpriteBatch = new PolygonSpriteBatch(); // Required to render meshes. SpriteBatch can't render meshes.
		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true);
		
		skeletonDebugRenderer = new SkeletonRendererDebug();
//		debugRenderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
		skeletonDebugRenderer.setBoundingBoxes(false);
		skeletonDebugRenderer.setRegionAttachments(false);
		skeletonDebugRenderer.setMeshTriangles(false);
		box2dDebugRenderer=new Box2DDebugRenderer();
	}
	public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
	}


    public void update(float deltaTime) {
//		box2dWorld.step(delta, 6, 2);
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            SkeletonBox2dComponent sbComponent = skeletonBox2dComponents.get(entity); 

//    		skeletons.first().updateWorldTransform();
    		
//    		phyBatch.setProjectionMatrix(physicalStage.getCamera().combined);
    		polygonSpriteBatch.setProjectionMatrix(phyStage.getCamera().combined);
    		polygonSpriteBatch.begin();
    		skeletonRenderer.draw(polygonSpriteBatch, sbComponent.skeleton);//渲染spiner
    		polygonSpriteBatch.end();
    		
    		skeletonDebugRenderer.getShapeRenderer().setProjectionMatrix(phyStage.getCamera().combined);
    		skeletonDebugRenderer.setScale(sbComponent.pxToPhy);
//    		skeletonDebugRenderer.draw(sbComponent.skeleton);			//渲染spiner debug
        }//end for each entity 
        
		box2dDebugRenderer.render(GameData.box2dWorld, phyStage.getCamera().combined);//渲染box2d debug
    }

	@Override
	public void removedFromEngine (Engine engine) {
		entities = null;
	}


	/**
	 * @return set of entities processed by the system
	 */
	public ImmutableArray<Entity> getEntities () {
		return entities;
	}

	/**
	 * @return the Family used when the system was created
	 */
	public Family getFamily () {
		return family;
	}
}
=======
/**
 * @author billzhu
 */
package com.stary.ems.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.stary.data.GameData;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.utils.Box2dUtil;

/*no need process by ashley*/
public class SpineRenderSystem extends EntitySystem {
	PolygonSpriteBatch polygonSpriteBatch;
	
	SkeletonRenderer skeletonRenderer;
	
	SkeletonRendererDebug debugRenderer;
	Box2DDebugRenderer box2dDebugRenderer;
	
	Family family;
	
	Stage phyStage;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<SkeletonBox2dComponent> skeletonBox2dComponents = ComponentMapper.getFor(SkeletonBox2dComponent.class);
    
	public SpineRenderSystem(Family family) {
		setProcessing(false);
		
		this.family=family;
		
		phyStage=GameData.instance.phyStage;
		
		polygonSpriteBatch = new PolygonSpriteBatch(); // Required to render meshes. SpriteBatch can't render meshes.
		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true);
		
		debugRenderer = new SkeletonRendererDebug();
//		debugRenderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
		debugRenderer.setBoundingBoxes(false);
		debugRenderer.setRegionAttachments(false);
		debugRenderer.setMeshTriangles(false);
		box2dDebugRenderer=new Box2DDebugRenderer();
	}
	public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
	}


    public void update(float deltaTime) {
//		box2dWorld.step(delta, 6, 2);
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            SkeletonBox2dComponent sbComponent = skeletonBox2dComponents.get(entity); 

//    		skeletons.first().updateWorldTransform();
    		
//    		phyBatch.setProjectionMatrix(physicalStage.getCamera().combined);
    		polygonSpriteBatch.setProjectionMatrix(phyStage.getCamera().combined);
    		polygonSpriteBatch.begin();
    		skeletonRenderer.draw(polygonSpriteBatch, sbComponent.skeleton);//渲染spiner
    		polygonSpriteBatch.end();
    		
    		debugRenderer.getShapeRenderer().setProjectionMatrix(phyStage.getCamera().combined);
    		debugRenderer.setScale(sbComponent.pxToPhy);
    		debugRenderer.draw(sbComponent.skeleton);			//渲染spiner debug
        }//end for each entity 
        
		box2dDebugRenderer.render(GameData.box2dWorld, phyStage.getCamera().combined);//渲染box2d debug
    }

	@Override
	public void removedFromEngine (Engine engine) {
		entities = null;
	}


	/**
	 * @return set of entities processed by the system
	 */
	public ImmutableArray<Entity> getEntities () {
		return entities;
	}

	/**
	 * @return the Family used when the system was created
	 */
	public Family getFamily () {
		return family;
	}
}
>>>>>>> parent of b47b57b... Revert "core android"
