package com.coders.chatapplication.presentation.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.coders.chatapplication.R
import java.util.Random
import kotlin.math.max
import kotlin.math.roundToLong


class AvatarImageView
@JvmOverloads constructor(
	context: Context,
	attributeSet: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attributeSet, defStyle) {

	private var borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private var paint: Paint
	private var textPaint: TextPaint
	private lateinit var text: String
	private lateinit var drwa: Drawable
	private var clipPath: Path
	private var imageSize = 0
	private var cornerRadius = 0f
	private var rectF: RectF
	private var shape = 0
	private var borderWidth = 0f
	private var color: Int = 0x0

	init {
		borderPaint.style = Paint.Style.STROKE
		borderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)

		rectF = RectF()
		clipPath = Path()

		paint = Paint(Paint.ANTI_ALIAS_FLAG)
		textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
		textPaint.color = Color.WHITE

		attributeSet?.let { attrs ->
			val a = context.theme.obtainStyledAttributes(
				attrs,
				R.styleable.AvatarView,
				0, 0
			)
			try {
				val avatarShape = a.getString(R.styleable.AvatarView_avatar_shape)
				shape = if (avatarShape == null) {
					CIRCLE
				} else {
					if (context.getString(R.string.rectangle) == avatarShape) {
						RECTANGLE
					} else {
						CIRCLE
					}
				}
				imageSize = a.getDimensionPixelSize(R.styleable.AvatarView_avatar_size, 50)
				cornerRadius =
					a.getDimensionPixelSize(R.styleable.AvatarView_corner_radius, 2)
						.toFloat()

				val color = a.getColor(R.styleable.AvatarView_background_color, 0x00000000)
				if (color == 0) {
					val rnd = Random()
					paint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
				} else {
					paint.color = color
				}

				textPaint.textSize = a.getDimension(
					R.styleable.AvatarView_text_size,
					22f * resources.displayMetrics.scaledDensity
				)

				borderPaint.color = a.getColor(R.styleable.AvatarView_border_color, 0x0000000)
				borderWidth = a.getDimension(R.styleable.AvatarView_border_width, 0f)
				borderPaint.strokeWidth = borderWidth
			} finally {
				a.recycle()
			}
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val screenWidth = MeasureSpec.getSize(widthMeasureSpec)
		val screenHeight = MeasureSpec.getSize(heightMeasureSpec)
		rectF.set(0f, 0f, screenWidth.toFloat(), screenHeight.toFloat())
	}

	override fun onDraw(canvas: Canvas) {
		if (shape == RECTANGLE) {
			canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, borderPaint);
			clipPath.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
		} else {
			canvas.drawCircle(
				rectF.centerX(),
				rectF.centerY(),
				(rectF.height() / 2) - borderWidth,
				borderPaint
			)

			clipPath.addCircle(
				rectF.centerX(),
				rectF.centerY(),
				(rectF.height() / 2),
				Path.Direction.CW
			)
		}
		canvas.clipPath(clipPath)
		super.onDraw(canvas)
	}

	fun setText(text: String, colorInt: Int) {
		paint.setARGB(255, colorInt, colorInt, colorInt)
		drwa = object : Drawable() {
			override fun draw(canvas: Canvas) {
				val centerX = (bounds.width() * 0.5f).roundToLong()
				val centerY = (bounds.height() * 0.5f).roundToLong()

				val textWidth = textPaint.measureText(text) * 0.5f
				val textBaseLineHeight = textPaint.fontMetrics.ascent * -0.4f
				if (shape == GradientDrawable.RECTANGLE) {
					canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
				} else {
					canvas.drawCircle(
						centerX.toFloat(),
						centerY.toFloat(),
						max(bounds.height() / 2f, textWidth / 2),
						paint
					)
				}
				canvas.drawText(
					text,
					centerX - textWidth,
					centerY + textBaseLineHeight,
					textPaint
				)
			}

			override fun setAlpha(alpha: Int) {
				//no - op
			}

			override fun getOpacity(): Int {
				return PixelFormat.OPAQUE
			}

			override fun setColorFilter(colorFilter: ColorFilter?) {
				//no-op
			}
		}
		setImageDrawable(drwa)
		invalidate()
	}


	companion object {
		private const val RECTANGLE = 1
		private const val CIRCLE = 0
	}
}