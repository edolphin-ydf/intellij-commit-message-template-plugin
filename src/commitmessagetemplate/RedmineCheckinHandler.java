package commitmessagetemplate;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import commitmessagetemplate.network.RpcUtils;
import commitmessagetemplate.network.redmine.UpdateIssueRequest;
import commitmessagetemplate.platform.redmine.RedmineConfigurable;
import commitmessagetemplate.platform.redmine.RuntimeData;

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
        RedmineConfigurable rc = (RedmineConfigurable)cfg.getCommitState().getConfig();
        IssueTrackerPlatformCenter center = IssueTrackerPlatformCenter.getInstance(checkinProjectPanel.getProject());
        String platform = cfg.getCommitState().selectedPlatform;

        if (platform.isEmpty() || platform.equals(RedmineConfigurable.PlatformName))
            return;
        RuntimeData runtimeData = (RuntimeData)center.getRuntimeData(platform);
        if (runtimeData == null)
            return;

        if (rc.getDefaultToStatusId() != null && runtimeData.isChangeStatus()) {
            (new Thread(() -> {
                UpdateIssueRequest request = new UpdateIssueRequest();
                request.getIssue().setStatus_id(rc.getDefaultToStatusId());
                RpcUtils.PUT(rc.getHost() + "issues/" + runtimeData.getCurrentIssue().getId()+ ".json?key=" + rc.getKey(), request);
                runtimeData.setCurrentIssue(null);
                runtimeData.setChangeStatus(false);
            })).start();
        }
    }
}
