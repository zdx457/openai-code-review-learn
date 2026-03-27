package com.zdx.ai.sdk.infrastructure.git;

import com.zdx.ai.sdk.types.utils.RandomStringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GitCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String githubReviewLogUrl;
    private final String githubToken;
    private final String project;
    private final String branch;
    private final String author;
    private final String message;


    public GitCommand(String githubReviewLogUrl, String githubToken, String project, String branch, String author, String message) {
        this.githubReviewLogUrl = githubReviewLogUrl;
        this.githubToken = githubToken;
        this.project = project;
        this.branch = branch;
        this.author = author;
        this.message = message;
    }

    public String diff() throws IOException, InterruptedException {
        // openai.itedus.cn
        ProcessBuilder logprocessBuilder = new ProcessBuilder("git","log","-1","--pretty=format:%H"); // git命令

        logprocessBuilder.directory(new File("."));
        Process logProcess = logprocessBuilder.start();
        BufferedReader logReader = new BufferedReader(new InputStreamReader(logProcess.getInputStream()));
        String latestCommitHash = logReader.readLine();
        logReader.close();
        logProcess.waitFor();


        ProcessBuilder diffProcessBuilder = new ProcessBuilder("git", "diff", latestCommitHash + "^", latestCommitHash);
        diffProcessBuilder.directory(new File("."));
        Process diffProcess = diffProcessBuilder.start();

        StringBuilder diffCode = new StringBuilder();
        BufferedReader diffReader = new BufferedReader(new InputStreamReader(diffProcess.getInputStream()));
        String line;
        while((line=diffReader.readLine())!=null){
            diffCode.append(line).append("\n");
        }
        diffReader.close();

        int exitCode = diffProcess.waitFor();
        if(exitCode!=0){
            throw new RuntimeException("Failed to get diff, exit diff"+exitCode);
        }

        return diffCode.toString();
    }



    public String commitAndPush(String recommend) throws GitAPIException, IOException {
        Git git = Git.cloneRepository()
                .setURI(githubReviewLogUrl+".git")
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, ""))
                .call();

        // 创建分支
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String dateFolderName = sdf.format(new Date());

        File dateFolder = new File("repo/" + dateFolderName);
        if(!dateFolder.exists()){
            dateFolder.mkdir();
        }

        String fileName = project+"-"+branch+"-"+author+System.currentTimeMillis()+"-"+ RandomStringUtils.randomString(4)+".md";

        File newfile = new File(dateFolder,fileName);

        try (FileWriter writer = new FileWriter(newfile)) {
            writer.write(recommend);
        }

        git.add().addFilepattern(dateFolderName+"/"+fileName).call();
        git.commit().setMessage("ADD new file").call();
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, "")).call();

        logger.info("openai-code-review git commit and push done!{}", fileName);

        return githubReviewLogUrl + "blob/Master/" + dateFolderName + "/" +fileName;
    }

    public  String getProject(){
        return project;
    }
    public  String getBranch(){
        return branch;
    }

    public  String getAuthor(){
        return author;
    }
    public  String getMessage(){
        return message;
    }


}
