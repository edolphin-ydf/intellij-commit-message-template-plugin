package commitmessagetemplate.platform.redmine;

import com.intellij.openapi.project.Project;
import commitmessagetemplate.CommitMessageTemplateConfig;
import commitmessagetemplate.domain.Issue;
import commitmessagetemplate.network.RpcUtils;
import commitmessagetemplate.network.redmine.IssuesResponse;
import commitmessagetemplate.platform.IssueManager;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RedmineIssueManagerImpl implements IssueManager {
    private Project project;
    private Option option;

    private IssuesResponse issuesResponse;
    private String host;
    private Map<String, Object> parameter = new HashMap<>();

    private static String KEY_ASSIGNED_TO_ID= "assigned_to_id";

    static {
        IssueManager.platformIssueMgrClassMap.put(RedmineConfigurable.PlatformName, RedmineIssueManagerImpl.class);
    }

    @Override
    public void init(Project project, CommitMessageTemplateConfig.CommitState config, Option option, Consumer<List<Issue>> consumer) {
        this.project = project;
        RedmineConfigurable cfg = (RedmineConfigurable) config.getConfig();
        if (cfg == null)
            return;
        this.option = option;

        host = cfg.getHost();
        String key = cfg.getKey();

        if (host == null || host.equals("")) {
            JOptionPane.showMessageDialog(null, "please set host first, in File->Settings->Tools->Commit Message Template");
            return;
        }
        if (key == null || key.equals("")) {
            JOptionPane.showMessageDialog(null, "please set redmine api key first, in File->Settings->Tools->Commit Message Template. " +
                    "You can find your API key on your account page ( /my/account ) when logged in, on the right-hand pane of the default layout.");
            return;
        }

        parameter.put("key", key);

        this.refreshIssues(consumer);
    }

    @Override
    public List<Issue> getIssues(Predicate<Issue> filter) {
        if (filter == null) {
            return issuesResponse.getIssues().stream().map(i -> (Issue)i).collect(Collectors.toList());
        } else {
            return issuesResponse.getIssues().stream().map(i -> (Issue)i).filter(filter).collect(Collectors.toList());
        }
    }

    @Override
    public void refreshIssues(Consumer<List<Issue>> consumer) {
        Thread thread = new Thread(() -> {
            if (option.assignToMe) {
                parameter.put(KEY_ASSIGNED_TO_ID, "me");
            } else {
                parameter.remove(KEY_ASSIGNED_TO_ID);
            }
            this.issuesResponse = RpcUtils.getResponseFromServerGET(host + "issues.json",  IssuesResponse.class, parameter);
            this.issuesResponse.getIssues().forEach(i -> i.setIdeaProject(project));
            if (consumer != null) {
                consumer.accept(this.getIssues(null));
            }
        });
        thread.start();
    }

    @Override
    public void refreshIssues(Option option, Consumer<List<Issue>> consumer) {
        this.option = option;
        this.refreshIssues(consumer);
    }
}
