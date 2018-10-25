package commitmessagetemplate.platform.tapd;

import commitmessagetemplate.CommitMessageTemplateConfig;
import commitmessagetemplate.platform.Templatable;

public class TapdConfigurable implements Templatable {
    public static final String PlatformName = "Tapd";

    public String userName;
    public String password;
    public String cookies;
    public String projectId;
    public String template;

    static {
        CommitMessageTemplateConfig.platformConfigClassMap.put(PlatformName, TapdConfigurable.class);
    }

    @Override
    public String getPlatformName() {
        return PlatformName;
    }

    @Override
    public String getTemplate() {
        return template;
    }
}
