package com.deutschebank.trade.store;

import com.deutschebank.trade.store.entity.TradeStore;
import com.deutschebank.trade.store.repository.TradeStoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TradeStoreService {

    private TradeStoreRepository tradeStoreRepository;

    public TradeStore addTrade(TradeStore tradeStore) throws Exception {
        Optional<TradeStore> trade = tradeStoreRepository.findById(tradeStore.getTradeId());
        Date todayDate = getTodayDate();
        verifyMaturityDate(tradeStore, todayDate);
        if (trade.isPresent()) {
            verifyVersion(tradeStore, trade.get());
            tradeStore.setCreatedDate(trade.get().getCreatedDate());
            tradeStore.setExpired(trade.get().getExpired());
        } else {
            tradeStore.setCreatedDate(todayDate);
            tradeStore.setExpired("N");
        }
        return tradeStoreRepository.save(tradeStore);
    }

    private void verifyMaturityDate(TradeStore tradeStore, Date todayDate) throws Exception {
        if (tradeStore.getMaturityDate().before(todayDate)) {
            throw new Exception("Maturity Date should be after current date");
        }
    }

    private void verifyVersion(TradeStore tradeStore, TradeStore trade) throws Exception {
        if (tradeStore.getVersion() < trade.getVersion()) {
            throw new Exception("Trade version should be greater");
        }
    }

    private Date getTodayDate() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant());
    }

    public List<TradeStore> findAllTrades() {
        return tradeStoreRepository.findAll();
    }
}
