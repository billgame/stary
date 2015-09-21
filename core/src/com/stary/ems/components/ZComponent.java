/**
 * @author billzhu
 */
package com.stary.ems.components;

import com.badlogic.ashley.core.Component;

public class ZComponent implements Component{
//	ObjectType type;
	public float z;
	public boolean isSpine;
	public ZComponent(float z, boolean isSpine) {
		super();
		this.z = z;
		this.isSpine = isSpine;
	}
	
//	enum ObjectType{
//		scene,spine;
//	}
}
