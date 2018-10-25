package commitmessagetemplate;

import com.intellij.openapi.project.Project;

import javax.swing.*;

public interface PlatformGUI {

    JPanel createUI(Project project);

    JPanel getRootPannel();

    boolean isModified();

    void apply();

    void reset();
}
