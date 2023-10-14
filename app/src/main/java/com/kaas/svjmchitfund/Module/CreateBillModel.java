package com.kaas.svjmchitfund.Module;

import java.util.Date;

public class CreateBillModel {
    public String message;
    public String status;
    public Data data;
    public class Billings{
        public int customers_id;
        public String staff_id;
        public String billings_id;
        public int bill_no_password;
        public Date updated_at;
        public Date created_at;
        public int id;
    }

    public class Customers{
        public String group_id;
        public String customers_id;
        public String staff_id;
        public String name;
        public String mobile;
        public String place;
        public String installment;
        public String total_amount;
        public String route;
        public Date updated_at;
        public Date created_at;
        public int id;
    }

    public class Data{
        public Customers customers;
        public Billings billings;
    }




}
