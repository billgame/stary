package com.stary.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.stary.data.GameData;

public class Box2dUtil {
	public static Shape getShape(MapObject mapObject,float pxToPhy){
		Shape shape = null;
		if (mapObject instanceof TextureMapObject) {
			
		}else if(mapObject instanceof RectangleMapObject){
			shape=getRectangle((RectangleMapObject)mapObject,pxToPhy);
		}else if(mapObject instanceof CircleMapObject){
			shape=getCircle((CircleMapObject)mapObject,pxToPhy);
		}else if(mapObject instanceof PolygonMapObject){
			shape=getPolygon((PolygonMapObject)mapObject,pxToPhy);
		}else if(mapObject instanceof PolylineMapObject){
			shape=getPolyline((PolylineMapObject)mapObject,pxToPhy);
		}
		return shape;
	}
	public static float[] getVertices(MapObject mapObject,float pxToPhy){
		float[] vs = null;
		if (mapObject instanceof TextureMapObject) {
			
		}else if(mapObject instanceof RectangleMapObject){
		}else if(mapObject instanceof CircleMapObject){
		}else if(mapObject instanceof PolygonMapObject){
		}else if(mapObject instanceof PolylineMapObject){
			vs=getPolylineVertices((PolylineMapObject)mapObject,pxToPhy);
		}
		return vs;
	}
	public static Shape getRectangle(RectangleMapObject rectangleMapObject,float pxToPhy){
		Vector2 center=new Vector2();
		Rectangle rect=rectangleMapObject.getRectangle();
		rect.getCenter(center);
//		center.set(rect.getX(), rect.getY());
		center.scl(pxToPhy);
		
		float r=rectangleMapObject.getProperties().get("rotation", 0f, Float.class);
		System.out.println("rotation "+r);
//		float cos=MathUtils.cosDeg(r);
//		float sin=MathUtils.cosDeg(r);
//		center.rotate(-r);
		PolygonShape polygonShape=new PolygonShape();
		//fixme TODO 搞不定
		r=0;
		polygonShape.setAsBox(rect.width*0.5f*pxToPhy, rect.height*0.5f*pxToPhy, center, -r*MathUtils.degreesToRadians);
		return polygonShape;
	}
	public static Shape getCircle(CircleMapObject circleMapObject,float pxToPhy){
		Circle c=circleMapObject.getCircle();
		Vector2 pos=new Vector2(c.x*pxToPhy, c.y*pxToPhy);
		CircleShape circleShape=new CircleShape();
		circleShape.setPosition(pos);
		circleShape.setRadius(c.radius*pxToPhy);
		return circleShape;
	}
	public static Shape getPolygon(PolygonMapObject polygonMapObject,float pxToPhy){
		float[] vertices=polygonMapObject
				.getPolygon().getTransformedVertices();
		float[] meterVertices=new float[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			meterVertices[i]=vertices[i]*pxToPhy;
		}
		PolygonShape polygonShape=new PolygonShape();
		polygonShape.set(meterVertices);
		return polygonShape;
	}
	public static float[] getPolylineVertices(PolylineMapObject polylineMapObject,float pxToPhy){
		float[] vertices=polylineMapObject
				.getPolyline().getTransformedVertices();
//		System.out.println(polylineMapObject.getPolyline().getRotation());
		float[] meterVertices=new float[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			meterVertices[i]=vertices[i]*pxToPhy;
		}
		return meterVertices;
	}
	public static Shape getPolyline(PolylineMapObject polylineMapObject,float pxToPhy){
		float[] vertices=polylineMapObject
				.getPolyline().getTransformedVertices();
//		System.out.println(polylineMapObject.getPolyline().getRotation());
		float[] meterVertices=new float[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			meterVertices[i]=vertices[i]*pxToPhy;
		}
		ChainShape chainShape=new ChainShape();
		chainShape.createChain(meterVertices);
		return chainShape;
	}
	public static void box2dToSpine(Array<Slot> slots){
		for (Slot slot : slots) {
			if (!(slot.getAttachment() instanceof Box2dBoundingBoxAttachment)) continue;
			Box2dBoundingBoxAttachment attachment = (Box2dBoundingBoxAttachment)slot.getAttachment();
			if (attachment.body == null) continue;
			Body body=attachment.body;
			Skeleton skeleton=slot.getSkeleton();
			Bone bone=slot.getBone();
			Vector2 pos=body.getPosition();//game world
			pos.sub(skeleton.getX(),skeleton.getY());//->skeleton world
			slot.getBone().getParent().worldToLocal(pos);//skl world -> local world
			bone.setPosition(pos.x, pos.y);
			float worldRotation=body.getAngle()*MathUtils.radDeg;//rad
//			worldRotation=worldRotation-skeleton.getRootBone().
			float localRotation=worldRotation-bone.getParent().getWorldRotation();
			bone.setRotation(localRotation);
			bone.updateWorldTransform();
		}
	}
	/**
	 * spine 2 box2d 按spine skeleton所有boundingbox的位置/角度给与box2d的body
	 * @param slots
	 */
	public static void spineToBox2d(Array<Slot> slots){
		for (Slot slot : slots) {
			if (!(slot.getAttachment() instanceof Box2dBoundingBoxAttachment)) continue;
			Box2dBoundingBoxAttachment attachment = (Box2dBoundingBoxAttachment)slot.getAttachment();
			if (attachment.body == null) continue;
			Skeleton skeleton=slot.getSkeleton();
			float x = skeleton.getX() + slot.getBone().getWorldX();
			float y = skeleton.getY() + slot.getBone().getWorldY();
			float rotation = slot.getBone().getWorldRotation();
//			System.out.println("body "+x+" "+y);
			attachment.body.setTransform(x,y,rotation*MathUtils.degRad);
		}
	}
	/**
	 * 
	 * @param line 地平线的顶点数组{x0,y0,x1,y1,x2,y2....} world coordinates
	 * @param x	角色所处的x坐标(world coordinates
	 * @return 地平线的Y坐标
	 */
	public static Float getLandY(float[] line,float x){
		for (int i = 0; i < line.length; i=i+2) {
			float lineX=line[i];
			if (i<line.length-2 && lineX<=x && x<line[i+2]) {
				float lineY=line[i+1];
				float line2X=line[i+2];
				float line2Y=line[i+3];
				float dx=line2X-lineX;
				float dy=line2Y-lineY;
				//dy/dx=?/x
				float newY=dy*(x-lineX)/dx;
				return newY+lineY;
			}
		}
		return null;
	}
	public static void updateBody(Skeleton skeleton){
		for (int i = 0; i < skeleton.getSlots().size; i++) {
			Slot slot=skeleton.getSlots().get(i);
			Bone bone=slot.getBone();
			Attachment attachment=slot.getAttachment();
			if (attachment instanceof Box2dBoundingBoxAttachment) {
				Box2dBoundingBoxAttachment bbba=(Box2dBoundingBoxAttachment)attachment;
				for (Fixture f:bbba.getBody().getFixtureList()) {
					float[] vertices=((Box2dBoundingBoxAttachment) attachment).getVertices();
					PolygonShape shape=(PolygonShape)f.getShape();
					for(int v=0;i<vertices.length;v++){
					       vertices[v] = vertices[v] - bone.getWorldX();
					       v++;
					       vertices[v] = vertices[v] - bone.getWorldY();
					} 
					shape.set(vertices);
				}
			}
		}
	}
}
