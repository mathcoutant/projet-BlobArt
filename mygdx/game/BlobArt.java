package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class BlobArt extends Game{
	SpriteBatch batch;
	InputMultiplexer inputMultiplexer;
	public static Level[] levels;

	@Override
	public void create () {
		batch = new SpriteBatch();
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);

		updateLevels();

		this.setScreen(new SplashScreen(this, inputMultiplexer));
	}

	// create the array of level from the files
	public void updateLevels() {
		LevelParser parser = new LevelParser();
		BlobArt.levels = parser.parseFile(Gdx.files.local("levels"));
	}

	// called to recreate the files from the array of levels
	public void updateFiles(){
		LevelParser parser = new LevelParser();
		parser.updateFiles(Gdx.files.local("/levels"), levels);
	}

	// to add a level in the array of levels
	public void levelListAppend(Level level){
		Level[] newlevels = new Level[BlobArt.levels.length + 1];
		for (int i=0; i<BlobArt.levels.length; i++){
			newlevels[i] = BlobArt.levels[i];
		}
		newlevels[BlobArt.levels.length]=level;
		BlobArt.levels = newlevels;
	}

	@Override
	public void dispose () {
		batch.dispose();
		updateFiles();
	}

}
