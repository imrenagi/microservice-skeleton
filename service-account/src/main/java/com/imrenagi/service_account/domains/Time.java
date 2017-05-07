package com.imrenagi.service_account.domains;

import java.util.Random;

/**
 * Created by imrenagi on 5/6/17.
 */
public class Time {

    public Long t;
    public Integer ranx;

    private Random r = new Random();

    public Time() {
        this.t = System.currentTimeMillis();
        this.ranx = r.nextInt();
    }

}
