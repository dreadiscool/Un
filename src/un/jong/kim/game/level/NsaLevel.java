package un.jong.kim.game.level;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import un.jong.kim.game.Game;
import un.jong.kim.game.Position;
import un.jong.kim.game.entity.BossObamaEntity;

import java.io.File;

public class NsaLevel extends Level {

    private static final int ASSET_COUNT = 5;

    private Music musicNsa;
    private BossObamaEntity bossObama;

    protected void renderLoadingScreen(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Loading...", 570, 370);
        g.setColor(Color.red);
        g.fillRect(200, 425, 800, 50);
        g.setColor(Color.green);
        displayProgress(g, ASSET_COUNT);
        if (musicNsa == null) {
            musicNsa = loadAndUpdateMusic(g, Game.ASSET_PATH + "music" + File.separator + "nsa.ogg", ASSET_COUNT);
            musicNsa.loop();
            return;
        }
        if (background == null) {
            background = loadAndUpdateImage(g, Game.ASSET_PATH + "level-nsa-bg.png", ASSET_COUNT);
            return;
        }
        if (scroll == null) {
            scroll = loadAndUpdateImage(g, Game.ASSET_PATH + "level-nsa.png", ASSET_COUNT);
            return;
        }
        if (levelMap == null) {
            levelMap = new Map("level-nsa");
            levelMap.load();
        }
        if (bossObama == null) {
            bossObama = new BossObamaEntity(loadAndUpdateImage(g, Game.ASSET_PATH + "obama.png", ASSET_COUNT));
        }
        previousTimestamp = System.currentTimeMillis();
        Game.getInstance().getPlayer().setPosition(new Position(25, 600));
        allowRender = true;
    }

    protected void renderLevel(Graphics g) {
        System.out.println(this.levelOffsetX);
    }

    public void destroy() { }
}
