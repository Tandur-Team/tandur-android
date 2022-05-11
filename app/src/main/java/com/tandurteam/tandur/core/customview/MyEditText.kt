package com.tandurteam.tandur.core.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.tandurteam.tandur.R

class MyEditText : AppCompatEditText {

    private lateinit var startIcon: Drawable
    private lateinit var warningIcon: Drawable
    private lateinit var passIcon: Drawable
    private var label: String = ""

    // state variable
    var isNotEmpty: Boolean = false
    var isEmailValid: Boolean = false
    var isPasswordValid: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        initLabel()
        initWarningIcon()
        initPassIcon()
        initStartIcon()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // set tint on text changed
                if (text?.isNotEmpty() as Boolean) {
                    startIcon.setTint(
                        ContextCompat.getColor(
                            context,
                            R.color.dark_accent
                        )
                    )
                }
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, count: Int) {
                // set tint on text changed
                if (text.isNullOrEmpty()) {
                    isNotEmpty = false
                    startIcon.setTint(
                        ContextCompat.getColor(
                            context,
                            R.color.light_disabled
                        )
                    )
                    showWarningIcon()
                } else {
                    isNotEmpty = true
                    startIcon.setTint(
                        ContextCompat.getColor(
                            context,
                            R.color.dark_accent
                        )
                    )

                    // check is not email and password
                    if (
                        this@MyEditText.inputType != PASSWORD &&
                        this@MyEditText.inputType != EMAIL
                    ) {
                        if (this@MyEditText.isEnabled) showPassIcon()
                    }
                }

                if (this@MyEditText.inputType == PASSWORD) { // check password type
                    if ((text?.length ?: 0) < 6) {
                        isPasswordValid = false
                        showWarningIcon()
                    } else {
                        isPasswordValid = true
                        showPassIcon()
                    }
                } else if (this@MyEditText.inputType == EMAIL) { // check email type
                    Log.d("TAG", "onTextChanged: ${validateEmailType(text.toString())}")
                    if (validateEmailType(text.toString())) {
                        showPassIcon()
                    } else showWarningIcon()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun initWarningIcon() {
        warningIcon = ContextCompat.getDrawable(
            context,
            R.drawable.ic_password_wrong
        ) as Drawable
    }

    private fun initPassIcon() {
        passIcon = ContextCompat.getDrawable(
            context,
            R.drawable.ic_password_check
        ) as Drawable
    }

    private fun initLabel() {
        label = when (this.inputType) {
            PASSWORD -> "Password"
            EMAIL -> "Email"
            PERSON_NAME -> "Full Name"
            else -> "Description"
        }
    }

    private fun initStartIcon() {
        startIcon = when (this.inputType) {
            PASSWORD -> {
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_password
                ) as Drawable
            }
            EMAIL -> {
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_email
                ) as Drawable
            }
            PERSON_NAME -> {
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_person
                ) as Drawable
            }
            else -> {
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_search
                ) as Drawable
            }
        }.apply {
            setTint(
                ContextCompat.getColor(
                    context,
                    R.color.light_disabled
                )
            )
        }

        setDrawable(startIcon)
    }

    fun onEmailInvalid() {
        setDrawable(startOfTheText = startIcon, endOfTheText = warningIcon)
        Toast.makeText(
            context,
            context.getString(R.string.email_not_valid),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onEmailDuplicate() {
        setDrawable(startOfTheText = startIcon, endOfTheText = warningIcon)
        Toast.makeText(
            context,
            context.getString(R.string.email_duplicate),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onPasswordInvalid() {
        setDrawable(startOfTheText = startIcon, endOfTheText = warningIcon)
        Toast.makeText(
            context,
            context.getString(R.string.password_invalid),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onFormEmpty() {
        setDrawable(startOfTheText = startIcon, endOfTheText = warningIcon)
        Toast.makeText(
            context,
            context.getString(R.string.form_could_not_be_empty, label),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun validateEmailType(text: String): Boolean {
        isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
        return isEmailValid
    }

    private fun showWarningIcon() {
        setDrawable(startOfTheText = startIcon, endOfTheText = warningIcon)
    }

    private fun showPassIcon() {
        setDrawable(startOfTheText = startIcon, endOfTheText = passIcon)
    }

    private fun setDrawable(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )

        background = ContextCompat.getDrawable(context, R.drawable.bg_radius_8dp)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        compoundDrawablePadding = 48
        setTextColor(ContextCompat.getColor(context, R.color.dark_accent))
        setHintTextColor(ContextCompat.getColor(context, R.color.light_disabled))
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    companion object {
        const val PASSWORD = 129
        const val EMAIL = 33
        const val PERSON_NAME = 97
    }
}