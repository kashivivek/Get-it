package customfonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MyTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        if (!isInEditMode) typeface = mavenPro(context)
    }

    private companion object {
        @Volatile
        private var cached: Typeface? = null

        fun mavenPro(context: Context): Typeface =
            cached ?: Typeface.createFromAsset(context.assets, "fonts/MavenPro-Regular.ttf").also { cached = it }
    }
}
