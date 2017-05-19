package commitmessagetemplate.network.redmine

import com.alibaba.fastjson.JSON

/**
 * Created by edolphin on 17-5-19.
 */
class IssuesResponse {

    List<Issue> issues

    int total_count

    int offset

    int limit

    public static void main(String[] args) {
        println JSON.parseObject("{\"issues\":[{\"id\":3,\"project\":{\"id\":1,\"name\":\"test\"},\"tracker\":{\"id\":5,\"name\":\"你是傻逼吗\"},\"status\":{\"id\":1,\"name\":\"新建\"},\"priority\":{\"id\":2,\"name\":\"普通\"},\"author\":{\"id\":1,\"name\":\"Redmine Admin\"},\"subject\":\"123\",\"description\":\"123\",\"start_date\":\"2017-05-18\",\"done_ratio\":0,\"created_on\":\"2017-05-18T07:03:30Z\",\"updated_on\":\"2017-05-18T07:03:30Z\"},{\"id\":2,\"project\":{\"id\":1,\"name\":\"test\"},\"tracker\":{\"id\":4,\"name\":\"需求\"},\"status\":{\"id\":3,\"name\":\"已解决\"},\"priority\":{\"id\":2,\"name\":\"普通\"},\"author\":{\"id\":1,\"name\":\"Redmine Admin\"},\"assigned_to\":{\"id\":1,\"name\":\"Redmine Admin\"},\"subject\":\"目的地系统订单功能开发\",\"description\":\"目的地系统订单功能开发\",\"start_date\":\"2017-05-18\",\"done_ratio\":0,\"created_on\":\"2017-05-18T06:11:41Z\",\"updated_on\":\"2017-05-18T06:17:19Z\"},{\"id\":1,\"project\":{\"id\":1,\"name\":\"test\"},\"tracker\":{\"id\":2,\"name\":\"功能\"},\"status\":{\"id\":1,\"name\":\"新建\"},\"priority\":{\"id\":2,\"name\":\"普通\"},\"author\":{\"id\":1,\"name\":\"Redmine Admin\"},\"subject\":\"123123\",\"description\":\"123123\",\"start_date\":\"2017-05-18\",\"done_ratio\":0,\"created_on\":\"2017-05-18T06:07:51Z\",\"updated_on\":\"2017-05-18T06:07:51Z\"}],\"total_count\":3,\"offset\":0,\"limit\":25}", IssuesResponse.class)
        print("test")
    }
}
