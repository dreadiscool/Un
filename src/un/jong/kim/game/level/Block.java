package un.jong.kim.game.level;

public class Block {

    private int x;
    private int y;
    private int width;
    private int height;

    public Block(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getCollisionSide(Block b) {
        boolean topLeftCollides = b.x > this.x && b.x < (this.x + this.width) && b.y > this.y && b.y < (this.y + this.height);
        if (topLeftCollides)
            return 1;
        boolean topRightCollides = (b.x + b.width) > this.x && (b.x + b.width) < (this.x + this.width) && b.y > this.y && b.y < (this.y + this.height);
        if (topRightCollides)
            return 2;
        boolean bottomRightCollides = (b.x + b.width) > this.x && (b.x + b.width) < (this.x + this.width) && (b.y + b.height) > this.y && (b.y + b.height) < (this.y + this.height);
        if (bottomRightCollides)
            return 3;
        boolean bottomLeftCollides = b.x > this.x && b.x < (this.x + this.width) && (b.y + b.height) > this.y && (b.y + b.height) < (this.y + this.height);
        if (bottomLeftCollides)
            return 4;
        return 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isColliding(Block b) {
        return getCollisionSide(b) > 0;
    }

    public String toString() {
        return "{x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + "}";
    }
}
