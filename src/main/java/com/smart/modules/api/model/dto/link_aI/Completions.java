package com.smart.modules.api.model.dto.link_aI;

import lombok.Data;

import java.util.List;
@Data
public class Completions {
    private String app_code;
    private List<Message> messages;
}
