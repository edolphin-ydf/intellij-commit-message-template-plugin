package commitmessagetemplate;

import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * Created by matan.goren on 23-Sep-16.
 */
public class CommitMessageTemplateConfigurableGUI {
    private JPanel rootPanel;
    private JTextField host;
    private JTextField key;
    private JLabel keyLabel;
    private JLabel hostLabel;
    private JTextArea template;
    private JLabel templateLabel;
    private CommitMessageTemplateConfig config;

    void createUI(Project project) {
        config = CommitMessageTemplateConfig.getInstance(project);

        if (config != null) {
            if (config.getTemplate() == null || config.getTemplate().equals("")) {
                config.setTemplate("#${issue.id}[${issue.tracker.name}]:${issue.subject}");
            }
            host.setText(config.getHost());
            key.setText(config.getKey());
            template.setText(config.getTemplate());
        }
    }

    JPanel getRootPanel() {
        return rootPanel;
    }

    boolean isModified() {
        return !host.getText().equals(config.getHost()) ||
                !key.getText().equals(config.getKey()) ||
                !template.getText().equals(config.getTemplate());
    }

    void apply() {
        config.setHost(host.getText());
        config.setKey(key.getText());
        config.setTemplate(template.getText());
    }

    void reset() {
        host.setText(config.getHost());
        key.setText(config.getKey());
        template.setText(config.getTemplate());
    }
}
