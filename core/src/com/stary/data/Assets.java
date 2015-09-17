package com.stary.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader.TextureAtlasParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin; 
//import com.bill.mygdxgame.slg.data.CharacterData;
//import com.bill.mygdxgame.slg.data.GameData;
//import com.bill.mygdxgame.slg.data.UnitData;

public class Assets {
	public static final String txt="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890" +
			"\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*《》，、。（）【】：" +
			"“”！？·￥…—∞";
	public static final String FACES="data/faces/faces.atlas";
	public static final String UI1="data/ui/textureAtlas1.pack";
	public static final String UIs="data/ui/uiskin.atlas";
	public static final String SKIN="data/ui/uiskin.json";
	public static final String ANIMs="data/animations/";
	public static final String CHARs="data/characters/";
	public static final String _ATLAS=".atlas";
	public static final String _PNG=".png";
	public static final Assets instance=new Assets();
	public boolean isInited=false;
	public FreeTypeFontGenerator fontGenerator;
	public BitmapFont fontNormal=null;//打击次数
	public BitmapFont font2=null;
	public BitmapFont font2_flip=null;
	public BitmapFont fontNormal_flip=null;
	public BitmapFont fontNormal_small_flip=null;
//	public Skin defaultSkin;
	public Skin custom1Skin;
	public TextureAtlas facesAtlas;
	public TextureAtlas charactersTextureAtlas;
	public TextureAtlas ui1;
	public AssetManager assetManager=new AssetManager();
//	public Map<String, TextureAtlas> animsTextureAtlas = new HashMap<String, TextureAtlas>();
	private Assets(){
	}
	public void init(){
		load();
		isInited=true;
	}
	public void load(){
		if (isInited) {
			return;
		}
		isInited=true;
//		System.out.println("[Assets.load()]各资源加载");
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
//		manager.setLoader(TextureAtlas.class,".atlas", new TextureAtlasLoader(resolver));
//		System.out.println("[Assets]Assets load");

		FreeTypeFontLoaderParameter size1Params = new FreeTypeFontLoaderParameter();
		size1Params.fontFileName = "font/fangzhengcat.ttf";
		size1Params.fontParameters.size = 32;
		size1Params.fontParameters.characters =txt;
		
		assetManager.load("size32.ttf", BitmapFont.class, size1Params);
		
		FreeTypeFontLoaderParameter size2Params = new FreeTypeFontLoaderParameter();
		size2Params.fontFileName = "font/fangzhengcat.ttf";
		size2Params.fontParameters.size = 32;
		size2Params.fontParameters.characters =txt;
		size2Params.fontParameters.flip = true; 
		assetManager.load("size32_flip.ttf", BitmapFont.class, size2Params);
		
		FreeTypeFontLoaderParameter size3Params = new FreeTypeFontLoaderParameter();
		size3Params.fontFileName = "font/fangzhengcat.ttf";
		size3Params.fontParameters.size = 12;
		size3Params.fontParameters.characters =txt;
		assetManager.load("size12.ttf", BitmapFont.class, size3Params);
		
		FreeTypeFontLoaderParameter size4Params = new FreeTypeFontLoaderParameter();
		size4Params.fontFileName = "font/fangzhengcat.ttf";
		size4Params.fontParameters.size = 12;
		size4Params.fontParameters.characters =txt;
		size4Params.fontParameters.flip = true; 
		assetManager.load("size12_flip.ttf", BitmapFont.class, size4Params); 

		
//		assetManager.load(FACES, TextureAtlas.class);
//		assetManager.load(UI1, TextureAtlas.class);
//		
//		SkinParameter skinParams=new SkinParameter(UIs);
//		assetManager.load(SKIN, Skin.class,skinParams);
		
		

//		//加载所有单位动画(行走,站立等动画)
//		 Collection<UnitData> unitDatas=GameData.instance.units_db.values();
//		 for (UnitData unitData : unitDatas) {
//			String fullname=unitData.fullname;
////			TextureAtlas atlas=animsTextureAtlas.get(fullname);
////			if (atlas == null) {
//				String path=ANIMs+fullname+_ATLAS;
//				FileHandle atlasFile = Gdx.files.internal(path);
////				System.out.println(path+" "+atlasFile.exists());
////				if (!atlasFile.exists()) {
//////					continue;
////					throw new RuntimeException("没有该动画:"+path);
////				}
//				assetManager.load(path, TextureAtlas.class);
////				atlas = new TextureAtlas(atlasFile);
////				animsTextureAtlas.put(fullname, atlas);
////			}
//		}
//		TextureParameter textureParameter = new TextureParameter();
//		textureParameter.magFilter = TextureFilter.Linear;
//		textureParameter.minFilter = TextureFilter.Linear;
//		Collection<CharacterData> chars = GameData.instance.characters_db
//				.values();
//		for (CharacterData aChar : chars) {
//			String fullname = aChar.fullname;
//			String path = CHARs + fullname + _PNG;
//			FileHandle bust = Gdx.files.internal(path);
//			if (!bust.exists()) {
//				System.out.println(path+" 不存在");
//				continue;
//			}
//			assetManager.load(path, Texture.class, textureParameter);
//			System.out.println("装载 "+path);
//		}
		/////////////
		assetManager.finishLoading();
//		 for (UnitData unitData : unitDatas) {
//			String fullname=unitData.fullname;
//				String path=ANIMs+fullname+_ATLAS;
//				FileHandle atlasFile = Gdx.files.internal(path); 
//				TextureAtlas ta=assetManager.get(path,TextureAtlas.class);
//				System.out.println(ta);
//		}
		 
		fontNormal=assetManager.get("size32.ttf");
		fontNormal_flip=assetManager.get("size32_flip.ttf");
		font2=assetManager.get("size12.ttf");
		font2_flip=assetManager.get("size12_flip.ttf");
//		facesAtlas=assetManager.get(FACES);
//		ui1=assetManager.get(UI1);
//		custom1Skin=assetManager.get(SKIN);
		//////////////
//			FileHandle file = Gdx.files.internal(FACES);
//			facesAtlas = new TextureAtlas(file);
//			ui1 = new TextureAtlas(Gdx.files.internal(
//					"data/ui/textureAtlas1.pack"));
		
			
//		String fonts=Gdx.files.internal("data/fonts.txt").readString("utf-8");
//		fonts="";
////		System.out.println(fonts);
//		fonts+="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890" +
//				"\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*《》，、。（）【】：" +
//				"“”！？·￥…—∞";
//		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/fangzhengcat.ttf"));
//		fontNormal=fontGenerator.generateFont(32, fonts, false);
//		fontNormal_flip=fontGenerator.generateFont(32, fonts, true);
//		font2=fontGenerator.generateFont(12, fonts, false);
//		font2_flip=fontGenerator.generateFont(12, fonts, true);
//		fontGenerator.dispose();
		
//		defaultSkin=  new Skin(Gdx.files.internal("data/uiskin.json"),
//						new TextureAtlas("data/uiskin.atlas"));
//		custom1Skin=  new Skin(Gdx.files.internal("data/ui/uiskin.json"),
//				new TextureAtlas("data/ui/uiskin.atlas"));
		
	}

}
