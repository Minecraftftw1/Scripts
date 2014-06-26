package Minecraftftw.hunter;

import org.parabot.environment.scripts.framework.*;
import org.parabot.environment.scripts.*;
import org.parabot.environment.*;
import java.util.ArrayList;

import org.parabot.environment.scripts.framework.*;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.*;
import org.parabot.environment.api.utils.*;
import org.parabot.environment.input.*;
import org.rev317.min.Loader;
import java.util.Random;

/**
 * Made by: Minecraftftw
 * Copyright (c) $today.year.
 */

@ScriptManifest(author = "Minecraftftw", category = Category.OTHER, description = "Hunts kingly implings and banks them for great profit. Start script with butterfly net equipped and at least 1m in inventory.", name = "Kingly Catcher", servers = { "PKhonor" }, version = 1.0)
public class KinglyCatcher extends Script{

	private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();

    @Override
    public boolean onExecute() {
        strategies.add(new teleToMan());
        strategies.add(new buyJars());
        strategies.add(new teleToImplings());
        strategies.add(new action());
        strategies.add(new teleToBank());
        strategies.add(new bank());
    	provide(strategies);
        return true;  
    }

    public Npc getNpc(int id) {
        try {
            Npc[] nearestNpcs = Npcs.getNearest(id);
            if (nearestNpcs[0] != null) {
                return nearestNpcs[0];
            } else {
                return null;
            }
        } catch (Exception e) {
            
        }
        return null;
    }

    public SceneObject getObject(int id) {
        try {
            SceneObject[] nearestObject = SceneObjects.getNearest(id);
            if (nearestObject[0] != null) {
                if (Calculations.distanceTo(nearestObject[0].getLocation()) < 12) {
                    return nearestObject[0];
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            
        }
        return null;
    }

    public class teleToMan implements Strategy {

        private Npc man;

        private final int emptyJars = 11261;
        private final int fullJars = 17293;

        private final int dImp = 6054;
        private final int kImp = 6343;

        @Override
        public boolean activate() {
            man = getNpc(5112);
            if (Inventory.getCount(emptyJars) == 0 && Inventory.getCount(fullJars) == 0 && man == null) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("teleToMan activated");

            Menu.sendAction(1027, 91, 0, 0, 1);
            Time.sleep(750);
            Menu.sendAction(315, 91, 0, 1170, 1);
            Time.sleep(750);
            Menu.sendAction(315, 91, 0, 2498, 1);
            Time.sleep(750);
            Menu.sendAction(315, 91, 0, 2498, 1);
            Time.sleep(750);
            Menu.sendAction(315, 91, 0, 2495, 1);
            Time.sleep(750);
            Menu.sendAction(1024, 318177280, 198, 29, 1);
            Time.sleep(3700);

        }
    }

    public class buyJars implements Strategy {

        private Npc man;

        private final int emptyJars = 11261;
        
        private final int fullJars = 17293;

        private final int dImp = 6054;
        private final int kImp = 6343;

        public Npc getNpc(int id) {
            try {
                Npc[] nearestNpcs = Npcs.getNearest(id);
                if (nearestNpcs[0] != null) {
                    return nearestNpcs[0];
                } else {
                    return null;
                }
            } catch (Exception e) {
                
            }
            return null;
        }

        @Override
        public boolean activate() {
            man = getNpc(5112);
            if (Inventory.getCount(emptyJars) == 0 && Inventory.getCount(fullJars) == 0 && man != null) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("buyJars activated");

            man.interact(0);
            Time.sleep(1250);
            Menu.sendAction(315, 522, 246, 2482, 1);
            Time.sleep(1150);
            Menu.sendAction(53, 11260, 5, 3900, 3);
            Time.sleep(1150);
            Menu.sendAction(200, 10037, 21, 3902, 1);
            Time.sleep(1150);

        }
    }

    public class teleToImplings implements Strategy {

        private Npc imp;

        private final int emptyJars = 11261;
        private final int fullJars = 17293;

        private final int dImp = 6054;
        private final int kImp = 6343;

        @Override
        public boolean activate() {
            imp = getNpc(kImp);
            if (Inventory.getCount(emptyJars) == 27 && imp == null && Players.getMyPlayer().getAnimation() == -1) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("teleToImplings activated");

            Menu.sendAction(20, 522, 0, 0, 3);
            Time.sleep(750);
            Menu.sendAction(315, 522, 274, 2483, 1);
            Time.sleep(750);
            Menu.sendAction(315, 522, 274, 2495, 1);
            Time.sleep(3500);

            Mouse.getInstance().click(555, 79, true);
            Time.sleep(2250);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

            Mouse.getInstance().click(555, 79, true);
            Time.sleep(2250);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

            Mouse.getInstance().click(579, 29, true);
            Time.sleep(2250);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

            Mouse.getInstance().click(613, 34, true);
            Time.sleep(2250);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

            Mouse.getInstance().click(613, 34, true);
            Time.sleep(2250);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

        }
    }

    public class action implements Strategy {

        private Npc imp;
        private final Skill HUNTER = Skill.CONSTRUCTION;
        private boolean isCatching = false;

        private final int dImp = 6054;
        private final int kImp = 6343;

        public boolean checkCatching() {
            long start_time = System.currentTimeMillis();
            long wait_time = 2000;
            long end_time = start_time + wait_time;

            while (System.currentTimeMillis() < end_time) {
                if (Players.getMyPlayer().getAnimation() != -1) {
                    Time.sleep(350);
                    return true;
                }
            }
            return false;
        }
        @Override
        public boolean activate() {
            isCatching = checkCatching();
            imp = getNpc(kImp);
            return imp != null && !isCatching && Inventory.getCount(11261) != 0;
        }

        @Override
        public void execute() {
            System.out.println("Interacting");
            imp.interact(0);
            Time.sleep(1000);
        }
    }

    public class teleToBank implements Strategy {

        private final int emptyJars = 11261;
        private final int fullJars = 17293;

        private SceneObject bank1;

        @Override
        public boolean activate() {
            bank1 = getObject(2213);
            if (bank1 == null && Inventory.getCount(fullJars) == 27) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("teleToBank activated");

            Menu.sendAction(1027, 1520, 222, 34, 1);
            Time.sleep(750);
            Menu.sendAction(315, 1520, 222, 1195, 1);
            Time.sleep(4450);

        }
    }

    public class bank implements Strategy {

        private final int emptyJars = 11261;
        private final int fullJars = 17293;

        private SceneObject bank;

        @Override
        public boolean activate() {
            bank = getObject(2213);
            if (bank != null && Inventory.getCount(fullJars) == 27) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("bank activated");

            Time.sleep(550);
            bank = getObject(2213);

            while (Loader.getClient().getOpenInterfaceId() != 23350) {
                System.out.println("Opening bank.");
                bank.interact(0);
                Time.sleep(1050);
            }
            while (Loader.getClient().getOpenInterfaceId() == 23350) {
                int impCount = Inventory.getCount(fullJars);

                if (impCount > 0) {
                    impCount = Inventory.getCount(fullJars);
                    System.out.println("Banking jars.");
                    Menu.sendAction(432, 17292, 5, 5064, 2);
                    Time.sleep(550);
                }

                impCount = Inventory.getCount(fullJars);

                if (impCount == 0) {
                    Menu.sendAction(200, 14596, 0, 5384, 1);
                    Time.sleep(250);
                }
            }
        }
    }
}
