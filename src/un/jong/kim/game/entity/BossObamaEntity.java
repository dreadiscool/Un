package un.jong.kim.game.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import un.jong.kim.game.Position;

public class BossObamaEntity extends Entity {

    private Image image;
    private Position location = new Position(12000, 50);
    private boolean activated;
    private long timestamp;

    public BossObamaEntity(Image image) {
        this.image = image;
    }

    public void activateObama() {
        this.activated = true;
        this.timestamp = System.currentTimeMillis();
    }

    public void render(Graphics g, long delta) {

    }
}
