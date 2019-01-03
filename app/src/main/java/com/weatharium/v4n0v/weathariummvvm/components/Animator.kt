package com.weatharium.v4n0v.weathariummvvm.components

import android.animation.ArgbEvaluator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.ImageView


class Animator {
    companion object {

        fun zoomOutAnimation(): AnimationSet {
            val showAnimationSet = AnimationSet(false)
            showAnimationSet.addAnimation(scaleAnimation(1f, 1.1f, 200, 0))
            showAnimationSet.addAnimation(scaleAnimation(1.1f, 0f, 300, 200))
            return showAnimationSet

        }

        fun zoomInAnimation(duration: Long =500, offset: Long = 0): AnimationSet {
            val showAnimationSet = AnimationSet(false)
            showAnimationSet.addAnimation(scaleAnimation(0f, 1f, duration, offset))
            showAnimationSet.interpolator = MyBounceInterpolator(0.2, 6.0)
            return showAnimationSet
        }

        private fun  positioInAnimation(x:Float, y:Float, duration: Long = 400, offset: Long = 0):AnimationSet{
            val animationSet = AnimationSet(false)
            animationSet.addAnimation(positionAnimation(x, 0f, y, 0f, duration, offset))
            return animationSet
        }

        private fun positionAnimation(xFrom: Float, xTo: Float, yFrfom: Float, yTo: Float, duration: Long, offset: Long): Animation {
            val animation = TranslateAnimation(xFrom, xTo, yFrfom, yTo)
            animation.duration = duration
            animation.startOffset = offset
            return animation
        }

        fun bigZoomOutAnimation(): AnimationSet {
            val showAnimationSet = AnimationSet(false)
            showAnimationSet.addAnimation(scaleAnimation(10f, 1f, 400, 0))
            return showAnimationSet
        }

        private fun scaleAnimation(from: Float, to: Float, duration: Long, offset: Long): Animation {
            val animation = ScaleAnimation(
                    from, to, from, to,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f)
            animation.duration = duration
            animation.startOffset = offset

            return animation
        }


        fun colorAnimation(view: ImageView, colorFrom: Int, colorTo: Int) {
            val animator = ValueAnimator.ofInt(colorFrom, colorTo)
            animator.addUpdateListener { animation -> view.setBackgroundColor((animation.animatedValue as Int)) }
            animator.start()
        }


        fun changeColorAnimation(view: View, colorFrom: Int, colorTo: Int) {
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimation.duration = 1250 // milliseconds
            colorAnimation.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
            colorAnimation.start()

        }
    }
}

internal class MyBounceInterpolator(amplitude: Double, frequency: Double) : android.view.animation.Interpolator {
    private var mAmplitude = 1.0
    private var mFrequency = 10.0

    init {
        mAmplitude = amplitude
        mFrequency = frequency
    }

    override fun getInterpolation(time: Float): Float {
        return (-1.0 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1).toFloat()
    }
}