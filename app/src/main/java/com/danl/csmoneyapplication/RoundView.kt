package com.danl.csmoneyapplication

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import androidx.preference.PreferenceManager
import com.danl.csmoneyapplication.model.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min

class RoundView : View {

    private lateinit var mapBitmap: Bitmap
    private lateinit var ctPlayerBitmap: Bitmap
    private lateinit var tPlayerBitmap: Bitmap
    private lateinit var c4Bitmap: Bitmap
    private lateinit var deathBitmap: Bitmap
    private var mapSize: Int = -1
    var playerPositions: List<PlayerPosition>? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var players: List<Player>? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var bombPosition: BombPosition? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var events: List<Event>? = null
        set(value) {
            field = value
            postInvalidate()
        }
    var roundEndState: RoundEndState? = null
        set(value) {
            field = value
            postInvalidate()
        }

    private val paint = Paint()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private fun initBitmaps() {
        mapBitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.map),
            mapSize,
            mapSize,
            false
        )
        ctPlayerBitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.player_ct),
            256 * mapSize / 5120,
            256 * mapSize / 5120,
            false
        )
        tPlayerBitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.player_t),
            256 * mapSize / 5120,
            256 * mapSize / 5120,
            false
        )
        c4Bitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.c4),
            256 * mapSize / 5120,
            256 * mapSize / 5120,
            false
        )
        deathBitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.death),
            256 * mapSize / 5120,
            256 * mapSize / 5120,
            false
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mapSize = min(measuredWidth, measuredHeight)

        initBitmaps()
    }

    private fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val directionMatrix = Matrix()
        directionMatrix.postRotate(angle)
        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.width,
            source.height,
            directionMatrix,
            true
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mapBitmap, 0.0f, 0.0f, paint)
        val frameCount = this.frameCount.get()
        for (playerPosition in playerPositions ?: return) {
            if (frameCount < playerPosition.xArray.size && frameCount < playerPosition.yArray.size) {
                canvas.drawBitmap(
                    if (players?.find { it.id == playerPosition.playerId }?.team == 3) rotateBitmap(
                        ctPlayerBitmap,
                        playerPosition.directions[frameCount]
                    ) else rotateBitmap(tPlayerBitmap, playerPosition.directions[frameCount]),
                    ((playerPosition.xArray[frameCount] + 2560) * mapSize / 5120) + 560 * mapSize / 5120,
                    mapSize - ((playerPosition.yArray[frameCount] + 2560) * mapSize / 5120) - 960 * mapSize / 5120,
                    paint
                )
            }
        }

        if (bombPosition != null) {
            if (frameCount > bombPosition!!.startFrame && frameCount < bombPosition!!.endFrame) {
                canvas.drawBitmap(
                    c4Bitmap,
                    ((bombPosition!!.x[frameCount - bombPosition!!.startFrame] + 2560) * mapSize / 5120) + 560 * mapSize / 5120,
                    mapSize - ((bombPosition!!.y[frameCount - bombPosition!!.startFrame] + 2560) * mapSize / 5120) - 960 * mapSize / 5120,
                    paint
                )
            } else if (frameCount < bombPosition!!.startFrame) {
                canvas.drawBitmap(
                    c4Bitmap,
                    ((bombPosition!!.x[0] + 2560) * mapSize / 5120) + 560 * mapSize / 5120,
                    mapSize - ((bombPosition!!.y[0] + 2560) * mapSize / 5120) - 960 * mapSize / 5120,
                    paint
                )
            } else {
                canvas.drawBitmap(
                    c4Bitmap,
                    ((bombPosition!!.x[bombPosition!!.x.size - 1] + 2560) * mapSize / 5120) + 560 * mapSize / 5120,
                    mapSize - ((bombPosition!!.y[bombPosition!!.y.size - 1] + 2560) * mapSize / 5120) - 960 * mapSize / 5120,
                    paint
                )
            }
        }

        for (event in events ?: return) {
            if (event.eventType == 1 && frameCount >= event.frameNumber) {
                canvas.drawBitmap(
                    deathBitmap,
                    ((event.data.victimPos.x + 2560) * mapSize / 5120) + 560 * mapSize / 5120,
                    mapSize - ((event.data.victimPos.y + 2560) * mapSize / 5120) - 960 * mapSize / 5120,
                    paint
                )
            }
        }
    }

    private var thread: Thread? = null

    private var frameCount = AtomicInteger(0)
    private var stopped = true

    fun start() {
        if (players != null) {
            if (stopped) {
                frameCount = AtomicInteger(0)
                stopped = false
                thread = Thread {
                    while (!stopped) {
                        if (frameCount.get() < roundEndState?.frameNumber ?: break) {
                            frameCount.incrementAndGet()
                            println(frameCount)
                            postInvalidate()
                            SystemClock.sleep(
                                110 - PreferenceManager.getDefaultSharedPreferences(context).getInt(
                                    "frame_time",
                                    100
                                ).toLong()
                            )
                        } else {
                            break
                        }
                    }
                    stop()
                }
                thread!!.start()
            }
        }
    }

    fun stop() {
        if (players != null) {
            frameCount = AtomicInteger(0)
            stopped = true
            invalidate()
        }
    }
}