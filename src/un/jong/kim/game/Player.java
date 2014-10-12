package un.jong.kim.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.io.File;

public class Player {

    private Position position;
    private Image[] animationImagesRight;
    private Image[] animationImagesLeft;
    private long lastRenderTime;
    private int lastCount = 0;
    public boolean moving;
    public boolean facingRight = true;
    public float velocityY = 0f;
    public boolean grounded = false;

    public Position getPosition() {
        return position;
    }

    public void load() {
        try {
            File file = new File(Game.ASSET_PATH + File.separator + "animation" + File.separator + "kim");
            File[] tiles = file.listFiles();
            int count = 0;
            for (File f : tiles) {
                if (f.getName().endsWith(".png")) {
                    count++;
                }
            }
            animationImagesRight = new Image[count];
            animationImagesLeft = new Image[count];
            count = 0;
            for (File f : tiles) {
                if (f.getName().endsWith(".png")) {
                    animationImagesRight[count] = new Image(f.getAbsolutePath());
                    animationImagesLeft[count] = animationImagesRight[count++].getFlippedCopy(true, false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        lastRenderTime = System.currentTimeMillis();
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    // Walk cycle is 500ms
    public void render(Graphics g) {
        long delta = System.currentTimeMillis() - lastRenderTime;
        if (delta > (500f / (float) animationImagesRight.length)) {
            lastRenderTime = System.currentTimeMillis();
            lastCount++;
        }
        Image[] cache = facingRight ? animationImagesRight : animationImagesLeft;
        g.drawImage(cache[moving ? lastCount : 0], position.getX(), position.getY());
        if (lastCount == animationImagesRight.length - 1) {
            lastCount = 0;
        }
    }
}
