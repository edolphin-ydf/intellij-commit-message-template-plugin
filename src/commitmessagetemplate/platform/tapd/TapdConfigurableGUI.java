package commitmessagetemplate.platform.tapd;

import com.intellij.openapi.project.Project;
import commitmessagetemplate.IssueTrackerPlatformCenter;
import commitmessagetemplate.PlatformGUI;

import javax.swing.*;

public class TapdConfigurableGUI implements PlatformGUI {
    private JTextField userName;
    private JTextField password;
    private JTextField projectId;
    private JTextArea template;
    private JPanel rootPanel;

    static {
        IssueTrackerPlatformCenter.platformConfigGuiClassMap.put(TapdConfigurable.PlatformName, TapdConfigurableGUI.class);
    }

    public JPanel createUI(Project project) {
        return rootPanel;
    }

    @Override
    public JPanel getRootPannel() {
        return rootPanel;
    }

    public boolean isModified() {
        return true;
    }

    public void apply() {
    }


    public void reset() {
    }
}
