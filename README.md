# Game Roots
Framework Components for Game Programming with Java

##Sorted Batch Rendering for LibGdx
###Motivation
This is a spin-off from the development of the game [Biodrone Battle](http://www.biodronebattle.com).
In the beginning, rendering was split up into multiple classes and as the code base grew,
it was tedious to keep the right order for rendering layers of tiles and sprite components.
Furthermore, a lot of texture switches occurred. Whether that was a performance hit,
was not analysed, but it was fixed easily after introducing sorted batch rendering.
It is no longer necessary to provide an open SpriteBatch to each render class.

There are default implementations for rendering textures, particles and vertices.
The first sort criterium is always the layer (z-index) followed by individual criteria like texture handle or blend mode.

###Module and Packages:

gameroots-frontend-libgdx

`de.voodoosoft.gameroots.frontend.gdx.view.render.batch`

###Examples:

gameroots-frontend-examples

`de.voodoosoft.gameroots.examples.batch`
