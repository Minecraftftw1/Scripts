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

@ScriptManifest(author = "Minecraftftw", category = Category.OTHER, description = "Hunts kingly implings and banks them for great profit. Start script with butterfly net equipped and at least 1m in inventory. Keep camera facing north.", name = "Kingly Catcher", servers = { "PKhonor" }, version = 1.1)
public class KinglyCatcher extends Script{

    // Variables
    private final int KINGLY_IMPLING = 6343;
    private final int EMPTY_JARS = 11261;
    private final int FULL_JARS = 17293;
    private final int BANK_OBJ = 2213;
    private final int BANK_INTERFACE = 23350;
    private final Skill HUNTER = Skill.CONSTRUCTION;

    private int impsCaught = 0;

	private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();

    @Override
    public boolean onExecute() {
        strategies.add(new TeleToMan());
        strategies.add(new BuyJars());
        strategies.add(new TeleToImplings());
        strategies.add(new Action());
        strategies.add(new TeleToBank());
        strategies.add(new Bank());
    	provide(strategies);
        return true;  
    }

    @Override
    public void onFinish() {
        System.out.println("The script caught: " + impsCaught + " Kingly Implings! Thanks for using the script :)");
    }

    private Npc getNearestNpc(int id) {
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

    private SceneObject getNearestObject(int id) {
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

    private Item getInventoryItem(int id) {
        try {
            Item[] itemInInv = Inventory.getItems(id);
            if (itemInInv[0] != null) {
                return itemInInv[0];
            } else {
                return null;
            }
        } catch (Exception e) {
            
        }
        return null;
    }

    public class TeleToMan implements Strategy {

        @Override
        public boolean activate() {
            Npc man = getNearestNpc(5112);
            if (Inventory.getCount(EMPTY_JARS) == 0 && Inventory.getCount(FULL_JARS) == 0 && man == null) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("TeleToMan activated.");

            Menu.sendAction(1027, 91, 0, 0, 1);
            Time.sleep(750, 800);
            Menu.sendAction(315, 91, 0, 1170, 1);
            Time.sleep(750, 800);
            Menu.sendAction(315, 91, 0, 2498, 1);
            Time.sleep(750, 800);
            Menu.sendAction(315, 91, 0, 2498, 1);
            Time.sleep(750, 800);
            Menu.sendAction(315, 91, 0, 2495, 1);
            Time.sleep(750, 800);
            Menu.sendAction(1024, 318177280, 198, 29, 1);
            Time.sleep(3700, 3800);

        }
    }

    public class BuyJars implements Strategy {

        Npc man;

        @Override
        public boolean activate() {
            man = getNearestNpc(5112);
            if (Inventory.getCount(EMPTY_JARS) == 0 && Inventory.getCount(FULL_JARS) == 0 && man != null) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("BuyJars activated.");

            man.interact(0);
            Time.sleep(1250, 1300);
            Menu.sendAction(315, 522, 246, 2482, 1);
            Time.sleep(1150, 1200);
            Menu.sendAction(53, EMPTY_JARS - 1, 5, 3900, 3);
            Time.sleep(1150, 1200);
            Menu.sendAction(200, 10037, 21, 3902, 1);
            Time.sleep(1150, 1200);

        }
    }

    public class TeleToImplings implements Strategy {

        boolean playerIsMoving = false;

        @Override
        public boolean activate() {
            Npc kinglyImp = getNearestNpc(KINGLY_IMPLING);
            if (Inventory.getCount(EMPTY_JARS) == 27 && kinglyImp == null && Players.getMyPlayer().getAnimation() == -1) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("TeleToImplings activated.");

            Menu.sendAction(20, 522, 0, 0, 3);
            Time.sleep(750, 800);
            Menu.sendAction(315, 522, 274, 2483, 1);
            Time.sleep(750, 800);
            Menu.sendAction(315, 522, 274, 2495, 1);
            Time.sleep(3500, 3550);

            Mouse.getInstance().click(555, 79, true);
            Time.sleep(2250, 2300);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

            Mouse.getInstance().click(555, 79, true);
            Time.sleep(2250, 2300);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

            Mouse.getInstance().click(579, 29, true);
            Time.sleep(2250, 2300);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

            Mouse.getInstance().click(613, 34, true);
            Time.sleep(2250, 2300);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

            Mouse.getInstance().click(613, 34, true);
            Time.sleep(2250, 2300);
            while(Players.getMyPlayer().getAnimation() != -1) {
                Time.sleep(400);
            }

        }
    }

    public class Action implements Strategy {

        Npc kinglyImp;
        boolean playerIsCatching = false;

        @Override
        public boolean activate() {
            kinglyImp = getNearestNpc(KINGLY_IMPLING);
            return kinglyImp != null && Players.getMyPlayer().getAnimation() == -1 && Inventory.getCount(EMPTY_JARS) != 0;
        }

        @Override
        public void execute() {

            final int START_IMP_COUNT = Inventory.getCount(FULL_JARS);

            System.out.println("Interacting");

            kinglyImp.interact(0);
            Time.sleep(400, 600);

            long startTime = System.currentTimeMillis();
            long waitTime = 3500;
            long endTime = startTime + waitTime;

            while (System.currentTimeMillis() < endTime) {
                if (Players.getMyPlayer().getAnimation() != -1) {
                    System.out.println("playerIsCatching = true");
                    playerIsCatching = true;
                    break;
                }
            }

            if (playerIsCatching) {
                System.out.println("Player is catching SleepCondition activated.");
                Time.sleep(new SleepCondition() {
                
                    @Override
                    public boolean isValid() {
                        return Inventory.getCount(FULL_JARS) > START_IMP_COUNT;
                    }
                }, 8200);
            }
        }
    }

    public class TeleToBank implements Strategy {

        SceneObject bank;

        @Override
        public boolean activate() {
            bank = getNearestObject(BANK_OBJ);
            if (bank == null && Inventory.getCount(FULL_JARS) == 27) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("TeleToBank activated.");

            Menu.sendAction(1027, 1520, 222, 34, 1);
            Time.sleep(750, 850);
            Menu.sendAction(315, 1520, 222, 1195, 1);
            Time.sleep(4550, 4650);

        }
    }

    public class Bank implements Strategy {

        SceneObject bank;

        @Override
        public boolean activate() {
            bank = getNearestObject(BANK_OBJ);
            if (bank != null && Inventory.getCount(FULL_JARS) == 27) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            System.out.println("Bank activated.");

            Time.sleep(550, 800);

            while (Loader.getClient().getOpenInterfaceId() != BANK_INTERFACE) {
                System.out.println("Opening Bank.");
                bank.interact(0);
                Time.sleep(1000, 1150);
            }
            while (Loader.getClient().getOpenInterfaceId() == BANK_INTERFACE) {
                int impCount = Inventory.getCount(FULL_JARS);

                if (impCount > 0) {
                    impCount = Inventory.getCount(FULL_JARS);
                    Item impInInv = getInventoryItem(FULL_JARS);
                    System.out.println("Banking jars.");
                    Menu.sendAction(432, FULL_JARS - 1, impInInv.getSlot(), 5064, 2);
                    impsCaught += impCount;
                    Time.sleep(550);
                }

                impCount = Inventory.getCount(FULL_JARS);

                if (impCount == 0) {
                    Menu.sendAction(200, 14596, 0, 5384, 1);
                    Time.sleep(250);
                }
            }
        }
    }
}
