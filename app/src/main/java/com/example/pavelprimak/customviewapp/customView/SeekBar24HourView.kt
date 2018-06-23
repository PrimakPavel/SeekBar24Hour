package com.example.pavelprimak.customviewapp.customView

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.example.pavelprimak.customviewapp.R
import com.example.pavelprimak.customviewapp.utils.ConvertValueUtil

class SeekBar24HourView : LinearLayout {

    //COLORS
    private var backgroundColors = LineGraphView.DEFAULT_BACKGROUND_COLOR
    private var textColor = LineGraphView.DEFAULT_TEXT_COLOR
    private var mainEventColor = LineGraphView.DEFAULT_MAIN_EVENT_COLOR
    private var markEventColor = LineGraphView.DEFAULT_MARK_EVENT_COLOR

    //SIZES
    private var dividerType = LineGraphView.DIVIDER_MINUTES
    private var mainWidthInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.WIDTH_MINUTES_GRAPH, context)
    private var topMarginInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.TOP_MARGIN, context)
    private var mainHeightInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_HEIGHT_GRAPH, context)
    private var lineWidthInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_LINE_WIDTH, context)
    private var lineHeightInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_LINE_HEIGHT, context)
    private var textSizeInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_TEXT_SIZE, context)
    private var textTopMarginInPx = ConvertValueUtil.convertDpToPixel(LineGraphView.DEFAULT_TEXT_TOP_MARGIN, context)


    private var scrollContainer: HorizontalScrollView? = null
    var lineGraphView: LineGraphView? = null

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


        scrollContainer = findViewById(R.id.scroll_view)
        lineGraphView = findViewById(R.id.line_graph_view)
        lineGraphView?.setValuesAndInvalidate(backgroundColors, mainEventColor, markEventColor, textColor, textSizeInPx.toInt(), dividerType, mainHeightInPx.toInt())

    }


}