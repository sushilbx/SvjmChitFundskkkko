package com.kaas.svjmchitfund.Module;

import java.util.List;

public class CustomerreportModel {
    public String status;
    public String message;
    public List<CustomerreportModel.Customer_report> customer_report;
    public int code;

    public class Customer_report {

        public String id;
        public String billings_id;
        public String created;
        public int customers_id;

        public CustomerreportModel.Customer_report.Customer customer;
        public class Customer
        {
            public String id;
            public String name;

            public String customers_id;
            public String billings_id;
            public String total_amount;

        }


    }
}
