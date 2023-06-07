package com.kaas.svjmchitfund.Module;

import java.util.List;

public class BilllistModel {
    public String status;
    public String message;
    public List<BilllistModel.Billing> billing;
    public class Billing
    {
        public String id;
        public String customers_id;
        public Customer customer;
        public String shop_name;
        public String gst_no;
        public String billings_id;
        public String account_no;
        public String updated_at;



        public class Customer
        {
            public String name;
            public String place;
            public String month;
            public String previous;
            public String bill_no;
            public String  amount;
            public String total_amount;
            public String customers_id;
            public String mobile;
            public String route;
            public String installment;
        }



    }
}
