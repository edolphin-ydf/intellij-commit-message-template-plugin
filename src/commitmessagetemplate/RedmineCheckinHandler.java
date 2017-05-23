package commitmessagetemplate;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import commitmessagetemplate.network.RpcUtils;
import commitmessagetemplate.network.redmine.UpdateIssueRequest;

/**
 * Created by edolphin on 20/05/2017.
 */
public class RedmineCheckinHandler extends CheckinHandler {

    CheckinProjectPanel checkinProjectPanel;

    CommitContext commitContext;

    public RedmineCheckinHandler(CheckinProjectPanel checkinProjectPanel, CommitContext commitContext) {
        this.checkinProjectPanel = checkinProjectPanel;
        this.commitContext = commitContext;
    }

    @Override
    public void checkinSuccessful() {
        CommitMessageTemplateConfig cfg = CommitMessageTemplateConfig.getInstance(checkinProjectPanel.getProject());
        if (cfg.getDefaultToStatusId() != null && cfg.isChangeStatus()) {
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    UpdateIssueRequest request = new UpdateIssueRequest();
                    request.getIssue().setStatus_id(cfg.getDefaultToStatusId());
                    RpcUtils.PUT(cfg.getHost() + "issues/" + cfg.getCurrentIssue().getId()+ ".json?key=" + cfg.getKey(), request);
                    cfg.setCurrentIssue(null);
                    cfg.setChangeStatus(false);
                }
            })).start();
        }
    }
}
