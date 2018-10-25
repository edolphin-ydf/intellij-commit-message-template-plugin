package commitmessagetemplate.platform.redmine;

import com.intellij.openapi.project.Project;
import commitmessagetemplate.CommitMessageTemplateConfig;
import commitmessagetemplate.IssueTrackerPlatformCenter;
import commitmessagetemplate.PlatformGUI;
import commitmessagetemplate.network.RpcUtils;
import commitmessagetemplate.network.redmine.IssueStatusesResponse;
import commitmessagetemplate.network.redmine.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RedmineConfigurableGUI implements PlatformGUI {
    private JTextField host;
    private JTextField key;
    private JTextArea template;
    private JComboBox statusSelector;
    private JPanel statusCheckPannel;
    private JPanel rootPanel;

    private CommitMessageTemplateConfig config;
    private RedmineConfigurable redmineConfig;

    boolean statusSelectorInited = false;

    IssueStatusesResponse statusesResponse;
    Long selectedStatus;

    private List<JCheckBox> statusCheckBoxes = new ArrayList<>();

    static {
        IssueTrackerPlatformCenter.platformConfigGuiClassMap.put(RedmineConfigurable.PlatformName, RedmineConfigurableGUI.class);
    }

    public JPanel createUI(Project project) {
        config = CommitMessageTemplateConfig.getInstance(project);
        redmineConfig = (RedmineConfigurable) config.getCommitState().getConfig();

        host.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (host.getText() != null && !host.getText().equals("")) {
                    redmineConfig.setHost(host.getText());
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
                    redmineConfig.setKey(key.getText());
                    setStatusList();
                }
            }
        });

        if (config != null) {
            if (redmineConfig.getTemplate() == null || redmineConfig.getTemplate().equals("")) {
                redmineConfig.setTemplate("#${issue.id}[${issue.tracker.name}]:${issue.subject}");
            }
            host.setText(redmineConfig.getHost());
            key.setText(redmineConfig.getKey());
            template.setText(redmineConfig.getTemplate());
            selectedStatus = redmineConfig.getDefaultToStatusId();
        }

        statusSelector.addActionListener(e -> selectedStatus = statusesResponse.getIssue_statuses().get(statusSelector.getSelectedIndex()).getId());

        setStatusList();

        return rootPanel;
    }

    @Override
    public JPanel getRootPannel() {
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return !host.getText().equals(redmineConfig.getHost()) ||
                !key.getText().equals(redmineConfig.getKey()) ||
                !template.getText().equals(redmineConfig.getTemplate()) ||
                (selectedStatus != null && !selectedStatus.equals(redmineConfig.getDefaultToStatusId())) ||
                isGetNotifyStatusIdsModified()
                ;
    }

    boolean isGetNotifyStatusIdsModified() {
        List<Long> newIds = getNotifyStatusIds();
        List<Long> added = newIds.stream().filter(id -> !redmineConfig.getNotifyStatusIds.contains(id)).collect(Collectors.toList());
        if (added.isEmpty()) {
            List<Long> removed = redmineConfig.getNotifyStatusIds.stream().filter(id -> !newIds.contains(id)).collect(Collectors.toList());
            if (removed.isEmpty())
                return false;
        }
        return true;
    }

    List<Long> getNotifyStatusIds() {
        List<Long> newIds = new ArrayList<>();
        for (int i = 0; i < statusCheckBoxes.size(); i++) {
            JCheckBox box = statusCheckBoxes.get(i);
            if (box.isSelected())
                newIds.add(statusesResponse.getIssue_statuses().get(i).getId());
        }
        return newIds;
    }

    @Override
    public void apply() {
        redmineConfig.setHost(host.getText());
        redmineConfig.setKey(key.getText());
        redmineConfig.setTemplate(template.getText());
        if (selectedStatus != null)
            redmineConfig.setDefaultToStatusId(selectedStatus);
        redmineConfig.getNotifyStatusIds = getNotifyStatusIds();
        fixHost();
    }

    private void fixHost() {
        if (redmineConfig.getHost() == null || redmineConfig.getHost().equals(""))
            return;

        if (!redmineConfig.getHost().endsWith("/"))
            redmineConfig.setHost(redmineConfig.getHost() + "/");
        if (!redmineConfig.getHost().startsWith("http://"))
            redmineConfig.setHost("http://" + redmineConfig.getHost());
        host.setText(redmineConfig.getHost());

        for (int i = 0; i < statusCheckBoxes.size(); i++) {
            JCheckBox box = statusCheckBoxes.get(i);
            if (redmineConfig.getNotifyStatusIds.contains(statusesResponse.getIssue_statuses().get(i).getId()))
                box.setSelected(true);
            else
                box.setSelected(false);
        }
    }

    @Override
    public void reset() {
        host.setText(redmineConfig.getHost());
        key.setText(redmineConfig.getKey());
        template.setText(redmineConfig.getTemplate());
    }

    private void setStatusList() {
        synchronized (this) {
            if (statusSelectorInited)
                return;
        }

        if (redmineConfig.getHost() == null || redmineConfig.getHost().equals("") ||
                redmineConfig.getKey() == null || redmineConfig.getKey().equals("")) {
            ComboBoxModel jListModel = new DefaultComboBoxModel(new String[]{"set host and key first, then this is avaliable"});
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
                    statusesResponse = RpcUtils.getResponseFromServerGET(redmineConfig.getHost() + "issue_statuses.json?key=" + redmineConfig.getKey(), IssueStatusesResponse.class);
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
        SwingUtilities.invokeLater(() -> {
            ComboBoxModel jListModel = new DefaultComboBoxModel(texts.toArray());
            statusSelector.setModel(jListModel);
            if (idx != -1)
                statusSelector.setSelectedIndex(idx);

            statusCheckPannel.setLayout(new GridLayout(4, texts.size() / 4));
            for (int i = 0; i < texts.size(); i++) {
                String text = texts.get(i);
                JCheckBox checkBox = new JCheckBox(text);
                statusCheckBoxes.add(checkBox);
                statusCheckPannel.add(checkBox);

                if (redmineConfig.getNotifyStatusIds.contains(statusesResponse.getIssue_statuses().get(i).getId()))
                    checkBox.setSelected(true);
            }
        });
    }
}
