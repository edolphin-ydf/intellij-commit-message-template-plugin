package commitmessagetemplate;

import commitmessagetemplate.platform.redmine.RedmineIssueManagerImpl;
import commitmessagetemplate.platform.redmine.RedmineConfigurable;
import commitmessagetemplate.platform.redmine.RedmineConfigurableGUI;
import commitmessagetemplate.platform.tapd.TapdConfigurable;
import commitmessagetemplate.platform.tapd.TapdConfigurableGUI;
import commitmessagetemplate.platform.tapd.TapdIssueManagerImpl;

public class ClassLoader {
    public static void LoadClass() {
        try {
            Class.forName(RedmineConfigurable.class.getName());
            Class.forName(RedmineConfigurableGUI.class.getName());
            Class.forName(RedmineIssueManagerImpl.class.getName());

            Class.forName(TapdConfigurable.class.getName());
            Class.forName(TapdConfigurableGUI.class.getName());
            Class.forName(TapdIssueManagerImpl.class.getName());
        } catch (ClassNotFoundException e) {
        }
    }
}
