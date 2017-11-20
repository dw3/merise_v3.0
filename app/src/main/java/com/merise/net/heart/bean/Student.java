package com.merise.net.heart.bean;

import com.android.framewok.bean.Entity;

import java.util.List;

/**
 * Created by wangdawang on 2016/8/30 0030.
 */
public class Student extends Entity {
    private String name;
    private String number;
    private List<String> course;

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public List<String> getCourse() {
        return course;
    }

    public Student(Builder builder) {
        this.name = builder.name;
        this.number = builder.number;
        this.course = builder.course;
    }

    public class Builder {
        private final String name;
        private String number;
        private List<String> course;

        public Builder(String name) {
            this.name = name;
        }

        public Builder setNumber(String number) {
            this.number = number;
            return this;
        }

        public Builder setCourse(List<String> course) {
            this.course = course;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}
