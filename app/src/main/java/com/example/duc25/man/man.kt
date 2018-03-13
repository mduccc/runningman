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
import com.example.duc25.audio.audioGame
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class Man(contex: Context, var screenW: Float, var screenH: Float): View(contex){
    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.step1)
    private var PosX: Float = screenW*0.15F
    private var PosY: Float = screenH*0.72F
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)//pass param khử răng cưa
    val valueAnimation = ValueAnimator.ofFloat(PosY, screenH*0.3F)
    val timer = Timer()
    private var run = 1
    var jum = 1 //when gameover set jump = 0 don't start valueAnimation after cancel
    var Audio = audioGame(contex)
    var time: Long = 0

    //control jump
    override fun onTouchEvent(event: MotionEvent): Boolean{
        if(event.action == MotionEvent.ACTION_DOWN){
            if(getYMan() >= (screenH*0.72F-10F)) {
                if(jum == 1) {
                    valueAnimation.cancel()
                    moveMan()
                    Audio.audioJump()
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas){
        drawMan(canvas)
        if(run == 1){
            run = 0
            ManRun().execute()
        }
    }

    private fun drawMan(canvas: Canvas){
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, (screenW*0.07).toInt(), (screenW*0.07).toInt(), true)
                , PosX, PosY, paint)
    }

    private fun moveMan(){
        valueAnimation.addUpdateListener {
            val value = it.animatedValue as Float
            if(value>0){
                PosY = value
            }else{
                valueAnimation.cancel()
            }
            postInvalidateOnAnimation()
        }
        valueAnimation.repeatMode = ValueAnimator.REVERSE
        valueAnimation.repeatCount = 1
        valueAnimation.duration = time
        //valueAnimation.interpolator = AccelerateInterpolator(1.5f) // tang toc
        valueAnimation.interpolator = LinearInterpolator()
        valueAnimation.start()
    }

    fun getYMan(): Float{
        return PosY
    }

    fun getXMan(): Float{
        return PosX
    }

    fun getWidthMan(): Float{
        return screenW*0.07F
    }

    fun getHeightMan(): Float{
        return screenW*0.07F
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
            postInvalidateOnAnimation()
        }

    }
}