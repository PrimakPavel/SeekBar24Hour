package com.pavelprimak.seekbar24hour.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import com.pavelprimak.seekbar24hour.OnSeek24BarChangeListener
import com.pavelprimak.seekbar24hour.R
import com.pavelprimak.seekbar24hour.customView.LineGraphView.Companion.SEC_IN_MIN
import com.pavelprimak.seekbar24hour.utils.ConvertValueUtil


class SeekBar24HourView : LinearLayout {
    companion object {
        const val MINUTES_IN_GRAPH = 1440
    }

    //COLORS
    private var backgroundColors = LineGraphView.DEFAULT_BACKGROUND_COLOR
    private var textColor = LineGraphView.DEFAULT_TEXT_COLOR
    private var mainEventColor = LineGraphView.DEFAULT_MAIN_EVENT_COLOR
    private var markEventColor = LineGraphView.DEFAULT_MARK_EVENT_COLOR

    //SIZES
    private var dividerType = LineGraphView.DIVIDER_MINUTES
    private var topMarginInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.TOP_MARGIN, context)
    private var mainHeightInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_HEIGHT_GRAPH, context)
    private var lineWidthInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_LINE_WIDTH, context)
    private var lineHeightInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_LINE_HEIGHT, context)
    private var textSizeInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_TEXT_SIZE, context)
    private var changeListener: OnSeek24BarChangeListener? = null

    private var scrollView: CustomHorizontalScrollView? = null
    private var isUserTouch = false
    var lineGraphView: LineGraphView? = null
    private var cursorView: ImageView? = null
    var percents: Float = 0f

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        inflate(context, R.layout.seek_bar_24_hour, this)

        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SeekBar24HourView,
                0, 0)
        backgroundColors = typedArray.getColor(R.styleable.SeekBar24HourView_Sb24hBackgroundColor, LineGraphView.DEFAULT_BACKGROUND_COLOR)
        mainEventColor = typedArray.getColor(R.styleable.SeekBar24HourView_Sb24hMainEventColor, LineGraphView.DEFAULT_MAIN_EVENT_COLOR)
        markEventColor = typedArray.getColor(R.styleable.SeekBar24HourView_Sb24hMarkEventColor, LineGraphView.DEFAULT_MARK_EVENT_COLOR)
        textColor = typedArray.getColor(R.styleable.SeekBar24HourView_Sb24hTextColor, LineGraphView.DEFAULT_TEXT_COLOR)
        textSizeInPx = typedArray.getDimension(R.styleable.SeekBar24HourView_Sb24hTextSize, textSizeInPx)
        dividerType = typedArray.getInt(R.styleable.SeekBar24HourView_Sb24hMinDivider, LineGraphView.DIVIDER_MINUTES)
        mainHeightInPx = typedArray.getDimension(R.styleable.SeekBar24HourView_Sb24hBorderHeight, mainHeightInPx) + topMarginInPx

        lineWidthInPx = typedArray.getDimension(R.styleable.SeekBar24HourView_Sb24hDivLineWidth, lineWidthInPx)
        lineHeightInPx = typedArray.getDimension(R.styleable.SeekBar24HourView_Sb24hDivLineHeight, lineHeightInPx)

        scrollView = findViewById(R.id.scroll_view)
        cursorView = findViewById(R.id.cursor_view)
        lineGraphView = findViewById(R.id.line_graph_view)
        prepareScrollEventListener(scrollView)
        prepareTouchEventListener(scrollView)
        scrollView?.setOnScrollStoppedListener(object : CustomHorizontalScrollView.OnScrollStoppedListener {
            override fun onScrollStopped() {
                isUserTouch = false
                changeListener?.onStopTrackingTouch(percents)
                Log.e("Scroll", "onStopTrackingTouch % = $percents")
            }
        })
    }

    fun setCursorDrawable(cursorDrawable: Drawable) {
        try {
            cursorView?.setImageDrawable(cursorDrawable)
        } catch (e: Exception) {
        }
    }

    fun getPositionInSec(): Int {
        return (getPositionInPercents() * SEC_IN_MIN * MINUTES_IN_GRAPH).toInt()
    }

    fun setPositionInSec(seconds: Int) {
        val per = seconds * 1f / SEC_IN_MIN / MINUTES_IN_GRAPH
        setPositionInPercents(per)
    }

    fun setPositionInPercents(percents: Float) {
        if (percents in 0f..100f) {
            this.percents = percents
            lineGraphView?.width?.let { graphWidth ->
                scrollView?.width?.let { scrollWidth ->
                    val positionX = percents * (graphWidth - scrollWidth) / 100f
                    scrollView?.scrollTo(positionX.toInt(), 0)
                }
            }
        }
    }

    fun getPositionInPercents(): Float {
        lineGraphView?.width?.let { graphWidth ->
            scrollView?.width?.let { scrollWidth ->
                scrollView?.scrollX?.let { x ->
                    percents = x * 100f / (graphWidth - scrollWidth)
                    return percents
                }
            }
        }
        return 0f
    }

    fun setOnSeek24BarChangeListener(listener: OnSeek24BarChangeListener) {
        changeListener = listener
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.d("Scroll", "ScrollContainerWidth = " + scrollView?.width)
        val marginInPx = scrollView?.width?.toFloat() ?: 0f
        lineGraphView?.setValuesAndInvalidate(backgroundColors, mainEventColor, markEventColor, textColor, textSizeInPx, dividerType, mainHeightInPx, lineWidthInPx, lineHeightInPx, marginInPx / 2, marginInPx / 2)
    }

    private fun prepareScrollEventListener(scrollView: CustomHorizontalScrollView?) {
        scrollView?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollX = scrollView.scrollX // For HorizontalScrollView
            // DO SOMETHING WITH THE SCROLL COORDINATES
            lineGraphView?.width?.let { width ->
                percents = scrollX * 100f / (width - scrollView.width)
                if (percents in 0f..100f) {
                    changeListener?.onProgressChanged(percents, isUserTouch)
                    Log.d("Scroll", "onProgressChanged % = $percents.IsUser = $isUserTouch")
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepareTouchEventListener(scrollView: CustomHorizontalScrollView?) {
        scrollView?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isUserTouch = true
                    percents = getPositionInPercents()
                    changeListener?.onStartTrackingTouch(percents)
                    Log.e("Scroll", "onStartTrackingTouch % = $percents")
                }
                MotionEvent.ACTION_MOVE -> {
                    isUserTouch = true
                }
                MotionEvent.ACTION_UP ->
                    scrollView.startScrollerTask()
            }
            false
        }
    }
}