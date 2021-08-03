package com.emmanuelmess.scrollinggame

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

fun ShapeRenderer.draw(color: Color, type: ShapeRenderer.ShapeType, f: ShapeRenderer.() -> Unit) {
    begin(type)
    this.color = color
    f()
    end()
}