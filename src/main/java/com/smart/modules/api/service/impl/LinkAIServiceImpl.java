package com.smart.modules.api.service.impl;


import com.google.gson.Gson;
import com.smart.core.http.Exchange;
import com.smart.core.http.HttpRequest;
import com.smart.modules.api.model.dto.link_aI.*;
import com.smart.modules.api.service.LinkAIService;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LinkAIServiceImpl implements LinkAIService {

	@Override
	public String getCompletions(CompletionBody completionBody) {
		StringBuffer resString = new StringBuffer();

		Completions completions = new Completions();
		completions.setApp_code(completionBody.getAppCode());
		Message message = new Message();
		message.setContent(completionBody.getPrompt());
		message.setRole("user");
		ArrayList<Message> messageList = new ArrayList<>();
		messageList.add(message);
		completions.setMessages(messageList);
		Gson gson = new Gson();
		CompletionsResult completionsResult = HttpRequest.post("https://api.link-ai.chat/v1/chat/completions")
			.addHeader("Authorization", "Bearer " + completionBody.getToken())
			.bodyString(gson.toJson(completions))//表单内容
			.execute().onSuccess(response -> response.asValue(CompletionsResult.class));
		List<Choice> choices = completionsResult.getChoices();
		choices.stream().forEach((item) -> {
			resString.append(item.getMessage().getContent());
		});

		return resString.toString();
	}


}
