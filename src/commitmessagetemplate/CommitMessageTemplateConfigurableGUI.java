package commitmessagetemplate;

import com.intellij.openapi.project.Project;
import commitmessagetemplate.network.RpcUtils;
import commitmessagetemplate.network.redmine.IssueStatusesResponse;
import commitmessagetemplate.network.redmine.Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

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
    private JComboBox statusSelector;
    private JLabel statusesListLabel;
    private CommitMessageTemplateConfig config;

    boolean statusSelectorInited = false;

    IssueStatusesResponse statusesResponse;
    Long selectedStatus;

    void createUI(Project project) {
        config = CommitMessageTemplateConfig.getInstance(project);

        host.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (host.getText() != null && !host.getText().equals("")) {
                    config.setHost(host.getText());
                    fixHost();
                    setStatusList();
                }
            }
        });

        key.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (key.getText() != null && !key.getText().equals("")) {
                    config.setKey(key.getText());
                    setStatusList();
                }
            }
        });

        if (config != null) {
            if (config.getTemplate() == null || config.getTemplate().equals("")) {
                config.setTemplate("#${issue.id}[${issue.tracker.name}]:${issue.subject}");
            }
            host.setText(config.getHost());
            key.setText(config.getKey());
            template.setText(config.getTemplate());
            selectedStatus = config.getDefaultToStatusId();
        }

        statusSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedStatus = statusesResponse.getIssue_statuses().get(statusSelector.getSelectedIndex()).getId();
            }
        });

        setStatusList();
    }

    JPanel getRootPanel() {
        return rootPanel;
    }

    boolean isModified() {
        return !host.getText().equals(config.getHost()) ||
                !key.getText().equals(config.getKey()) ||
                !template.getText().equals(config.getTemplate()) ||
                (selectedStatus != null && !selectedStatus.equals(config.getDefaultToStatusId()));
    }

    void apply() {
        config.setHost(host.getText());
        config.setKey(key.getText());
        config.setTemplate(template.getText());
        if (selectedStatus != null)
            config.setDefaultToStatusId(selectedStatus);
        fixHost();
    }

    private void fixHost() {
        if (config.getHost() == null || config.getHost().equals(""))
            return;

        if (!config.getHost().endsWith("/"))
            config.setHost(config.getHost() + "/");
        if (!config.getHost().startsWith("http://"))
            config.setHost("http://" + config.getHost());
        host.setText(config.getHost());
    }

    void reset() {
        host.setText(config.getHost());
        key.setText(config.getKey());
        template.setText(config.getTemplate());
    }

    private void setStatusList() {
        synchronized (this) {
            if (statusSelectorInited)
                return;
        }

        if (config.getHost() == null || config.getHost().equals("") ||
                config.getKey() == null || config.getKey().equals("")) {
            ComboBoxModel jListModel =  new DefaultComboBoxModel(new String[]{"set host and key first, then this is avaliable"});
            statusSelector.setModel(jListModel);
            statusSelector.setEditable(false);
            statusSelector.setEnabled(false);
            return;
        } else {
            statusSelector.setEditable(true);
            statusSelector.setEnabled(true);
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    statusesResponse = RpcUtils.getResponseFromServerGET(config.getHost() + "issue_statuses.json?key=" + config.getKey(), IssueStatusesResponse.class);
                    List<String> statusesStr = new ArrayList<String>();
                    for (Status status : statusesResponse.getIssue_statuses()) {
                        statusesStr.add(status.getName());
                    }

                    int idx = -1;
                    if (selectedStatus != null) {
                        for (int i = 0; i < statusesResponse.getIssue_statuses().size(); ++i) {
                            if (selectedStatus.equals(statusesResponse.getIssue_statuses().get(i).getId()))
                                idx = i;
                        }
                    }
                    updateList(statusesStr, idx);
                    synchronized (this) {
                        statusSelectorInited = true;
                    }
                }
            })).start();
        }
    }

    private void updateList(List<String> texts, int idx) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ComboBoxModel jListModel =  new DefaultComboBoxModel(texts.toArray());
                statusSelector.setModel(jListModel);
                if (idx != -1)
                    statusSelector.setSelectedIndex(idx);
            }
        });
    }
}
