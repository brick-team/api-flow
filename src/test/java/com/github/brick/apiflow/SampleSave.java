package com.github.brick.apiflow;

import com.github.brick.apiflow.model.rest.ApiEntity;
import com.github.brick.apiflow.repo.ApiEntityRepo;
import com.github.brick.apiflow.repo.FlowEntityRepo;
import com.github.brick.apiflow.repo.ResultEntityRepo;
import com.google.gson.Gson;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SampleSave {
  @Autowired
  private ApiEntityRepo apiEntityRepo;
  @Autowired
  private FlowEntityRepo flowEntityRepo;
  @Autowired
  private ResultEntityRepo resultEntityRepo;
  Gson gson = new Gson();
  @Test
  public void hh(){
    // getAgeApi
    Optional<ApiEntity> getAgeApi = apiEntityRepo.findById("642a40231db3cf100dbdfca3");
    if (getAgeApi.isPresent()) {
      System.out.println(gson.toJson(getAgeApi.get()));
    }
    Optional<ApiEntity> getUsApi = apiEntityRepo.findById("642a40231db3cf100dbdfca4");
    if (getUsApi.isPresent()) {
      System.out.println(gson.toJson(getUsApi.get()));
    }

    System.out.println();
  }
}
