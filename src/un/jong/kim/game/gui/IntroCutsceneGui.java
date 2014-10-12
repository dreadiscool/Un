package un.jong.kim.game.gui;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import un.jong.kim.game.Game;
import un.jong.kim.game.level.NsaLevel;

public class IntroCutsceneGui implements Gui {

    private long startTime = System.currentTimeMillis();
    private Music heyey;
    private Image theInternet;
    private Image introCutsceneText;
    private int imageHeight;

    public IntroCutsceneGui(Music heyey) {
        this.heyey = heyey;
        this.heyey.fade(1000, 0.2f, false);
        try {
            this.theInternet = new Image(Game.ASSET_PATH + "the-internet.png");
            this.introCutsceneText = new Image(Game.ASSET_PATH + "intro-cutscene-text.png");
            this.imageHeight = this.introCutsceneText.getHeight();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public void render(Graphics g) {
        g.drawImage(theInternet, 0, 0);
        long millisPassed = (System.currentTimeMillis() - startTime) - 1000;
        if (millisPassed > 30000) {
            Game.getInstance().submitGui((Gui) new NsaLevel());
            Game.getInstance().removeGui(this.getClass());
        } else if (millisPassed > 0) {
            double percent = (millisPassed / 30000d);
            int offsetY = (int) Math.floor(percent * (double) imageHeight);
            g.drawImage(introCutsceneText, 325, 800 - offsetY);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Game.getInstance().submitGui((Gui) new NsaLevel());
            Game.getInstance().removeGui(this.getClass());
        }
    }

    public void destroy() {
        heyey.fade(700, 0f, true);
    }
}
