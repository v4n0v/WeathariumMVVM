package com.weatharium.v4n0v.weathariummvvm.activities

import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.weatharium.v4n0v.weathariummvvm.R
import java.util.HashMap

enum class Transitions { NO_TRANSITION, SLIDE_UP, FADE }

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private val MyDefaultTransition = Transitions.NO_TRANSITION
    }


    protected fun beginTransaction(fragment: Fragment, id: Int) {
        beginTransaction(fragment, id, MyDefaultTransition, true)
    }

    protected fun beginTransaction(fragment: Fragment, id: Int, transition: Transitions) {
        beginTransaction(fragment, id, transition, true)
    }


    fun beginTransaction(fragment: Fragment, id: Int, transition: Transitions, isReturn: Boolean) {
        val s = supportFragmentManager.beginTransaction()
                .setCustomAnimations(getIn(transition), getOut(transition))
                .replace(id, fragment)

        if (!isReturn)
            s.disallowAddToBackStack()
        else
            s.addToBackStack(null)

        s.commit()

    }

    private fun getIn(transition: Transitions): Int {
        return when (transition) {
            Transitions.SLIDE_UP -> R.animator.animator_slide_in
            Transitions.FADE -> R.animator.transitions_fragment_in
            Transitions.NO_TRANSITION -> 0
        }
    }

    private fun getOut(transition: Transitions): Int {
        return when (transition) {
            Transitions.SLIDE_UP -> R.animator.animator_slide_out
            Transitions.FADE -> R.animator.transitions_fragment_out
            Transitions.NO_TRANSITION -> 0
        }
    }

    fun log(msg: String) {
        Log.d(this.javaClass.simpleName, msg)
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun toastLong(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun showInformDialog(title: String, message: String, onClickListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ОК", onClickListener)
                .create()
                .show()
    }

    interface OnEditTextListener {
        fun click(editText: EditText)
    }

    fun showCancelableEditTextDialog(title: String, message: String, onClickListener: OnEditTextListener) {
        val editText = EditText(this)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        val bilder = AlertDialog.Builder(this)
                .setTitle(title)
        val padding = getDimens(R.dimen.margin_25dp)
        // lp.setMargins(35, 35, 35, 35)
        val container = LinearLayout(this)
        editText.gravity = android.view.Gravity.TOP or android.view.Gravity.START
        container.orientation = LinearLayout.VERTICAL
        editText.layoutParams = lp
        editText.hint = getString(R.string.city_default)
        container.addView(editText)
        container.setPadding(50, 0, 50, 0)
        bilder.setView(container)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }

        val dialog = bilder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            onClickListener.click(editText)
            dialog.dismiss()
        }
    }

    fun getDimens(id: Int): Int {
        return (resources.getDimension(id) / resources.displayMetrics.density).toInt()

    }

    fun showCancelableInformDialog(title: String, message: String, onClickListener: DialogInterface.OnClickListener) {

        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("ОК", onClickListener)
                .create()
                .show()
    }

    /// abstract fun switchFragment(state: State)


//    fun beginTransaction(fragment: Fragment, idContainer: Int) {
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, fragment)
//                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
////                .addToBackStack(null)
//                .commit()
//    }
}