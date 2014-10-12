package un.jong.kim.game.gui;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import un.jong.kim.game.Game;

import java.io.File;

public class MainMenuGui implements Gui {

    private Image backgroundImage;
    private Image kimImage;
    private Music heyey;
    private long startTime = System.currentTimeMillis();

    public MainMenuGui() {
        try {
            this.backgroundImage = new Image(Game.ASSET_PATH + "main-menu-bg.png");
            this.kimImage = new Image(Game.ASSET_PATH + "main-menu-kim.png");
            this.heyey = new Music(Game.ASSET_PATH + "music" + File.separator + "gangnam.ogg");
            this.heyey.loop();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImage, 0, 0);
        long thresh = (System.currentTimeMillis() - startTime) - 1000;
        int offsetY = 0;
        if (thresh > 0 && thresh < 1000) {
            double percent = 350d - (thresh / 1000d) * 350d;
            offsetY = 350 - (int) percent;
        } else if (thresh >= 1000) {
            offsetY = 350;
        }
        g.drawImage(kimImage, -30, 800 - offsetY);
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            Game.getInstance().removeGui(this.getClass());
            Game.getInstance().submitGui(new IntroCutsceneGui(heyey));
        }
    }

    public void destroy() {

    }
}
