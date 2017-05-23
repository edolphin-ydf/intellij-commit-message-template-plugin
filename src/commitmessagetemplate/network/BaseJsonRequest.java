package commitmessagetemplate.network;

import com.alibaba.fastjson.JSON;

/**
 * Created by edolphin on 17-3-2.
 */
public interface BaseJsonRequest {

    /**
     * 返回一个json string
     * @return
     */
    default String toJsonString() {
        return JSON.toJSONString(this);
    }
}
