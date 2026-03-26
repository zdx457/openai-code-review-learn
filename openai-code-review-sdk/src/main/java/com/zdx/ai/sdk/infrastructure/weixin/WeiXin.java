package com.zdx.ai.sdk.infrastructure.weixin;

import com.alibaba.fastjson2.JSON;
import com.zdx.ai.sdk.infrastructure.weixin.dto.TemplateMessageDTO;
import com.zdx.ai.sdk.types.utils.WXAccessTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

import static com.zdx.ai.sdk.types.utils.WXAccessTokenUtils.getAccessToken;

/**
 * @author zdx
 * @description
 * @create 2026-03-26 15:32
 */
public class WeiXin {

    private final Logger logger = LoggerFactory.getLogger(WeiXin.class);

    private final String appid;
    private final String secret;
    private final String touser;
    private final String template_id;

    public WeiXin(String appid, String secret, String touser, String templateId) {
        this.appid = appid;
        this.secret = secret;
        this.touser = touser;
        template_id = templateId;
    }


    public void sendTemplateMessage(String logUrl, Map<String ,Map<String,String>> data)throws Exception{
        String accessToken = WXAccessTokenUtils.getAccessToken(appid, secret);

        // 创建消息
        TemplateMessageDTO templateMessageDTO = new TemplateMessageDTO();
        templateMessageDTO.setUrl(logUrl);
        templateMessageDTO.setData(data);

        String urlString = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);


        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = JSON.toJSONString(templateMessageDTO).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name())) {
            String response = scanner.useDelimiter("\\A").next();
            logger.info("openai-code-review weixin temple message {}", response);
        }

    }
}
