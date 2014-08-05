package Minecraftftw.mCombat;

import javax.swing.DefaultListModel;

import org.parabot.environment.scripts.framework.*;
import org.parabot.environment.scripts.*;
import org.parabot.environment.*;
import java.util.ArrayList;

import org.parabot.environment.scripts.framework.*;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.*;
import org.parabot.environment.api.utils.*;
import org.parabot.environment.input.*;
import org.parabot.environment.api.interfaces.Paintable;
import org.rev317.min.Loader;
import java.util.Random;

import java.util.ArrayList;
import javax.swing.*;
import java.util.*;

import java.io.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

@ScriptManifest(author = "MinecraftFtw", category = Category.COMBAT, description = "AIO Combat script.", name = "mCombat", servers = {
    "Ikov"
}, version = 1.6)
public class mCombat extends Script implements Paintable {

    private final ArrayList < Strategy > strategies = new ArrayList < Strategy > ();

    private static Set < String > npcListStr = new HashSet < String > ();
    private static Set < String > lootListStr = new HashSet < String > ();
    private static final int[] npcIds = new int[30];
    private static final int[] lootIds = new int[30];

    private static String status = "Starting.";
    private final int START_HP_LEVEL = Skill.HITPOINTS.getRealLevel();
    private final int START_ATTACK_LEVEL = Skill.ATTACK.getRealLevel();
    private final int START_STRENGTH_LEVEL = Skill.STRENGTH.getRealLevel();
    private final int START_DEFENCE_LEVEL = Skill.DEFENSE.getRealLevel();
    private final int START_RANGE_LEVEL = Skill.RANGE.getRealLevel();
    private final int START_MAGIC_LEVEL = Skill.MAGIC.getRealLevel();

    private final int START_HP_EXP = Skill.HITPOINTS.getExperience();
    private final int START_ATTACK_EXP = Skill.ATTACK.getExperience();
    private final int START_STRENGTH_EXP = Skill.STRENGTH.getExperience();
    private final int START_DEFENCE_EXP = Skill.DEFENSE.getExperience();
    private final int START_RANGE_EXP = Skill.RANGE.getExperience();
    private final int START_MAGIC_EXP = Skill.MAGIC.getExperience();

    BotGUI startPanel = new BotGUI();

    @Override
    public boolean onExecute() {
        strategies.add(new Eat());
        strategies.add(new Loot());
        strategies.add(new Fight());
        startPanel.setVisible(true);
        while (npcIds[0] == 0) {
            Time.sleep(50);
        }
        startPanel.dispose();
        provide(strategies);
        return true;
    }

    @Override
    public void onFinish() {

    }

    //START: Code generated using Enfilade's Easel
    private final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private final long startTime = System.currentTimeMillis();

    private String getTime() {
        DecimalFormat df = new DecimalFormat("00");
        final long millis = System.currentTimeMillis() - startTime;
        final long second = (millis / 1000) % 60;
        final long minute = (millis / (1000 * 60)) % 60;
        final long hour = (millis / (1000 * 60 * 60)) % 24;
        return df.format(hour) + ":" + df.format(minute) + ":" + df.format(second);
    }

    private String getGainedLevels() {
        int totalLevels = 0;
        final int hpGained = Skill.HITPOINTS.getRealLevel();
        if (hpGained > 99 == false) {
            totalLevels += hpGained - START_HP_LEVEL;
        }
        final int attGained = Skill.ATTACK.getRealLevel();
        if (attGained > 99 == false) {
            totalLevels += attGained - START_ATTACK_LEVEL;
        }
        final int strGained = Skill.STRENGTH.getRealLevel();
        if (strGained > 99 == false) {
            totalLevels += strGained - START_STRENGTH_LEVEL;
        }
        final int defGained = Skill.DEFENSE.getRealLevel();
        if (defGained > 99 == false) {
            totalLevels += defGained - START_DEFENCE_LEVEL;
        }
        final int rangGained = Skill.RANGE.getRealLevel();
        if (rangGained > 99 == false) {
            totalLevels += rangGained - START_RANGE_LEVEL;
        }
        final int magGained = Skill.MAGIC.getRealLevel();
        if (magGained > 99 == false) {
            totalLevels += magGained - START_MAGIC_LEVEL;
        }
        return String.valueOf(totalLevels);
    }

