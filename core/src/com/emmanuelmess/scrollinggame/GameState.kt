package com.emmanuelmess.scrollinggame

import com.badlogic.gdx.math.Vector2
import com.emmanuelmess.scrollinggame.Direction.*
import com.emmanuelmess.scrollinggame.ScrollingGame.Companion.BACKGROUND_SPEED
import com.emmanuelmess.scrollinggame.ScrollingGame.Companion.BALL_MOVEMENT_VELOCITY
import com.emmanuelmess.scrollinggame.ScrollingGame.Companion.BALL_RADIUS
import com.emmanuelmess.scrollinggame.Size.HEIGHT
import com.emmanuelmess.scrollinggame.Size.WIDTH
import kotlin.random.Random

enum class Direction {
    LEFT, RIGHT
}

class GameState {
    var ballPositionX: Float = 0f
        private set

    fun move(direction: Direction, deltaTime: Long) {
        when(direction) {
            LEFT -> ballPositionX -= BALL_MOVEMENT_VELOCITY * deltaTime
            RIGHT -> ballPositionX += BALL_MOVEMENT_VELOCITY * deltaTime
        }

        ballPositionX = ballPositionX.coerceIn(-WIDTH /2f + BALL_RADIUS, WIDTH /2f - BALL_RADIUS)
    }

    val points = mutableListOf<Vector2>()

    fun moveBackground(deltaTime: Long) {
        for (point in points) {
            point.y += deltaTime * BACKGROUND_SPEED
        }

        if(points.isEmpty()) {
            points.add(Vector2(WIDTH / 2f, HEIGHT))
        }

        if(points.size < 2) {
            points.add(0, generatePoint(points[0]))
            points.add(0, generatePoint(points[0]))
        }

        if(points.first().y >= 0) {
            points.add(0, generatePoint(points[0]))
        }

        if(points[points.size - 2].y > HEIGHT) {
            points.removeAt(points.size - 1)
        }
    }

    private fun generatePoint(lastPoint: Vector2, reusePoint: Vector2 = Vector2()): Vector2 {
        reusePoint.x = WIDTH/2 + Random.nextInt(-(WIDTH / 5).toInt(), (2* WIDTH / 5).toInt())
        reusePoint.y = lastPoint.y - Random.nextInt((HEIGHT / 5).toInt(), (2* HEIGHT / 5).toInt())

        return reusePoint
    }

}
