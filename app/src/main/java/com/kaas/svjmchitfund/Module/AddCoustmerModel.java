package com.kaas.svjmchitfund.Module;

public class AddCoustmerModel {
    public String message;
    public String status;
    public Customer customer;
    public int code;

    public class Customer{
        public int id;
        public String group_id;
        public String customers_id;
        public String name;
        public String mobile;
        public String month;
        public String place;
        public String installment;
        public String route;
        public String created_at;
        public String updated_at;
    }
}
