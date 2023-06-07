package com.kaas.svjmchitfund.Module;

import java.security.acl.Group;
import java.util.Date;
import java.util.List;

public class GroupModel {
    public String status;
    public String message;
    public Group group;
    public int code;



    public class Group {
        public int id;
        public String name;
        public String amount;


    }
}
