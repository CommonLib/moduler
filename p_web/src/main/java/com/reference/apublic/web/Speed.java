package com.reference.apublic.web;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by byang059 on 9/21/17.
 */
@Entity
public class Speed {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "USERNAME")
    private String name;
    private int age;
    @Generated(hash = 1071977093)
    public Speed(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    @Generated(hash = 1069754329)
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
