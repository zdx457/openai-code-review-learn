package com.zdx.ai.sdk.testcase;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zdx.ai.sdk.types.utils.BearerTokenUtils;
import com.zdx.ai.sdk.types.utils.WXAccessTokenUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ApiTest {
    public static void main(String[] args) {
        String apiKeyScret = "68eed0df5cfa49798d1430eed248f1c3.RAHA1k881OEjDIms";
        String token = BearerTokenUtils.getToken(apiKeyScret);
        System.out.println(token);
    }
    @Test
    public void test_http() throws Exception {
        String apiKeyScret = "68eed0df5cfa49798d1430eed248f1c3.RAHA1k881OEjDIms";
        String token = BearerTokenUtils.getToken(apiKeyScret);
        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);

        String code = "1+1";

        String jsonInpuString = "{"
                + "\"model\":\"glm-4-flash\","
                + "\"messages\": ["
                + "    {"
                + "        \"role\": \"user\","
                + "        \"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: " + code + "\""
                + "    }"
                + "]"
                + "}";


        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInpuString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

//        System.out.println(content);
        // ====================== 核心：JSON 转 纯文本 ======================
        String jsonResult = content.toString();
        JSONObject jsonObject = JSON.parseObject(jsonResult);
        String reviewContent = jsonObject
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");

        System.out.println("\n===== AI 代码评审结果 =====");
        System.out.println(reviewContent);



    }

    @Test
    public void test_wx() {
        String accessToken = WXAccessTokenUtils.getAccessToken();
        System.out.println(accessToken);


        // 创建消息
        Message message = new Message();
        message.put("project","big-market");
        message.put("review","feat: 新加功能");

        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
        sendPostRequest(url, JSON.toJSONString(message));
    }

    private static void sendPostRequest(String urlString, String jsonBody) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
                String response = scanner.useDelimiter("\\A").next();
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Message {
        private String touser = "opo1P3DS0ApRx229mh5HW1g9y3PA";
        private String template_id = "scjRjtU7q43JuNU2ysWSR8i1EpRZ8EmGNFcWtaVo2Gw";
        private String url = "https://github.com/zdx457/log/blob/master/2026-03-25/neN0K1rapAXb.md";
        private Map<String, Map<String, String>> data = new HashMap<>();

        public void put(String key, String value) {
            data.put(key, new HashMap<String, String>() {
                {
                    put("value", value);
                }
            });
        }

        public String getTouser() {
            return touser;
        }

        public void setTouser(String touser) {
            this.touser = touser;
        }

        public String getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(String template_id) {
            this.template_id = template_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, Map<String, String>> getData() {
            return data;
        }

        public void setData(Map<String, Map<String, String>> data) {
            this.data = data;
        }
    }


}
