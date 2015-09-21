/**
 * @author billzhu
 */
package com.stary.ems.components;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

public     class ZComparator implements Comparator<Entity> {
	private ComponentMapper<SceneItemComponent> itemConponents 
	= ComponentMapper.getFor(SceneItemComponent.class);;
    @Override
    public int compare(Entity e1, Entity e2) {
        return (int)Math.signum(itemConponents.get(e2).z-itemConponents.get(e1).z );
    }
}