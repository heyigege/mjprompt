package com.smart.modules.api.model.dto.link_aI;

import lombok.Data;

import java.util.List;

@Data
public class CompletionsResult {
    private List<Choice> choices;
    private Usage usage;
}
