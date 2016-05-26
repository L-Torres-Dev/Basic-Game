package com.badlogic.game.screens;

import com.badlogic.game.BasicGame;
import com.badlogic.game.characters.Enemy;
import com.badlogic.game.characters.Hero;
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
public class WinScreen implements Screen {

    private BasicGame game;
    private OrthographicCamera gamecam;

    private SpriteBatch batch;

    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    OrthogonalTiledMapRenderer renderer;

    String results;
    BitmapFont font;

    private int xpGain;
    private int toNextLevel;

    public WinScreen(BasicGame game, Hero hero, Enemy enemy){
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


        xpGain = enemy.getXpGive();

        hero.setXp(hero.getXp() + xpGain);
        toNextLevel = hero.getXpNeed() - hero.getXp();

        results = "YOU WIN!!"
                + "\n" +  "You gained " + xpGain + "xp!"
                + "\n" + "To next Level: " + toNextLevel
                + "\n" + "Xp/XpNeed " + hero.getXp() + " / " + hero.getXpNeed();


        if(toNextLevel <= 0){
            levelUp(hero);
        }

        hero.updateHero();

    }

    private void levelUp(Hero hero){
        hero.updateHero();
        double randNum;
        int statPoints = 5;
        int currentLevel = hero.getLevel();
        hero.setLevel(currentLevel + 1);

        hero.setMaxHealth(hero.getMaxHealth() + 1);
        toNextLevel = hero.getXpNeed() - hero.getXp();

        for(int i = 0; i <= statPoints; i++){

            randNum = Math.random();
            if(randNum <= .33){
                hero.setMaxHealth(hero.getMaxHealth() + 1);
            }
            else if(randNum <= .66){
                hero.setAttk(hero.getAttk() + 1);
            }
            else{
                hero.setDefense(hero.getDefense() + 1);
            }
        }

        hero.setCurrentHealth(hero.getMaxHealth());
        results = "YOU WIN!!"
                + "\n" +  "You gained " + xpGain + "xp!"
                +"\n" + "To next Level: " + toNextLevel
                +"\n" + "YOU LEVELED UP!"
                + "\n" + "Xp/XpNeed " + hero.getXp() + " / " + hero.getXpNeed();;

    }

    private void handleInput(float dt){
        if(Gdx.input.justTouched()){
            game.setScreen(game.play);

            this.dispose();
        }
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
        font.draw(batch, results, gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2);
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
