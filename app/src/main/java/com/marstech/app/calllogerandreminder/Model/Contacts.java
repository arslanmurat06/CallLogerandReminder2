package com.marstech.app.calllogerandreminder.Model;

/**
 * Created by HP-PC on 21.07.2017.
 */

public class Contacts {
    String contactName;
    String contactNumber;
    String normalizedNumber;
    String bildirimZaman;
    String bildirimMesaj;

    public String getNormalizedNumber() {
        return normalizedNumber;
    }

    public void setNormalizedNumber(String normalizedNumber) {
        this.normalizedNumber = normalizedNumber;
    }

    public Contacts(String contactName, String contactNumber, String normalizedNumber) {

        this.contactName=contactName;
        this.contactNumber=contactNumber;
        this.normalizedNumber=normalizedNumber;



    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
