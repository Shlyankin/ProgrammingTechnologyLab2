package com.heads.thinking.programmingtechnologylab2

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.heads.thinking.programmingtechnologylab2.models.Point
import com.heads.thinking.programmingtechnologylab2.models.Triangle
import com.heads.thinking.programmingtechnologylab2.mvp.MainPresenter
import com.heads.thinking.programmingtechnologylab2.mvp.MainView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : MvpAppCompatActivity(), MainView {


    @InjectPresenter
    lateinit var presenter: MainPresenter

    var plotTask = PlotAsyncTask(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        plotBtn.setOnClickListener {
            if(updatePoints())
                presenter.plotGraph()
            else
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
        }
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(updatePoints())
                    presenter.checkTriangle()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        }
        point1X.addTextChangedListener(textWatcher)
        point2X.addTextChangedListener(textWatcher)
        point3X.addTextChangedListener(textWatcher)
        point4X.addTextChangedListener(textWatcher)
        point1Y.addTextChangedListener(textWatcher)
        point2Y.addTextChangedListener(textWatcher)
        point3Y.addTextChangedListener(textWatcher)
        point4Y.addTextChangedListener(textWatcher)
    }

    override fun onResume() {
        super.onResume()
        presenter.getData()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        presenter.getData()
    }

    override fun onPause() {
        super.onPause()
        if(updatePoints())
            presenter.writeXML()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun updatePoints() : Boolean {
        if( !point1X.text.toString().isEmpty()  && !point1Y.text.toString().isEmpty() &&
            !point2X.text.toString().isEmpty()  && !point2Y.text.toString().isEmpty() &&
            !point3X.text.toString().isEmpty()  && !point3Y.text.toString().isEmpty() &&
            !point4X.text.toString().isEmpty()  && !point4Y.text.toString().isEmpty()) {
            val point1 =
                Point(point1X.text.toString().toDouble(), point1Y.text.toString().toDouble())
            val point2 =
                Point(point2X.text.toString().toDouble(), point2Y.text.toString().toDouble())
            val point3 =
                Point(point3X.text.toString().toDouble(), point3Y.text.toString().toDouble())
            val point4 =
                Point(point4X.text.toString().toDouble(), point4Y.text.toString().toDouble())
            presenter.saveData(Triangle(point1, point2, point3), point4)
            return true
        }
        return false
    }

    override fun updatePointPositionLabel(inTriangle: Boolean) {
        if(inTriangle)
            inTriangleTextView.text = getString(R.string.inTriangle)
        else
            inTriangleTextView.text = getString(R.string.NotInTriangle)
    }

    override fun updateFields(triangle: Triangle, point: Point) {
        point1X.setText(triangle.point1.x.toString())
        point1Y.setText(triangle.point1.y.toString())
        point2X.setText(triangle.point2.x.toString())
        point2Y.setText(triangle.point2.y.toString())
        point3X.setText(triangle.point3.x.toString())
        point3Y.setText(triangle.point3.y.toString())
        point4X.setText(point.x.toString())
        point4Y.setText(point.y.toString())
    }


    override fun plotGraph(triangle: Triangle, point: Point) {
        if(plotTask.status != AsyncTask.Status.RUNNING)
            PlotAsyncTask(this).execute(Pair(triangle, point))
    }



    companion object {
        class PlotAsyncTask constructor(context: Context) : AsyncTask<Pair<Triangle, Point>, Unit, Array<SimpleXYSeries>>() {

            private val activityReference = WeakReference(context)

            override fun doInBackground(vararg pair: Pair<Triangle, Point>): Array<SimpleXYSeries> {
                val pair = pair[0]
                val triangle = pair.first
                val point = pair.second
                val line1 = getLine("Line1-2", triangle.point1, triangle.point2)
                val line2 = getLine("Line1-3", triangle.point1, triangle.point3)
                val line3 = getLine("Line2-3", triangle.point2, triangle.point3)
                val line4 = SimpleXYSeries(
                    SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "Point", point.x, point.y
                )
                return arrayOf(line1, line2, line3, line4)
            }

            override fun onPostExecute(lines: Array<SimpleXYSeries>) {
                val activity = activityReference.get()
                if(activity == null || activity !is MainActivity || activity.isFinishing) return
                activity.plot.clear()
                activity.plot.legend.isVisible = false
                activity.plot.addSeries(lines[0], LineAndPointFormatter(activity, R.xml.line_point_formatter))
                activity.plot.addSeries(lines[1], LineAndPointFormatter(activity, R.xml.line_point_formatter))
                activity.plot.addSeries(lines[2], LineAndPointFormatter(activity, R.xml.line_point_formatter))
                activity.plot.addSeries(lines[3], LineAndPointFormatter(activity, R.xml.line_point_formatter2))
                activity.plot.redraw()
            }
        }

        private fun getLine(tittle: String, point1: Point, point2: Point) : SimpleXYSeries {
            val xSeries = ArrayList<Double>().apply {
                this.add(point1.x)
                this.add(point2.x)
            }
            val ySeries = ArrayList<Double>().apply {
                this.add(point1.y)
                this.add(point2.y)
            }
            return SimpleXYSeries(xSeries, ySeries, tittle)
        }
    }
}
