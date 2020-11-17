package io.ssbswang.nflrushingserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ssbswang.nflrushingserver.models.DataDTO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Utils {

  private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

  /**
   * Reads from file and concatenates the data as a string. Each line is trimmed to reduce char count.
   * @param location the file location
   * @return file data as string, null if file is a folder, unreadable, not found and etc
   */
  public static String readFile(String location) {
    Resource resource = new ClassPathResource(location);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));) {
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line.trim());
      }
      return sb.toString();
    } catch (IOException e) {
      LOG.error(e.getMessage());
      return null;
    }
  }

  /**
   * Converts a raw JSON string to deserialized array of data DTO.
   * @param dataStr the data string
   * @return array of DataDTO
   */
  public static DataDTO[] jsonToPojo(String dataStr) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(dataStr, DataDTO[].class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Simple helper method to clean up some data values for comparison.
   * @param str the raw data string
   * @return the parsed float value
   */
  public static float strToFloat(String str) {
    return Float.parseFloat(str.replace(",", "").replace("T", "").trim());
  }
}