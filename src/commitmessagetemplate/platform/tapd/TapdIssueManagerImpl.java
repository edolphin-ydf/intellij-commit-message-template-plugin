package commitmessagetemplate.platform.tapd;

import com.intellij.openapi.project.Project;
import commitmessagetemplate.CommitMessageTemplateConfig;
import commitmessagetemplate.domain.Issue;
import commitmessagetemplate.platform.IssueManager;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TapdIssueManagerImpl implements IssueManager {
    TapdConfigurable cfg;
    List<commitmessagetemplate.platform.tapd.Issue> issues = new ArrayList<>();
    List<Cookie> cookies = new ArrayList<>();

    static {
        IssueManager.platformIssueMgrClassMap.put(TapdConfigurable.PlatformName, TapdIssueManagerImpl.class);
    }

    @Override
    public void init(Project project, CommitMessageTemplateConfig.CommitState config, Option option, Consumer<List<Issue>> consumer) {
        if (!config.selectedPlatform.equals(TapdConfigurable.PlatformName))
            return;

        cfg = (TapdConfigurable) config.getConfig();

        this.parseCookies(cfg.cookies);
        this.refreshIssues(consumer);
    }

    @Override
    public List<Issue> getIssues(Predicate<Issue> filter) {
        if (filter == null)
            return issues.stream().map(i -> (Issue) i).collect(Collectors.toList());
        else
            return issues.stream().map(i -> (Issue) i).filter(filter).collect(Collectors.toList());
    }

    @Override
    public void refreshIssues(Consumer<List<Issue>> consumer) {
        // TODO
    }

    @Override
    public void refreshIssues(Option option, Consumer<List<Issue>> consumer) {
        this.refreshIssues(consumer);
    }

    private void parseCookies(String cookies) {
        String[] cookiesStr = cookies.split(";");
        for (String s : cookiesStr) {
            String[] cookieStr = s.split("=");
            if (cookieStr.length != 2)
                continue;
            Cookie cookie = new BasicClientCookie(cookieStr[0], cookieStr[1]);
            this.cookies.add(cookie);
        }
    }

    private Map<String, String> cookiesToMap() {
        Map<String, String> res = new HashMap<>();
        for (Cookie cookie : this.cookies) {
            res.put(cookie.getName(), cookie.getValue());
        }
        return res;
    }
}
