package com.kaas.svjmchitfund.Module;

import java.util.List;

public class GrouplistModel {
    public String status;
    public String message;
    public List<GrouplistModel.Group> group;
    public int code;

    public class Group {
        public String id;
        public String name;
        public String amount;


    }
}
