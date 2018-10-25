package commitmessagetemplate.network.redmine

import commitmessagetemplate.CommitMessageTemplateConfig
import commitmessagetemplate.util.VelocityHelper

/**
 * Created by edolphin on 17-5-19.
 */
class Issue implements commitmessagetemplate.domain.Issue {

    Long id

    Project project

    Tracker tracker

    Status status

    Priority priority

    Author author

    def subject

    def description

    def start_data

    def done_ratio

    Date created_on

    Date updated_on

    com.intellij.openapi.project.Project ideaProject

    @Override
    String toString() {
        CommitMessageTemplateConfig cfg = CommitMessageTemplateConfig.getInstance(ideaProject)
        String template = cfg.getState().getConfig().getTemplate()
        return VelocityHelper.fromTemplate(template, "issue", this)
    }

    String getId() {
        return id
    }


    String getTitle() {
        return subject
    }

    String getContent() {
        return description
    }
}
