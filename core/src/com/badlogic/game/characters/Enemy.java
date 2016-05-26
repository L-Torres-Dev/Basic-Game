package com.badlogic.game.characters;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by louie on 5/25/2016.
 */
public class Enemy extends Character {


    public Enemy(){

        super();
        texture = new Texture("GreenApple.png");
    }

    public Enemy(int lvl){

        super();
        texture = new Texture("GreenApple.png");



        level = lvl;
        int x = ((level * 10) - ((level * 5) / 2));
        xpGive = x - (x / 2);
        maxHealth = 8;
        attk = 3;
        defense = 3;

        calculateStats(lvl);
        currentHealth = maxHealth;

    }

    public void attackHero(Hero hero){
        double dmg = ((int) attk - hero.getDefense());
        double heroHealth = hero.getCurrentHealth();

        if (dmg < 1){
            dmg = 1;
        }

        heroHealth -= dmg;
        hero.setCurrentHealth(heroHealth);
    }

    private void calculateStats(int lvl){
        int statPoints = (lvl - 1) * 5;
        double randNum;

        for(int i = 0; i < statPoints; i++){
            randNum = Math.random();
            if(randNum <= .33){
                maxHealth += 1;
            }
            else if(randNum <= .66){
                attk += 1;
            }
            else{
                defense += 1;
            }
        }
    }


}
