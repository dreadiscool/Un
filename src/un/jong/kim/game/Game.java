package un.jong.kim.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import un.jong.kim.game.gui.Gui;
import un.jong.kim.game.gui.MainMenuGui;

import java.util.ArrayList;

public class Game extends BasicGame {

    private static Game instance;

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static final String ASSET_PATH = "C:\\Users\\dreadiscool\\IdeaProjects\\KimJongUn\\assets\\";

    public static Game getInstance() {
        return instance;
    }

    private ArrayList<Gui> guiList = new ArrayList<Gui>();
    private Player thePlayer = new Player();

    public Game() {
        super("Kim Jong Un - An Epic Adventure - HackRU 2014 - Paras Jha");
    }

    public void init(GameContainer container) {
        thePlayer.load();
        submitGui(new MainMenuGui());
    }

    public void update(GameContainer container, int status) { }

    public void render(GameContainer container, Graphics g) {
        Gui[] guis;
        synchronized (guiList) {
            guis = guiList.toArray(new Gui[guiList.size()]);
        }
        for (Gui gui : guis) {
            gui.render(g);
        }
    }

    public void removeGui(Class clazz) {
        synchronized (guiList) {
            for (int i = 0; i < guiList.size(); i++) {
                if (guiList.get(i).getClass() == clazz) {
                    guiList.get(i).destroy();
                    guiList.remove(i);
                    i--;
                }
            }
        }
    }

    public void submitGui(Gui gui) {
        synchronized (guiList) {
            guiList.add(gui);
        }
    }

    public static void main(String[] args) {
        try {
            instance = new Game();
            AppGameContainer container = new AppGameContainer(instance);
            container.setDisplayMode(Game.WIDTH, Game.HEIGHT, false);
            container.setVSync(true);
            container.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Player getPlayer() {
        return thePlayer;
    }
}