    private String getXpGained() {
        int totalXpGained = 0;
        final int hpGained = Skill.HITPOINTS.getExperience() - START_HP_EXP;
        totalXpGained += hpGained;
        final int attGained = Skill.ATTACK.getExperience() - START_ATTACK_EXP;
        totalXpGained += attGained;
        final int strGained = Skill.STRENGTH.getExperience() - START_STRENGTH_EXP;
        totalXpGained += strGained;
        final int defGained = Skill.DEFENSE.getExperience() - START_DEFENCE_EXP;
        totalXpGained += defGained;
        final int rangGained = Skill.RANGE.getExperience() - START_RANGE_EXP;
        totalXpGained += rangGained;
        final int magGained = Skill.MAGIC.getExperience() - START_MAGIC_EXP;
        totalXpGained += magGained;
        return String.valueOf(totalXpGained);

    }

    private String getXpPerHour() {
        final int gainedXp = Integer.parseInt(getXpGained());
        DecimalFormat df = new DecimalFormat("00");
        final long millis = System.currentTimeMillis() - startTime;
        final long second = millis / 1000;
        final long xpPerHour = ((gainedXp / second) * 60) * 60;
        return df.format(xpPerHour);

    }

    // private String 

    private final Color color1 = new Color(255, 255, 255);

    private final Font font1 = new Font("Arial", 0, 13);
    private final Font font2 = new Font("Arial", 0, 12);
    private final Font font3 = new Font("Arial", 0, 15);

    private final Image img1 = getImage("http://i.imgur.com/YeAvokx.png");

    public void paint(Graphics g1) {

        String currentTime = getTime();
        String gainedLevels = getGainedLevels();
        String gainedXp = getXpGained();
        String xpPerHour = getXpPerHour();

        Graphics2D g = (Graphics2D)g1;
        g.setRenderingHints(antialiasing);

        g.drawImage(img1, 2, 336, null);
        g.setFont(font1);
        g.setColor(color1);
        g.drawString("Running for: " + currentTime, 11, 450);
        g.setFont(font2);
        g.drawString("Total XP Gained: " + gainedXp, 310, 450);
        g.setFont(font3);
        g.drawString("Status: " + status, 11, 413);
        g.drawString("Levels gained: " + gainedLevels, 390, 413);
        g.drawString("XP per hour: " + xpPerHour, 197, 413);
    }
    //END: Code generated using Enfilade's Easel

    public class Fight implements Strategy {

        Npc enemy;

        @Override
        public boolean activate() {
            enemy = null;
            if (!Players.getMyPlayer().isInCombat()) {
                enemy = nextEnemy();
            }
            if (enemy != null && !Players.getMyPlayer().isInCombat()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            status = "Attacking enemy";

            enemy.interact(1);

            Time.sleep(700, 750);

            Time.sleep(new SleepCondition() {

                @Override
                public boolean isValid() {
                    return Players.getMyPlayer().isInCombat() || Players.getMyPlayer().getAnimation() != -1 && enemy.isInCombat() && !Players.getMyPlayer().isInCombat();
                }
            }, 5050);

            Time.sleep(1800, 2050);

            if (enemy.isInCombat() && Players.getMyPlayer().isInCombat()) {

                Time.sleep(new SleepCondition() {

                    @Override
                    public boolean isValid() {
                        return !enemy.isInCombat();
                    }
                }, 10000);
            }
        }
    }

    public class Eat implements Strategy {

        private int currentHP;
        private int levelOfHP;
        private int eatPoints;

        private final int[] foodList = {
            7943, 7944, 7947, 6970, 386, 14617, 3145, 3148, 392, 380, 330, 316, 374, 362
        };

