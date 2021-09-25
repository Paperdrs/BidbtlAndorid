package com.android.bidbatl.Model;

public class MyTransctionHiistory {
    public String timestamp;
    public String invoice_no;
    public String transaction_type;
    public String amount;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String orderNumber;

    public String getTimestamp() {
        return timestamp;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
