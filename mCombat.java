package org.parabot.minecraftftw.scripts.mCombat;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.GroundItem;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.Npc;
import org.rev317.min.api.wrappers.Tile;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@ScriptManifest(author = "minecraftftw", category = Category.COMBAT, description = "A combat script that features an easy to use GUI, looting items, auto eating most foods and loading/saving of your previously entered data. Works on PkHonor, not tested on IKOV", name = "mCombat", servers = {
        "PkHonor"
}, version = 1.00)
public class mCombat extends Script {

    org.parabot.environment.api.utils.Timer timer = new org.parabot.environment.api.utils.Timer();
    BotGUI startPanel = new BotGUI();

    private final ArrayList< Strategy > strategies = new ArrayList < Strategy > ();

    private static Set< String > npcListStr = new HashSet< String >();
    private static Set < String > lootListStr = new HashSet < String > ();
    private static final int[] NPC_IDS = new int[30];
    private static final int[] LOOT_IDS = new int[30];

    private final String[] SKILLS = { "ATTACK", "STRENGTH", "DEFENSE", "RANGE", "MAGIC"};
    private final int[] START_LEVELS = new int[5];
    private final int[] START_EXP = new int[5];
    private final int[] CURRENT_LEVELS = new int[5];
    private final int[] CURRENT_EXP = new int[5];

    private static String status = "Starting.";
    private static int itemsLooted = 0;
    private final long startTime = System.currentTimeMillis();
    private String skillBeingTrained = "";
    private String previousSkill = "";

    @Override
    public boolean onExecute() {
        strategies.add(new Eat());
        strategies.add(new Loot());
        strategies.add(new Fight());

        startPanel.setVisible(true);

        while (NPC_IDS[0] == 0) {
            Time.sleep(50);
        }

        startPanel.dispose();

        getStartSkillData();
        getCurrentSkillData();

        provide(strategies);
        return true;
    }

    private final String getPrevSkill() {
        return skillBeingTrained;
    }

    private String getSkillTraining() {

        previousSkill = getPrevSkill();

        for (int i = 0; i < SKILLS.length; i++) {
            final Skill combatSkill = Skill.valueOf(SKILLS[i]);
            final int currentXp = combatSkill.getExperience();
            final int xpGained = currentXp - CURRENT_EXP[i];
            if (xpGained > 0) {
                return SKILLS[i];
            }
        }

        return previousSkill;
    }

    private String getGainedLevels(String skillName) {
        for (int i = 0; i < SKILLS.length; i++) {
            if (skillName.equals(SKILLS[i])) {
                final Skill combatSkill = Skill.valueOf(SKILLS[i]);
                final int inFightLevel = combatSkill.getRealLevel();
                if (inFightLevel <= 99) {
                    final int levelsGained = inFightLevel - START_LEVELS[i];
                    return String.valueOf(levelsGained);
                }
            }
        }
        return "0";
    }

    private void getStartSkillData() {
        for (int i = 0; i < SKILLS.length; i++) {
            final Skill combatSkill = Skill.valueOf(SKILLS[i]);
            START_LEVELS[i] = combatSkill.getRealLevel();
            START_EXP[i] = combatSkill.getExperience();
        }
    }

    private void getCurrentSkillData() {
        for (int i = 0; i < SKILLS.length; i++) {
            final Skill combatSkill = Skill.valueOf(SKILLS[i]);
            CURRENT_LEVELS[i] = combatSkill.getRealLevel();
            CURRENT_EXP[i] = combatSkill.getExperience();
        }
    }


    private String getXpRemaining(String skillName) {
        for (int i = 0; i < SKILLS.length; i++) {
            if (skillName.equals(SKILLS[i])) {
                final Skill combatSkill = Skill.valueOf(SKILLS[i]);
                final int xpRemaining = combatSkill.getRemaining();
                if (CURRENT_LEVELS[i] <= 99) {
                    return String.valueOf(xpRemaining);
                }
            }
        }

        return "0";
    }

