# Game Roots
Framework Components for Game Programming with Java

##Sorted Batch Rendering for LibGdx
###Motivation
This is a spin-off from the development of the game [Lethal Running](http://www.lethalrunning.com).
In the beginning, rendering was split up into multiple classes and as the code base grew,
it was tedious to keep the right order for rendering layers of tiles and sprite components.

Furthermore, a lot of texture switches occurred. Whether that was a performance hit,
was not analysed, but it was fixed easily after introducing sorted batch rendering.
Taking the blend mode as sort criterium reduces the number of `SpriteBatch` flushes as well.

It is no longer necessary to provide an open `SpriteBatch` to each render class.

There are default implementations for rendering textures, particles and vertices. In case they don't meet your needs, just derive your own item classes from `BatchRenderItem`.
The first sort criterium is always the layer (z-index) followed by individual criteria like texture handle or blend mode.
In case the layer is the only relevant criterium, no actual sorting is performed for rendering because items are always presorted by layer when they are added to the render queue.

###Examples:
Run `BatchQueueLauncher` to get a first impression.

Create a render queue for texture items:
```
		renderQueue = new BatchRenderQueue(MAX_LAYERS);
		renderQueue.addItemClass(TextureBatchItem.class, new Pool<BatchRenderItem>() {
			@Override
			protected TextureBatchItem newObject() {
				return new TextureBatchItem();
			}
		});
```
Queue a batch item:
```
			TextureBatchItem batchItem = renderQueue.obtainItem(TextureBatchItem.class);
			batchItem.setLayer(1);
			batchItem.setTextureRegion(SPRITE_REGION);
			batchItem.setLocation(123, 456);
			batchItem.setColor(Color.WHITE);

			renderQueue.queueItem(batchItem);
```
Draw all queued items:
```
private void render(BatchRenderQueue renderQueue, SpriteBatch batch) {
		batch.begin();
		long time = System.nanoTime();
		renderQueue.render(batch, time);
		batch.end();
```

###Modules and Packages:
**gameroots-frontend-libgdx:**  
`de.voodoosoft.gameroots.frontend.gdx.view.render.batch`

**gameroots-frontend-examples:**  
`de.voodoosoft.gameroots.examples.batch`

##Tweening
A simple to use basic tween library.

###Features:
- value interpolation tweens (see LibGdx Interpolation classes)
- sequences tweens
- delay tweens

###Examples:
Simulating day-night cycles.
```
		long start = timer.getTimeValue();

		SequenceTween dayNightTweens = new SequenceTween();
		dayNightTweens.setLoop(true);

		// dawn tween: increase value from 0.0 to 1.0 in timed steps of 0.1
		LinearFixedStepTween dawnTween = new LinearFixedStepTween(0, 1, 0.1f, Timing.millisToNano(10));
		dawnTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onUpdate(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " dawn: " + value);
			}
		});
		dayNightTweens.addTween(dawnTween);

		// day tween: simply wait for for 3 seconds
		DelayTween dayTween = new DelayTween(Timing.secsToNano(3));
		dayTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onStart(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " day");
			}
		});
		dayNightTweens.addTween(dayTween);

		// dusk tween: decrease value from 1.0 to 0.0 in timed steps of 0.1
		LinearFixedStepTween duskTween = new LinearFixedStepTween(1, 0, -0.1f, Timing.millisToNano(10));
		duskTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onUpdate(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " dusk: " + value);
			}
		});
		dayNightTweens.addTween(duskTween);

		// night tween: simply wait for 3 seconds
		DelayTween nightTween = new DelayTween(Timing.secsToNano(3));
		nightTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onStart(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " night");
			}
		});
		dayNightTweens.addTween(nightTween);

		while (!dayNightTweens.isDone()) {
			dayNightTweens.update(timer.getTimeValue());
		}
```