package com.infnet.AtPurchase.service;

import com.infnet.AtPurchase.model.Purchase;
import com.infnet.AtPurchase.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository repository;

    public PurchaseService(PurchaseRepository repository) {
        this.repository = repository;
    }

    public Purchase createPurchase(Purchase purchase) {
        return repository.save(purchase);
    }

    public List<Purchase> getAllPurchases() {
        return repository.findAll();
    }
}
