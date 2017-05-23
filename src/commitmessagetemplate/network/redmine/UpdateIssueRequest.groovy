package commitmessagetemplate.network.redmine

import commitmessagetemplate.network.BaseJsonRequest

/**
 * Created by edolphin on 21/05/2017.
 */
class UpdateIssueRequest implements BaseJsonRequest {

    class UpdateIssue {
        long status_id
    }

    UpdateIssue issue

    UpdateIssueRequest() {
        issue = new UpdateIssue()
    }
}
