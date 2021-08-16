package com.emmanuelmess.scrollinggame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FillViewport
import com.emmanuelmess.scrollinggame.Size.HEIGHT
import com.emmanuelmess.scrollinggame.Size.WIDTH
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.TimeUtils.*
import com.emmanuelmess.scrollinggame.Direction.*
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch

import com.badlogic.gdx.math.Intersector
import kotlin.properties.Delegates


object Size {
    const val WIDTH = 1000f
    const val HEIGHT = 16 * WIDTH / 9
}

class ScrollingGame : ApplicationAdapter() {

    companion object {
        const val BACKGROUND_SPEED = 500f/1000f

        const val BALL_MOVEMENT_VELOCITY = 500f/1000f

        const val BALL_RADIUS = 50f

        const val BALL_POSITION_Y = HEIGHT - BALL_RADIUS - 50

        private const val LINE_HALF_WIDTH = 125f
    }

    private lateinit var viewport: FillViewport
    private lateinit var camera: OrthographicCamera

    private lateinit var polygonSpriteBatch: PolygonSpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer

    private val gameState = GameState()

    private var paused = false

    override fun create() {
        camera = OrthographicCamera().apply {
            setToOrtho(true, WIDTH, HEIGHT)
        }
        viewport = FillViewport(WIDTH, HEIGHT, camera)

        polygonSpriteBatch = PolygonSpriteBatch()
        shapeRenderer = ShapeRenderer()

        Gdx.graphics.isContinuousRendering = true
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    private var lastRenderUpdate = millis()
    override fun render() {
        inputUpdate()


        val deltaTime = timeSinceMillis(lastRenderUpdate)
        if(deltaTime < 1000/60) {
            return
        }
        lastRenderUpdate = millis()


        if(paused) {
            return
        }


        gameState.moveBackground(deltaTime)


        ScreenUtils.clear(0f, 0f, 0f, 1f)

        shapeRenderer.projectionMatrix = viewport.camera.combined

        shapeRenderer.draw(type = ShapeRenderer.ShapeType.Filled) {
            color = Color.WHITE
            for (i in 0 until (gameState.points.size - 1)) {
                slantedRectangle(gameState.points[i], gameState.points[i + 1], LINE_HALF_WIDTH)
            }

            color = Color.RED
            circle(WIDTH / 2f + gameState.ballPositionX.toInt(), BALL_POSITION_Y, BALL_RADIUS)
        }


        physicsUpdate()
    }

    private fun ShapeRenderer.slantedRectangle(start: Vector2, end: Vector2, halfWidth: Float) {
        val x1 = end.x - halfWidth
        val y1 = end.y

        val x2 = end.x + halfWidth
        val y2 = end.y

        val x3 = start.x + halfWidth
        val y3 = start.y

        val x4 = start.x - halfWidth
        val y4 = start.y

        triangle(x1, y1, x2, y2,x3, y3)
        triangle(x1, y1, x3, y3, x4, y4)
    }

    private var touchPoint = Vector3()
    private var lastInputUpdate = millis()
    private fun inputUpdate() {
        val deltaTime = timeSinceMillis(lastInputUpdate)
        if(deltaTime < 1000/120) {
            return
        }

        lastInputUpdate = millis()

        if (!Gdx.input.isTouched) {
            return
        }

        if(paused) {
            paused = false
        } else {
            touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPoint)

            if (touchPoint.x < WIDTH / 2f) {
                gameState.move(LEFT, deltaTime)
            } else if (touchPoint.x > WIDTH / 2f) {
                gameState.move(RIGHT, deltaTime)
            }
        }
    }

    val ballPosition = Vector2()
    private fun physicsUpdate() {
        var segmentOfBall: Int? = null
        for (i in 0 until (gameState.points.size - 1)) {
            if((gameState.points[i].y .. gameState.points[i + 1].y).contains(BALL_POSITION_Y)) {
                segmentOfBall = i
            }
        }

        segmentOfBall?.let {
            ballPosition.set(gameState.ballPositionX, BALL_POSITION_Y)
            val distance = Intersector.distanceSegmentPoint(
                gameState.points[segmentOfBall],
                gameState.points[segmentOfBall + 1],
                ballPosition
            )
            val crashedAgainstWall = distance > LINE_HALF_WIDTH - BALL_RADIUS

            if (crashedAgainstWall) {
                paused = true
            }
        }
    }

    override fun pause() {
        super.pause()

        paused = true
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}