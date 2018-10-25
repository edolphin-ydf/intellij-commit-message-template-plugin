package commitmessagetemplate;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by matan.goren on 23-Sep-16.
 */
public class CommitMessageTemplateConfigurableGUI {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JPanel platformSelectionsPanel;

    private Map<String, PlatformGUI> tabs;
    private String selectedPlatform;
    private CommitMessageTemplateConfig cfg;

    JPanel createUI(Project project) {
        cfg = CommitMessageTemplateConfig.getInstance(project);
        selectedPlatform = cfg.getCommitState().selectedPlatform;


        tabs = IssueTrackerPlatformCenter.getInstance(project).getPlatformGuiMap();
        tabs.forEach((name, tabPanel) -> tabbedPane1.addTab(name, tabPanel.getRootPannel()));

        platformSelectionsPanel.setLayout(new GridLayout(2, tabs.size() / 4));
        ButtonGroup bg = new ButtonGroup();
        tabs.forEach((name, tabPanel) -> {
            JRadioButton radioButton = new JRadioButton(name);
            if (selectedPlatform.equals(name))
                radioButton.setSelected(true);
            platformSelectionsPanel.add(radioButton);
            bg.add(radioButton);
            radioButton.addChangeListener((e) -> selectedPlatform = ((JRadioButton)e.getSource()).getText());
        });

        return rootPanel;
    }

    boolean isModified() {
        for (String key : tabs.keySet()) {
            PlatformGUI gui = tabs.get(key);
            if (gui.isModified())
                return true;
        }
        if (!selectedPlatform.equals(cfg.getCommitState().selectedPlatform))
            return true;
        return false;
    }

    void apply() {
        cfg.getCommitState().selectedPlatform = selectedPlatform;
        tabs.forEach((name, gui) -> gui.apply());
    }


    void reset() {
    }
}
