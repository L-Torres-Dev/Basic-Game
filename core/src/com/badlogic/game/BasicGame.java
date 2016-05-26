package com.badlogic.game;

import com.badlogic.game.screens.BattleScreen;
import com.badlogic.game.screens.PlayScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BasicGame extends Game {

	public static final int V_WIDTH = 450;
	public static final int V_HEIGHT = 450;

	public Music music;
	public SpriteBatch batch;

	public PlayScreen play;



	@Override
	public void create () {
		batch = new SpriteBatch();
		play = new PlayScreen(this);
		setScreen(play);
		music = Gdx.audio.newMusic(Gdx.files.internal("Take2.wav"));
		music.setLooping(true);
		music.setVolume(.2f);
		music.play();

	}

	@Override
	public void render () {
		super.render();
	}
}
