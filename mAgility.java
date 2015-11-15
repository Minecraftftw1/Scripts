package org.parabot.minecraftftw.scripts.mAgility;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.accessors.Player;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.Area;
import org.rev317.min.api.wrappers.Npc;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

@ScriptManifest(author = "minecraftftw", category = Category.AGILITY, description = "Completes Wildy and Gnome Agility course. Start script at courses.", name = "mAgility", servers = {"PKHonor"}, version = 1.00)
public class mAgility extends Script {

    public static ArrayList<Strategy> strategies = new ArrayList<>();

    Gui g = new Gui();

    private static final Course wilderness = new Course("Wilderness",
            new Area(new Tile(3007, 3929), new Tile(3007, 3966), new Tile(2987, 3966), new Tile(2987, 3929)),

            new AgilityObject[]{
            new AgilityObject(2283, new Tile(3004, 3950)),
            new AgilityObject(2311, new Tile(3005, 3958)),
            new AgilityObject(2297, new Tile(2996, 3960)),
            new AgilityObject(2328, new Tile(2993, 3945)),
            new AgilityObject(2288, new Tile(2993, 3933))
            }
    );

    private static final Course gnome = new Course("Gnome",
            new Area(new Tile(2470, 3438), new Tile(2471, 3416), new Tile(2490, 3416), new Tile(2490, 3438)),

            new AgilityObject[]{
            new AgilityObject(2295, new Tile(2484, 3437)),
            new AgilityObject(2285, new Tile(2474, 3429)),
            new AgilityObject(2313, new Tile(2473, 3424)),
            new AgilityObject(2312, new Tile(2473, 3420)),
            new AgilityObject(2314, new Tile(2483, 3420)),
            new AgilityObject(2286, new Tile(2485, 3419)),
            new AgilityObject(154, new Tile(2484, 3427))
            }
    );

    private final static Course[] courses = {wilderness, gnome};

    @Override
    public boolean onExecute() {

        g.setVisible(true);

        while(g.isVisible()) {
            Time.sleep(50);
        }

        for (Course c: courses) {
            if (c.getName().equals(g.getSelectedCourse())) {
                c.setIsCompleting(true);
            }
        }

        g.dispose();

        strategies.add(new Relog());
        strategies.add(new AntiRandoms());
        strategies.add(new TeleToCourse());
        strategies.add(new Action());
        provide(strategies);
        return true;
    }

    private static class Course {

        private String name = "";
        private AgilityObject[] objects;
        private Area area;
        private boolean justArrived = true;
        private boolean isCompleting = false;

        public Course(String n, Area a, AgilityObject[] o) {
            this.name = n + " course";
            this.area = a;
            this.objects = o;
        }

        public void complete() {
            for (AgilityObject o: objects) {
                if (isLoggedIn()) {
                    try {
                        Time.sleep(200, 250);
                        while (Players.getMyPlayer().getAnimation() != -1 && isAtCourse() && !o.isReady()) {
                            Time.sleep(150, 200);
                        }
                        if (isAtCourse()) {
                            if (o.isReady() || justArrived) {
                                o.interact();
                                o.sleep();
                            }
                        }
                    } catch (Exception ignored) {
                        System.out.println("Error.");
                    }
                }
            }
        }

        public String getName() {
            return name;
        }

        public boolean hasJustArrived() {
            return justArrived;
        }

        public void setJustArrived(boolean justArrived) {
            this.justArrived = justArrived;
        }

        public boolean isCompleting() {
            return isCompleting;
        }

        public void setIsCompleting(boolean isCompleting) {
            this.isCompleting = isCompleting;
        }

        private boolean isAtCourse() {
            return area.contains(Players.getMyPlayer().getLocation().getX(), Players.getMyPlayer().getLocation().getY());
        }

