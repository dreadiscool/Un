package un.jong.kim.game.level;

import un.jong.kim.game.Game;
import un.jong.kim.game.level.modifier.Trampoline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Map {

    public String name;
    private ArrayList<Block> platforms = new ArrayList<Block>();

    public Map(String name) {
        this.name = name;
    }

    public void load() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(Game.ASSET_PATH + name + ".lvl"));
            String in;
            while ((in = br.readLine()) != null) {
                if (in.trim().equals("")) {
                    continue;
                }
                String[] doubleCoords = in.split(" ");
                String[] coordPos1String;
                String[] coordPos2String;
                Block block;
                if (doubleCoords[0].startsWith("T")) {
                    System.out.println("Adding a trampoline");
                    coordPos1String = doubleCoords[1].split(",");
                    coordPos2String = doubleCoords[2].split(",");
                    block = new Trampoline(Float.parseFloat(doubleCoords[0].substring(1)), Integer.parseInt(coordPos1String[0]), Integer.parseInt(coordPos1String[1]), Integer.parseInt(coordPos2String[0]), Integer.parseInt(coordPos2String[1]));
                } else {
                    coordPos1String = doubleCoords[0].split(",");
                    coordPos2String = doubleCoords[1].split(",");
                    block = new Block(Integer.parseInt(coordPos1String[0]), Integer.parseInt(coordPos1String[1]), Integer.parseInt(coordPos2String[0]), Integer.parseInt(coordPos2String[1]));
                }

                platforms.add(block);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public Block collidesWithWorld(Block block) {
        for (Block b : platforms) {
            if (b.isColliding(block)) {
                return b;
            }
        }
        return null;
    }
}
