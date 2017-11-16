package com.reference.apublic.web;

/**
 * Created by byang059 on 9/21/17.
 */
public class Speed {
    private Long id;
    private String name;
    private int age;

    public Speed(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Speed() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
