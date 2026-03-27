package com.zdx.ai.sdk.infrastructure.openai.Impl;

import com.alibaba.fastjson2.JSON;
import com.zdx.ai.sdk.infrastructure.openai.IOpenAI;
import com.zdx.ai.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import com.zdx.ai.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;
import com.zdx.ai.sdk.types.utils.BearerTokenUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatGLM implements IOpenAI {

    // 请求地址 + 密钥
    private final String apiHost;
    private final String apiKeySecret;

    // 构造器注入配置
    public ChatGLM(String apiHost, String apiKeySecret) {
        this.apiHost = apiHost;
        this.apiKeySecret = apiKeySecret;
    }

    @Override
    public ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception {
        // 1. 生成 ChatGLM 鉴权 token
        String token = BearerTokenUtils.getToken(apiKeySecret);

        // ====================== 加在这里！======================
        System.out.println("===== 打印 API 地址 =====");
        System.out.println(apiHost);

        System.out.println("===== 打印请求 JSON =====");
        String json = JSON.toJSONString(requestDTO);
        System.out.println(json);
        // ======================================================

        // 2. 创建 HTTP 连接
        URL url = new URL(apiHost);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 请求方法：POST
        connection.setRequestMethod("POST");

        // 身份认证令牌
        connection.setRequestProperty("Authorization", "Bearer " + token);

        // 数据格式：JSON
        connection.setRequestProperty("Content-Type", "application/json");

        // 模拟浏览器请求
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 允许发送请求体数据
        connection.setDoOutput(true);

        // 3. 写入请求参数
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = JSON.toJSONString(requestDTO).getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        // 4. 读取响应
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }

        // 5. 关闭连接
        connection.disconnect();

        // 6. JSON 转对象返回
        return JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
    }
}