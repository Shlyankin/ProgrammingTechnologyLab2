package com.heads.thinking.programmingtechnologylab2.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Triangle")
public class Triangle {
    @Element(name = "point1")
    public Point point1;
    @Element(name = "point2")
    public Point point2;
    @Element(name = "point3")
    public Point point3;

    public Triangle() {
        super();
    }

    public Triangle(Point point1, Point point2, Point point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    public boolean pointInTriangle(Point point) {
        double s1 = (point1.x - point.x) * (point2.y - point1.y) - (point2.x - point1.x) * (point1.y - point.y);
        double s2 = (point2.x - point.x) * (point3.y - point2.y) - (point3.x - point2.x) * (point2.y - point.y);
        double s3 = (point3.x - point.x) * (point1.y - point3.y) - (point1.x - point3.x) * (point3.y - point.y);
        return (s1 < 0 && s2 < 0 && s3 < 0) || (s1 > 0 && s2 > 0 && s3 > 0);
    }
}