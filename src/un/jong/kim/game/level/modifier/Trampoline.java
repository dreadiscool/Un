package un.jong.kim.game.level.modifier;

import un.jong.kim.game.Game;
import un.jong.kim.game.level.Block;

public class Trampoline extends Block {

    private float bounce;

    public Trampoline(float bounce, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.bounce = bounce;
    }

    public int getCollisionSide(Block b) {
        int status = super.getCollisionSide(b);
        if (status == 3 || status == 4) {
            Game.getInstance().getPlayer().velocityY = -4f;
            Game.getInstance().getPlayer().grounded = false;
        }
        return status;
    }

    public float getBounce() {
        return bounce;
    }
}
