package com.kaas.svjmchitfund.Module;

import java.util.List;

public class BannerModel {
    public String success;
    public String message;
    public List<banners> banners;
    public int code;

    public class banners {
        public int id;
        public String name;
        public String image;

    }
}
