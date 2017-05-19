package commitmessagetemplate.network;

/**
 * Created by edolphin on 17-3-1.
 */
public interface BaseQueryRequest {

    /**
     * name=value&name1=value1&....
     * @return
     */
    String toQueryString();
}
