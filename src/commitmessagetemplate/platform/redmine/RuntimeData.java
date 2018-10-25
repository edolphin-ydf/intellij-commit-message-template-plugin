package commitmessagetemplate.platform.redmine;

import commitmessagetemplate.network.redmine.Issue;
import commitmessagetemplate.platform.NeedUpdateIssueData;

public class RuntimeData implements NeedUpdateIssueData {
    // 是否更新issue的状态
    private boolean changeStatus = false;

    // 提交代码时, 当前选择的issure
    private Issue currentIssue = null;


    // region getter/setter
    public boolean isChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(boolean changeStatus) {
        this.changeStatus = changeStatus;
    }

    public commitmessagetemplate.domain.Issue getCurrentIssue() {
        return currentIssue;
    }

    public void setCurrentIssue(commitmessagetemplate.domain.Issue currentIssue) {
        this.currentIssue = (Issue)currentIssue;
    }
    // endregion getter/setter
}
