package com.github.http.sdk.entity.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 16:52
 */
@Data
public class TokenResp extends WechatErrorResp{
    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "expires_in")
    private Integer expireIn;
}
