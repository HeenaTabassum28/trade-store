package com.deutschebank.trade.store.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class TradeStore {
    @Id
    private Long tradeId;
    private Integer version;
    private String counterPartyId;
    private String bookId;
    private Date maturityDate;
    private Date createdDate;
    private String expired;
}
