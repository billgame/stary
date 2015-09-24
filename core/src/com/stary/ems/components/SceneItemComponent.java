/**
 * @author billzhu
 */
package com.stary.ems.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class SceneItemComponent implements Component {

	public TextureMapObject textureMapObject;
	public TextureRegion region;
	public boolean isVisable=true;
	public String name;
	public float x;
	public float y;
	public float z;
	public float dx,dy;
	public float regionWidth;
	public float regionHeight;
	public float scaleX;
	public float scaleY;
	public float rotatioin;
	public float originX;
	public float originY;
	
}
