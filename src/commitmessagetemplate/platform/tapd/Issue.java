package commitmessagetemplate.platform.tapd;

public class Issue implements commitmessagetemplate.domain.Issue {
    String id;
    String title;
    String content;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }
}
