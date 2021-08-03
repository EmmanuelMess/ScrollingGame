package com.emmanuelmess.scrollinggame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FillViewport

class ScrollingGame : ApplicationAdapter() {

    object Size {
        const val WIDTH = 1000f
        const val HEIGHT = 16 * WIDTH / 9

        const val C = 10f
    }


    private lateinit var viewport: FillViewport
    private lateinit var camera: OrthographicCamera

    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var img: Texture

    override fun create() {
        camera = OrthographicCamera().apply {
            setToOrtho(true, Size.WIDTH, Size.HEIGHT)
        }
        viewport = FillViewport(Size.WIDTH, Size.HEIGHT, camera)

        shapeRenderer = ShapeRenderer()
        img = Texture("badlogic.jpg")

        Gdx.graphics.isContinuousRendering = true
    }


    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f)

        shapeRenderer.projectionMatrix = viewport.camera.combined

        val points = listOf(
            Vector2(Size.WIDTH / 2, 0F),
            Vector2(Size.WIDTH / 2, Size.HEIGHT / 2),
            Vector2(Size.WIDTH / 2, Size.HEIGHT)
        )

        shapeRenderer.draw(color = Color.WHITE, type = ShapeRenderer.ShapeType.Filled) {
            for (i in 0 until (points.size - 1)) {
                rectLine(points[i], points[i + 1], 50F)
            }

        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
        img.dispose()
    }
}