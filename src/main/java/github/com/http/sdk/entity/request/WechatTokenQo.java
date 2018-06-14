package github.com.http.sdk.entity.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 16:51
 */
@Data
@Builder
public class WechatTokenQo {
    @JSONField(name = "appid")
    private String appId;
    @JSONField(name = "openid")
    private String openId;
    private String secret;
    private String lang;
}
