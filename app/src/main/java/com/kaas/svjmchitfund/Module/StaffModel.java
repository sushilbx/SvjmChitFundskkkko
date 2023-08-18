package com.kaas.svjmchitfund.Module;

import java.util.List;

public class StaffModel {
    public String message;
    public String status;
    public List<Datum> data;

    public class Datum {
        public String name;
        public String id;
    }
}
