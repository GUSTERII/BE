package com.usv.examtimetabling.seeding;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class OrarApiService {

  private final RestTemplate restTemplate;

  public OrarApiService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public <T> List<T> fetchApiData(String url, Class<T[]> responseType) {
    try {
      T[] response = restTemplate.getForObject(url, responseType);
      return Arrays.asList(response);
    } catch (Exception e) {
      // Handle exceptions (log or rethrow)
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  public String fetchRawApiResponse(String url) {
    try {
      return restTemplate.getForObject(url, String.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch API data from: " + url, e);
    }
  }
}
