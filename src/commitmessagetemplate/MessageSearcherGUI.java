package commitmessagetemplate;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by edolphin on 17-5-18.
 */
public class MessageSearcherGUI extends DialogWrapper implements ActionListener {
    private JPanel panel1;
    private JTextField textField1;
    private JBList list1;
    private JButton searchButton;

    private CommitMessageTemplateAction action;

    private List<String> texts = new ArrayList<String>();

    public MessageSearcherGUI(@Nullable Project project, CommitMessageTemplateAction action) {
        super(project);
        this.action = action;
        this.setModal(true);
        texts.add("张三");
        texts.add("李四");

        init();
    }

    public MessageSearcherGUI(boolean canBeParent) {
        super(canBeParent);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        ListModel jListModel =  new DefaultComboBoxModel(texts.toArray());
        list1.setModel(jListModel);

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList myList = (JList) e.getSource();
                    int index = myList.getSelectedIndex();    //已选项的下标
                    Object obj = myList.getModel().getElementAt(index);  //取出数据
                    System.out.println(obj.toString());
                    action.setCommitMessage(obj.toString());
                    MessageSearcherGUI.this.close(0);
                }
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
            ListModel listModel = new DefaultComboBoxModel(filtered.toArray());
            list1.setModel(listModel);
        }
    }
}


