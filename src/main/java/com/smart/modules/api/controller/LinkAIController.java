package com.smart.modules.api.controller;

import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.tool.api.R;
import com.smart.modules.api.model.dto.link_aI.CompletionBody;
import com.smart.modules.api.service.impl.LinkAIServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/link_aI")
@Slf4j
public class LinkAIController extends SmartController {
    @Resource
    private LinkAIServiceImpl linkAIService;
    @PostMapping("/getCompletions")

    public R<String> getCompletions(@RequestBody CompletionBody completionBody) {
        String completions = linkAIService.getCompletions(completionBody);
        return R.success(completions);

    }
}
