package commitmessagetemplate.platform;


import commitmessagetemplate.domain.Issue;

public interface NeedUpdateIssueData {
    boolean isChangeStatus();

    void setChangeStatus(boolean changeStatus);

    Issue getCurrentIssue();

    void setCurrentIssue(Issue currentIssue);
}
