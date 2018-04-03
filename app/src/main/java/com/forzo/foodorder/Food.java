package com.forzo.foodorder;

/**
 * Created by Leon on 03-04-18.
 */

class Food {
    private String username, count, itemname;

    public Food() {
    }

    public Food(String username, String count, String itemname) {
        this.username = username;
        this.count = count;
        this.itemname = itemname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getItemName() {
        return itemname;
    }

    public void setItem(String item) {
        this.itemname = itemname;
    }
}
