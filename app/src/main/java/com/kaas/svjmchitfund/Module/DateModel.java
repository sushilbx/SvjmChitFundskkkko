package com.kaas.svjmchitfund.Module;

import java.util.Date;
import java.util.List;

public class DateModel {
    public String message;
    public String status;
    public List<Datum> data;

    public class Datum {
        public int id;
        public String customers_id;
        public String name;
        public String place;
        public String total_amount;
        public Date created_at;
    }

}
