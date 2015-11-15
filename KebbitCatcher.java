package org.parabot.minecraftftw.scripts.KebbitCatcher;
import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.Area;
import org.rev317.min.api.wrappers.Npc;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

@ScriptManifest(author = "minecraftftw", category = Category.AGILITY, description = "Hunts most Kebbits!", name = "KebbitCatcher", servers = {"PKHonor"}, version = 1.0)
public class KebbitCatcher extends Script implements MessageListener {

    // Hunting variables
    public static ArrayList<Strategy> strategies = new ArrayList<>();
    private boolean messageRecieved = false;
    private static int[] inspectIDs = {
            // Snow kebbits (1)
//            19419,19420,19421,19423,19424,19640,
//            19641,

            // Desert kebbits (13)
//            19389,19390,19391,19392,19393,19394,
//            19395,19396,19397,19398,19399,19400,
//            19401,19402, 19552,19553,

            19440, 19362, 19363, 19379, 19372, 19380,
            19378, 19374, 19378, 19373, 19377, 19375,
            19376, 19365, 19364, 19363, 19361, 19357
            // woodland razor-back kebbits (49)
//            19356,19357,19358,19359,19360,19361,
//            19362,19363,19364,19365,19372,19373,
//            19374,19375,19376,19377,19378,19379,
//            19380,19365,19364,19438,19439,19440
    };

    @Override
    public void messageReceived(MessageEvent m)
    {
        if (m.getMessage().contains("You fail to catch")) {
            messageRecieved = true;
        } else if (m.getMessage().contains("You successfully catch")) {
            messageRecieved = true;
        }
    }

    @Override
    public boolean onExecute() {
        strategies.add(new Relog());
        strategies.add(new AntiRandoms());
        strategies.add(new Action());
        provide(strategies);
        return true;
    }

    public class Action implements Strategy {

        public boolean isCatching = false;
        SceneObject plant;


        public SceneObject getObject(int id) {
            try {
                SceneObject[] nearestObject = SceneObjects.getNearest(id);
                if (nearestObject[0] != null) {
                    return nearestObject[0];
                } else {
                    return null;
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        public boolean activate() {
            return !isCatching;
        }

        @Override
        public void execute() {
            for (int inspectID : inspectIDs) {
                if (isLoggedIn()) {
                    try {
                        messageRecieved = false;
                        Time.sleep(200, 300);
                        while (Players.getMyPlayer().getAnimation() != -1) {
                            Time.sleep(500);
                        }
                        plant = getObject(inspectID);
                        plant.interact(0);
                        Time.sleep(() -> messageRecieved, 5000);
                    } catch (Exception ignored) {

                    }
                } else {
                    break;
                }
            }
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

    private boolean isLoggedIn() {
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
        }

    }
}
