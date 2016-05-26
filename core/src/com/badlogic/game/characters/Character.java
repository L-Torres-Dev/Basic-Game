package com.badlogic.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by louie on 5/26/2016.
 */
public class Character extends Sprite{

    public Texture texture;

    public int level;
    public double attk;
    public double defense;
    public int xp;
    public int xpGive;
    public int xpNeed;
    public double maxHealth;
    public double currentHealth;
    public boolean isMyTurn;


    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
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

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getXpGive() {
        return xpGive;
    }

    public void setXpGive(int xpGive) {
        this.xpGive = xpGive;
    }

    public double getAttk() {
        return attk;
    }

    public void setAttk(double attk) {
        this.attk = attk;
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
    }
}
