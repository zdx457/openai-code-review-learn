package com.zdx.ai.sdk.infrastructure.openai;


import com.zdx.ai.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import com.zdx.ai.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;

public interface IOpenAI {
    ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO)throws Exception;
}
