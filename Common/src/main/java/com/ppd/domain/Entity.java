package com.ppd.domain;

import java.io.Serializable;

public interface Entity<ID> extends Serializable {
    public ID getId();
}
