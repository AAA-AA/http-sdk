package service;


import com.github.http.sdk.anno.Http;
import com.github.http.sdk.anno.HttpParam;
import com.github.http.sdk.anno.RootApi;
import com.github.http.sdk.anno.TempParam;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 18:02
 */
@RootApi(root = "https://api.weixin.qq.com",filter = AccessFilter.class)
public interface WechatTokenClient {

    /**
     * 获取accessToken接口
     * @param appId
     * @param secret
     * @param grantType
     */
    @Http(method = Http.Method.GET, path = "cgi-bin/token", request = Http.Content.JSON, response = Http.Content.JSON)
    String loadToken(@TempParam Long brandId, @HttpParam(name = "appid") String appId, String secret, @HttpParam(name = "grant_type") String grantType);

}
