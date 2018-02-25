package com.example.duc25.runningman


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.AsyncTask
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class Man(contex: Context, var screenW: Float, var screenH: Float): View(contex){
    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.step1)
    var x1: Float = screenW*0.15F
    var y1: Float = screenH*0.72F
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)//pass param khử răng cưa
    val valueAnimation = ValueAnimator.ofFloat(y1, screenH*0.15F)
    val timer = Timer()
    var run = 1
    //control jump
    override fun onTouchEvent(event: MotionEvent): Boolean{
        if(event.action == MotionEvent.ACTION_DOWN){
            if(getYMan() == (screenH*0.72F)) {
                moveMan()
            }
            //Toast.makeText(context, event.getY().toString(), Toast.LENGTH_LONG).show()
        }
        return true
    }

    override fun onDraw(canvas: Canvas){
        drawEmemple(canvas)
        drawMan(canvas)
        if(run == 1){
            run = 0
            ManRun().execute()
        }
    }

    fun updateDraw(){
        postInvalidateOnAnimation()
    }

    fun moveMan(){
        valueAnimation.addUpdateListener {
            val value = it.animatedValue as Float
            y1 = value
            updateDraw()
        }
        valueAnimation.repeatMode = ValueAnimator.REVERSE
        valueAnimation.repeatCount = 1
        valueAnimation.duration = 400
        //valueAnimation.interpolator = AccelerateInterpolator(1.5f) // tang toc
        valueAnimation.interpolator = LinearInterpolator()
        valueAnimation.start()
    }

    fun getYMan(): Float{
        return y1
    }

    fun getXMan(): Float{
        return x1
    }

    fun getWidthMan(): Float{
        return screenW*0.07F
    }

    fun getHeightMan(): Float{
        return screenW*0.07F
    }

    fun drawMan(canvas: Canvas){
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, (screenW*0.07).toInt(), (screenW*0.07).toInt(), true)
                , x1, y1, paint)
    }

    fun drawEmemple(canvas: Canvas){
        canvas.drawRGB(255, 219, 77)//BG color
        paint.setARGB(255, 255, 0, 0)
        //paint.setStrokeWidth(2f)//set độ dày
        //canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)
        //paint.setStrokeWidth(10f)
        //canvas.drawLine(0f, 60f, width.toFloat(), 60f, paint)
        //paint.setStrokeWidth(15f)//set độ dày
        //canvas.drawPoint(screenW/2, screenH/2, paint)
    }

    @SuppressLint("StaticFieldLeak")
    inner class ManRun: AsyncTask<Void, String, Float>() {
        override fun doInBackground(vararg p0: Void?): Float {
            var i = 0
            timer.scheduleAtFixedRate(0, 90){
                if(getYMan() == (screenH*0.72F)) {
                    if (i == 0) {
                        i = 1
                        publishProgress("1")
                    } else {
                        i = 0
                        publishProgress("0")
                    }
                }
            }
            return getYMan()
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            if(values[0]!!.toInt() == 1){
                bitmap = BitmapFactory.decodeResource(resources, R.drawable.step2)
            }
            if(values[0]!!.toInt() == 0){
                bitmap = BitmapFactory.decodeResource(resources, R.drawable.step1)

            }
            updateDraw()
        }

        override fun onPostExecute(result: Float?) {
            super.onPostExecute(result)
        }
    }
}