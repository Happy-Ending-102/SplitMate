// File: BaseEntity.java
package com.splitmate.model;

import org.springframework.data.annotation.Id;

public abstract class BaseEntity {
    @Id
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

