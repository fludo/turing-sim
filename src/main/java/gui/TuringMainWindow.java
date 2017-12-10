/*
 *   Copyright (©) 2009 | 16 January 2009 | EPFL (Ecole Polytechnique fédérale de Lausanne)
 *
 *   TuringSim is free software ; you can redistribute it and/or modify it under the terms of the
 *   GNU General Public License as published by the Free Software Foundation ; either version 3 of
 *   the License, or (at your option) any later version.
 *
 *   TuringSim is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY ;
 *   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with TuringSim ;
 *   if not, write to the Free Software Foundation,
 *
 *   Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 *
 *
 *   Author : Ludovic Favre <ludovic.favre@epfl.ch>
 *
 *   Project supervisor : Mahdi Cheraghchi <mahdi.cheraghchi@epfl.ch>
 *
 *   Web site : http://icwww.epfl.ch/~lufavre
 *
 */
package gui;

import core.MultiTapeTuringMachine;
import core.SimpleTuringMachine;
import core.TuringMachine;
import core.io.XMLReader;
import java.awt.Component;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 * The application's main frame.
 *
 * @author Ludovic Favre
 */
public class TuringMainWindow extends FrameView {

    public TuringMainWindow(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = TuringApp.getApplication().getMainFrame();
            aboutBox = new TuringAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        TuringApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        tabPanel = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        loadFromMenuItem = new javax.swing.JMenuItem();
        newMenu = new javax.swing.JMenu();
        newSimpleTMItem = new javax.swing.JMenuItem();
        newMultiTMItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveToMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        tmMenu = new javax.swing.JMenu();
        editAlphabetItem = new javax.swing.JMenu();
        editInputAlphabetItem = new javax.swing.JMenuItem();
        editTapeAlphabetItem = new javax.swing.JMenuItem();
        editInformationItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setMaximumSize(new java.awt.Dimension(800, 600));
        mainPanel.setMinimumSize(new java.awt.Dimension(800, 600));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        tabPanel.setName("tabPanel"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gui.TuringApp.class).getContext().getResourceMap(TuringMainWindow.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gui.TuringApp.class).getContext().getActionMap(TuringMainWindow.class, this);
        loadFromMenuItem.setAction(actionMap.get("loadFromFile")); // NOI18N
        loadFromMenuItem.setText(resourceMap.getString("loadFromMenuItem.text")); // NOI18N
        loadFromMenuItem.setName("loadFromMenuItem"); // NOI18N
        fileMenu.add(loadFromMenuItem);

        newMenu.setAction(actionMap.get("newSimpleTuringMachine")); // NOI18N
        newMenu.setText(resourceMap.getString("newMenu.text")); // NOI18N
        newMenu.setName("newMenu"); // NOI18N

        newSimpleTMItem.setAction(actionMap.get("newSimpleTuringMachine")); // NOI18N
        newSimpleTMItem.setText(resourceMap.getString("newSimpleTMItem.text")); // NOI18N
        newSimpleTMItem.setName("newSimpleTMItem"); // NOI18N
        newMenu.add(newSimpleTMItem);

        newMultiTMItem.setAction(actionMap.get("newMultiTapeTuringMachine")); // NOI18N
        newMultiTMItem.setText(resourceMap.getString("newMultiTMItem.text")); // NOI18N
        newMultiTMItem.setName("newMultiTMItem"); // NOI18N
        newMenu.add(newMultiTMItem);

        jMenuItem1.setAction(actionMap.get("newTinyLangPanel")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        newMenu.add(jMenuItem1);

        fileMenu.add(newMenu);

        saveMenuItem.setAction(actionMap.get("saveFile")); // NOI18N
        saveMenuItem.setText(resourceMap.getString("saveMenuItem.text")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        saveToMenuItem.setAction(actionMap.get("saveToFile")); // NOI18N
        saveToMenuItem.setText(resourceMap.getString("saveToMenuItem.text")); // NOI18N
        saveToMenuItem.setName("saveToMenuItem"); // NOI18N
        fileMenu.add(saveToMenuItem);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        tmMenu.setText(resourceMap.getString("tmMenu.text")); // NOI18N
        tmMenu.setName("tmMenu"); // NOI18N

        editAlphabetItem.setText(resourceMap.getString("editAlphabetItem.text")); // NOI18N
        editAlphabetItem.setName("editAlphabetItem"); // NOI18N

        editInputAlphabetItem.setAction(actionMap.get("editInputAlphabet")); // NOI18N
        editInputAlphabetItem.setText(resourceMap.getString("editInputAlphabetItem.text")); // NOI18N
        editInputAlphabetItem.setName("editInputAlphabetItem"); // NOI18N
        editInputAlphabetItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editInputAlphabetItemMouseClicked(evt);
            }
        });
        editAlphabetItem.add(editInputAlphabetItem);

