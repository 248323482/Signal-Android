package org.thoughtcrime.securesms.mediasend.v2.text

import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText
import android.widget.TextView
import org.signal.core.util.BreakIteratorCompat
import org.signal.core.util.DimensionUnit
import org.signal.core.util.EditTextUtil

class TextStoryTextWatcher private constructor(private val textView: TextView) : TextWatcher {

  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    ensureProperTextSize(textView)
  }

  override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

  override fun afterTextChanged(s: Editable) = Unit

  companion object {
    fun ensureProperTextSize(textView: TextView) {
      val breakIteratorCompat = BreakIteratorCompat.getInstance()
      breakIteratorCompat.setText(textView.text)
      val length = breakIteratorCompat.countBreaks()
      val expectedTextSize = when {
        length < 50 -> 36f
        length < 200 -> 24f
        else -> 18f
      }

      textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DimensionUnit.DP.toPixels(expectedTextSize))
    }

    fun install(textView: TextView) {
      val watcher = TextStoryTextWatcher(textView)

      if (textView is EditText) {
        EditTextUtil.addGraphemeClusterLimitFilter(textView, 700)
      }

      textView.addTextChangedListener(watcher)
    }
  }
}
