package com.kaas.svjmchitfund.Module;

import java.util.List;

public class TotalAmountModel {
    public String message;
    public String status;
    public List<Datum> data;

    public class Datum {
        public int id;
        public String group_id;
        public String customers_id;
        public String name;
        public Object mobile;
        public String place;
        public int installment;
        public int total_amount;
        public String route;
        public String created_at;
        public String updated_at;
    }
}
