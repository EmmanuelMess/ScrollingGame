package com.emmanuelmess.scrollinggame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FillViewport
import com.emmanuelmess.scrollinggame.ScrollingGame.Size.HEIGHT
import com.emmanuelmess.scrollinggame.ScrollingGame.Size.WIDTH
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.TimeUtils
import com.emmanuelmess.scrollinggame.ScrollingGame.GameState.Direction.*

class ScrollingGame : ApplicationAdapter() {

    object Size {
        const val WIDTH = 1000f
        const val HEIGHT = 16 * WIDTH / 9

        const val C = 10f
    }

    companion object {
        private const val BALL_MOVEMENT_VELOCITY = 500f/1000f

        private const val BALL_RADIUS = 50f

        private const val LINE_HALF_WIDTH = 125f
    }

    private lateinit var viewport: FillViewport
    private lateinit var camera: OrthographicCamera

    private lateinit var shapeRenderer: ShapeRenderer

    private class GameState {
        var ballPositionX: Float = 0f
            private set

        enum class Direction {
            LEFT, RIGHT
        }

        fun move(direction: Direction, deltaTime: Long) {
            when(direction) {
                LEFT -> ballPositionX -= BALL_MOVEMENT_VELOCITY * deltaTime
                RIGHT -> ballPositionX += BALL_MOVEMENT_VELOCITY * deltaTime
            }

            ballPositionX = ballPositionX.coerceIn(-WIDTH/2f + BALL_RADIUS, WIDTH/2f - BALL_RADIUS)
        }
    }

    private val gameState = GameState()

    override fun create() {
        camera = OrthographicCamera().apply {
            setToOrtho(true, WIDTH, HEIGHT)
        }
        viewport = FillViewport(WIDTH, HEIGHT, camera)

        shapeRenderer = ShapeRenderer()

        Gdx.graphics.isContinuousRendering = true
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render() {
        update()

        ScreenUtils.clear(0f, 0f, 0f, 1f)

        shapeRenderer.projectionMatrix = viewport.camera.combined

        val points = listOf(
            Vector2(WIDTH / 2f, 0f),
            Vector2(WIDTH / 2f, HEIGHT / 2),
            Vector2(WIDTH / 2f, HEIGHT)
        )

        shapeRenderer.draw(type = ShapeRenderer.ShapeType.Filled) {
            color = Color.WHITE
            for (i in 0 until (points.size - 1)) {
                rectLine(points[i], points[i + 1], LINE_HALF_WIDTH * 2f)
            }

            color = Color.RED
            circle(
                WIDTH / 2f + gameState.ballPositionX.toInt(),
                HEIGHT - BALL_RADIUS - 50, BALL_RADIUS
            )
        }
    }

    private var touchPoint = Vector3()
    private var lastUpdate = TimeUtils.millis()
    fun update() {
        val deltaTime = TimeUtils.timeSinceMillis(lastUpdate)
        if(deltaTime < 1000/120) {
            return
        }

        lastUpdate = TimeUtils.millis()

        if (Gdx.input.isTouched) {
            touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPoint)

            if(touchPoint.x < WIDTH / 2f) {
                gameState.move(LEFT, deltaTime)
            } else if (touchPoint.x > WIDTH / 2f) {
                gameState.move(RIGHT, deltaTime)
            }
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}