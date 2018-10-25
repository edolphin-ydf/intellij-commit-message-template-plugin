package commitmessagetemplate.platform;

import com.intellij.openapi.project.Project;
import commitmessagetemplate.CommitMessageTemplateConfig;
import commitmessagetemplate.domain.Issue;
import commitmessagetemplate.platform.redmine.RedmineConfigurable;
import commitmessagetemplate.platform.tapd.TapdConfigurable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IssueManager {
    class Option {
        public boolean assignToMe;
    }

    class OptionBuilder {
        private Option option;

        public OptionBuilder SetAssignToMe(boolean assignToMe) {
            option.assignToMe = assignToMe;
            return this;
        }

        public Option build() {
            return option;
        }
    }

    void init(Project project, CommitMessageTemplateConfig.CommitState config, Option option, Consumer<List<Issue>> consumer);

    List<Issue> getIssues(Predicate<Issue> predicate);

    void refreshIssues(Consumer<List<Issue>> consumer);

    void refreshIssues(Option option, Consumer<List<Issue>> consumer);

    static IssueManager newIssueManager(String platform) {
        Class<? extends IssueManager> clazz = platformIssueMgrClassMap.get(platform);
        if (clazz == null)
            return null;

        try {
            return clazz.newInstance();
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    Map<String, Class<? extends IssueManager>> platformIssueMgrClassMap = new HashMap<>();
}
