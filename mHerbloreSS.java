package org.parabot.minecraftftw.mHerbloreSS.main;

/*
 * Copyright minecraftftw 2014.
 */

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.soulsplit.api.methods.*;
import org.soulsplit.api.methods.Menu;
import org.soulsplit.api.wrappers.Item;
import org.soulsplit.api.wrappers.SceneObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@ScriptManifest(author = "minecraftftw", category = Category.HERBLORE, description = "AIO Herblore script.", name = "mHerbloreSS", servers = {
        "Soulsplit"
}, version = 0.76)

public class mHerbloreSS extends Script implements Paintable {

    HerbTimer timer = new HerbTimer();

    private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();

    private static Potions myPot;

    private static final int WATER_VIAL = 228;
    
    private static int unfMade = 0;
    private static int finMade = 0;

    @Override
    public boolean onExecute() {

        Gui main = new Gui();

        while (myPot == null) {
            Time.sleep(new SleepCondition() {
                @Override
                public boolean isValid() {
                    return myPot != null;
                }
            }, 350);
        }

        main.dispose();

        strategies.add(new OpenBank());
        provide(strategies);
        return true;
    }

    //START: Code generated using Enfilade's Easel
    private final Color color1 = new Color(77, 255, 0, 79);
    private final Color color2 = new Color(0, 0, 0);
    private final Color color3 = new Color(255, 255, 255);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 0, 16);
    private final Font font2 = new Font("Arial", 0, 12);

    final int NEW_Y = 57;

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        g.setColor(color1);
        g.fillRect(546, 208 + NEW_Y, 190, 163);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(546, 208 + NEW_Y, 190, 163);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("mHerbloreSS", 558, 229 + NEW_Y);
        g.setFont(font2);
        g.drawString("Run time: " + timer.toString(), 558, 260 + NEW_Y);
        g.drawString("Unf Potions made: " + timer.formatNumb(timer.getUnfMade()) + " ( " + timer.formatNumb(timer.getPerHour((timer.getUnfMade()))) + " /h)", 558, 284 + NEW_Y);
        g.drawString("Fin Potions made: " + timer.formatNumb(timer.getFinMade()) + " ( " + timer.formatNumb(timer.getPerHour((timer.getFinMade()))) + " /h)", 558, 308 + NEW_Y);
        g.drawString("Xp Gained: " + timer.formatNumb(timer.getXpGained()) + " ( " + timer.formatNumb(timer.getPerHour((timer.getXpGained()))) + " /h)", 558, 332 + NEW_Y);
        g.drawString("Levels Gained: " + timer.formatNumb(timer.getLevelsGained()) + " ( " + timer.formatNumb(timer.getPerHour((timer.getLevelsGained()))) + " /h)", 560, 356 + NEW_Y);
    }
    //END: Code generated using Enfilade's Easel

    public class OpenBank implements Strategy {

        SceneObject nearestBank;

        @Override
        public boolean activate() {

            nearestBank = null;

            for (SceneObject i : SceneObjects.getNearest(2213, 21301)) {
                nearestBank = i;
                return nearestBank != null;
            }

            return nearestBank != null;
        }

        @Override
        public void execute() {
            if (!Bank.isOpen()) {
                nearestBank.interact(0);
                Time.sleep(new SleepCondition() {
                    @Override
                    public boolean isValid() {
                        return Bank.isOpen();
                    }
                }, 4250);
            }

            if (Bank.isOpen()) {
                if (myPot.isOther()) {
                    if (myPot.hasOtherIngredients()) {
                        myPot.makeOther();
                        Bank.depositAll();
                    }
                    if (bankHasOtherIngredients() && !myPot.hasOtherIngredients()) {
                        System.out.println("Withdrawing other ingredients.");
                        Time.sleep(200, 270);
                        Menu.sendAction(867, myPot.getIngredId() - 1, getBankSlot(myPot.getIngredId()), 5382);
                        Time.sleep(new SleepCondition() {
                            @Override
                            public boolean isValid() {
                                return Inventory.getCount(myPot.getIngredId()) > 0;
                            }
                        }, 1300);
                        Menu.sendAction(867, myPot.getHerbId() - 1, getBankSlot(myPot.getHerbId()), 5382);
                        Time.sleep(new SleepCondition() {
                            @Override
                            public boolean isValid() {
                                return myPot.hasOtherIngredients();
                            }
                        }, 1000);
                        if (myPot.hasOtherIngredients()) {
                            myPot.makeOther();
                        } else {
                            Bank.depositAll();
                        }
                    } else if (!bankHasOtherIngredients() && !myPot.hasOtherIngredients()) {
                        setState(Script.STATE_STOPPED);
                        String text = "No ingredients found, script stopped.";
                        JOptionPane.showMessageDialog(new JOptionPane(), text, "Script stopped!", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (!myPot.isOther()) {
                    if (myPot.hasUnfinIngredients()) {
                        myPot.makeUnf();
                        Bank.depositAll();
                    } else if (myPot.hasFinIngredients()) {
                        myPot.makeFin();
                        Bank.depositAll();
                    }
                    if (bankHasUnfinIngredients() && !myPot.hasUnfinIngredients()) {
                        System.out.println("Withdrawing unf ingredients.");
                        Time.sleep(200, 270);
                        Menu.sendAction(867, WATER_VIAL - 1, getBankSlot(WATER_VIAL), 5382);
                        Time.sleep(new SleepCondition() {
                            @Override
                            public boolean isValid() {
                                return Inventory.getCount(WATER_VIAL) > 0;
                            }
                        }, 1300);
                        Menu.sendAction(867, myPot.getHerbId() - 1, getBankSlot(myPot.getHerbId()), 5382);
                        Time.sleep(new SleepCondition() {
                            @Override
                            public boolean isValid() {
                                return myPot.hasUnfinIngredients();
                            }
                        }, 1000);
                        if (myPot.hasUnfinIngredients()) {
                            myPot.makeUnf();
                        } else {
                            Bank.depositAll();
                        }
                    } else if (bankHasFinIngredients() && !bankHasUnfinIngredients() && !myPot.hasFinIngredients()) {
                        System.out.println("Withdrawing fin ingredients.");
                        Time.sleep(200, 270);
                        Menu.sendAction(867, myPot.getIngredId() - 1, getBankSlot(myPot.getIngredId()), 5382);
                        Time.sleep(new SleepCondition() {
                            @Override
                            public boolean isValid() {
                                return Inventory.getCount(myPot.getIngredId()) > 0;
                            }
                        }, 1300);
                        Menu.sendAction(867, myPot.getUnfinId() - 1, getBankSlot(myPot.getUnfinId()), 5382);
                        Time.sleep(new SleepCondition() {
                            @Override
                            public boolean isValid() {
                                return myPot.hasFinIngredients();
                            }
                        }, 1000);
                        if (myPot.hasFinIngredients()) {
                            myPot.makeFin();
                        } else {
                            Bank.depositAll();
                        }
                    } else if (!bankHasFinIngredients() && !myPot.hasFinIngredients() || !bankHasUnfinIngredients() && !myPot.hasUnfinIngredients()) {
                        setState(Script.STATE_STOPPED);
                        String text = "No ingredients found, script stopped.";
                        JOptionPane.showMessageDialog(new JOptionPane(), text, "Script stopped!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private boolean bankHasUnfinIngredients() {
        return getBankStackSize(WATER_VIAL) > 0 && getBankStackSize(myPot.getHerbId()) > 0;
    }

    private boolean bankHasFinIngredients() {
        return getBankStackSize(myPot.getIngredId()) > 0 && getBankStackSize(myPot.getUnfinId()) > 0;
    }

    private boolean bankHasOtherIngredients() {
        return getBankStackSize(myPot.getIngredId()) > 0 && getBankStackSize(myPot.getHerbId()) > 0;
    }

    private int getBankStackSize(int id) {
        long[] stacks = Bank.getBankStacks();
        for (int i = 0; i < stacks.length; i++) {
            if (i == getBankSlot(id)) {
                return (int) stacks[i];
            }
        }
        return -1;
    }

    private int getBankSlot(int id) {

        long[] bankIds = Bank.getBankItemIDs();
        int[] intBankIds = new int[bankIds.length];

        for (int i = 0; i < bankIds.length; i++) {
            intBankIds[i] = (int) bankIds[i];
        }

        for (int i = 0; i < intBankIds.length; i++) {
            if (intBankIds[i] == id) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isLoggedIn() {
        SceneObject[] nearestObjects = SceneObjects.getNearest();
        return nearestObjects.length > 0;
    }

    public class HerbTimer extends Timer {

        private final int START_LEVEL = Skill.HERBLORE.getRealLevel();
        private final int START_EXP = Skill.HERBLORE.getExperience();
        private int CURRENT_LEVEL = Skill.HERBLORE.getRealLevel();
        private int CURRENT_EXP = Skill.HERBLORE.getExperience();

        public HerbTimer() {
        }

        public String formatNumb(int number) {
            String numberString = String.valueOf(number);
            if (Integer.parseInt(numberString) < 1000) {
                return numberString;
            } else if (Integer.parseInt(numberString) > 1000 && Integer.parseInt(numberString) < 10000) {
                return numberString.charAt(0) + "." + numberString.charAt(1) + "K";
            } else if (Integer.parseInt(numberString) > 10000 && Integer.parseInt(numberString) < 100000) {
                return numberString.substring(0, 2) + "." + numberString.charAt(2) + "K";
            } else if (Integer.parseInt(numberString) > 100000 && Integer.parseInt(numberString) < 1000000) {
                return numberString.substring(0, 3) + "." + numberString.charAt(3) + "K";
            } else if (Integer.parseInt(numberString) > 1000000 && Integer.parseInt(numberString) < 10000000) {
                return numberString.charAt(0) + "." + numberString.charAt(1) + "M";
            } else if (Integer.parseInt(numberString) > 10000000 && Integer.parseInt(numberString) < 100000000) {
                return numberString.substring(0, 2) + "." + numberString.charAt(2) + "M";
            } else if (Integer.parseInt(numberString) > 100000000 && Integer.parseInt(numberString) < 1000000000) {
                return numberString.substring(0, 3) + "." + numberString.charAt(3) + "M";
            } else if (Long.valueOf(numberString) > 1000000000L && Long.valueOf(numberString) < 10000000000L) {
                return numberString.charAt(0) + "." + numberString.charAt(1) + "B";
            } else if (Long.valueOf(numberString) > 10000000000L && Long.valueOf(numberString) < 100000000000L) {
                return numberString.substring(0, 2) + "." + numberString.charAt(2) + "B";
            } else {
                return numberString;
            }
        }
        
        public int getUnfMade() {
            return unfMade;
        }
        
        public int getFinMade() {
            return finMade;
        }

        public int getLevelsGained() {
            CURRENT_LEVEL = Skill.HERBLORE.getRealLevel() - START_LEVEL;
            return CURRENT_LEVEL;
        }

        public int getXpGained() {
            CURRENT_EXP = Skill.HERBLORE.getExperience() - START_EXP;
            return CURRENT_EXP;
        }
    }

    public enum Potions {

        ATTACK(250, 222, 92, 122, 1),
        ANTIPOISON(252, 236, 94, 176, 5),
        STRENGTH(254, 226, 96, 116, 12),
        SERUM_207(254, 593, 96, 3411, 15),
        RESTORE(256, 224, 98, 128, 22),
        BLAMISH(256, 1582, 98, 1583, 25),
        ENERGY(256, 1976, 98, 3011, 26),
        DEFENCE(258, 240, 100, 134, 30),
        AGILITY(2999, 2153, 3003, 3035, 34),
        COMBAT(256, 9737, 98, 9742, 36),
        PRAYER(258, 232, 100, 140, 38),
        SUMMONING(16773, 16741, 16777, 16763, 40),
        CRAFTING(14855, 5005, 14857, 14841, 42),
        SUPER_ATTACK(260, 222, 102, 146, 45),
        SUPER_ANTIPOISON(260, 236, 102, 182, 48),
        FISHING(262, 232, 104, 152, 50),
        SUPER_ENERGY(262, 2971, 104, 3019, 52),
        HUNTER(262, 10112, 104, 10001, 53),
        SUPER_STRENGTH(264, 226, 106, 158, 55),
        FLETCHING(14855, 11526, 14857, 14849, 58),
        WEAPON_POISON(264, 242, 106, 188, 60),
        SUPER_RESTORE(3001, 224, 3005, 3027, 63),
        SUPER_DEFENCE(266, 240, 108, 164, 65),
        ANTIFIRE(2482, 242, 2484, 2455, 69),
        RANGING(268, 246, 110, 170, 72),
        MAGIC(2482, 3139, 2484, 3043, 76),
        ZAMORAK_BREW(270, 248, 112, 190, 78),
        SARADOMIN_BREW(2999, 6694, 3003, 6688, 81),

        SUPER_ANTIFIRE(2455, 4622, 15306, 85),
        EXTREME_ATTACK(146, 262, 14578, 88),
        EXTREME_STRENGTH(158, 268, 14590, 89),
        EXTREME_DEFENCE(164, 2482, 14582, 90),
        EXTREME_MAGIC(3043, 9595, 14602, 91),
        EXTREME_RANGING(170, 8303, 14586, 92),
        SUPER_PRAYER(140, 6811, 14598, 94),
        PRAYER_RENEWAL(21625, 21623, 14678, 96);

        private int herbId;
        private int ingredId;
        private int unfinId;
        private int finId;
        private int levelReq;
        private boolean isOther;

        Potions(int herbId, int ingredId, int finId, int levelReq) {
            this.herbId = herbId;
            this.ingredId = ingredId;
            this.finId = finId;
            this.levelReq = levelReq;
            this.isOther = true;
        }

        Potions(int herbId, int ingredId, int unfinId, int finId, int levelReq) {
            this.herbId = herbId;
            this.ingredId = ingredId;
            this.unfinId = unfinId;
            this.finId = finId;
            this.levelReq = levelReq;
            this.isOther = false;
        }

        public int getHerbId() {
            return herbId;
        }

        public int getIngredId() {
            return ingredId;
        }

        public int getUnfinId() {
            return unfinId;
        }

        public int getFinId() {
            return finId;
        }

        public int getLevelReq() {
            return levelReq;
        }

        public boolean isOther() {
            return isOther;
        }

        public boolean hasUnfinIngredients() {
            return Inventory.getCount(WATER_VIAL) > 0 && Inventory.getCount(herbId) > 0;
        }

        public boolean hasFinIngredients() {
            return Inventory.getCount(ingredId) > 0 && Inventory.getCount(unfinId) > 0;
        }

        public boolean hasOtherIngredients() {
            return Inventory.getCount(herbId) > 0 && Inventory.getCount(ingredId) > 0;
        }

        public void makeUnf() {
            Item herbItem;
            Item[] waterItems = Inventory.getItems(WATER_VIAL);
            try {
                for (Item i : waterItems) {
                    herbItem = Inventory.getItems(herbId)[Inventory.getItems(herbId).length - 1];
                    Menu.sendAction(447, WATER_VIAL - 1, i.getSlot(), 3214);
                    Time.sleep(130, 150);
                    Menu.sendAction(870, herbId - 1, herbItem.getSlot(), 3214);
                    Time.sleep(130, 150);
                }
                unfMade += Inventory.getCount(unfinId);
                Time.sleep(300, 400);
                Bank.depositAll();
            } catch (ArrayIndexOutOfBoundsException e) {
                Bank.depositAll();
            }
        }

        public void makeFin() {
            Item unfPotItem;
            Item[] ingredItems = Inventory.getItems(ingredId);
            try {
                for (Item i : ingredItems) {
                    unfPotItem = Inventory.getItems(unfinId)[Inventory.getItems(unfinId).length - 1];
                    Menu.sendAction(447, ingredId - 1, i.getSlot(), 3214);
                    Time.sleep(130, 150);
                    Menu.sendAction(870, unfinId - 1, unfPotItem.getSlot(), 3214);
                    Time.sleep(130, 150);
                }
                finMade += Inventory.getCount(finId);
                Time.sleep(300, 400);
                Bank.depositAll();
            } catch (ArrayIndexOutOfBoundsException e) {
                Bank.depositAll();
            }
        }

        public void makeOther() {
            Item herbItem;
            Item[] ingredItems = Inventory.getItems(ingredId);
            try {
                for (Item i : ingredItems) {
                    herbItem = Inventory.getItems(herbId)[Inventory.getItems(herbId).length - 1];
                    Menu.sendAction(447, ingredId - 1, i.getSlot(), 3214);
                    Time.sleep(130, 150);
                    Menu.sendAction(870, herbId - 1, herbItem.getSlot(), 3214);
                    Time.sleep(130, 150);
                }
                finMade += Inventory.getCount(finId);
                Time.sleep(300, 400);
                Bank.depositAll();
            } catch (ArrayIndexOutOfBoundsException e) {
                Bank.depositAll();
            }
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
        }
    }

    public class Gui {

        private JFrame mainFrame;
        private JButton startButton;
        private JComboBox potionList;
        private JLabel chooseLabel;
        private JPanel mainPanel;

        public Gui() {
            setUpComponents();
        }

        private void setUpComponents() {
            mainPanel = new JPanel();
            mainPanel.setSize(280, 142);
            GridLayout mainLayout = new GridLayout(3, 1);
            mainLayout.setVgap(13);
            mainPanel.setLayout(mainLayout);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(13, 13, 13, 13));

            chooseLabel = new JLabel("Select the potion you wish to make:", JLabel.CENTER);
            potionList = new JComboBox();
            for (mHerbloreSS.Potions i : mHerbloreSS.Potions.values()) {
                potionList.addItem(i.toString() + " Potion - Level " + i.getLevelReq());
            }
            startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (isLoggedIn()) {
                        try {
                            myPot = mHerbloreSS.Potions.values()[potionList.getSelectedIndex()];
                        } catch (Exception err) {
                            Time.sleep(300, 400);
                            err.printStackTrace();
                        }
                    } else if (!isLoggedIn()) {
                        String text = "Please login first.";
                        JOptionPane.showMessageDialog(new JOptionPane(), text, "Script error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            mainPanel.add(chooseLabel);
            mainPanel.add(potionList);
            mainPanel.add(startButton);

            mainFrame = new JFrame("Choose your potion - Version 0.76");
            mainFrame.add(mainPanel);
            mainFrame.setBounds(523, 334, 300, 170);
            mainFrame.setResizable(false);
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        public void dispose() {
            this.mainFrame.dispose();
        }

        public boolean isVisible() {
            return this.mainFrame.isVisible();
        }
    }
}
