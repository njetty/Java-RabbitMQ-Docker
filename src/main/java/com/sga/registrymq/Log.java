package com.sga.registrymq;

import lombok.Data;
import lombok.ToString;

@Data 
@ToString(includeFieldNames=true)
public class Log{
    private String date;
    private long req_no;
    private String timest;
    private String room;
    private int type;
}