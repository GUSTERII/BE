package com.usv.examtimetabling.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class DataConfiguration {

  @Value("${default.email.sender:}")
  private String defaultEmailSender;

}
