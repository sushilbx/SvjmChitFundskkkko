package com.kaas.svjmchitfund.Module;

import java.util.List;

public class WeeklyReportModel {
    public String message;
    public String status;
    public List<Weekly> weekly;
    public int code;

    public class Weekly {
        public int id;
        public String group_id;
        public int code;
        public String name;
        public String mobile;
        public String month;
        public String place;
        public String installment;
        public String route;
        public String status;
        public String created_at;
        public String updated_at;
    }

}