        editTapeAlphabetItem.setAction(actionMap.get("editTapeAlphabet")); // NOI18N
        editTapeAlphabetItem.setText(resourceMap.getString("editTapeAlphabetItem.text")); // NOI18N
        editTapeAlphabetItem.setName("editTapeAlphabetItem"); // NOI18N
        editTapeAlphabetItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editTapeAlphabetItemMouseClicked(evt);
            }
        });
        editAlphabetItem.add(editTapeAlphabetItem);

        tmMenu.add(editAlphabetItem);

        editInformationItem.setAction(actionMap.get("editTmInfo")); // NOI18N
        editInformationItem.setText(resourceMap.getString("editInformationItem.text")); // NOI18N
        editInformationItem.setName("editInformationItem"); // NOI18N
        tmMenu.add(editInformationItem);

        menuBar.add(tmMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 616, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    
    private void editInputAlphabetItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editInputAlphabetItemMouseClicked
        int index = this.tabPanel.getModel().getSelectedIndex();
        if (index >= 0) {
            Component c = this.tabPanel.getComponentAt(index);
            if (c instanceof SingleTapePanel) {
                ((SingleTapePanel)c).editInputAlphabet();
            } else if (c instanceof MultiTapePanel) {
                ((MultiTapePanel)c).editInputAlphabet();
            }
        }
}//GEN-LAST:event_editInputAlphabetItemMouseClicked

    
    private void editTapeAlphabetItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editTapeAlphabetItemMouseClicked
        int index = this.tabPanel.getModel().getSelectedIndex();
        if (index >= 0) {
            Component c = this.tabPanel.getComponentAt(index);
            if (c instanceof SingleTapePanel) {
                ((SingleTapePanel)c).editTapeAlphabet();
            } else if (c instanceof MultiTapePanel) {
                ((MultiTapePanel)c).editTapeAlphabet();
            }
        }
    }//GEN-LAST:event_editTapeAlphabetItemMouseClicked

    @Action
    public void saveToFile() {
         int index = this.tabPanel.getModel().getSelectedIndex();
        if (index >= 0) {
            Component c = this.tabPanel.getComponentAt(index);
            if (c instanceof SingleTapePanel) {
                ((SingleTapePanel)c).saveTo();
            } else if (c instanceof MultiTapePanel) {
               ((MultiTapePanel)c).saveTo();
            }
        }
    }

    @Action
    public void saveFile() {
        int index = this.tabPanel.getModel().getSelectedIndex();
        if (index >= 0) {
            Component c = this.tabPanel.getComponentAt(index);
            if (c instanceof SingleTapePanel) {
                ((SingleTapePanel)c).save();
            } else if (c instanceof MultiTapePanel) {
                ((MultiTapePanel)c).save();
            }
        }

    }

    @Action
    public void loadFromFile() {
        JFrame mainFrame = TuringApp.getApplication().getMainFrame();
        OpenFileBox openBox = new OpenFileBox(mainFrame, true);
        openBox.setLocationRelativeTo(mainFrame);
        //System.out.println("Showing the openbox");

        TuringApp.getApplication().show(openBox);

        //System.out.println("Retrieving the file...");
        File selectedFile = openBox.jFileChooser().getSelectedFile();
        if (selectedFile == null) {
            //System.out.println("canceled...");
        } else {
            //System.out.println("Got the file !");
            //openBox.dispose();
            System.out.println(selectedFile.getName());
            try {
                this.tabPanel.add(selectedFile.getName(), buildTabFromFile(selectedFile, this.tabPanel));
                this.tabPanel.setSelectedIndex(this.tabPanel.getTabCount() - 1);
            //this.tabPanel.set
            } catch (Exception e) {
                System.out.println("Exception encountered !" + e.getMessage());
            }

        }




    }

    @Action
    public void closeTab(int index) {
        this.tabPanel.remove(index);
    }

    private Component buildTabFromFile(File file, javax.swing.JTabbedPane jtable) throws Exception {
        core.io.XMLReader reader = new XMLReader();
        TuringMachine tm = reader.read(file);

        if (tm instanceof SimpleTuringMachine) {
            //System.out.println("Got a simple turingmachine file...");
            return new SingleTapePanel((SimpleTuringMachine) tm, jtable, jtable.getTabCount(),file.getAbsolutePath());
        } else if (tm instanceof MultiTapeTuringMachine) {
            //System.out.println("Got a multitape turingmachine file...");
            return new MultiTapePanel((MultiTapeTuringMachine) tm, jtable, jtable.getTabCount(),file.getAbsolutePath());
        } else {
            throw new Exception("Invalid turing machine instance !");
        }

    }

    @Action
    public void newSimpleTuringMachine() {
        SimpleTuringMachine tm = new SimpleTuringMachine();
        this.tabPanel.add(new SingleTapePanel(tm, this.tabPanel, this.tabPanel.getTabCount(),null));
        this.tabPanel.setSelectedIndex(this.tabPanel.getTabCount() - 1);
    }

    @Action
    public void newTinyLangPanel(){
        this.tabPanel.add(new TinyLangPanel(this.tabPanel, this.tabPanel.getTabCount()));
        this.tabPanel.setSelectedIndex(this.tabPanel.getTabCount() - 1);
    }
    @Action
    public void newMultiTapeTuringMachine() {
        MultiTapeTuringMachine tm = new MultiTapeTuringMachine();
        this.tabPanel.add(new MultiTapePanel(tm, this.tabPanel, this.tabPanel.getTabCount(),null));
        this.tabPanel.setSelectedIndex(this.tabPanel.getTabCount() - 1);
    }

    @Action
    public void editInputAlphabet() {
        int index = this.tabPanel.getModel().getSelectedIndex();
        if (index >= 0) {
            Component c = this.tabPanel.getComponentAt(index);
            if (c instanceof SingleTapePanel) {
                ((SingleTapePanel)c).editInputAlphabet();
            } else if (c instanceof MultiTapePanel) {
               ((MultiTapePanel)c).editInputAlphabet();
            }
        }
    }

    @Action
    public void editTapeAlphabet() {
        int index = this.tabPanel.getModel().getSelectedIndex();
        if (index >= 0) {
            Component c = this.tabPanel.getComponentAt(index);
            if (c instanceof SingleTapePanel) {
                ((SingleTapePanel)c).editTapeAlphabet();
            } else if (c instanceof MultiTapePanel) {
                ((MultiTapePanel)c).editTapeAlphabet();
            }
        }
        //System.out.println("EDIT TAPE ALPHABET");
    }

    @Action
    public void editTmInfo() {
        int index = this.tabPanel.getModel().getSelectedIndex();
        if (index >= 0) {
            Component c = this.tabPanel.getComponentAt(index);
            if (c instanceof SingleTapePanel) {
                ((SingleTapePanel)c).editTmInfo();
            } else if (c instanceof MultiTapePanel) {
                ((MultiTapePanel)c).editTmInfo();
            }
            else{
                //no effect, its a TinyLang
            }
            
        }
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu editAlphabetItem;
    private javax.swing.JMenuItem editInformationItem;
    private javax.swing.JMenuItem editInputAlphabetItem;
    private javax.swing.JMenuItem editTapeAlphabetItem;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem loadFromMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu newMenu;
    private javax.swing.JMenuItem newMultiTMItem;
    private javax.swing.JMenuItem newSimpleTMItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem saveToMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JMenu tmMenu;
    // End of variables declaration//GEN-END:variables
    private DefaultTableModel model_ = new DefaultTableModel();
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
