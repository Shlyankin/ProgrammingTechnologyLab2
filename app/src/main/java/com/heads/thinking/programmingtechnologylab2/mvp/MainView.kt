package com.heads.thinking.programmingtechnologylab2.mvp

import com.arellomobile.mvp.MvpView
import com.heads.thinking.programmingtechnologylab2.models.Point
import com.heads.thinking.programmingtechnologylab2.models.Triangle


interface MainView: MvpView {
    fun updateFields(triangle: Triangle, point : Point)
    fun updatePointPositionLabel(inTriangle: Boolean)
    fun plotGraph(triangle: Triangle, point : Point)
}