package com.zdx.ai.sdk;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zdx.ai.sdk.types.utils.BearerTokenUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OpenAiCodeReview {
    public static void main(String[] args) throws Exception { // 本文件用于gitHub的actions来执行的文件
        System.out.println("测试执行");

        // 1.代码检出

        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1","HEAD");
        processBuilder.directory(new File("."));
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder diffCode = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            diffCode.append(line);
        }

        int exitCode = process.waitFor();
        System.out.println("Exit with code: " + exitCode);
        System.out.println("diff code"+diffCode.toString());

        // 2. chatglm 代码评审
        String log = codeReview(diffCode.toString());
//        String log = codeReview("1+1");
        System.out.println("log: "+log);

    }

    private static String codeReview(String diffCode) throws Exception {
        String apiKeyScret = "68eed0df5cfa49798d1430eed248f1c3.RAHA1k881OEjDIms";
        String token = BearerTokenUtils.getToken(apiKeyScret);
        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);

        // 用 JSONObject 自动构建 JSON，不会出错！
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "glm-4-flash");

        JSONArray messages = new JSONArray();
        JSONObject msg = new JSONObject();
        msg.put("role", "user");
        msg.put("content", "你是一个高级编程架构师，请根据git diff记录，严格评审代码。git diff内容：" + diffCode);
        messages.add(msg);

        requestBody.put("messages", messages);
        String jsonInputString = requestBody.toJSONString();


        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
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
//        System.out.println(reviewContent);


        return reviewContent;

    }
}
