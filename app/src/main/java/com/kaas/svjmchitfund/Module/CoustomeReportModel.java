package com.kaas.svjmchitfund.Module;

import java.util.List;

public class CoustomeReportModel {

    public String message;
    public String status;
    public List<Customer> customer;
    public int code;
    public class Customer{
        public int id;
        public int group_id;
        public String customers_id;
        public String name;
        public Object mobile;
        public String place;
        public String installment;
        public String total_amount;
        public String route;
        public String created_at;
        public String updated_at;
        public Group group;
    }

    public class Group{
        public int id;
        public String name;
        public String slug;
        public int amount;
        public String created_at;
        public String updated_at;
    }


}
