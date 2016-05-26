package com.badlogic.game.screens;

import com.badlogic.game.BasicGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by louie on 5/26/2016.
 */
public class LoseScreen implements Screen{
    private BasicGame game;
    private OrthographicCamera gamecam;

    private SpriteBatch batch;

    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    OrthogonalTiledMapRenderer renderer;

    String lose;
    BitmapFont font;

    public LoseScreen(BasicGame game){
        this.game = game;
        batch = game.batch;


        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(BasicGame.V_WIDTH, BasicGame.V_HEIGHT, gamecam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Battle Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        lose = "YOU LOSE!!";

    }

    private void handleInput(float dt){

    }

    private void update(float dt){

        handleInput(dt);
        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        renderer.render();

        batch.setProjectionMatrix(gamecam.combined);
        batch.begin();
        font.draw(batch, lose, gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
