package commitmessagetemplate.network.redmine

import commitmessagetemplate.CommitMessageTemplateConfig
import commitmessagetemplate.util.VelocityHelper

/**
 * Created by edolphin on 17-5-19.
 */
class Issue {

    def id

    Project project

    Tracker tracker

    Status status

    Priority priority

    Author author

    def subject

    def description

    def start_data

    def done_ratio

    def create_on

    def updated_on

    com.intellij.openapi.project.Project ideaProject

    @Override
    public String toString() {
        CommitMessageTemplateConfig cfg = CommitMessageTemplateConfig.getInstance(ideaProject);
        return VelocityHelper.fromTemplate(cfg.getTemplate(), "issue", this);
    }
}
