package commitmessagetemplate;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import commitmessagetemplate.network.RpcUtils;
import commitmessagetemplate.network.redmine.Issue;
import commitmessagetemplate.network.redmine.IssuesResponse;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
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

    private IssuesResponse issuesResponse;

    private CommitMessageTemplateAction action;

    private List<String> texts = new ArrayList<String>();

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

    private void updateList(List<String> texts) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ListModel jListModel =  new DefaultComboBoxModel(texts.toArray());
                list1.setModel(jListModel);
            }
        });
    }



    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        updateList(texts);

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList myList = (JList) e.getSource();
                    int index = myList.getSelectedIndex();    //已选项的下标
                    Object obj = myList.getModel().getElementAt(index);  //取出数据
                    action.setCommitMessage(obj.toString());
                    action.setIssue(issuesResponse.getIssues().get(index));
                    MessageSearcherGUI.this.close(0);
                } else if (e.getClickCount() == 1 && e.isControlDown()){
                    int index = list1.getSelectedIndex();    //已选项的下标
                    BrowserUtil.browse(host + "issues/" + issuesResponse.getIssues().get(index).getId());
                }
            }
        });

        assignedToMe.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (assignedToMe.isSelected()) {
                    parameter.put(KEY_ASSIGNED_TO_ID, "me");
                } else {
                    parameter.remove(KEY_ASSIGNED_TO_ID);
                }
                searchServer();
            }
        });

        searchButton.addActionListener(this);
        return panel1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            final String pattern = textField1.getText();
            List<String> filtered = texts.stream().filter(t -> t.contains(pattern)).collect(Collectors.toList());
            updateList(filtered);
        }
    }

    @Override
    protected void doOKAction() {
        int index = list1.getSelectedIndex();
        Object obj = list1.getModel().getElementAt(index);
        action.setCommitMessage(obj.toString());
        action.setIssue(issuesResponse.getIssues().get(index));
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
        CommitMessageTemplateConfig cfg = CommitMessageTemplateConfig.getInstance(project);
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

        if (!host.endsWith("/"))
            host += "/";
        if (!host.startsWith("http://"))
            host = "http://" + host;

        parameter.put("key", key);

        if (assignedToMe.isSelected()) {
            parameter.put(KEY_ASSIGNED_TO_ID, "me");
        }
    }

    private void searchServer() {
        Thread thread = new Thread(() -> {
            issuesResponse = RpcUtils.getResponseFromServerGET(host + "issues.json",  IssuesResponse.class, parameter);

            texts.clear();
            for (Issue issue : issuesResponse.getIssues()) {
                texts.add("#" + issue.getId() + "[" + issue.getTracker().getName() + "]:" + issue.getSubject());
            }
            updateList(texts);
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


