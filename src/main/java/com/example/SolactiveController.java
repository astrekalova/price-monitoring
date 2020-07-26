package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class SolactiveController {

    private final Logger log = LoggerFactory.getLogger(SolactiveController.class);

    static final long WINDOW_SIZE_MS = 60 * 1000;
    static final long REFRESH_SIZE_MS = 60 * 1000;

    private StatisticsService statisticsService =
            new StatisticsService(WINDOW_SIZE_MS, REFRESH_SIZE_MS);

    @PostMapping("/tick")
    public ResponseEntity newTick(@Valid @RequestBody Tick tick) {
        long now = System.currentTimeMillis();

        if (tick.getTimestamp() + WINDOW_SIZE_MS < now) {
            log.info("Ignored tick {} because it's too old", tick);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            log.info("Accepted tick {}", tick);
            statisticsService.updateStatistics(tick);
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }

    @GetMapping("/statistics")
    public Result getAggregatedStatistics() {
        return statisticsService.getAggregatedStatistics();
    }

    @RequestMapping(value = "/statistics/{instrument}", method = GET)
    public Result getStatisticsForInstrument(@PathVariable String instrument) {
        return statisticsService.getStatisticsForInstrument(instrument);
    }
}