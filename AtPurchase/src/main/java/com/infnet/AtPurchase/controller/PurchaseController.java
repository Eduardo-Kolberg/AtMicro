package com.infnet.AtPurchase.controller;

import com.infnet.AtPurchase.model.Purchase;
import com.infnet.AtPurchase.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService service;

    public PurchaseController(PurchaseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        purchase.setStatus("CONFIRMED");
        Purchase createdPurchase = service.createPurchase(purchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPurchase);
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        List<Purchase> purchases = service.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }
}
