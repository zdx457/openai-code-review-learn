package com.zdx.ai.sdk;


import com.zdx.ai.sdk.domain.service.Impl.OpenAiCodeReviewService;
import com.zdx.ai.sdk.infrastructure.git.GitCommand;
import com.zdx.ai.sdk.infrastructure.openai.Impl.ChatGLM;
import com.zdx.ai.sdk.infrastructure.weixin.WeiXin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAiCodeReview {

    // 日志
    private static final Logger logger = LoggerFactory.getLogger(OpenAiCodeReview.class);

    // 微信配置
    private String weixin_appid = "wx8352e48dddf96bd5";
    private String weixin_secret = "3e304b7e83747e6e04a5aceeed5f40c6";
    private String weixin_touser = "opo1P3DS0ApRx229mh5HW1g9y3PA";
    private String weixin_template_id = "WCddLPcekcIQC4ZYzHnnCz4NxVnx_B9AtuIndKjkfoY";

    // ChatGLM 配置
    private String chatglm_apiHost = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private String chatglm_apiKeySecret = "";

    // Github 配置
    private String code_review_log_uri;
    private String github_token;

    // 工程配置 - 自动获取
    private String github_project;
    private String github_branch;
    private String github_author;



    public static void main(String[] args) throws Exception { // 本文件用于gitHub的actions来执行的文件
        GitCommand gitCommand = new GitCommand(
                getEnv("CODE_REVIEW_LOG_URI"), // ctrl+shift+U  大小写互变
                getEnv("GITHUB_TOKEN"),
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("COMMIT_MESSAGE")
        );

        //项目：{{repo_name.DATA}} 分支：{{branch_name.DATA}} 作者：{{commit_author.DATA}} 说明：{{commit_message.DATA}}
        WeiXin weiXin = new WeiXin(
                getEnv("WEIXIN_APPID"),
                getEnv("WEIXIN_SECRET"),
                getEnv("WEIXIN_TOUSER"),
                getEnv("WEIXIN_TEMPLATE_ID")
        );
        ChatGLM chatGLM = new ChatGLM(
                getEnv("CHATGLM_APIHOST"),
                getEnv("CHATGLM_APIKEYSECRET")
        );

        OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitCommand, chatGLM,weiXin);
        openAiCodeReviewService.exec();
        logger.info("openai-code-review done!");

    }

    private static String getEnv(String key) throws Exception {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("value is null");
        }
        return value;
    }

}
