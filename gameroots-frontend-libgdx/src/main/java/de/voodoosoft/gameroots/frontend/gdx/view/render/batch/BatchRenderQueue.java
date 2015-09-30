package de.voodoosoft.gameroots.frontend.gdx.view.render.batch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Layered and sorted batch drawing of graphics items.
 * <p/>
 * Before each render cycle, all batch items to be drawn must be added by calling <code>queueItem</code>
 * and freed again by invoking <code>reset</code>.
 * <br/>While rendering, items are first sorted by item layer, then for each layer by individual item sort orders.
 * <br/>Batch items are held in object pools. Each item type must have been registered once by calling <code>addItemClass</code>.
 * <p/>
 * Typical method call order for one render cycle:
 * <br/>1. <code>obtainItem()</code>
 * <br/>2. <code>queueItem()</code>
 * <br/>3. <code>render()</code>
 * <br/>4. <code>reset()</code>
 */
public class BatchRenderQueue {
	/**
	 * Creates a new render queue with the given number of maximal layers.
	 * @param maxLayers
	 */
	public BatchRenderQueue(int maxLayers) {
		this(1024, maxLayers);
	}

	/**
	 * Creates a new render queue with the given starting and maximal layer numbers.
	 * @param initialCapacity
	 * @param maxLayers
	 */
	public BatchRenderQueue(int initialCapacity, int maxLayers) {
		if (initialCapacity == 0) {
			throw new IllegalArgumentException("initial capacity must be > 0");
		}

		this.maxLayers = maxLayers;
		itemCount = new int[maxLayers];

		itemLayers = new ArrayList<BatchRenderItem[]>();
		for (int i = 0; i < maxLayers; i++) {
			BatchRenderItem[] batchItems = new BatchRenderItem[initialCapacity];
			itemLayers.add(batchItems);
		}

		itemPools = new ObjectMap<Class, Pool<BatchRenderItem>>();
	}

	/**
	 * Registers the specified batch item type.
	 *
	 * @param itemClass batch item typ
	 * @param pool the pool supplying batch items
	 */
	public void addItemClass(Class<? extends BatchRenderItem> itemClass, Pool<BatchRenderItem> pool) {
		itemPools.put(itemClass, pool);
	}

	/**
	 * Returns a free batch item from its individual object pool.
	 *
	 * @param itemClass
	 * @param <T>
	 * @return
	 */
	public <T extends BatchRenderItem> T obtainItem(Class<T> itemClass) {
		return (T)itemPools.get(itemClass).obtain();
	}

	/**
	 * Adds a batch item to be drawn during the next call of <code>render</code>.
	 *
	 * @param item batch item
	 */
	public void queueItem(BatchRenderItem item) {
		int layer = item.getLayer();
		BatchRenderItem[] items = itemLayers.get(layer);
		if (itemCount[layer] == items.length) {
			items = Arrays.copyOf(items, items.length * 2);
		}
		int i = itemCount[layer]++;
		items[i] = item;
	}

	/**
	 * Draws all queued batch items.
	 * <p/>
	 * Draw order is dependent on layer and batch item order.
	 * The sprite batch must have been opened before calling <code>render</code> and closed afterwards.
	 * <br/>Make sure to start rendering with a <i>reset</i> render queue.
	 *
	 * @see #reset()
	 *
	 * @param batch sprite batch used for drawing
	 * @param time current time in ns
	 */
	public void render(SpriteBatch batch, long time) {
		for (int i = 0; i < maxLayers; i++) {
			int itemCount = this.itemCount[i];
			if (itemCount > 0) {
				BatchRenderItem[] items = itemLayers.get(i);
				Sort.instance().sort(items, 0, itemCount - 1);

				for (int j = 0; j < itemCount; j++) {
					items[j].render(batch, time);
				}
			}
		}
	}

	/**
	 * Resets this render queue.
	 * <p/>Typically called after or before rendering batches with <code>render</code>.
	 * <br/>Frees all pooled batch items.
	 */
	public void reset() {
		for (int i = 0; i < maxLayers; i++) {
			BatchRenderItem[] items = itemLayers.get(i);
			int itemCount = this.itemCount[i];
			for (int j = 0; j < itemCount; j++) {
				BatchRenderItem batchItem = items[j];
				Pool<BatchRenderItem> itemPool = itemPools.get(batchItem.getClass());
				itemPool.free(batchItem);
				items[j] = null;
			}
			this.itemCount[i] = 0;
		}
	}

	private int maxLayers;
	private ObjectMap<Class, Pool<BatchRenderItem>> itemPools;
	private List<BatchRenderItem[]> itemLayers;
	private int itemCount[];
}
