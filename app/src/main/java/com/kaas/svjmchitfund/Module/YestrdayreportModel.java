package com.kaas.svjmchitfund.Module;

import java.util.List;

public class YestrdayreportModel {
    public String status;
    public String message;
    public List<YestrdayreportModel.Yesterday_report> yesterday_report;
    public int code;

    public class Yesterday_report {

        public String id;
        public String billings_id;
        public String created_at;
        public String customers_id;

        public YestrdayreportModel.Yesterday_report.Customer customer;
        public class Customer
        {
            public String id;
            public String name;
            public String place;
            public String month;
            public String customer_id;
            public String billings_id;
            public String  amount;
            public String  group_id;
            public String total_amount;
            public String mobile;
            public String route;
            public String installment;

        }



    }
}
