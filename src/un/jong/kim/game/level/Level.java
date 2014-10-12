package un.jong.kim.game.level;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import un.jong.kim.game.Game;
import un.jong.kim.game.Player;
import un.jong.kim.game.Position;
import un.jong.kim.game.gui.Gui;
import un.jong.kim.game.level.modifier.Trampoline;

import java.util.ArrayList;

public abstract class Level implements Gui {

    private static final float VERTICAL_VELOCITY_CLAMP = 0.4f;

    protected Image background;
    protected Image scroll;
    protected Map levelMap;
    protected int loadedImages = 0;
    protected boolean allowRender = false;
    protected float levelOffsetX = 0;
    protected float playerScreenOffsetX = 0;
    protected long previousTimestamp;
    protected long lastFireShot = System.currentTimeMillis();
    protected ArrayList<Bullet> bulletsInLevel = new ArrayList<Bullet>();

    public void render(Graphics g) {
        if (!allowRender) {
            renderLoadingScreen(g);
        } else {
            int bWidth = background.getWidth();
            float bOffset = levelOffsetX == 0 ? 0 : (levelOffsetX % bWidth) * -1;
            g.drawImage(background, bOffset, 0);
            g.drawImage(background, bOffset - bWidth, 0);
            g.drawImage(background, bOffset + bWidth, 0);
            g.drawImage(scroll, levelOffsetX * -1, 0f);
            Player player = Game.getInstance().getPlayer();
            long delta = System.currentTimeMillis() - previousTimestamp;
            float deltaX = delta * 0.25f;
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                this.playerScreenOffsetX -= deltaX;
                player.facingRight = false;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                this.playerScreenOffsetX += deltaX;
                player.facingRight = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_W) && player.grounded) {
                player.grounded = false;
                player.velocityY = -3.5f;
            } else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && !player.grounded) {
                if (player.velocityY < 0f)
                    player.velocityY = 0f;
            }
            if (Mouse.isButtonDown(0)) {
                handleGunFiring();
            }
            player.moving = true;
            if (!Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
                player.moving = false;
            }
            if (this.playerScreenOffsetX < 200f) {
                this.playerScreenOffsetX = 200f;
                this.levelOffsetX -= deltaX;
            } else if (this.playerScreenOffsetX > 1000f) {
                this.playerScreenOffsetX = 1000f;
                this.levelOffsetX += deltaX;
            }
            this.levelOffsetX = Math.max(0, this.levelOffsetX);
            applyHorizontalPhysics(); // Constrain left-right movement
            applyGravity();
            applyVerticalPhysics(); // Constrain top-bottom movement
            player.getPosition().update((int) (this.playerScreenOffsetX), player.getPosition().getY());
            renderLevel(g);
            Game.getInstance().getPlayer().render(g);
            for (int i = 0; i < bulletsInLevel.size(); i++) {
                if (bulletsInLevel.get(i).render(g, delta)) {
                    bulletsInLevel.remove(i);
                    i--;
                }
            }
            renderHud(g);
            previousTimestamp = System.currentTimeMillis();
        }
    }

    protected void handleGunFiring() {
        if (System.currentTimeMillis() - lastFireShot > 300) {
            lastFireShot = System.currentTimeMillis();
            bulletsInLevel.add(new Bullet((int) playerScreenOffsetX, Game.getInstance().getPlayer().getPosition().getY()));
        }
    }

    protected void applyHorizontalPhysics() {
        Block collidingBlock = levelMap.collidesWithWorld(this.generatePlayerBlock());
        if (collidingBlock != null) {
            int side = collidingBlock.getCollisionSide(this.generatePlayerBlock());
            if (side == 1 || side == 4) { // (Top or bottom) left corner collides with right side
                int intrude = (collidingBlock.getX() + collidingBlock.getWidth()) - (int) (this.levelOffsetX + this.playerScreenOffsetX);
                this.playerScreenOffsetX += intrude;
            } else if (side == 2 || side == 3) { // (Top or bottom) right corner collides with left side
                int intrude = (int) ((this.levelOffsetX + this.playerScreenOffsetX + 40)) - collidingBlock.getX();
                this.playerScreenOffsetX -= intrude;
            }
        }
    }

    protected void applyGravity() {
        long delta = System.currentTimeMillis() - previousTimestamp;
        Player player = Game.getInstance().getPlayer();
        player.velocityY += 0.015f * delta;
        if (player.velocityY > VERTICAL_VELOCITY_CLAMP)
            player.velocityY = VERTICAL_VELOCITY_CLAMP;
        float velocityToPosition = player.velocityY * delta;
        if (player.velocityY < VERTICAL_VELOCITY_CLAMP * -1f)
            velocityToPosition = delta * VERTICAL_VELOCITY_CLAMP * -1f;
        player.getPosition().update(player.getPosition().getX(), player.getPosition().getY() + (int) Math.floor(velocityToPosition));
    }

    protected void applyVerticalPhysics() {
        Player player = Game.getInstance().getPlayer();
        Position position = player.getPosition();
        Block collidingBlock = levelMap.collidesWithWorld(this.generatePlayerBlock());
        if (collidingBlock != null) {
            player.velocityY = 0f;
            int side = collidingBlock.getCollisionSide(this.generatePlayerBlock());
            if (side == 1 || side == 2) { // Top left or top right collides with bottom
                position.update(position.getX(), collidingBlock.getY() + collidingBlock.getHeight() + 1);
            } else if (side == 3 || side == 4) { // Bottom left or bottom right collides with top
                position.update(position.getX(), collidingBlock.getY() - 50 - 1);
                player.grounded = true;
                player.velocityY = 0f;
                if (collidingBlock instanceof Trampoline) {
                    Game.getInstance().getPlayer().velocityY = ((Trampoline) collidingBlock).getBounce() * -1f;
                    Game.getInstance().getPlayer().grounded = false;
                }
            }
        }
    }

    protected abstract void renderLoadingScreen(Graphics g);

    protected abstract void renderLevel(Graphics g);

    protected void renderHud(Graphics g) {

    }

    protected Block generatePlayerBlock() {
        return new Block((int) (levelOffsetX + playerScreenOffsetX), Game.getInstance().getPlayer().getPosition().getY(), 40, 50);
    }

    protected Image loadAndUpdateImage(Graphics g, String path, double max) {
        try {
            Image i = new Image(path);
            loadedImages++;
            displayProgress(g, max);
            return i;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    protected Music loadAndUpdateMusic(Graphics g, String path, double max) {
        try {
            Music m = new Music(path);
            loadedImages++;
            displayProgress(g, max);
            return m;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    protected void displayProgress(Graphics g, double max) {
        g.fillRect(200, 425, (int) Math.floor((loadedImages / max) * 800), 50);
    }

    public void destroy() { }
}
