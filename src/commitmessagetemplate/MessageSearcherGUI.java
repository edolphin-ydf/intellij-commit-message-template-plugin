package commitmessagetemplate;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import commitmessagetemplate.network.RpcUtils;
import commitmessagetemplate.network.redmine.Issue;
import commitmessagetemplate.network.redmine.IssuesResponse;
import commitmessagetemplate.util.VelocityHelper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by edolphin on 17-5-18.
 */
public class MessageSearcherGUI extends DialogWrapper implements ActionListener {
    private JPanel panel1;
    private JTextField textField1;
    private JBList list1;
    private JButton searchButton;
    private JCheckBox assignedToMe;
    private JButton searchServerBtn;
    private JCheckBox whetherChangeStatus;

    private IssuesResponse issuesResponse;

    private CommitMessageTemplateAction action;
    private CommitMessageTemplateConfig cfg;

    private String host;
    private Map parameter = new HashMap();

    private Project project;

    private static String KEY_ASSIGNED_TO_ID= "assigned_to_id";

    public MessageSearcherGUI(@Nullable Project project, CommitMessageTemplateAction action) {
        super(project);
        this.project = project;
        this.action = action;
        this.setModal(true);

        this.initParameter();
        this.searchServer();

        init();
    }

    public MessageSearcherGUI(boolean canBeParent) {
        super(canBeParent);
    }

    private void updateList(List<Issue> issues) {
        SwingUtilities.invokeLater(() -> {
            ListModel jListModel =  new DefaultComboBoxModel(issues.toArray());
            list1.setModel(jListModel);
        });
    }



    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList myList = (JList) e.getSource();
                int index = myList.getSelectedIndex();    //已选项的下标
                Object obj = myList.getModel().getElementAt(index);  //取出数据
                Issue issue = (Issue) obj;

                if (e.getClickCount() == 2) {
                    action.setCommitMessage(obj.toString());

                    action.setIssue(issue);
                    cfg.setCurrentIssue(issue);
                    cfg.setChangeStatus(whetherChangeStatus.isSelected());
                    MessageSearcherGUI.this.close(0);
                } else if (e.getClickCount() == 1 && e.isControlDown()){
                    BrowserUtil.browse(host + "issues/" + issue.getId());
                }
            }
        });

        assignedToMe.addItemListener(e -> {
            if (assignedToMe.isSelected()) {
                parameter.put(KEY_ASSIGNED_TO_ID, "me");
            } else {
                parameter.remove(KEY_ASSIGNED_TO_ID);
            }
            searchServer();
        });

        searchServerBtn.addActionListener(e -> searchServer());

        searchButton.addActionListener(this);

        textField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                doSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                doSearch();
            }

            public void doSearch() {
                doNativeSearch();
            }
        });
        return panel1;
    }

    private void doNativeSearch() {
        final String pattern = textField1.getText();
        List<Issue> filtered = issuesResponse.getIssues().stream().filter(i -> i.toString().contains(pattern)).collect(Collectors.toList());
        ListModel jListModel =  new DefaultComboBoxModel(filtered.toArray());
        list1.setModel(jListModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            doNativeSearch();
        }
    }

    @Override
    protected void doOKAction() {
        int index = list1.getSelectedIndex();
        Object obj = list1.getModel().getElementAt(index);
        Issue issue = (Issue)obj;
        action.setCommitMessage(obj.toString());
        action.setIssue(issue);
        cfg.setCurrentIssue(issue);
        cfg.setChangeStatus(whetherChangeStatus.isSelected());
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }

    @Override
    public void doCancelAction(AWTEvent source) {
        super.doCancelAction(source);
    }

    private void initParameter() {
        cfg = CommitMessageTemplateConfig.getInstance(project);
        host = cfg.getHost();
        String key = cfg.getKey();

        if (host == null || host.equals("")) {
            JOptionPane.showMessageDialog(null, "please set host first, in File->Settings->Tools->Commit Message Template");
            return;
        }
        if (key == null || key.equals("")) {
            JOptionPane.showMessageDialog(null, "please set redmine api key first, in File->Settings->Tools->Commit Message Template. " +
                    "You can find your API key on your account page ( /my/account ) when logged in, on the right-hand pane of the default layout.");
            return;
        }

        parameter.put("key", key);

        if (assignedToMe.isSelected()) {
            parameter.put(KEY_ASSIGNED_TO_ID, "me");
        }
    }

    private void searchServer() {
        Thread thread = new Thread(() -> {
            setIssuesResponse(RpcUtils.getResponseFromServerGET(host + "issues.json",  IssuesResponse.class, parameter));
            getIssuesResponse().getIssues().forEach(i -> i.setIdeaProject(project));
            updateList(getIssuesResponse().getIssues());
        });
        thread.start();
    }

    synchronized public IssuesResponse getIssuesResponse() {
        return issuesResponse;
    }

    synchronized public void setIssuesResponse(IssuesResponse issuesResponse) {
        this.issuesResponse = issuesResponse;
    }
}


