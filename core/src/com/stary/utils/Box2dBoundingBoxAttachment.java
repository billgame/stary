
package com.stary.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;

public class Box2dBoundingBoxAttachment extends BoundingBoxAttachment {
	Body body;
	public Box2dBoundingBoxAttachment (String name) {
		super(name);
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	
}