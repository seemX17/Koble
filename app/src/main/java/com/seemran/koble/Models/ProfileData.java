package com.seemran.koble.Models;

/**
 * Created by Seemran on 05-Aug-17.
 */

public class ProfileData {
    public String nametxt,usernametxt,numbertxt;

    public ProfileData() {
    }

    public String getNametxt() {
        return nametxt;
    }
    public void setNametxt(String nametxt) {
        this.nametxt = nametxt;
    }

    public String getUsernametxt() {
        return usernametxt;
    }

    public void setUsernametxt(String usernametxt) {
        this.usernametxt = usernametxt;
    }

    public String getNumbertxt() {
        return numbertxt;
    }

    public void setNumbertxt(String numbertxt) {
        this.numbertxt = numbertxt;
    }
}
