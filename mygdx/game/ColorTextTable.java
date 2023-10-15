package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ColorTextTable extends Table {
    private Label.LabelStyle labelStyle;
    private TextField.TextFieldStyle textFieldStyle;
    private TextField redField,greenField,blueField,hueField,satField,valueField,hexField;

    private LevelEditorScreen editorScreen;


    public ColorTextTable(final LevelEditorScreen editorScreen){
        super();
        this.editorScreen = editorScreen;

        SpriteDrawable background = new SpriteDrawable();
        background.setSprite(new Sprite(new Texture(Gdx.files.internal("ui/box.png"))));

        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY);
        textFieldStyle = new TextField.TextFieldStyle(new BitmapFont(),Color.WHITE,null,null,background);

        // setting the textFields to select the color
        TextField.TextFieldListener textFieldListener = new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                boolean rgbField = textField == redField || textField == greenField || textField == blueField;
                boolean hsvField = textField == hueField || textField == satField || textField == valueField;
                String text = textField.getText();
                if(text.equals("")){
                    textField.setText("0");
                }
                else{
                    int value = Integer.parseInt(text);
                    int newValue = Math.min(255,Math.max(value, 0));
                    textField.setText(Integer.toString(newValue));
                }

                System.out.println(textField.getText());

                if (true){
                    System.out.println("change occur");
                    Color color = new Color();
                    if(rgbField){
                        float r = Integer.parseInt(redField.getText())/255f;
                        float g = Integer.parseInt(greenField.getText())/255f;
                        float b = Integer.parseInt(blueField.getText())/255f;
                        Color.rgba8888ToColor(color,Color.rgba8888(r,g,b,1.0f));
                    } else if (hsvField) {

                        float h = Integer.parseInt(hueField.getText())*360f/255f;
                        float s = Integer.parseInt(satField.getText())/255f;
                        float v = Integer.parseInt(valueField.getText())/255f;

                        color.fromHsv(h,s,v);
                        // libgdx seems to set alpha value to 0, so we fix this
                        color = Color.valueOf(color.toString().substring(0,6)+"ff");
                    }
                    System.out.println(color.toString());
                    update(color);
                    editorScreen.updateLastSelectedColor(color);
                }
            }
        };

        TextField.TextFieldFilter hexFilter = new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return "0123456789abcdef".contains(Character.toString(c));
            }
        };

        TextField.TextFieldListener hexListener = new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                String text = textField.getText();
                if(text.length()==6){
                    Color color = Color.valueOf(text+"ff");
                    update(color);
                    editorScreen.updateLastSelectedColor(color);
                }
            }
        };

        redField = new TextField("255",textFieldStyle);
        redField.setAlignment(1);
        redField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        redField.setTextFieldListener(textFieldListener);
        
        greenField = new TextField("255",textFieldStyle);
        greenField.setAlignment(1);
        greenField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        greenField.setTextFieldListener(textFieldListener);

        blueField = new TextField("255",textFieldStyle);
        blueField.setAlignment(1);
        blueField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        blueField.setTextFieldListener(textFieldListener);

        hueField = new TextField("0",textFieldStyle);
        hueField.setAlignment(1);
        hueField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        hueField.setTextFieldListener(textFieldListener);

        satField = new TextField("0",textFieldStyle);
        satField.setAlignment(1);
        satField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        satField.setTextFieldListener(textFieldListener);

        valueField = new TextField("255",textFieldStyle);
        valueField.setAlignment(1);
        valueField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        valueField.setTextFieldListener(textFieldListener);

        hexField = new TextField("ffffff",textFieldStyle);
        hexField.setTextFieldFilter(hexFilter);
        hexField.setMaxLength(6);
        hexField.setTextFieldListener(hexListener);

        // setting the labels
        float padding = 6f;
        this.add(new Label("R:",labelStyle)).padBottom(padding);
        this.add(redField).width(Gdx.graphics.getWidth()*0.025f).padBottom(padding);
        this.row();
        this.add(new Label("G:",labelStyle)).padBottom(padding);
        this.add(greenField).width(Gdx.graphics.getWidth()*0.025f).padBottom(padding);
        this.row();
        this.add(new Label("B:",labelStyle)).padBottom(40f);
        this.add(blueField).width(Gdx.graphics.getWidth()*0.025f).padBottom(40f);
        this.row();
        this.add(new Label("H:",labelStyle)).padBottom(padding);
        this.add(hueField).width(Gdx.graphics.getWidth()*0.025f).padBottom(padding);
        this.row();
        this.add(new Label("S:",labelStyle)).padBottom(padding);
        this.add(satField).width(Gdx.graphics.getWidth()*0.025f).padBottom(padding);
        this.row();
        this.add(new Label("V:",labelStyle)).padBottom(40f);
        this.add(valueField).width(Gdx.graphics.getWidth()*0.025f).padBottom(40f);
        this.row();
        this.add(new Label("Hex:",labelStyle)).padBottom(padding);
        this.add(hexField).width(Gdx.graphics.getWidth()*0.025f).padBottom(padding);
    }

    // called to update the textFields when the color is picked on the wheel
    public void update(Color color){
        
        String hex = color.toString();
        int r = Integer.parseInt(hex.substring(0,2),16);
        int g = Integer.parseInt(hex.substring(2,4),16);
        int b = Integer.parseInt(hex.substring(4,6),16);
        redField.setText(Integer.toString(r));
        greenField.setText(Integer.toString(g));
        blueField.setText(Integer.toString(b));
        
        float[] hsv = new float[3];
        color.toHsv(hsv);
        
        int h = Math.round(hsv[0]*255f/360f);
        int s = Math.round(hsv[1]*255f);
        int v = Math.round(hsv[2]*255f);
        hueField.setText(Integer.toString(h));
        satField.setText(Integer.toString(s));
        valueField.setText(Integer.toString(v));

        hexField.setText(color.toString().substring(0,6));
    }
}
