package io.ssbswang.nflrushingserver;

import io.ssbswang.nflrushingserver.models.DataDTO;
import io.ssbswang.nflrushingserver.utils.Utils;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for loading and parsing data source file during compilation to prevent
 * unnecessary additional data parsing. The main assumption used here is the data file will not be
 * frequently modified.
 */
@Configuration
public class DataConfig {

  private static final String DATA_PATH = "data/rushing.json";

  @Bean
  public DataDTO[] getDataList() {
    return Utils.jsonToPojo(Utils.readFile(DATA_PATH));
  }

}
