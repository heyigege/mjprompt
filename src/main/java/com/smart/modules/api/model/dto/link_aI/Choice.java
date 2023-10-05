package com.smart.modules.api.model.dto.link_aI;

import lombok.Data;

@Data
public class Choice {
    private int index;
    private Message message;
    private String finish_reason;
}
