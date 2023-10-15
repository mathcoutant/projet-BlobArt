package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;


public class LevelEditorController implements InputProcessor {

    private LevelEditorScreen levelEditorScreen;
    private boolean leftClick;

    LevelEditorController(LevelEditorScreen editor) {
        levelEditorScreen = editor;
    }

    @Override
    public boolean keyDown(int keycode) {
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
        int[] cellCoordinates = levelEditorScreen.getCellCoordinates(screenX, screenY);
        boolean onWheel = levelEditorScreen.wheel.mouseOnWheel(screenX, screenY);
        leftClick = button == 0;

        if (cellCoordinates != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                if (leftClick)
                    levelEditorScreen.leftSelected.setColor(levelEditorScreen.getCellColor(cellCoordinates));
                else
                    levelEditorScreen.rightSelected.setColor(levelEditorScreen.getCellColor(cellCoordinates));
            } else {
                levelEditorScreen.updateCell(cellCoordinates, leftClick);
            }
        }

        if (onWheel) {
            if (leftClick)
                levelEditorScreen.leftSelected.setColor(getPixelColor(screenX, screenY));
            else
                levelEditorScreen.rightSelected.setColor(getPixelColor(screenX, screenY));
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        int[] cellCoordinates = levelEditorScreen.getCellCoordinates(screenX, screenY);
        boolean onWheel = levelEditorScreen.wheel.mouseOnWheel(screenX, screenY);

        if (cellCoordinates != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {

                Color color = levelEditorScreen.getCellColor(cellCoordinates);
                levelEditorScreen.colorTextTable.update(color);

                if (leftClick) {
                    levelEditorScreen.leftSelected.setColor(color);
                }
                else {
                    levelEditorScreen.rightSelected.setColor(color);
                }

            } else {

                levelEditorScreen.updateCell(cellCoordinates, leftClick);

            }

        }

        if (onWheel) {

            Color color = getPixelColor(screenX, screenY);
            levelEditorScreen.colorTextTable.update(color);

            if (leftClick)
                levelEditorScreen.leftSelected.setColor(color);
            else
                levelEditorScreen.rightSelected.setColor(color);
        }

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

    public Color getPixelColor(int screenX, int screenY) {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        Color color = new Color(pixmap.getPixel(screenX, screenY));
        pixmap.dispose();
        return color;
    }
}
