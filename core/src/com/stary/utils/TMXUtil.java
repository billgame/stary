package com.stary.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;

public class TMXUtil {
	public static Vector2 getPosition(MapObject obj,float pxToPhy){
		Vector2 position=new Vector2();
		if (obj instanceof TextureMapObject) {
			TextureMapObject objtmp=(TextureMapObject)obj;
			position.set(objtmp.getX(), objtmp.getY());
			position.scl(pxToPhy, pxToPhy);
		}else if (obj instanceof RectangleMapObject) {
			RectangleMapObject objtmp=(RectangleMapObject)obj;
			objtmp.getRectangle().getCenter(position);
			position.scl(pxToPhy, pxToPhy);
		}else if (obj instanceof CircleMapObject) {
			CircleMapObject objtmp=(CircleMapObject)obj; 
			position.set(objtmp.getCircle().x, objtmp.getCircle().y);
			position.scl(pxToPhy, pxToPhy);
		}else if (obj instanceof EllipseMapObject) {
			EllipseMapObject objtmp=(EllipseMapObject)obj; 
			position.set(objtmp.getEllipse().x, objtmp.getEllipse().y);
			position.scl(pxToPhy, pxToPhy);
		}else if (obj instanceof PolygonMapObject) {
			PolygonMapObject objtmp=(PolygonMapObject)obj;
			position.set(objtmp.getPolygon().getX(),objtmp.getPolygon().getY());
			position.scl(pxToPhy, pxToPhy); 
		}else if (obj instanceof PolylineMapObject) {
			PolylineMapObject objtmp=(PolylineMapObject)obj;
			position.set(objtmp.getPolyline().getX(),objtmp.getPolyline().getY());
			position.scl(pxToPhy, pxToPhy); 
		}
		return position;
	}
}
