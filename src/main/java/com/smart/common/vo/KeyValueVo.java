package com.smart.common.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class KeyValueVo
 {
    private String key;
    private Long value;

}
