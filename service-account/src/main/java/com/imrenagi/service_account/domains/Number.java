package com.imrenagi.service_account.domains;

import java.util.Random;

/**
 * Created by imrenagi on 5/7/17.
 */
public class Number {

    public int x;
    public int y;

    public int res;

    private Random r = new Random();

    public Number() {
        this.x = r.nextInt();
        this.y = r.nextInt();
        this.res = this.x + this.y;
    }

}
