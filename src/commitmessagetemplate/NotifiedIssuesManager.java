package commitmessagetemplate;

import com.alibaba.fastjson.JSON;
import commitmessagetemplate.network.redmine.Issue;
import commitmessagetemplate.util.file.FileReaderUtil;
import commitmessagetemplate.util.file.FileWriter;

/**
 * Created by edolphin on 17-5-23.
 */
public class NotifiedIssuesManager {

    static NotifiedIssues notifiedIssues;

    static String filePath = ".idea/redmine_notified_issues.json";

    synchronized public static NotifiedIssues getNotifiedIssues() {
        if (notifiedIssues == null)
            load();
        return notifiedIssues;
    }


    public static void addNotifiedIssue(Issue issue) {
        getNotifiedIssues().notifiedIssues.add(new NotifiedIssues.NotifiedIssue(issue.getId(), issue.getStatus().getId()));
    }

    public static boolean isIssueNotified(Issue issue) {
        return getNotifiedIssues().notifiedIssues.contains(new NotifiedIssues.NotifiedIssue(issue.getId(), issue.getStatus().getId()));
    }

    public static void load() {
        String json = FileReaderUtil.readFile(filePath);
        if (json == null || json.isEmpty()) {
            notifiedIssues = new NotifiedIssues();
            return;
        }

        notifiedIssues = JSON.parseObject(json, NotifiedIssues.class);
    }

    public static void save() {
        FileWriter.writeStringToFile(JSON.toJSONString(notifiedIssues), filePath);
    }
}
