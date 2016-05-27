package com.badlogic.game.characters;

import com.badlogic.game.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by louie on 5/16/2016.
 */
public class Hero extends Character{

    public World world;
    public Body b2body;
    private PolygonShape rect;
    private Texture texture;
    private PlayScreen screen;


    private float x;
    private float y;

    private Vector2 position;
    private Vector2 oldPosition;



    boolean moveUp;
    boolean moveDown;
    boolean moveRight;
    boolean moveLeft;
    boolean isMyTurn;

    private float move;
    private float moveX;
    private float moveY;

    private double angelVel;

    public Hero(World world, PlayScreen screen){
        super();
        this.screen = screen;
        this.world = world;
        position = new Vector2(this.getX(), this.getY());
        oldPosition = new Vector2(position);




        level = 1;
        int formula = ((level * 16) - ((level * 5) / 2)) + level;
        maxHealth = 10;
        currentHealth = maxHealth;
        attk = 7;
        defense = 5;
        xpNeed = (level * 4) * ((level * 2) / 2) + 5;
        xp = 0;

        x = screen.getGamePort().getWorldWidth() / 2;
        y = screen.getGamePort().getWorldHeight() / 2;


        move = 100f;
        moveX = 0f;
        moveY = 0f;

        angelVel = Math.sqrt(Math.pow(move, 2) / 2) - (move / 4);

        defineHero();
        texture = new Texture("Apple.png");

        setTexture(texture);



    }

    public void attackEnemy(Enemy enemy){
        double dmg = ((int) attk - enemy.getDefense());
        double enemyHealth = enemy.getCurrentHealth();

        if (dmg < 1){
            dmg = 1;
        }
        enemyHealth -= dmg;

        enemy.setCurrentHealth(enemyHealth);
    }

    public void update(float dt){

        handleInput(dt);
        position = b2body.getPosition();

    }
    public void updateHero(){
        int xpNeed = (level * 4) * ((level * 2) / 2) + 5;
        this.setXpNeed(xpNeed);
        System.out.println(xpNeed);
    }

    public Texture getTexture() {
        return texture;
    }

    public void handleInput(float dt){
        move();
    }

    public void defineHero(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        position = bdef.position;


        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(13);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }


    private void move() {
        moveUp = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        moveDown = Gdx.input.isKeyPressed(Input.Keys.DOWN)  || Gdx.input.isKeyPressed(Input.Keys.S);
        moveRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT)  || Gdx.input.isKeyPressed(Input.Keys.D);
        moveLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT)  || Gdx.input.isKeyPressed(Input.Keys.A);

        moveX = 0;
        moveY = 0;

        if(moveUp){
            this.b2body.setLinearVelocity(0, move);

        }

        if(moveUp && moveRight){
            this.b2body.setLinearVelocity(move, move);

        }

        if(moveUp && moveLeft){
            this.b2body.setLinearVelocity(-move, move);

        }


        if(moveDown){
            this.b2body.setLinearVelocity(0, -move);

        }
        if(moveDown && moveRight){
            this.b2body.setLinearVelocity(move, -move);
        }

        if(moveDown && moveLeft){
            this.b2body.setLinearVelocity(-move, -move);
        }


        if(moveRight && (!moveUp && !moveDown)){
            this.b2body.setLinearVelocity(move, 0);

        }

        if(moveLeft && (!moveUp && !moveDown)){
            this.b2body.setLinearVelocity(-move, 0);

        }

        if(!moveUp && !moveDown && !moveRight && !moveLeft) {
            this.b2body.setLinearVelocity(0, 0);
        }
    }

    /*public double getAttk() {
        return attk;
    }

    public void setAttk(double attk) {
        this.attk = attk;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getCurrentHealth(){
        return currentHealth;
    }

    public void setCurrentHealth(double currentHealth){
        this .currentHealth = currentHealth;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public int getXpNeed() {
        return xpNeed;
    }

    public void setXpNeed(int xpNeed) {
        this.xpNeed = xpNeed;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }*/

}
