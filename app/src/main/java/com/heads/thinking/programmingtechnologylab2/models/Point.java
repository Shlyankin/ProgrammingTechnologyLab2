package com.heads.thinking.programmingtechnologylab2.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Point")
public class Point {

    @Element(name = "x")
    public Double x;
    @Element(name = "y")
    public Double y;

    public Point() {
        super();
    }

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
