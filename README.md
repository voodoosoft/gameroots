# Game Roots
Framework Components for Game Programming with Java

##Sorted Batch Rendering
This is a spin-off from the development of the game [Biodrone Battle](http://www.biodronebattle.com).
In the beginning, rendering was split up into multiple classes and as the code base grew,
it was tedious to keep the right order for rendering layers of tiles and sprite components.
Furthermore, a lot of texture switches occurred. Whether that was a performance hit,
was not analysed, but it was fixed easily after introducing sorted batch rendering.
It is no longer necessary to provide an open SpriteBatch to each render class.

**module and packages:**

gameroots-frontend-libgdx

`de.voodoosoft.gameroots.frontend.gdx.view.render.batch`

**examples:**

`de.voodoosoft.gameroots.examples.batch`
