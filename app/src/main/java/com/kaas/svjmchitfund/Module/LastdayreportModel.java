package com.kaas.svjmchitfund.Module;

import java.util.List;

public class LastdayreportModel {
    public String status;
    public String message;
    public List<Yesterday> yesterday;
    public class Yesterday
    {
        public int id;
        public int group_id;
        public int installment;
        public int route;
        public int code;
        public String name;
        public String place;
        public String month;
        public String status;
        public String mobile;


    }
}
