package com.example.zypher;


// HOLD VAULT DETAILS
public class vaultAccounts {
    private String name;
    private String ID;
    private String passw;
    private int photo;

    public String getName() {
        return name;
    }

    public void setName(String  name) {
        this.name = name;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getPassw() {
        return passw;
    }
    public void setPassw(String passw) {
        this.passw = passw;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(String name) {
        switch (name) {
            case "Google":
                this.photo = R.drawable.google_logo_icon;
                break;
            case "Facebook":
                this.photo = R.drawable.facebook;
                break;
            case "Youtube":
                this.photo = R.drawable.youtube;
                break;
            case "Linkedin":
                this.photo = R.drawable.linkedin;
                break;
            case "Reddit":
                this.photo = R.drawable.reddit;
                break;
            case "Instagram":
                this.photo = R.drawable.instagram;
                break;
            default:
                this.photo = R.drawable.ic_baseline_account_circle_24;
                break;
        }
    }

}
