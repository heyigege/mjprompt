package com.smart.modules.api.service;

import com.smart.modules.api.model.dto.link_aI.CompletionBody;

public interface LinkAIService {

    String getCompletions(CompletionBody completionBody);
}
