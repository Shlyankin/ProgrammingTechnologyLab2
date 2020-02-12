package com.heads.thinking.programmingtechnologylab2.mvp

import android.os.Environment
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.heads.thinking.programmingtechnologylab2.models.Point
import com.heads.thinking.programmingtechnologylab2.models.Triangle
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.File
import java.lang.Exception

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {
    private var triangle: Triangle = Triangle(Point(0.0, 0.0), Point(0.0, 0.0), Point(0.0, 0.0))
    private var point : Point = Point(0.0, 0.0)
    private var serializer: Serializer = Persister()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        try {
            val trianglefile = File(Environment.getExternalStorageDirectory().toString() + "/" + File.separator + "triangle.xml")
            val pointfile = File(Environment.getExternalStorageDirectory().toString() + "/" + File.separator + "point.xml")
            if(trianglefile.exists())
                serializer.read(triangle, trianglefile)
            if(pointfile.exists())
                serializer.read(point, pointfile)
        } catch (e: Exception) {
            Log.e("error", e.localizedMessage?: "File exception")
        }
    }


    fun saveData(triangle: Triangle, point: Point) {
        this.triangle = triangle
        this.point = point
    }

    fun plotGraph() {
        viewState.plotGraph(triangle, point)
    }

    fun writeXML() {
        try {
            val trianglefile = File(Environment.getExternalStorageDirectory().toString() + "/" + File.separator + "triangle.xml")
            val pointfile = File(Environment.getExternalStorageDirectory().toString() + "/" + File.separator + "point.xml")
            if(!trianglefile.exists())
                trianglefile.createNewFile()
            if(!pointfile.exists())
                pointfile.createNewFile()
            serializer.write(point, pointfile)
            serializer.write(triangle, trianglefile)

        } catch (e: Exception) {
            Log.e("error", e.localizedMessage?: "File exception")
        }
    }

    fun getData() {
        viewState.updateFields(triangle, point)
        viewState.plotGraph(triangle, point)
        checkTriangle()
    }

    fun checkTriangle() {
        viewState.updatePointPositionLabel(triangle.pointInTriangle(point))
    }
}