        @Override
        public boolean activate() {

            if (Players.getMyPlayer().isInCombat()) {
                currentHP = Players.getMyPlayer().getHealth();
            } else {
                currentHP = 0;
            }

            levelOfHP = Skill.HITPOINTS.getRealLevel();
            eatPoints = (int)((levelOfHP / 10) * 6);

            if (currentHP != 0 && currentHP <= eatPoints) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            for (Item food : Inventory.getItems(foodList)) {
                currentHP = Players.getMyPlayer().getHealth();
                if (currentHP != 0 && Players.getMyPlayer().isInCombat()) {

                    eatPoints = (int)((levelOfHP / 10) * 6); 

                    if (food != null && currentHP <= eatPoints) {
                        status = "Eating.";
                        Menu.sendAction(74, food.getId() - 1, food.getSlot(), 3214, 4);
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
            if (toLoot != null && Inventory.getCount() != 28) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void execute() {

            status = "Looting.";

            final int startCount = Inventory.getCount(true, toLoot.getId());

            toLoot.take();

            Time.sleep(700, 750);

            Time.sleep(new SleepCondition() {

                @Override
                public boolean isValid() {
                    return Inventory.getCount(true, toLoot.getId()) > startCount;
                }
            }, 2500);

        }

    }

    private Npc nextEnemy() {
        try {
            Npc[] npcList = Npcs.getNearest(npcIds);
            for (Npc toFight : npcList) {
                if (toFight != null && !toFight.isInCombat() && !Players.getMyPlayer().isInCombat()) {
                    return toFight;
                }
            }
        } catch (Exception e) {
            System.out.println(String.valueOf(e));
            Time.sleep(2500);
            nextEnemy();
        }
        return null;
    }

    private GroundItem nextLoot() {
        try {
            GroundItem[] lootList = GroundItems.getNearest(lootIds);
            for (GroundItem toLoot : lootList) {
                if (toLoot != null && Calculations.distanceTo(toLoot.getLocation()) < 12) {
                    return toLoot;
                }
            }
        } catch (Exception e) {
            System.out.println(String.valueOf(e));
            Time.sleep(2500);
            nextLoot();
        }
        return null;
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
            for (int i = 0; i < fightList.getModel().getSize(); i++) {
                String fightListString = fightListModel.getElementAt(i).toString();
                if (fightListString != null) {
                    int startPosition = fightListString.indexOf("- (") + "- (".length();
                    int endPosition = fightListString.indexOf(")", startPosition);
                    npcIds[i] = Integer.parseInt(fightListString.substring(startPosition, endPosition));
                    // System.out.println("NPC ID from startButtonActionPerformed: " + String.valueOf(Integer.parseInt(fightListString.substring(startPosition, endPosition))));
                }
            }
            for (int l = 0; l < lootList.getModel().getSize(); l++) {
                String lootListString = lootListModel.getElementAt(l).toString();
                if (lootListString != null) {
                    lootIds[l] = Integer.parseInt(lootListString) - 1;
                    // System.out.println("Loot ID from startButtonActionPerformed: " + String.valueOf(lootIds[l]));
                }
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
                int[] ids = new int[idsTextEach.length];
                for (int i = 0; i < ids.length; i++) {
                    try {
                        ids[i] = Integer.parseInt(idsTextEach[i]);
                    } catch (NumberFormatException e) {
                        String text = "Your input contained a letter/word, remove it to add items to loot list.";
                        JOptionPane.showMessageDialog(this, text, "Input error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                for (int item = 0; item < ids.length; item++) {
                    if (ids[item] != 0) {
                        lootListStr.add(String.valueOf(ids[item]));
                    }
                }
                if (lootList.getModel().getSize() > 0) {
                    lootListModel.clear();
                }
                for (String s: lootListStr) {
                    lootListModel.addElement(s);
                }
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

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info: javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Default".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(BotGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(BotGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(BotGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(BotGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new BotGUI().setVisible(true);
                }
            });
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
                if (npcIds.length > 0) {
                    for (int i = 0; i < npcIds.length; i++) {
                        npcIds[i] = 0;
                    }
                    // System.out.println("Cleared id's");
                }
                for (String item : npcListStr) {
                    nearestNpcListModel.addElement(item);
                    // System.out.println("Adding to loaded: " + item);
                }
            } catch (Exception e) {
                String text = "Could not add nearest Npc's to list.";
                JOptionPane.showMessageDialog(this, text, "Npc error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void loadNpc() {
            Npc[] loadListObj = Npcs.getNearest();
            npcListStr.clear();
            try {
                BufferedReader lineReader = new BufferedReader(new FileReader("npcList.txt"));
                String line;
                while ((line = lineReader.readLine()) != null) {
                    for (int i = 0; i < loadListObj.length; i++) {
                        int idEndPosition = line.indexOf(" -");
                        if (line.substring(0, idEndPosition).equals(String.valueOf(loadListObj[i].getDef().getId()))) {
                            int nameStartPosition = line.indexOf("- ") + "- ".length();
                            String nameOfNpc = line.substring(nameStartPosition);
                            npcListStr.add(nameOfNpc + " - (" + loadListObj[i].getDef().getId() + ")");
                        }
                    }
                }
                lineReader.close();
            } catch (Exception e) {
                String text = "Could not load nearest Npc's.";
                JOptionPane.showMessageDialog(this, text, "Npc error", JOptionPane.ERROR_MESSAGE);
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
