package com.kaas.svjmchitfund.Module;

import java.util.List;

public class FilterModel {
    public String message;
    public String status;
    public List<Datum> data;
    public String excel;
    public class Bills{
        public int id;
        public String billings_id;
        public String customers_id;
        public String shop_name;
    }

    public class Datum{
        public int id;
        public String customers_id;
        public String name;
        public String total_amount;
        public String route;
        public String group_id;
        public Bills bills;
    }

}
