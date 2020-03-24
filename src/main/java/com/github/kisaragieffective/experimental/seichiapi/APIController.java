package com.github.kisaragieffective.experimental.seichiapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class APIController {
    @RequestMapping("/increase")
    public String increase() {
        return "Success: called increase";
    }
}
