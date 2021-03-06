package service;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.http.sdk.anno.Http;
import com.github.http.sdk.anno.RootApi;


/**
 * @author : hongqiangren.
 * @since: 2018/6/12 18:02
 */
@RootApi(root = "https://api.weixin.qq.com")
public interface MessageService {

    @Http(method = Http.Method.GET,path = "cgi-bin/template/api_set_industry",request = Http.Content.JSON, response = Http.Content.JSON)
    void setIndustry(Long brandId, @JSONField(name = "industry_id1") int industryId1, @JSONField(name = "industry_id2") int industryId2);

    /**
     * 获取用户基本信息
     *
     * @param openid  普通用户的标识，对当前公众号唯一
     * @param lang    返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return
     */
    @Http(path = "cgi-bin/user/info", method = Http.Method.GET, response = Http.Content.JSON)
    String loadUser(String accessToken, String openid, String lang);

}
