package com.smart.modules.api.model.dto.link_aI;

import lombok.Data;

@Data
public class CompletionBody {
    private String prompt;
    private String token;
    private String appCode;
}
