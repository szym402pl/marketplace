package me.xiaojibazhanshi.marketplace.objects.helper;

public record BlackMarketOptions(int refreshTimeMin, int itemAmount,
                                 double buyerDiscount, double sellerReimbursement) {}
