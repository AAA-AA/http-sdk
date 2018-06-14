import github.com.http.sdk.demoservice.WechatTokenClient;
import github.com.http.sdk.entity.response.TokenResp;
import github.com.http.sdk.proxy.HttpProxy;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 08:20
 */
public class HttpTest {

    public static void main(String[] args) {

        WechatTokenClient wechatApi = HttpProxy.create().proxy(WechatTokenClient.class);
        String appId = "";//微信appid
        String secret = "";//微信secret
        String grantType = "client_credential";
        TokenResp user = wechatApi.loadToken(appId, secret, grantType);


    }


}