        public void teleport() {
            switch (name) {
                case "Wilderness course":
                    System.out.println("Teleporting to Wilderness course.");
                    Menu.sendAction(1027, 91, 0, 0, 1);
                    Time.sleep(750, 800);
                    Menu.sendAction(315, 75, 0, 1164, 1);
                    Time.sleep(3000, 3250);
                    interactObject(6282);
                    Time.sleep(1500, 1800);
                    while (Players.getMyPlayer().getAnimation() != -1) {
                        Time.sleep(150, 200);
                    }
                    Menu.sendAction(315, 130, 12, 2497, 1);
                    Time.sleep(750, 800);
                    Menu.sendAction(315, 4882432, 240, 208, 2497);
                    Time.sleep(750, 800);
                    interactObject(2309);
                    Time.sleep(4000, 4500);
                    justArrived = true;
                    break;
                case "Gnome course":
                    System.out.println("Teleporting to Gnome course.");
                    Menu.sendAction(1027, 91, 0, 0, 1);
                    Time.sleep(750, 800);
                    Menu.sendAction(315, 590, 374, 1170, 1);
                    Time.sleep(750, 800);
                    Menu.sendAction(315, 590, 374, 2498, 1);
                    Time.sleep(750, 800);
                    Menu.sendAction(315, 590, 374, 2497, 1);
                    Time.sleep(750, 800);
                    Time.sleep(4200);
                    justArrived = true;
                    break;
            }
        }
    }

    private static void interactObject(int id) {
        SceneObject obj = null;
        for (SceneObject o : SceneObjects.getAllSceneObjects()) {
            if (o.getId() == id) {
                obj = o;
                break;
            }
        }
        if (obj != null) {
            Menu.sendAction(502, obj.getHash(), obj.getLocalRegionX(), obj.getLocalRegionY(), 3);
        }
    }

    private static Course getCompleting() {
        for (Course c: courses) {
            if (c.isCompleting()) {
                return c;
            }
        }
        // this should never ever be reached so null checking isn't needed in script
        return null;
    }

    private static class AgilityObject {
        private SceneObject obstacle;
        private int obstacleId;
        private Tile obstacleTile;

        public AgilityObject(int id, Tile tile) {
            this.obstacleId = id;
            this.obstacleTile = tile;
        }

        public boolean isReady() {
            return obstacleTile.getX() == Players.getMyPlayer().getLocation().getX() && obstacleTile.getY() == Players.getMyPlayer().getLocation().getY();
        }

        public void interact() {
            try {
                if (getCompleting().hasJustArrived() && getCompleting().getName().equalsIgnoreCase("wilderness course")) {
                    System.out.println("Activating wilderness course first time");
                    obstacle = SceneObjects.getClosest(2288);
                    obstacle.interact(0);
                    getCompleting().setJustArrived(false);
                } else if (getCompleting().hasJustArrived() && getCompleting().getName().equalsIgnoreCase("gnome course")) {
                    System.out.println("Activating gnome course first time");
                    obstacleId = 2295;
                    cInteract();
                    getCompleting().setJustArrived(false);
                } else {
                    cInteract();
                }
            } catch (Exception ignored) {

            }
        }

        private void cInteract() {
            SceneObject obj = null;
            for (SceneObject o : SceneObjects.getAllSceneObjects()) {
                if (o.getId() == obstacleId) {
                    obj = o;
                    break;
                }
            }
            if (obj != null) {
                Menu.sendAction(502, obj.getHash(), obj.getLocalRegionX(), obj.getLocalRegionY(), 3);
            }
        }

        public void sleep() {
            Time.sleep(() -> Players.getMyPlayer().getAnimation() == -1, 400);
        }
    }

    public class TeleToCourse implements Strategy {

        @Override
        public boolean activate() {
            return !getCompleting().isAtCourse();
        }

        @Override
        public void execute() {
            getCompleting().teleport();
        }
    }

    public class Action implements Strategy {

        @Override
        public boolean activate() {
            return true;
        }

