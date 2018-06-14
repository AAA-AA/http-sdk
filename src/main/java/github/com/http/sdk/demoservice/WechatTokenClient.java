package github.com.http.sdk.demoservice;

import github.com.http.sdk.anno.Http;
import github.com.http.sdk.anno.HttpParam;
import github.com.http.sdk.anno.RootApi;
import github.com.http.sdk.entity.response.TokenResp;


/**
 * @author : hongqiangren.
 * @since: 2018/6/12 18:02
 */
@RootApi(root = "https://api.weixin.qq.com")
public interface WechatTokenClient {

    /**
     * 获取accessToken接口
     * @param appId
     * @param secret
     * @param grantType
     */
    @Http(method = Http.Method.GET, path = "cgi-bin/token", request = Http.Content.JSON, response = Http.Content.JSON)
    TokenResp loadToken(@HttpParam(name = "appid") String appId, String secret, @HttpParam(name = "grant_type") String grantType);

}
