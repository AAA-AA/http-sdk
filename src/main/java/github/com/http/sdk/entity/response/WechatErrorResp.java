package github.com.http.sdk.entity.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 16:55
 */
@Data
public class WechatErrorResp {

    @JSONField(name = "errcode")
    private Integer errCode;

    @JSONField(name = "errmsg")
    private String errMsg;
}
