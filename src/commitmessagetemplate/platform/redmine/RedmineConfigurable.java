package commitmessagetemplate.platform.redmine;

import commitmessagetemplate.CommitMessageTemplateConfig;
import commitmessagetemplate.platform.Templatable;

import java.util.ArrayList;
import java.util.List;

public class RedmineConfigurable implements Templatable {
    public static final String PlatformName = "Redmine";


    public String host;
    public String key;
    public String template;
    public Long defaultToStatusId;
    public List<Long> getNotifyStatusIds = new ArrayList<>();

    static {
        CommitMessageTemplateConfig.platformConfigClassMap.put(PlatformName, RedmineConfigurable.class);
    }

    @Override
    public String getPlatformName() {
        return PlatformName;
    }

    @Override
    public String getTemplate() {
        return template;
    }


    // region getter/setter
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Long getDefaultToStatusId() {
        return defaultToStatusId;
    }

    public void setDefaultToStatusId(Long defaultToStatusId) {
        this.defaultToStatusId = defaultToStatusId;
    }
    // endregion getter/setter
}
