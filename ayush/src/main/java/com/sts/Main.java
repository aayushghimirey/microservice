package com.sts;

public class Main {
    public static void main(String[] args) {

        class User {
            int age;
            String name;
        }

        User user = new User();
        user.name = "Ayush";
        user.age = 21;

        System.out.println(user.name);

    }
}