/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Pololoers
 */
import java.math.BigDecimal;

public class BillingBreakdownItem {

    private String description;
    private BigDecimal amount;

    public BillingBreakdownItem() {
    }

    public BillingBreakdownItem(String description, BigDecimal amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
