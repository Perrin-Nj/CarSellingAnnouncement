package com.projet_bd.carselling.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponsePhoto {
    private String name;
    private String url;
    private String type;
    private long size;
}
