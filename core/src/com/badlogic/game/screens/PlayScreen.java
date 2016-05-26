package com.badlogic.game.screens;

import com.badlogic.game.BasicGame;
import com.badlogic.game.characters.Enemy;
import com.badlogic.game.characters.Hero;
import com.badlogic.game.tools.InteractiveTileObject;
import com.badlogic.game.tools.WorldContactListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by louie on 5/16/2016.
 */
public class PlayScreen implements Screen{

    private BitmapFont font;
    private String debug;

    private Game game;
    private OrthographicCamera gamecam;

    private SpriteBatch batch;

    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Hero hero;
    private float heroY;            //X coordinates for the Hero.
    private float heroX;            //Y coordinates for the Hero.


    //Debug variables
    private float timer;
    private int counter;

    private ArrayList <Rectangle> fences;
    Rectangle specialGround1;               //Rectangle Object that represents the are in which special ground is.
    Rectangle specialGround2;               //Rectangle Object that represents the are in which special ground is.
    Rectangle specialGround3;               //Rectangle Object that represents the are in which special ground is.
    Rectangle specialGround4;               //Rectangle Object that represents the are in which special ground is.


    private boolean heronOnSpecial;             //boolean that returns true if the player is on any of the special grounds
    private boolean heroOnGround1;              //boolean that returns true if the player is on ground 1
    private boolean heroOnGround2;              //boolean that returns true if the player is on ground 2
    private boolean heroOnGround3;              //boolean that returns true if the player is on ground 3
    private boolean heroOnGround4;              //boolean that returns true if the player is on ground 4


    private ArrayList<Rectangle> specGroundList;    //Rectangle Array to store the individual special ground areas


    public PlayScreen(BasicGame game) {
        this.game = game;
        this.batch = game.batch;

        timer = 0;
        counter = 0;

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(BasicGame.V_WIDTH, BasicGame.V_HEIGHT, gamecam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Basic Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        hero = new Hero(world, this);

        specGroundList = new ArrayList<Rectangle>();

        world.setContactListener(new WorldContactListener());

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        fences = new ArrayList<Rectangle>();

        MapObject spg1 = new MapObject();
        MapObject spg2 = new MapObject();
        MapObject spg3 = new MapObject();
        MapObject spg4 = new MapObject();


        spg1 = map.getLayers().get("Special Ground").getObjects().get("Level 1");
        spg2 = map.getLayers().get("Special Ground").getObjects().get("Level 5");
        spg3 = map.getLayers().get("Special Ground").getObjects().get("Level 10");
        spg4 = map.getLayers().get("Special Ground").getObjects().get("Level 20");

        specialGround1 = new Rectangle();
        specialGround2 = new Rectangle();
        specialGround3 = new Rectangle();
        specialGround4 = new Rectangle();

        specialGround1 = ((RectangleMapObject) spg1).getRectangle();
        specialGround2 = ((RectangleMapObject) spg2).getRectangle();
        specialGround3 = ((RectangleMapObject) spg3).getRectangle();
        specialGround4 = ((RectangleMapObject) spg4).getRectangle();

        createBodiesAndFixtures();

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

        batch.draw(hero.getTexture(), hero.getPosition().x - 17, hero.getPosition().y - 13);
        font.draw(batch, debug, 175, 200);
        font.draw(batch, "Level 1", 65, 400);
        font.draw(batch, "Level 5", 350, 400);
        font.draw(batch, "Level 10", 65, 90);
        font.draw(batch, "Level 20", 350, 90);
        batch.end();





    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){

    }

    public void update(float dt){
        handleInput(dt);

        timer += dt;

        world.step(1/60f, 6, 0);

        hero.update(dt);


        debug();
        checkOnGround();

        gamecam.update();
        renderer.setView(gamecam);


        if (timer >= 1){
            counter +=1;
            generateBattle();
            timer = 0;
        }


    }

    /**
     * sets the debug String to give the programmer
     * information about the hero's position and
     * wether or not he is in one of the fences
     * (For debugging purposes).
     */
    private void debug(){

        DecimalFormat format = new DecimalFormat("##.##");

        heroX = hero.getPosition().x;
        heroY = hero.getPosition().y;

        debug = "Player: " + format.format(heroX) + " " + format.format(heroY)
                + "\nin any level: " + heronOnSpecial
                + "\nin level 1: "  + heroOnGround1
                + "\nin level 5: "  + heroOnGround2
                + "\nin level 10: " + heroOnGround3
                + "\nin level 20: " + heroOnGround4
                + "\nTime: " + counter;
    }

    /**
     * Checks to see if the hero is on special ground
     * If he is, the game will generate a random number
     * to see if he will start a battle.
     */
    private void checkOnGround(){

        heronOnSpecial = (heroOnGround1 || heroOnGround2 || heroOnGround3 || heroOnGround4);
        heroOnGround1 = specialGround1.contains(hero.getPosition());
        heroOnGround2 = specialGround2.contains(hero.getPosition());
        heroOnGround3 = specialGround3.contains(hero.getPosition());
        heroOnGround4 = specialGround4.contains(hero.getPosition());
    }

    /**
     * Generates a battle if the player is
     * in one of the fences.
     */
    private void generateBattle(){

        float battleChance = .25f;


        if(heronOnSpecial) {
            double x = Math.random();

            if (heroOnGround1 && x <= battleChance) {
                Enemy enemy = new Enemy(1);
                game.setScreen(new BattleScreen((BasicGame) game, hero, enemy));
                this.hide();
                this.pause();
            }

            if (heroOnGround2 && x <= battleChance) {
                Enemy enemy = new Enemy(5);
                game.setScreen(new BattleScreen((BasicGame) game, hero, enemy));
                this.hide();
            }

            if (heroOnGround3 && x <= battleChance) {
                Enemy enemy = new Enemy(10);
                game.setScreen(new BattleScreen((BasicGame) game, hero, enemy));
                this.hide();
            }

            if (heroOnGround4 && x <= battleChance) {
                Enemy enemy = new Enemy(20);
                game.setScreen(new BattleScreen((BasicGame) game, hero, enemy));
                this.hide();
            }
        }
    }

    private void createBodiesAndFixtures() {

        //create fence bodies/fixtures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {


            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new InteractiveTileObject(world, map, rect);

        }

        //create border bodies/ fixtures
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new InteractiveTileObject(world, map, rect);
        }
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

    public Viewport getGamePort() {
        return gamePort;
    }

    public ArrayList<Rectangle> getFences() {
        return fences;
    }
}


