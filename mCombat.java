package Minecraftftw.combat;

import java.awt.Component;
import java.awt.PopupMenu;
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
import org.rev317.min.Loader;
import java.util.Random;

import java.util.ArrayList;
import javax.swing.*;
import java.util.*;

import java.io.*;

@ScriptManifest(author = "MinecraftFtw", category = Category.COMBAT, description = "AIO Combat script.", name = "mCombat", servers = {
    "Ikov"
}, version = 1.0)
public class mCombat extends Script {

    private final ArrayList < Strategy > strategies = new ArrayList < Strategy > ();

    public static Set < String > npcListStr = new HashSet < String > ();
    public static Set < String > lootListStr = new HashSet < String > ();
    public static final int[] npcIds = new int[30];
    public static final int[] lootIds = new int[30];

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

            System.out.println("Attacking enemy.");

            enemy.interact(1);

            Time.sleep(700, 750);

            Time.sleep(new SleepCondition() {

                @Override
                public boolean isValid() {
                    return Players.getMyPlayer().isInCombat();
                }
            }, 7000);

            Time.sleep(new SleepCondition() {

                @Override
                public boolean isValid() {
                    return !Players.getMyPlayer().isInCombat();
                }
            }, 10000);
        }
    }

    public class Eat implements Strategy {

        private int currentHP;
        private int levelOfHP;
        private int eatPoints;

        private final int[] food = {
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

            for (Item food : Inventory.getItems(food)) {
                currentHP = Players.getMyPlayer().getHealth();
                if (currentHP != 0) {

                    eatPoints = (int)((levelOfHP / 10) * 6); 

                    if (food != null && currentHP <= eatPoints) {
                        System.out.println("Found food! Eating it.");
                        Menu.sendAction(74, food.getId() - 1, food.getSlot(), 3214, 4);
                        Time.sleep(3300, 3550);
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

            System.out.println("Looting.");

            int startCount = Inventory.getCount(true, toLoot.getId());

            toLoot.take();

            Time.sleep(700, 750);

            Time.sleep(new SleepCondition() {

                @Override
                public boolean isValid() {
                    return Inventory.getCount(true, toLoot.getId()) > startCount;
                }
            }, 2000);

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
                if (toLoot != null) {
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

        DefaultListModel model = new DefaultListModel();
        DefaultListModel nearestNpcListModel = new DefaultListModel();
        DefaultListModel lootListModel = new DefaultListModel();

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

            tabPane = new javax.swing.JTabbedPane();
            mainPanel = new javax.swing.JPanel();
            npcTitle = new javax.swing.JLabel();
            jScrollPane2 = new javax.swing.JScrollPane();
            loadedList = new javax.swing.JList(nearestNpcListModel);
            moveButton = new javax.swing.JButton();
            removeButton = new javax.swing.JButton();
            loadedLabel = new javax.swing.JLabel();
            fightLabel = new javax.swing.JLabel();
            reloadButton = new javax.swing.JButton();
            jScrollPane3 = new javax.swing.JScrollPane();
            fightList = new javax.swing.JList(model);
            startButton = new javax.swing.JButton();
            jLabel2 = new javax.swing.JLabel();
            lootPanel = new javax.swing.JPanel();
            npcTitle1 = new javax.swing.JLabel();
            jScrollPane4 = new javax.swing.JScrollPane();
            lootList = new javax.swing.JList(lootListModel);
            lootItemField = new javax.swing.JTextField();
            addLootButton = new javax.swing.JButton();
            lootRemoveButton = new javax.swing.JButton();
            jLabel1 = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setLocation(new java.awt.Point(450, 225));

            npcTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
            npcTitle.setText("Choose NPC's to fight:");

            loadedList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            loadedList.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
            loadedList.setForeground(new java.awt.Color(0, 0, 0));
            loadedList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jScrollPane2.setViewportView(loadedList);

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
            jScrollPane3.setViewportView(fightList);

            startButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
            startButton.setText("Start!");
            startButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startButtonActionPerformed(evt);
                }
            });

            jLabel2.setText("Press the start button to start the script when you are ready.");

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(reloadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(moveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(startButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(fightLabel)))
                .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(npcTitle)
                .addComponent(jLabel2))
                .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap()));
            mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(npcTitle)
                .addGap(5, 5, 5)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(loadedLabel)
                .addComponent(fightLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(moveButton)
                .addComponent(removeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(reloadButton)
                .addComponent(startButton))
                .addContainerGap()));

            tabPane.addTab("Main", mainPanel);

            npcTitle1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
            npcTitle1.setText("Enter items to loot:");

            lootList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jScrollPane4.setViewportView(lootList);

            addLootButton.setText("Add item");
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

            jLabel1.setText("Integer ID's only! (Use debug to display id's)");

            javax.swing.GroupLayout lootPanelLayout = new javax.swing.GroupLayout(lootPanel);
            lootPanel.setLayout(lootPanelLayout);
            lootPanelLayout.setHorizontalGroup(
            lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(lootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addGroup(lootPanelLayout.createSequentialGroup()
                .addComponent(lootItemField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addLootButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(lootRemoveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGroup(lootPanelLayout.createSequentialGroup()
                .addComponent(npcTitle1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14)))
                .addContainerGap()));
            lootPanelLayout.setVerticalGroup(
            lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(lootPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(npcTitle1)
                .addGap(2, 2, 2)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(lootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lootItemField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(addLootButton, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lootRemoveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addGap(20, 20, 20)));

            tabPane.addTab("Loot", lootPanel);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane)
                .addContainerGap()));
            layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                .addContainerGap()));

            pack();
            addNPCs();
        } // </editor-fold>//GEN-END:initComponents

        private void addNPCs() {
            loadNpc();
            nearestNpcListModel.clear();
            if (npcIds.length > 0) {
                for (int i = 0; i < npcIds.length; i++) {
                    npcIds[i] = 0;
                }
                System.out.println("Cleared id's");
            }
            for (String item: npcListStr) {
                nearestNpcListModel.addElement(item);
                System.out.println("Adding to loaded: " + item);
            }
        }

        private void reloadButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_reloadButtonActionPerformed
            addNPCs();
        } //GEN-LAST:event_reloadButtonActionPerformed

        private void moveButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_moveButtonActionPerformed
            Object npcItem = loadedList.getSelectedValue();
            if (!model.contains(npcItem)) {
                model.addElement(npcItem);
            }
        } //GEN-LAST:event_moveButtonActionPerformed

        private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_removeButtonActionPerformed
            int removeItem = fightList.getSelectedIndex();
            try {
                model.remove(removeItem);
            } catch (ArrayIndexOutOfBoundsException e) {
                Time.sleep(50);
            }
        } //GEN-LAST:event_removeButtonActionPerformed

        private void startButtonActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_startButtonActionPerformed
            for (int i = 0; i < fightList.getModel().getSize(); i++) {
                String fightListString = model.getElementAt(i).toString();
                if (fightListString != null) {
                    int startPosition = fightListString.indexOf("- (") + "- (".length();
                    int endPosition = fightListString.indexOf(")", startPosition);
                    npcIds[i] = Integer.parseInt(fightListString.substring(startPosition, endPosition));
                    System.out.println("NPC ID from startButtonActionPerformed: " + String.valueOf(Integer.parseInt(fightListString.substring(startPosition, endPosition))));
                }
            }
            for (int l = 0; l < lootList.getModel().getSize(); l++) {
                String lootListString = lootListModel.getElementAt(l).toString();
                if (lootListString != null) {
                    lootIds[l] = Integer.parseInt(lootListString) - 1;
                    System.out.println("Loot ID from startButtonActionPerformed: " + String.valueOf(lootIds[l]));
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
                    // System.out.println("Bot will loot: " + s);
                }
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

        public static void loadNpc() {
            Npc[] loadListObj = Npcs.getNearest();
            npcListStr.clear();
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            try {
                BufferedReader lineReader = new BufferedReader(new FileReader("npcList.txt"));
                // for (String line : lineReader.readLine()) {
                String line;
                while ((line = lineReader.readLine()) != null) {
                    for (int i = 0; i < loadListObj.length; i++) {
                        int idStartPosition = line.indexOf(" -");
                        if (line.substring(0, idStartPosition).equals(String.valueOf(loadListObj[i].getDef().getId()))) {
                            int nameStartPosition = line.indexOf("- ") + "- ".length();
                            String nameOfNpc = line.substring(nameStartPosition);
                            npcListStr.add(nameOfNpc + " - (" + loadListObj[i].getDef().getId() + ")");
                        }
                    }
                }
                lineReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton addLootButton;
        private javax.swing.JLabel fightLabel;
        private javax.swing.JList fightList;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JLabel loadedLabel;
        private javax.swing.JList loadedList;
        private javax.swing.JTextField lootItemField;
        private javax.swing.JList lootList;
        private javax.swing.JPanel lootPanel;
        private javax.swing.JButton lootRemoveButton;
        private javax.swing.JPanel mainPanel;
        private javax.swing.JButton moveButton;
        private javax.swing.JLabel npcTitle;
        private javax.swing.JLabel npcTitle1;
        private javax.swing.JButton reloadButton;
        private javax.swing.JButton removeButton;
        private javax.swing.JButton startButton;
        private javax.swing.JTabbedPane tabPane;
        // End of variables declaration//GEN-END:variables
    }
}