    private String getTotalXpGained() {
        int totalXpGained = 0;

        for (int i = 0; i < SKILLS.length; i++) {
            final Skill combatSkill = Skill.valueOf(SKILLS[i]);
            final int currentXp = combatSkill.getExperience();
            final int xpGained = currentXp - START_EXP[i];
            totalXpGained += xpGained;
        }

        return String.valueOf(totalXpGained);
    }

    private String getXpGained(String skillName) {
        int skillXpGained = 0;

        for (int i = 0; i < SKILLS.length; i++) {
            if (skillName.equals(SKILLS[i])) {
                final Skill combatSkill = Skill.valueOf(SKILLS[i]);
                final int currentXp = combatSkill.getExperience();
                final int xpGained = currentXp - START_EXP[i];
                skillXpGained += xpGained;
            }
        }

        return String.valueOf(skillXpGained);
    }

    private String getXpPerHour(String skillName) {
        if (!previousSkill.equals(skillBeingTrained)) {
            timer.restart();
        }
        final int xpGained = Integer.parseInt(getXpGained(skillName));
        return String.valueOf(timer.getPerHour(xpGained));
    }


    public class Fight implements Strategy {
        Npc enemy;

        @Override
        public boolean activate() {
            enemy = nextEnemy();
            return enemy != null;
        }

        @Override
        public void execute() {
            getCurrentSkillData();
            try {
                if (!Players.getMyPlayer().isInCombat() && enemy.getInteractingCharacter() != Players.getMyPlayer()) {
                    status = "Attacking enemy.";
                    enemy.interact(1);
                    Time.sleep(() -> Players.getMyPlayer().isInCombat() || enemy.isInCombat() && !Players.getMyPlayer().isInCombat() || isGainingXp() && enemy.isInCombat(), 4750);

                    if (!Players.getMyPlayer().isInCombat() && !enemy.isInCombat()) {
                        enemy.interact(1);
                        Time.sleep(() -> enemy == null || Players.getMyPlayer().isInCombat() || enemy.isInCombat() && !Players.getMyPlayer().isInCombat() || isGainingXp() && enemy.isInCombat(), 4750);
                    }

                    if (enemy.isInCombat() && isGainingXp() || enemy.getInteractingCharacter() == Players.getMyPlayer()) {
                        status = "Fighting enemy.";
                        Time.sleep(() -> enemy == null || enemy.getHealth() == 0, 10000);
                    } else {
                        enemy = nextEnemy();
                    }

                } else if (enemy.getInteractingCharacter() == Players.getMyPlayer()) {
                    status = "Fighting enemy.";
                    Time.sleep(() -> enemy == null || enemy.getHealth() == 0, 10000);
                }
            } catch (Exception ignored) {

            }
        }

