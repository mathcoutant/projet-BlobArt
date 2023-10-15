package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class LevelEditorScreen extends ScreenAdapter {
    private BlobArt game;
    public Level level;

    private LevelEditorController levelEditorController;
    private InputMultiplexer inputMultiplexer;
    private Skin skin;
    private ImageButton goalButton, backButton;

    private ColorPaletteButton[] paletteButtons;

    private CellButton[] cellButtons;

    private CellButton cellType;

    ColorPaletteButton leftSelected, rightSelected,lastSelected;


    private Table globalTable, editorTable, colorPaletteTable, colorSelectionTable;
    private LevelInfoTable levelInfoTable;

    public ColorTextTable colorTextTable;
    private Slider slider;
    private Stage stage;
    public ColorWheel wheel;
    private float dimCell;
    private float originX;
    private float originY;

    private Sprite background;

    private boolean toggle;

    // TODO: Improve UI
    // TODO: Add cell type selection grid



    public LevelEditorScreen(BlobArt game, InputMultiplexer inputMultiplexer, Level level) {

        this.game = game;
        this.inputMultiplexer = inputMultiplexer;
        this.level = level;
        this.stage = new Stage();
        this.skin = new Skin();
        this.levelEditorController = new LevelEditorController(this);

        createButtons();
        createTables();
        setInputListeners();

        stage.addActor(globalTable);
        //stage.setDebugAll(true);
        wheel = new ColorWheel(slider);
        slider.addListener(wheel);

        stage.addActor(slider);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(levelEditorController);


    }

    private void createButtons() {

        skin.add("sliderBackground", new Texture(Gdx.files.internal("ui/sliderBackground.png")));
        skin.add("sliderKnob", new Texture(Gdx.files.internal("ui/sliderKnob.png")));
        skin.add("goalBut", new Texture(Gdx.files.internal("ui/goalButton.png")));
        skin.add("goalButChecked",new Texture(Gdx.files.internal("ui/goalButtonChecked.png")));
        skin.add("backButton-over",new Texture(Gdx.files.internal("LevelsScreen/backButton-Over.png")));
        skin.add("backButton-pressed",new Texture(Gdx.files.internal("LevelsScreen/backButton-Pressed.png")));
        skin.add("backButton-up",new Texture(Gdx.files.internal("LevelsScreen/backButton-Up.png")));

        ImageButton.ImageButtonStyle goalButtonStyle = new ImageButton.ImageButtonStyle();
        goalButtonStyle.imageUp = skin.getDrawable("goalBut");
        goalButtonStyle.imageChecked = skin.getDrawable("goalButChecked");

        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = skin.getDrawable("backButton-up");
        backButtonStyle.imageOver = skin.getDrawable("backButton-over");
        backButtonStyle.imageDown = skin.getDrawable("backButton-pressed");
        backButton = new ImageButton(backButtonStyle);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.getDrawable("sliderBackground");
        sliderStyle.knob = skin.getDrawable("sliderKnob");

        this.goalButton = new ImageButton(goalButtonStyle);
        this.slider = new Slider(0, 1, 0.001f, false, sliderStyle);

        paletteButtons = new ColorPaletteButton[20];

        paletteButtons[0] = new ColorPaletteButton();
        leftSelected = paletteButtons[0];
        paletteButtons[0].select(0);

        paletteButtons[1] = new ColorPaletteButton();
        rightSelected = paletteButtons[1];
        paletteButtons[1].select(1);

        lastSelected = paletteButtons[0];

        for (int i = 2; i < paletteButtons.length; i++) {
            paletteButtons[i] = new ColorPaletteButton();
        }

        cellButtons = new CellButton[3];
        cellButtons[0] = new CellButton(0,"ui/paintablebutton.png");
        cellButtons[1] = new CellButton(1,"ui/bucketbutton.png");
        cellButtons[2] = new CellButton(2,"ui/wallbutton.png");
        cellType = cellButtons[0];
        cellButtons[0].select();

    }

    private void createTables() {

        this.globalTable = new Table();
        globalTable.setFillParent(true);

        this.background = new Sprite(new Texture(Gdx.files.internal("ui/leveleditorscreen.png")));

        globalTable.add().colspan(3).height(Gdx.graphics.getHeight() / 20f).row();
        Table backButtonContainer = new Table();
        globalTable.add(backButtonContainer).width(Gdx.graphics.getWidth() / 10f).height(Gdx.graphics.getHeight()*9f/10f);
        backButtonContainer.add(backButton).row();
        backButtonContainer.add().expandY();
        globalTable.add().width(Gdx.graphics.getWidth() * 6f / 10f).expandY();
        this.editorTable = new Table();
        globalTable.add(editorTable).width(Gdx.graphics.getWidth() * 3f / 10f).row();
        this.colorPaletteTable = new Table();
        editorTable.add(colorPaletteTable);
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < paletteButtons.length/2; i++) {
                colorPaletteTable.add(paletteButtons[i+j*paletteButtons.length/2]).size(Gdx.graphics.getWidth()*0.03f);
            }
            colorPaletteTable.row();
        }
        colorSelectionTable = new Table();
        Table colorTable = new Table();
        editorTable.row();
        editorTable.add().height(Gdx.graphics.getHeight()*0.2f);
        editorTable.row();
        editorTable.add(colorTable);
        colorTable.add(colorSelectionTable).width(Gdx.graphics.getWidth()*0.2f).height(Gdx.graphics.getHeight()*0.4f);
        colorTextTable = new ColorTextTable(this);
        colorTable.add(colorTextTable).width(Gdx.graphics.getWidth()*0.1f).height(Gdx.graphics.getHeight()*0.4f);
        editorTable.row();
        levelInfoTable = new LevelInfoTable(this);
        editorTable.add(levelInfoTable).width(Gdx.graphics.getWidth()*0.3f).height(Gdx.graphics.getHeight()*0.15f);
        editorTable.row();
        Table cellSelectionTable = new Table();
        editorTable.add(cellSelectionTable);
        for (int i = 0; i < cellButtons.length; i++){
            cellSelectionTable.add(cellButtons[i]).size(Gdx.graphics.getWidth()*0.03f);
        }
        editorTable.row();
        editorTable.add(goalButton).row();

        globalTable.add().colspan(3).height(Gdx.graphics.getHeight() / 20f);

    }

    private void setInputListeners() {
        InputListener paletteInputListener = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Actor target = event.getTarget();
                ColorPaletteButton paletteButton = (ColorPaletteButton) target.getParent();
                if (paletteButton != leftSelected && button == 0) {
                    leftSelected.unselect();
                    paletteButton.select(button);
                    leftSelected = paletteButton;

                } else if (paletteButton != rightSelected && button == 1) {
                    rightSelected.unselect();
                    paletteButton.select(button);
                    rightSelected = paletteButton;

                }
                colorTextTable.update(paletteButton.getColor());
                paletteButton.select(button);
                lastSelected = paletteButton;

            }
        };

        InputListener cellButtonListener = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Actor target = event.getTarget();
                CellButton cellButton = (CellButton) target.getParent();
                if (cellButton != cellType ) {
                    cellType.unselect();
                    cellButton.select();
                    cellType = cellButton;
                }
            }
        };

        for (int i = 0; i < paletteButtons.length; i++) {
            paletteButtons[i].addListener(paletteInputListener);
        }

        for (int i = 0; i < cellButtons.length; i++){
            cellButtons[i].addListener(cellButtonListener);


        }

        this.backButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                inputMultiplexer.removeProcessor(stage);
                inputMultiplexer.removeProcessor(levelEditorController);
                if(toggle){
                    for (int i = 0; i < level.grid.cells.length; i++) {
                        for (int j = 0; j < level.grid.cells[0].length; j++) {
                            Cell cell = level.grid.cells[i][j];
                            if(cell instanceof PaintableCell){
                                ((PaintableCell) cell).toggle();
                            }
                        }
                    }
                }
                level.setLevelInfo(levelInfoTable.getName(),levelInfoTable.getMoveGoal(), levelInfoTable.getTimeGoal());
                level.grid.reset();
                game.setScreen(new LevelsScreen(game, inputMultiplexer));
            }
        });

        this.goalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggle = !toggle;
                for (int i = 0; i < level.grid.cells.length; i++) {
                    for (int j = 0; j < level.grid.cells[0].length; j++) {
                        Cell cell = level.grid.cells[i][j];
                        if(cell instanceof PaintableCell){
                            ((PaintableCell) cell).toggle();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void show() {

        updateSpritePosition();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();
        background.draw(game.batch);
        level.render(game.batch, dimCell, originX, originY, false);
        wheel.draw(game.batch);
        game.batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void updateSpritePosition() {


        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float nbrCellX = level.grid.cells.length;
        float nbrCellY = level.grid.cells[0].length;
        if (.9f * windowHeight / nbrCellY < .6f * windowWidth / nbrCellX) {
            this.dimCell = (9f / 10f) * windowHeight / nbrCellY;
            this.originY = (1f / 20f) * windowHeight;
            this.originX = (1f / 2f) * ((8f / 10f) * windowWidth - nbrCellX * this.dimCell);
        } else {
            this.dimCell = (6f / 10f) * (windowWidth / nbrCellX);
            this.originX = (1f / 10f) * windowWidth;
            this.originY = (1f / 2f) * (windowHeight - nbrCellY * this.dimCell);
        }
        Cell[][] cells = level.grid.cells;
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[0].length; y++) {
                cells[x][y].setCellSprite(x, y, dimCell, originX, originY);
            }
        }

        level.player.setPlayerSprite(dimCell);
    }


    public void updateCell(int[] cellCoordinates, boolean leftClick){
        int i = cellCoordinates[0];
        int j = cellCoordinates[1];

        Cell newCell;
        Cell oldCell = level.grid.cells[i][j];
        Color col;

        if(leftClick){
            col = leftSelected.getColor();
        }
        else {
            col = rightSelected.getColor();
        }

        if(cellType.index == 0) {
            if(goalButton.isChecked()){
                newCell = new PaintableCell(Color.WHITE,col);
                ((PaintableCell) newCell).toggle();
            }
            else{
                newCell = new PaintableCell(col, Color.WHITE);
            }
        }
        else if(cellType.index == 1) {
            newCell = new BucketCell(col);
        }
        else {
            newCell = new WallCell(col);
        }

        if (oldCell.getClass() == newCell.getClass()){
            oldCell.editColor(col);
        }
        else {
            oldCell.dispose();
            oldCell = newCell;
            level.grid.cells[i][j] = newCell;
            updateSpritePosition();
        }


    }

    public Color getCellColor(int[] cellCoordinates){
        int i = cellCoordinates[0];
        int j = cellCoordinates[1];
        Cell cell = level.grid.cells[i][j];
        return cell.getColor().cpy();
    }


    public int[] getCellCoordinates(int x, int y) {
        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        int gridWidth = level.grid.cells.length;
        int gridHeight = level.grid.cells[0].length;
        if (.9f * windowHeight / gridHeight < .6f * windowWidth / gridWidth) {
            this.dimCell = (9f / 10f) * windowHeight / gridHeight;
            this.originY = (1f / 20f) * windowHeight;
            this.originX = (1f / 2f) * ((8f / 10f) * windowWidth - gridWidth * this.dimCell);
        } else {
            this.dimCell = (6f / 10f) * (windowWidth / gridWidth);
            this.originX = (1f / 10f) * windowWidth;
            this.originY = (1f / 2f) * (windowHeight - gridHeight * this.dimCell);
        }
        int i = (int) ((x - this.originX) / this.dimCell);
        int j = (int) (gridHeight - ((y - this.originY) / this.dimCell));
        if (i < 0 || i >= gridWidth || j < 0 || j >= gridHeight) {
            return null;
        }
        return new int[]{i,j};
    }

    public void updateLastSelectedColor(Color color){
        lastSelected.setColor(color.cpy());
    }
    @Override
    public void hide() {

        skin.dispose();
        stage.dispose();
        dispose();
    }

    public void changeGridSize(int x,int y){
        level.changeGridSize(x,y);
        updateSpritePosition();
    }


}
