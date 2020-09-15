package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<String> context = Arrays.asList("事件1","事件2","事件3");
  @GetMapping("/qiu/list")
  public String getAllRsEvent(){
    return context.toString();
  }

  //@GetMapping()

}
