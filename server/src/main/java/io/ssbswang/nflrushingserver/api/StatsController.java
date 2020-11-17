package io.ssbswang.nflrushingserver.api;

import io.ssbswang.nflrushingserver.models.DataDTO;
import io.ssbswang.nflrushingserver.models.SortKey;
import java.util.Arrays;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for /stats endpoint with only GET implementation.
 * CORS is enabled at the endpoint level.
 */
@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "*")
public class StatsController {

  private static final Logger LOG = LoggerFactory.getLogger(StatsController.class);

  @Autowired
  private DataDTO[] data;

  @GetMapping()
  public ResponseEntity<Object> getAllStats(
      @RequestParam(name = "id", required = false) String id,
      @RequestParam(name = "order", required = false) SortKey order,
      @RequestParam(name = "nameText", required = false) String nameText) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // make shallow copy to store mutated data
    DataDTO[] processedData = data.clone();
    boolean asc = order == SortKey.ASC;

    if (id != null && order != null) {
      // the following uses Java built-in merge-sort so we do not have to implement our own
      switch (id) {
        case "Yds" :
          Arrays.sort(processedData, asc ? Comparator.comparing(DataDTO::compareYds) :
              Comparator.comparing(DataDTO::compareYds).reversed());
          break;
        case "Lng" :
          Arrays.sort(processedData, asc ? Comparator.comparing(DataDTO::compareLng) :
              Comparator.comparing(DataDTO::compareLng).reversed());
          break;
        case "TD" :
          Arrays.sort(processedData, asc ? Comparator.comparing(DataDTO::compareTd) :
              Comparator.comparing(DataDTO::compareTd).reversed());
          break;
      }
      LOG.info("processed sorting data for 'id={}', 'sort={}'", id, order.name());
    }

    if (nameText != null && !nameText.isEmpty()) {
      processedData = Arrays.stream(processedData)
          .filter(row -> row.player.contains(nameText))
          .toArray(DataDTO[]::new);
      LOG.info("processed name searching for '{}'", nameText);
    }
    return new ResponseEntity<>(processedData, headers, HttpStatus.OK);
  }
}