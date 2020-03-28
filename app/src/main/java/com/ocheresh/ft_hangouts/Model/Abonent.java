package com.ocheresh.ft_hangouts.Model;

public class Abonent implements Comparable<Abonent> {

    private String name;
    private String surname;
    private String telephonenumber;
    private String mail;
    private String adres;
    private String photo_path;
    public String getAdres() {
        return adres;
    }

    public Abonent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public String getMail() {
        return mail;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    @Override
    public int compareTo(Abonent o) {
        char first = name.charAt(0);
        char second = o.getName().charAt(0);
        if (first >= 97 && first <= 122)
            first = (char)(first - 32);
        if (second >= 97 && second <= 122)
            second = (char)(second - 32);
        return first - second;
    }

    public static class Builder {

        private Abonent data;

        public Builder() {
            data = new Abonent();
        }

        public Builder setName(String name) {
            data.name = name;
            return this;
        }

        public Builder setSurname(String surname) {
            data.surname = surname;
            return this;
        }

        public Builder setTelephNumber(String telephone) {
            data.telephonenumber = telephone;
            return this;
        }

        public Builder setMail(String mail) {
            data.mail = mail;
            return this;
        }

        public Builder setAdres(String adres) {
            data.adres = adres;
            return this;
        }

        public Builder setPhotoPath(String path) {
            data.photo_path = path;
            return this;
        }

        public Abonent build() {
            return data;
        }

    }
}
