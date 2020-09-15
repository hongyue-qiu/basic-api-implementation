package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList(){
    List<RsEvent> tempRsList = new ArrayList<>();
    tempRsList.add(new RsEvent("第一条事件","无分类"));
    tempRsList.add(new RsEvent("第二条事件","无分类"));
    tempRsList.add(new RsEvent("第三条事件","无分类"));
    return tempRsList;
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventByRange(@RequestParam(required = false) Integer start, @RequestParam(required = false)Integer end){
    if (start == null || end == null){
      return  rsList;
    }
    return rsList.subList(start-1,end);
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEventByRange(@PathVariable int index){
    return rsList.get(index -1);
  }

  @GetMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent) throws JsonProcessingException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    RsEvent rsEvent = objectMapper.readValue(rsEventStr,RsEvent.class);
    rsList.add(rsEvent);
  }

  @PutMapping("/rs/modify/{index}")
  public void modifyResearch(@PathVariable int index, @RequestBody RsEvent rsEvent) {

    RsEvent reEventModified = rsList.get(index - 1);
    if (!rsEvent.getEventName().isEmpty()) {
      reEventModified.setEventName(rsEvent.getEventName());
    }
    if (!rsEvent.getKeyword().isEmpty()) {
      reEventModified.setKeyword(rsEvent.getKeyword());
    }

    rsList.set(index - 1, reEventModified);
  }

  @DeleteMapping("/rs/delete/{index}")
  public void deleteResearch(@PathVariable int index) {
    rsList.remove(index - 1);
  }


}
