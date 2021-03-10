package com.deutschebank.trade.store;

import com.deutschebank.trade.store.entity.TradeStore;
import com.deutschebank.trade.store.repository.TradeStoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TradeStoreService {

    private TradeStoreRepository tradeStoreRepository;

    public TradeStore addTrade(TradeStore tradeStore) {
        return tradeStoreRepository.save(tradeStore);
    }

    public List<TradeStore> findAllTrades() {
        return tradeStoreRepository.findAll();
    }
}