        @Override
        public void execute() {
            getCompleting().complete();
        }
    }

    public class Relog implements Strategy
    {
        @Override
        public boolean activate()
        {
            return !isLoggedIn();
        }

        @Override
        public void execute()
        {
            Logger.addMessage("Relogging", true);

            Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);

            Time.sleep(() -> {
                return isLoggedIn();
            }, 5000);

            if (isLoggedIn())
            {
                Logger.addMessage("Waiting after relog", true);

                Time.sleep(4000);
            }
        }
    }

    private static boolean isLoggedIn() {
        try {
            return SceneObjects.getNearest().length > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public class AntiRandoms implements Strategy {

        private final int[] RANDOMS = { 410, 1091, 3117, 3022, 3351, 409 };
        private final Area BOBS_ISLAND = new Area(new Tile(2511, 4765), new Tile(
                2511, 4790), new Tile(2542, 4790), new Tile(2542, 4765));

        @Override
        public boolean activate() {
            for (Npc n : Npcs.getNearest(RANDOMS)) {
                if (n.getLocation().distanceTo() < 3)
                    return true;
            }
            return false;
        }

        public void execute() {
            Time.sleep(750);
            Npc[] n = Npcs.getNearest(RANDOMS);
            System.out.println("There is a random nearby!");
            Time.sleep(750);
            if (n[0].getDef().getId() == 1091) {
                SceneObject[] portal = SceneObjects.getNearest(8987);

                for (SceneObject aPortal : portal)
                    if (BOBS_ISLAND.contains(Players.getMyPlayer().getLocation())) {
                        final SceneObject portal2 = aPortal;
                        portal2.interact(0);
                        Time.sleep(() -> portal2.getLocation().distanceTo() < 2, 7500);
                        portal2.interact(0);
                        Time.sleep(1000);
                    } else
                        break;
                System.out.println("Bob's Island has been completed");
            } else if (n[0].getDef().getId() == 3022
                    || n[0].getDef().getId() == 3351 || n[0].getDef().getId() == 40) {
                System.exit(0);
                System.out.println("A mod called a Genie random onto you.\n"
                        + "The client was closed to protect your account.");
            } else {
                n[0].interact(0);
                Time.sleep(1500);
                System.out.println("Sandwich lady/Old man random has been completed");
            }

            getCompleting().setJustArrived(true);
        }
    }

    public class Gui extends JFrame {

        private JPanel mainPanel;
        private GridLayout mainLayout;
        private JLabel title;
        private JComboBox courseBox;
        private DefaultComboBoxModel courseModel;
        private JButton startButton;
        private JSeparator topSeparator;
        private JSeparator middleSeparator;
        private JSeparator bottomSeparator;

        public Gui() {
            initComponents();
        }

        public String getSelectedCourse() {
            return courseBox.getSelectedItem().toString();
        }

        private void initComponents() {
            mainPanel = new JPanel();
            mainLayout = new GridLayout(6, 0, 2, 1);
            topSeparator = new JSeparator();
            middleSeparator = new JSeparator();
            bottomSeparator = new JSeparator();
            title = new JLabel("Select an Agility course:", SwingConstants.CENTER);
            courseBox = new JComboBox();
            courseModel = new DefaultComboBoxModel();
            for (Course c : courses) {
                courseModel.addElement(c.getName());
            }
            startButton = new JButton("Start script");
            startButton.addActionListener(e -> setVisible(false));

            mainPanel.setLayout(mainLayout);
            mainPanel.add(topSeparator);
            mainPanel.add(title);
            courseBox.setModel(courseModel);
            mainPanel.add(courseBox);
            mainPanel.add(middleSeparator);
            mainPanel.add(startButton);
            mainPanel.add(bottomSeparator);

            add(mainPanel);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setBounds(450, 225, 300, 180);
            setTitle("mAgility - Version 1.00");
            setResizable(false);
        }
    }
}
