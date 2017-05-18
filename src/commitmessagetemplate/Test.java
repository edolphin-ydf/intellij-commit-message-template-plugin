package commitmessagetemplate;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by edolphin on 17-5-18.
 */
public class Test extends DialogWrapper {
    private JPanel panel1;
    private JTextField textField1;
    private JBList list1;
    private JButton searchButton;

    CommitMessageTemplateAction action;

    public Test(@Nullable Project project, CommitMessageTemplateAction action) {
        super(project);
        init();
        this.action = action;
        this.setModal(true);
    }

    public Test(boolean canBeParent) {
        super(canBeParent);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        ListModel jListModel =  new DefaultComboBoxModel(new String[] { "张三", "李四" });
        list1.setModel(jListModel);
//        list1.setPreferredSize(new java.awt.Dimension(192, 173));

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList myList = (JList) e.getSource();
                    int index = myList.getSelectedIndex();    //已选项的下标
                    Object obj = myList.getModel().getElementAt(index);  //取出数据
                    System.out.println(obj.toString());
                    action.setCommitMessage(obj.toString());
                    Test.this.close(0);
                }
            }
        });
        return panel1;
    }
}
