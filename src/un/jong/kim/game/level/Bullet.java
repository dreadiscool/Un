package un.jong.kim.game.level;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import un.jong.kim.game.Game;
import un.jong.kim.game.Position;

public class Bullet {

    private static final float DELTA_DISTANCE_PS = 1.5f;

    private Position position;
    private double theta;

    public Bullet(int charX, int charY) {
        this.position = new Position(charX, charY);
        System.out.println("Mouse Position: " + Mouse.getX() + ", " + Mouse.getY());
        int diffX = charX - Mouse.getX();
        int diffY = charY - Mouse.getY();
        theta = Math.atan2(diffY, diffX);
        System.out.println("Degrees: " + Math.toDegrees(theta));
    }

    public boolean render(Graphics g, long delta) {
        double addX = Math.cos(theta) * delta * DELTA_DISTANCE_PS;
        double addY = Math.sin(theta) * delta * DELTA_DISTANCE_PS;
        position.update((int) (position.getX() + addX), (int) (position.getY() + addY));
        if (position.getX() < 0 || position.getX() > Game.WIDTH) {
            return true;
        } else if (position.getY() < 0 || position.getY() > Game.HEIGHT) {
            return true;
        }
        g.setColor(Color.yellow);
        g.fillOval(position.getX(), position.getY(), 5f, 5f);
        return false;
    }
}
