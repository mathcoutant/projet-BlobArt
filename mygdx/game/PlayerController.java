package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

/** A class used to manage input to move the player */
public class PlayerController implements ControllerListener, InputProcessor {
    private Player player;
    private int gridWidth;
    private int gridHeight;

    public PlayerController(Player player, int gridWidth, int gridHeight) {
     this.player=player;
     this.gridWidth = gridWidth;
     this.gridHeight = gridHeight;
    }

    // For the keyboard
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT && player.posX>0){
            player.movePlayer(-1,0);
            return true;
        }
        if(keycode == Input.Keys.RIGHT && player.posX<gridWidth-1){
            player.movePlayer(1,0);
            return true;
        }
        if(keycode == Input.Keys.UP && player.posY<gridHeight-1){
            player.movePlayer(0,1);
            return true;
        }
        if(keycode == Input.Keys.DOWN && player.posY>0){
            player.movePlayer(0,-1);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    // For the controller
    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }


}
