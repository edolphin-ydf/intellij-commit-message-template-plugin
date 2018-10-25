package commitmessagetemplate;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import commitmessagetemplate.network.RpcUtils;
import commitmessagetemplate.network.redmine.Issue;
import commitmessagetemplate.network.redmine.IssuesResponse;
import commitmessagetemplate.util.NotificationHelper;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by edolphin on 17-5-23.
 */
public class RedmineIssuseBackgroundNotifier implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        NotifiedIssuesManager.filePath = project.getPresentableUrl() + "/" + NotifiedIssuesManager.filePath;
        (new Thread(new Runnable() {
            @Override
            public void run() {
//                while(true) {
//                    try {
//                        Thread.sleep(10_000L);
//                    } catch (InterruptedException e) {
//                        continue;
//                    }
//
//                    try {
//                        CommitMessageTemplateConfig cfg = CommitMessageTemplateConfig.getInstance(project);
//                        if (cfg.getHost() == null || cfg.getHost().equals("") ||
//                                cfg.getKey() == null || cfg.getKey().equals("") ||
//                                cfg.getNotifyStatusIds().isEmpty())
//                            continue;
//
//                        Map<String, Object> params = new HashMap<String, Object>();
//                        params.put("key", cfg.getKey());
//                        params.put("assigned_to_id", "me");
//                        params.put("status_id", cfg.getNotifyStatusIds().stream().map(Object::toString).collect(Collectors.joining("|")));
//                        IssuesResponse issuesResponse = RpcUtils.getResponseFromServerGET(cfg.getHost() + "issues.json", IssuesResponse.class, params);
//
//                        boolean needSave = false;
//                        for (Issue issue : issuesResponse.getIssues()) {
//                            if (issue.getCreated_on() != null &&
//                                    issue.getCreated_on().after(new Date((new Date()).toInstant().minus(1, ChronoUnit.DAYS).getEpochSecond() * 1000L)) &&
//                                    !NotifiedIssuesManager.isIssueNotified(issue)) {
//                                NotificationHelper.info("#" + issue.getId() + "【" + issue.getStatus().getName()+ "】" + "【" + issue.getAuthor().getName()+ "】", issue.getSubject().toString());
//                                NotifiedIssuesManager.addNotifiedIssue(issue);
//                                needSave = true;
//                            }
//                            else if (issue.getUpdated_on() != null &&
//                                    issue.getUpdated_on().after(new Date((new Date()).toInstant().minus(1, ChronoUnit.DAYS).getEpochSecond() * 1000L)) &&
//                                    !NotifiedIssuesManager.isIssueNotified(issue)) {
//                                NotificationHelper.info("#" + issue.getId() + "【" + issue.getStatus().getName()+ "】" + "【" + issue.getAuthor().getName()+ "】", issue.getSubject().toString());
//                                NotifiedIssuesManager.addNotifiedIssue(issue);
//                                needSave = true;
//                            }
//                        }
//
//                        if (needSave)
//                            NotifiedIssuesManager.save();
//                    } catch (Throwable e) {
////                        NotificationHelper.error("", e.toString());
//                        continue;
//                    }
//                }
            }
        })).start();
    }
}
