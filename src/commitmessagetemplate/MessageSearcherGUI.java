package commitmessagetemplate;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import commitmessagetemplate.domain.Issue;
import commitmessagetemplate.platform.IssueManager;
import commitmessagetemplate.platform.NeedUpdateIssueData;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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

    private CommitMessageTemplateAction action;
    private CommitMessageTemplateConfig cfg;
    private Object platformRuntimeData;
    private Object platformCfg;

    private IssueManager issueManager;

    private Project project;

    public MessageSearcherGUI(@Nullable Project project, CommitMessageTemplateAction action) {
        super(project);
        super.init();

        this.project = project;
        this.action = action;
        this.setModal(true);

        this.cfg = CommitMessageTemplateConfig.getInstance(project);
        this.platformCfg = this.cfg.getCommitState().getConfig();
        this.platformRuntimeData = IssueTrackerPlatformCenter.getInstance(project).getRuntimeData(this.cfg.getCommitState().selectedPlatform);

        this.issueManager = IssueManager.newIssueManager(this.cfg.getCommitState().selectedPlatform);
        this.issueManager.init(project, this.cfg.getCommitState(), new IssueManager.Option(), this::updateList);
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

                    if (platformRuntimeData instanceof NeedUpdateIssueData) {
                        NeedUpdateIssueData d = (NeedUpdateIssueData)platformRuntimeData;
                        d.setCurrentIssue(issue);
                        d.setChangeStatus(whetherChangeStatus.isSelected());
                    }
                    MessageSearcherGUI.this.close(0);
                } else if (e.getClickCount() == 1 && e.isControlDown()){
//                    BrowserUtil.browse(host + "issues/" + issue.getId());
                }
            }
        });

        assignedToMe.addItemListener(e -> {
            issueManager.refreshIssues((new IssueManager.OptionBuilder()).SetAssignToMe(assignedToMe.isSelected()).build(), this::updateList);
        });

        searchServerBtn.addActionListener(e -> issueManager.refreshIssues(this::updateList));

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
        List<Issue> filtered = issueManager.getIssues(i -> i.toString().contains(pattern));
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
        if (platformRuntimeData instanceof NeedUpdateIssueData) {
            NeedUpdateIssueData d = (NeedUpdateIssueData)platformRuntimeData;
            d.setCurrentIssue(issue);
            d.setChangeStatus(whetherChangeStatus.isSelected());
        }
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
}


