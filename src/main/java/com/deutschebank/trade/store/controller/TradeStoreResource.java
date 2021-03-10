package com.deutschebank.trade.store.controller;

import com.deutschebank.trade.store.TradeStoreService;
import com.deutschebank.trade.store.entity.TradeStore;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/trade-store/")
@AllArgsConstructor
public class TradeStoreResource {

    private final TradeStoreService tradeStoreService;

    @PostMapping
    public TradeStore addTrade(@RequestBody TradeStore tradeStore) {
        return tradeStoreService.addTrade(tradeStore);
    }

    @GetMapping
    public List<TradeStore> getTrades(){
        return tradeStoreService.findAllTrades();
    }
}
