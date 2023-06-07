package com.kaas.svjmchitfund.Module;

import java.util.List;

public class EditCoustmerModel {
    public String message;
    public String status;
    public List<EditCoustmerModel.Customer> customer;
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
        public String status;
        public String created_at;
        public String updated_at;

        public Group group;

        public class Group {
            public int id;
            public String amount;
        }
    }
}
