package com.emmanuelmess.scrollinggame

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

fun ShapeRenderer.draw(type: ShapeRenderer.ShapeType, f: ShapeRenderer.() -> Unit) {
    begin(type)
    f()
    end()
}