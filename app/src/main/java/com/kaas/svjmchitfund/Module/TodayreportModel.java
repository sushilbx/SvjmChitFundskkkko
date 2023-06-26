package com.kaas.svjmchitfund.Module;

import java.util.List;

public class TodayreportModel {
    public String status;
    public String message;
    public List<TodayreportModel.Today> today_report;
    public int code;

    public class Today {

        public String customers_id;
        public String id;
        public String billings_id;


        public TodayreportModel.Today.Customer customer;
        public class Customer
        {
            public String id;
            public String name;
            public String place;
            public String month;
            public String customers_id;
            public String  amount;
            public String  group_id;
            public int total_amount;
            public String mobile;
            public String route;
            public String installment;
        }


    }
}
