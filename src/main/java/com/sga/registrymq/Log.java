package com.sga.registrymq;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data 
@ToString(includeFieldNames=true)
public class Log{
    private String room;
    private String msg;
    private int status;
}