package com.deutschebank.trade.store;

import com.deutschebank.trade.store.entity.TradeStore;
import com.deutschebank.trade.store.repository.TradeStoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeStoreServiceTest {

    @Mock
    private TradeStoreRepository tradeStoreRepository;

    @InjectMocks
    private TradeStoreService tradeStoreService;

    @Test
    void verifyAddTradeSavesNewTradeWithValidMaturityDate() throws Exception {
        when(tradeStoreRepository.findById(any())).thenReturn(Optional.empty());
        when(tradeStoreRepository.save(any())).thenReturn(null);
        tradeStoreService.addTrade(getTradeStore("2022-03-04", 1, null, null));
        verify(tradeStoreRepository, Mockito.times(1)).save(any());
    }

    @Test
    void verifyAddTradeThrowsExceptionIfNewTradeHavingMaturityDateBeforeCurrentDate() {
        when(tradeStoreRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertEquals("Maturity Date should be after current date"
                , Assertions.assertThrows(Exception.class, () -> tradeStoreService.addTrade(getTradeStore("2020-03-04", 1, null, null))).getMessage());
        verify(tradeStoreRepository, never()).save(any());
    }

    @Test
    void verifyAddTradeThrowsExceptionIfATradeIsUpdatedWithLowerVersion() {
        when(tradeStoreRepository.findById(any())).thenReturn(Optional.of(getTradeStore("2022-03-04", 1, getDateFromString("2021-03-10"), "N")));
        Assertions.assertEquals("Trade version should be greater"
                , Assertions.assertThrows(Exception.class, () -> tradeStoreService.addTrade(getTradeStore("2022-03-04", 0, null, null))).getMessage());
        verify(tradeStoreRepository, never()).save(any());
    }

    @Test
    void verifyAddTradeUpdatesTradeIfPresent() throws Exception {
        when(tradeStoreRepository.findById(any())).thenReturn(Optional.of(getTradeStore("2021-03-28", 1, getDateFromString("2021-03-10"), "N")));
        tradeStoreService.addTrade(getTradeStore("2023-03-04", 2, null, null));
        verify(tradeStoreRepository, times(1)).save(any());
    }

    private TradeStore getTradeStore(String maturityDate, int version, Date createdDate, String expired) {
        return TradeStore.builder()
                .tradeId("T1")
                .version(version)
                .counterPartyId("CP-1")
                .bookId("B1")
                .maturityDate(getDateFromString(maturityDate))
                .createdDate(createdDate)
                .expired(expired)
                .build();
    }

    private Date getDateFromString(String date) {
        return Date.from(LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant());
    }

    @Test
    void verifyGetTrades() {
        when(tradeStoreRepository.findAll()).thenReturn(null);
        tradeStoreService.findAllTrades();
        verify(tradeStoreRepository, Mockito.times(1)).findAll();
    }
}