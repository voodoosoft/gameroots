package de.voodoosoft.gameroots.examples.batch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BatchRenderItem;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BatchRenderQueue;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl.TextureBatchItem;

import java.util.Random;



/**
 * Draws four layers of batched sprites sorted by z-order.
 */
public class BatchQueueSample extends ApplicationAdapter {
	private final Vector2 WINDOW_SIZE = new Vector2(1024, 768);
	private final int MAX_SPRITES = 200;
	private final int MAX_LAYERS = 4;

	private SpriteBatch batch;
	private OrthographicCamera cam;
	private Viewport viewport;
	private BatchRenderQueue renderQueue;
	private Random rnd;

	private TextureRegion[] spriteTextureRegions;
	private Vector2[] spriteLocations;
	private int[] spriteLayers;
	private Color[] spriteColors;

	@Override
	public void create() {
		// set up screen
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.fill, WINDOW_SIZE.x, WINDOW_SIZE.y, cam);

		// create the batch queue with a batch item provider
		renderQueue = new BatchRenderQueue(MAX_LAYERS+1);
		renderQueue.addItemClass(TextureBatchItem.class, new Pool<BatchRenderItem>() {
			@Override
			protected TextureBatchItem newObject() {
				return new TextureBatchItem();
			}
		});

		// load textures
		TextureAtlas atlas = new TextureAtlas("badges.txt");
		spriteTextureRegions = new TextureRegion[MAX_LAYERS];
		for (int i = 1; i <= MAX_LAYERS; i++) {
			TextureAtlas.AtlasRegion region = atlas.findRegion("a" + i);
			spriteTextureRegions[i - 1] = region;
		}

		// color layers to visualize z-order
		spriteColors = new Color[MAX_LAYERS];
		spriteColors[0] = new Color(1f, 1f, 1f, 0.25f);
		spriteColors[1] = new Color(1f, 1f, 1f, 0.5f);
		spriteColors[2] = new Color(1f, 1f, 1f, 0.75f);
		spriteColors[3] = new Color(1f, 1f, 1f, 1f);

		// position sprites at random locations
		rnd = new Random();
		spriteLocations = new Vector2[MAX_SPRITES];
		spriteLayers = new int[MAX_SPRITES];
		for (int i = 0; i < MAX_SPRITES; i++) {
			int randomX = (int)(rnd.nextFloat() * WINDOW_SIZE.x);
			int randomY = (int)(rnd.nextFloat() * WINDOW_SIZE.y);
			spriteLocations[i] = new Vector2(randomX, randomY);
			int randomItem = (int)(rnd.nextFloat() * MAX_LAYERS) + 1;
			spriteLayers[i] = randomItem;
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);

		// free all batch items from last cycle
		renderQueue.reset();

		// queue batch items to render
		for (int i = 0; i < MAX_SPRITES; i++) {
			// retrieve free batch item
			TextureBatchItem batchItem = renderQueue.obtainItem(TextureBatchItem.class);

			// setup batch item
			int layer = spriteLayers[i];
			batchItem.setLayer(layer);
			batchItem.setTextureRegion(spriteTextureRegions[layer - 1]);
			batchItem.setLocation(spriteLocations[i].x, spriteLocations[i].y);
			batchItem.setColor(spriteColors[layer - 1]);

			// queue batch item
			renderQueue.queueItem(batchItem);
		}

		// push queued batch items to SpriteBatch in sorted order
		long time = System.nanoTime();
		batch.begin();
		renderQueue.render(batch, time);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}
}