        private Npc nextEnemy() {
            try {
                Npc[] npcList = Npcs.getNearest(NPC_IDS);
                for (Npc toFight : npcList) {
                    Tile toFightLoc = toFight.getLocation();
                    if (toFightLoc.isReachable() && toFight.getInteractingCharacter() == Players.getMyPlayer() && toFight.getHealth() > 0) {
                        return toFight;
                    }
                    else if (toFightLoc.isReachable() && !toFight.isInCombat()) {
                        return toFight;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in nextEnemy");
                Time.sleep(2500);
                nextEnemy();
            }
            return null;
        }
    }

    public class Eat implements Strategy {

        private int currentHP = 0;
        private int levelOfHP;
        private int eatPoints;

        private final int[] foodList = {
                15273, 7943, 7944, 7947, 6970, 386, 14617, 3145, 3148, 392, 380, 330, 316, 374, 362
        };

        @Override
        public boolean activate() {
            if (Players.getMyPlayer().isInCombat()) {
                currentHP = Players.getMyPlayer().getHealth();
            }

            levelOfHP = Skill.HITPOINTS.getRealLevel();
            eatPoints = (levelOfHP / 10) * 6;

            return currentHP != 0 && currentHP <= eatPoints;
        }

        @Override
        public void execute() {
            for (Item food : Inventory.getItems(foodList)) {
                currentHP = Players.getMyPlayer().getHealth();
                if (currentHP != 0 && Players.getMyPlayer().isInCombat()) {

                    eatPoints = (levelOfHP / 10) * 6;

                    if (food != null && currentHP <= eatPoints) {
                        status = "Eating.";
                        Menu.sendAction(74, food.getId() - 1, food.getSlot(), 3214);
                        Time.sleep(3700, 3850);
                    }
                }
            }
        }
    }

    public class Loot implements Strategy {
        GroundItem toLoot;

        @Override
        public boolean activate() {
            toLoot = nextLoot();
            return toLoot != null && Inventory.getCount() != 28;
        }

        @Override
        public void execute() {
            try {
                status = "Looting.";
                final int startCount = Inventory.getCount(true, toLoot.getId());
                toLoot.take();
                Time.sleep(700, 750);
                Time.sleep(() -> Inventory.getCount(true, toLoot.getId()) > startCount, 2500);
                itemsLooted++;
            } catch (Exception ignored) {

            }
        }
    }

    private GroundItem nextLoot() {
        try {
            GroundItem[] lootList = GroundItems.getNearest(LOOT_IDS);
            for (GroundItem toLoot : lootList) {
                Tile toLootLoc = toLoot.getLocation();
                if (toLootLoc.isWalkable() && Calculations.distanceTo(toLootLoc) < 12) {
                    return toLoot;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in nextEnemy");
            Time.sleep(2500);
            nextLoot();
        }
        return null;
    }

    private static boolean isLoggedIn() {
        try {
            return SceneObjects.getNearest().length > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isGainingXp() {
        for (int i = 0; i < SKILLS.length; i++) {
            final Skill combatSkill = Skill.valueOf(SKILLS[i]);
            final int currentXp = combatSkill.getExperience();
            final int xpGained = currentXp - CURRENT_EXP[i];
            if (xpGained > 0) {
                return true;
            }
        }
        return false;
    }

    public static class BotGUI extends javax.swing.JFrame {

        /**
         * Creates new form BotGUI
         */
        public BotGUI() {
            initComponents();
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */

        DefaultListModel fightListModel = new DefaultListModel();
        DefaultListModel nearestNpcListModel = new DefaultListModel();
        DefaultListModel lootListModel = new DefaultListModel();
        DefaultListModel savesListModel = new DefaultListModel();

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

            tabPane = new javax.swing.JTabbedPane();
            mainPanel = new javax.swing.JPanel();
            npcTitle = new javax.swing.JLabel();
            loadedScrollPane = new javax.swing.JScrollPane();
            loadedList = new javax.swing.JList(nearestNpcListModel);
            moveButton = new javax.swing.JButton();
            removeButton = new javax.swing.JButton();
            loadedLabel = new javax.swing.JLabel();
            fightLabel = new javax.swing.JLabel();
            reloadButton = new javax.swing.JButton();
            npcFightListScrollPane = new javax.swing.JScrollPane();
            fightList = new javax.swing.JList(fightListModel);
            startButton = new javax.swing.JButton();
            npcSubtitle = new javax.swing.JLabel();
            lootPanel = new javax.swing.JPanel();
            lootTitle = new javax.swing.JLabel();
            lootScrollPane = new javax.swing.JScrollPane();
            lootList = new javax.swing.JList(lootListModel);
            lootItemField = new javax.swing.JTextField();
            addLootButton = new javax.swing.JButton();
            lootRemoveButton = new javax.swing.JButton();
            lootSubtitle = new javax.swing.JLabel();
            lootedPanelLabel = new javax.swing.JLabel();
            loadPanel = new javax.swing.JPanel();
            loadTitle = new javax.swing.JLabel();
            loadSubtitle = new javax.swing.JLabel();
            saveNameField = new javax.swing.JTextField();
            saveButton = new javax.swing.JButton();
            fileNameLabel = new javax.swing.JLabel();
            savesScrollPanel = new javax.swing.JScrollPane();
            savesList = new javax.swing.JList(savesListModel);
            savesFoundLabel = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setLocation(new java.awt.Point(450, 225));
            setTitle("mCombat - Version 1.00");

            npcTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
            npcTitle.setText("Choose NPC's to fight:");

            loadedList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            loadedList.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
            loadedList.setForeground(new java.awt.Color(0, 0, 0));
            loadedList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            loadedScrollPane.setViewportView(loadedList);

            moveButton.setText("Move to fight list");
            moveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    moveButtonActionPerformed(evt);
                }
            });

            removeButton.setText("Remove from fight list");
            removeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    removeButtonActionPerformed(evt);
                }
            });

            loadedLabel.setText("Loaded NPC's in your area:");

            fightLabel.setText("NPC's that the bot will fight:");

            reloadButton.setText("Reload NPCs");
            reloadButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    reloadButtonActionPerformed(evt);
                }
            });

            fightList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            fightList.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
            fightList.setForeground(new java.awt.Color(0, 0, 0));
            fightList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            npcFightListScrollPane.setViewportView(fightList);

            startButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
            startButton.setText("Start!");
            startButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startButtonActionPerformed(evt);
                }
            });

            npcSubtitle.setText("Press the start button to start the script when you are ready.");

            javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
            mainPanel.setLayout(mainPanelLayout);
            mainPanelLayout.setHorizontalGroup(
                    mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(mainPanelLayout.createSequentialGroup()
                                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(loadedLabel)
                                                            .addComponent(loadedScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                            .addComponent(reloadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(moveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(startButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(npcFightListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(fightLabel)))
                                            .addGroup(mainPanelLayout.createSequentialGroup()
                                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(npcTitle)
                                                            .addComponent(npcSubtitle))
                                                    .addGap(0, 0, Short.MAX_VALUE)))
                                    .addContainerGap())
            );
            mainPanelLayout.setVerticalGroup(
                    mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(npcTitle)
                                    .addGap(5, 5, 5)
                                    .addComponent(npcSubtitle)
                                    .addGap(18, 18, 18)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fightLabel)
                                            .addComponent(loadedLabel))
                                    .addGap(18, 18, 18)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(npcFightListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                            .addComponent(loadedScrollPane))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(moveButton)
                                            .addComponent(removeButton))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(reloadButton)
                                            .addComponent(startButton))
                                    .addContainerGap())
            );

            tabPane.addTab("Main", mainPanel);

            lootTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
            lootTitle.setText("Enter items to loot:");

            lootList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            lootScrollPane.setViewportView(lootList);

            addLootButton.setText("Add item(s)");
            addLootButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addLootButtonActionPerformed(evt);
                }
            });

            lootRemoveButton.setText("Remove item");
            lootRemoveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    lootRemoveButtonActionPerformed(evt);
                }
            });

            lootSubtitle.setText("Integer ID's only! (Use debug to display id's)");

            lootedPanelLabel.setText("Items that will be looted:");

            javax.swing.GroupLayout lootPanelLayout = new javax.swing.GroupLayout(lootPanel);
            lootPanel.setLayout(lootPanelLayout);
            lootPanelLayout.setHorizontalGroup(
                    lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(lootPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(lootPanelLayout.createSequentialGroup()
                                                    .addComponent(lootSubtitle)
                                                    .addGap(247, 247, 247))
                                            .addGroup(lootPanelLayout.createSequentialGroup()
                                                    .addGroup(lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(lootPanelLayout.createSequentialGroup()
                                                                    .addComponent(lootItemField)
                                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(addLootButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(lootPanelLayout.createSequentialGroup()
                                                                    .addComponent(lootedPanelLabel)
                                                                    .addGap(0, 0, Short.MAX_VALUE))
                                                            .addComponent(lootScrollPane)
                                                            .addComponent(lootRemoveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(lootTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addContainerGap())))
            );
            lootPanelLayout.setVerticalGroup(
                    lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(lootPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(lootTitle)
                                    .addGap(2, 2, 2)
                                    .addComponent(lootSubtitle)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lootItemField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(addLootButton, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(lootedPanelLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lootScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lootRemoveButton)
                                    .addGap(20, 20, 20))
            );

            tabPane.addTab("Loot", lootPanel);

            loadTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
            loadTitle.setText("Load/Save custom presets");

            loadSubtitle.setText("Load or save data you have entered previously.");

            saveButton.setText("Save");
            saveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    saveButtonActionPerformed(evt);
                }
            });

            fileNameLabel.setText("File name:");

            savesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            savesList.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    saveListActionPerformed(evt);
                }
            });
            savesScrollPanel.setViewportView(savesList);

            savesFoundLabel.setText("Saves found in current folder (double click one to load):");

            javax.swing.GroupLayout loadPanelLayout = new javax.swing.GroupLayout(loadPanel);
            loadPanel.setLayout(loadPanelLayout);
            loadPanelLayout.setHorizontalGroup(
                    loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(loadPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(loadPanelLayout.createSequentialGroup()
                                                    .addComponent(saveNameField)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(savesScrollPanel)
                                            .addGroup(loadPanelLayout.createSequentialGroup()
                                                    .addGroup(loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(loadTitle)
                                                            .addComponent(loadSubtitle)
                                                            .addComponent(fileNameLabel)
                                                            .addComponent(savesFoundLabel))
                                                    .addGap(0, 219, Short.MAX_VALUE)))
                                    .addContainerGap())
            );
            loadPanelLayout.setVerticalGroup(
                    loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(loadPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(loadTitle)
                                    .addGap(5, 5, 5)
                                    .addComponent(loadSubtitle)
                                    .addGap(18, 18, 18)
                                    .addComponent(fileNameLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(saveNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(savesFoundLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(savesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(33, 33, 33))
            );

            tabPane.addTab("Load/Save", loadPanel);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(tabPane)
                                    .addContainerGap())
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(tabPane)
                                    .addContainerGap())
            );

            pack();
            addNpcs();
            loadSaves();
        }// </editor-fold>

        private void reloadButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_reloadButtonActionPerformed
            addNpcs();
        } //GEN-LAST:event_reloadButtonActionPerformed

        private void moveButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_moveButtonActionPerformed
            Object npcItem = loadedList.getSelectedValue();
            if (!fightListModel.contains(npcItem)) {
                fightListModel.addElement(npcItem);
            }
        } //GEN-LAST:event_moveButtonActionPerformed

        private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_removeButtonActionPerformed
            int removeItem = fightList.getSelectedIndex();
            try {
                fightListModel.remove(removeItem);
            } catch (ArrayIndexOutOfBoundsException e) {
                Time.sleep(50);
            }
        } //GEN-LAST:event_removeButtonActionPerformed

        private void startButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_startButtonActionPerformed
            if (isLoggedIn()) {
                for (int i = 0; i < fightList.getModel().getSize(); i++) {
                    String fightListString = fightListModel.getElementAt(i).toString();
                    if (fightListString != null) {
                        int startPosition = fightListString.indexOf("- (") + "- (".length();
                        int endPosition = fightListString.indexOf(")", startPosition);
                        NPC_IDS[i] = Integer.parseInt(fightListString.substring(startPosition, endPosition));
                    }
                }
                for (int l = 0; l < lootList.getModel().getSize(); l++) {
                    String lootListString = lootListModel.getElementAt(l).toString();
                    if (lootListString != null) {
                        LOOT_IDS[l] = Integer.parseInt(lootListString);
                    }
                }
            } else {
                String text = "Please login first!";
                JOptionPane.showMessageDialog(this, text, "Script error", JOptionPane.ERROR_MESSAGE);
            }
        } //GEN-LAST:event_startButtonActionPerformed

        private void addLootButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_addLootButtonActionPerformed
            String idsText = lootItemField.getText();
            String idsTextEach[] = null;
            if (idsText != null) {
                if (idsText.contains(", ")) {
                    idsTextEach = idsText.split(",\\s");
                } else if (idsText.contains(",")) {
                    idsTextEach = idsText.split(",");
                } else if (!idsText.contains(",")) {
                    idsTextEach = new String[] {
                            idsText
                    };
                }
                int[] ids = new int[0];
                if (idsTextEach != null) {
                    ids = new int[idsTextEach.length];
                }
                for (int i = 0; i < ids.length; i++) {
                    try {
                        ids[i] = Integer.parseInt(idsTextEach[i]);
                    } catch (NumberFormatException e) {
                        String text = "Your input contained a letter/word, remove it to add items to loot list.";
                        JOptionPane.showMessageDialog(this, text, "Input error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                for (int id : ids) {
                    if (id != 0) {
                        lootListStr.add(String.valueOf(id));
                    }
                }
                if (lootList.getModel().getSize() > 0) {
                    lootListModel.clear();
                }
                lootListStr.forEach(lootListModel::addElement);
                lootItemField.setText("");
            }
        } //GEN-LAST:event_addLootButtonActionPerformed

        private void lootRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_lootRemoveButtonActionPerformed
            int removeItem = lootList.getSelectedIndex();
            String ofRemoveItem = lootList.getSelectedValue().toString();
            try {
                lootListModel.remove(removeItem);
            } catch (ArrayIndexOutOfBoundsException e) {
                Time.sleep(50);
            }
            for (String st: lootListStr) {
                if (st.equals(ofRemoveItem)) {
                    lootListStr.remove(st);
                }
            }
        } //GEN-LAST:event_lootRemoveButtonActionPerformed

        private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
            String fileName = saveNameField.getText();
            String npcSaves[] = new String[fightList.getModel().getSize()];
            String lootSaves[] = new String[lootList.getModel().getSize()];

            for (int i = 0; i < fightList.getModel().getSize(); i++) {
                String fightListString = fightListModel.getElementAt(i).toString();
                npcSaves[i] = fightListString;
            }

            for (int i = 0; i < lootList.getModel().getSize(); i++) {
                String lootListString = lootListModel.getElementAt(i).toString();
                lootSaves[i] = lootListString;
            }

            File saveFile = new File(fileName + ".mCmb");

            if (!saveFile.exists() && npcSaves.length > 0 || lootSaves.length > 0) {

                try {
                    saveFile.createNewFile();

                    FileWriter fw = new FileWriter(saveFile.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    if (npcSaves.length > 0) {
                        for (String npcData : npcSaves) {
                            bw.write("npc: " + npcData + "\n");
                        }
                    }
                    if (lootSaves.length > 0) {
                        for (String lootData : lootSaves) {
                            bw.write("loot: " + lootData + "\n");
                        }
                    }
                    bw.close();

                    String text = "Saved file in current folder as: " + fileName + ".mCmb";
                    JOptionPane.showMessageDialog(this, text, "Successful", JOptionPane.INFORMATION_MESSAGE);
                    saveNameField.setText("");
                    loadSaves();
                } catch (IOException ex) {
                    String text = "Couldn't save file!";
                    JOptionPane.showMessageDialog(this, text, "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                String text = "You haven't entered any Loot id's or added any Npc's to the list to save!";
                JOptionPane.showMessageDialog(this, text, "Save error", JOptionPane.ERROR_MESSAGE);
            }

        }

        private void saveListActionPerformed(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                try {
                    BufferedReader lineReader = new BufferedReader(new FileReader(savesList.getSelectedValue().toString()));
                    String line;
                    fightListModel.clear();
                    lootListModel.clear();
                    while ((line = lineReader.readLine()) != null) {
                        if (line.contains("npc: ")) {
                            int npcStartPosition = line.indexOf("npc: ") + "npc: ".length();
                            String nameOfNpc = line.substring(npcStartPosition);
                            fightListModel.addElement(nameOfNpc);
                        } else if (line.contains("loot: ")) {
                            int lootStartPosition = line.indexOf("loot: ") + "loot: ".length();
                            String lootID = line.substring(lootStartPosition);
                            lootListModel.addElement(lootID);
                        }
                    }
                    lineReader.close();
                    String text = "Loaded data! Press start in the main tab to begin.";
                    JOptionPane.showMessageDialog(this, text, "Saves loaded", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    String text = "Couldn't load save!";
                    JOptionPane.showMessageDialog(this, text, "Load error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void loadSaves() {
            try {
                savesListModel.clear();

                String nameOfDir = System.getProperty("user.dir");
                File dir = new File(nameOfDir);
                FilenameFilter textFilter = new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".mcmb");
                    }
                };

                File[] allFiles = dir.listFiles(textFilter);
                for (File f : allFiles) {
                    if (f.isFile()) {
                        savesListModel.addElement(f.getName());
                    }
                }
            } catch (Exception e) {
                String text = "Could not load files in current folder.";
                JOptionPane.showMessageDialog(this, text, "Save error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void addNpcs() {
            loadNpc();
            nearestNpcListModel.clear();
            try {
                if (NPC_IDS.length > 0) {
                    for (int i = 0; i < NPC_IDS.length; i++) {
                        NPC_IDS[i] = 0;
                    }
                }
                npcListStr.forEach(nearestNpcListModel::addElement);
            } catch (Exception e) {
                String text = "Could not add nearest Npc's to list.";
                JOptionPane.showMessageDialog(this, text, "Npc error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private ArrayList < String > npcListNames = new ArrayList < String > ();

        private void loadNpc() {
            try {
                Npc[] loadList = Npcs.getNearest();
                npcListStr.clear();
                if (npcListNames.size() == 0) {
                    try {
                        URL npcListUrl = new URL("https://raw.githubusercontent.com/Minecraftftw1/Scripts/master/npcList.txt");
                        BufferedReader lineReader = new BufferedReader(new InputStreamReader(npcListUrl.openStream()));
                        String line;
                        while ((line = lineReader.readLine()) != null) {
                            npcListNames.add(line);
                        }
                        lineReader.close();
                    } catch (Exception e) {
                        String text = "Could not download Npc List.";
                        JOptionPane.showMessageDialog(this, text, "Download error", JOptionPane.ERROR_MESSAGE);
                        loadNpc();
                    }
                }
                if (npcListNames.size() > 0) {
                    try {
                        for (String line : npcListNames) {
                            for (Npc aLoadList : loadList) {
                                final int id = aLoadList.getDef().getId();
                                final int idEndPosition = line.indexOf(" -");
                                if (line.substring(0, idEndPosition).equals(String.valueOf(id))) {
                                    int nameStartPosition = line.indexOf("- ") + "- ".length();
                                    String nameOfNpc = line.substring(nameStartPosition);
                                    npcListStr.add(nameOfNpc + " - (" + id + ")");
                                }
                            }
                        }
                    } catch (Exception e) {
                        loadNpc();
                        // String text = "Possible error loading nearest Npc's.";
                        // JOptionPane.showMessageDialog(this, text, "Npc error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ignored) {

            }
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton addLootButton;
        private javax.swing.JLabel fightLabel;
        private javax.swing.JList fightList;
        private javax.swing.JLabel fileNameLabel;
        private javax.swing.JPanel loadPanel;
        private javax.swing.JLabel loadSubtitle;
        private javax.swing.JLabel loadTitle;
        private javax.swing.JLabel loadedLabel;
        private javax.swing.JList loadedList;
        private javax.swing.JScrollPane loadedScrollPane;
        private javax.swing.JTextField lootItemField;
        private javax.swing.JList lootList;
        private javax.swing.JPanel lootPanel;
        private javax.swing.JButton lootRemoveButton;
        private javax.swing.JScrollPane lootScrollPane;
        private javax.swing.JLabel lootSubtitle;
        private javax.swing.JLabel lootTitle;
        private javax.swing.JLabel lootedPanelLabel;
        private javax.swing.JPanel mainPanel;
        private javax.swing.JButton moveButton;
        private javax.swing.JScrollPane npcFightListScrollPane;
        private javax.swing.JLabel npcSubtitle;
        private javax.swing.JLabel npcTitle;
        private javax.swing.JButton reloadButton;
        private javax.swing.JButton removeButton;
        private javax.swing.JButton saveButton;
        private javax.swing.JTextField saveNameField;
        private javax.swing.JLabel savesFoundLabel;
        private javax.swing.JList savesList;
        private javax.swing.JScrollPane savesScrollPanel;
        private javax.swing.JButton startButton;
        private javax.swing.JTabbedPane tabPane;
        // End of variables declaration//GEN-END:variables
    }
}
