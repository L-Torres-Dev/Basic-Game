package com.badlogic.game.screens;

import com.badlogic.game.BasicGame;
import com.badlogic.game.characters.Enemy;
import com.badlogic.game.characters.Hero;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.text.DecimalFormat;

/**
 * Created by louie on 5/24/2016.
 */
public class BattleScreen implements Screen {

    private Texture texture;

    private BasicGame game;
    private OrthographicCamera gamecam;

    private SpriteBatch batch;

    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    OrthogonalTiledMapRenderer renderer;


    private Hero hero;
    boolean isHeroMoved;
    boolean heroWins;
    private float heroX;
    private float heroY;

    private Enemy enemy;
    boolean isEnemyMoved;
    boolean enemyWins;
    private float enemyX;
    private float enemyY;

    //Box@d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    BitmapFont font;
    String debug;
    float debugX;
    float debugY;

    String heroStats;
    float heroStatsX;
    float heroStatsY;

    String enemyStats;
    float enemyStatsX;
    float enemyStatsY;
    int enemyTurnCounter;

    String turn;
    float timer;

    public BattleScreen(BasicGame game, Hero hero, Enemy enemy){

        this.game = game;
        this.batch = game.batch;
        this.hero = hero;
        this.enemy = enemy;

        hero.setMyTurn(true);
        enemy.setMyTurn(false);
        heroWins = false;
        enemyWins = false;

        heroX = 20;
        heroY = 220;

        enemyX = 395;
        enemyY = 220;

        heroStatsX = heroX;
        heroStatsY = heroY + 100;

        enemyStatsX = enemyX - 65;
        enemyStatsY = enemyY + 100;


        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(BasicGame.V_WIDTH, BasicGame.V_HEIGHT, gamecam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Battle Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight()/ 2, 0);


        debug = "";
        font = new BitmapFont();
        debugX = gamecam.position.x - 50;
        debugY = gamecam.position.y;

        heroStats = "health: " + hero.getCurrentHealth() + " / " + hero.getMaxHealth()
                    + "\nLevel" + hero.getLevel();

        enemyStats = "health: " + enemy.getCurrentHealth() + " / " + enemy.getMaxHealth()
                    + "\nLevel" + enemy.getLevel();



    }





    private void update(float dt){

        if(enemy.isMyTurn()) {
            timer += dt;
        }

        if(isHeroMoved == false){
            heroX = 20;
        }
        if(isEnemyMoved == false){
            enemyX = 395;
        }
        handleInput(dt);
        enemyTurn();

        gamecam.update();
        renderer.setView(gamecam);

        debug();
        updateString();

        if(timer >= 1){
            enemyTurnCounter += 1;
            timer = 0;

        }
    }

    private void handleInput(float dt){
        if(Gdx.input.isTouched() && hero.isMyTurn()){
            heroX = (heroX + 50);
            isHeroMoved = true;
            hero.attackEnemy(enemy);

            hero.setMyTurn(false);
            enemy.setMyTurn(true);
            if(enemy.getCurrentHealth() <= 0){
                heroWins = true;
                win();
            }
        }
    }

    private void enemyTurn(){
        if(enemyTurnCounter == 2){
            enemy.setMyTurn(true);
        }

        if(enemy.isMyTurn() && enemyTurnCounter == 1){
            enemyX = (enemyX - 50);
            isEnemyMoved = true;
            enemy.attackHero(hero);

            hero.setMyTurn(true);
            enemy.setMyTurn(false);
            enemyTurnCounter = 0;
        }

        if (hero.getCurrentHealth() <= 0){
            enemyWins = true;
            lose();
        }
    }

    private void win(){
        game.setScreen(new WinScreen(game, hero, enemy));

        this.dispose();
    }

    private void lose(){
        game.setScreen(new LoseScreen(game));

        this.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        // Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();


        batch.setProjectionMatrix(gamecam.combined);

        batch.begin();
        batch.draw(hero.getTexture(), heroX, heroY);
        batch.draw(enemy.getTexture(), enemyX, enemyY);
        font.draw(batch, debug, debugX, debugY);
        font.draw(batch, heroStats, heroStatsX, heroStatsY);
        font.draw(batch, enemyStats, enemyStatsX, enemyStatsY);
        font.draw(batch, turn, heroStatsX + 200, heroStatsY);
        batch.end();

        isHeroMoved = false;
        isEnemyMoved = false;


    }

    private void debug(){

        DecimalFormat format = new DecimalFormat("##.##");


        debug = gamecam.position.toString()
                + "\n" + enemyTurnCounter
                + "\n" + timer;
    }
    private void updateString(){
        if(hero.isMyTurn()){
            turn = "Turn: Hero";
        }
        else{
            turn = "Turn: Enemy";
        }

        heroStats = "health: " + hero.getCurrentHealth() + " / " + hero.getMaxHealth()
                + "\nLevel" + hero.getLevel();

        enemyStats = "health: " + enemy.getCurrentHealth() + " / " + enemy.getMaxHealth()
                + "\nLevel" + enemy.getLevel();
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
