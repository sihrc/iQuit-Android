package com.sihrc.quit;

import java.util.ArrayList;

/**
 * Created by chris on 12/27/13.
 */
public class GIF {
    ArrayList<byte[]> images;
    String url;
    Integer length;
    String ext;

    public GIF(String url, ArrayList<byte[]> images){
        this.url = url;
        this.images = images;
        this.length = images.size();
    }
}
