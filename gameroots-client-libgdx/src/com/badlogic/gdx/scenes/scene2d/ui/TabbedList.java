package com.badlogic.gdx.scenes.scene2d.ui;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pools;



/** A list (aka list box) displays textual items and highlights the currently selected item.
 * 
 * {@link ChangeEvent} is fired when the list selection changes.
 * <p>
 * The preferred size of the list is determined by the text bounds of the items and the size of the {@link ListStyle#selection}.
 * <p>
 * Items can include multiple sub items which are drawn left aligned in individual columns.
 * <p>An example using the tab character as delimiter:<p>
 * <code>
 * String[] items = {"12:00\tgame1\trunning", "13:00\tgame-2\tfinished"};
 * <br>TabbedList list = new TabbedList(items, mySkin);
 * <br>list.setColumnGap(20f);
 * <br>list.setHeader("Time\tName\tState");
 * </code>
 * <p>The items and the header are displayed like this:
 * <pre>
 *    Time  Name   State
 *    12:00 game1  running
 *    13:00 game-2 finished
 * </pre>
 * Each column width is derived from the widest sub item of all rows.
 * <p>
 * <code>TabbedList</code> is thread safe to be able to draw and replace list items from different threads.
 * </p>
 * <code>TabbedList</code> was derived from the original <code>List</code> class of LibGdx.
 * */
public class TabbedList extends Widget implements Cullable {

	private TabbedListStyle style;
	private String[] items;
	private int selectedIndex;
	private boolean selectable;
	private int lineCut;
	private Rectangle cullingArea;
	private float prefWidth, prefHeight;
	private float itemHeight;
	private float textOffsetX, textOffsetY;
	private List<Float> columnWidths;
	private String tabDelimiter;
	private String header;
	private float columnGap;

	public TabbedList(Object[] items, Skin skin) {
		this(items, skin.get(TabbedListStyle.class));
	}

	public TabbedList(Object[] items, Skin skin, String styleName) {
		this(items, skin.get(styleName, TabbedListStyle.class));
	}

	public TabbedList(Object[] items, TabbedListStyle style) {
		columnWidths = new ArrayList<Float>();
		tabDelimiter = "\\t";
		selectable = true;

		setStyle(style);
		setItems(items);
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());

		addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (pointer == 0 && button != 0)
					return false;
				TabbedList.this.touchDown(y);
				return true;
			}
		});

		selectedIndex = -1;
	}

	/**
	 * Defines the delimiter to separate sub items.
	 * @param tabDelimiter
	 */
	public synchronized void setTabDelimiter(String tabDelimiter) {
		this.tabDelimiter = tabDelimiter;
	}

	/**
	 * Sets the header row.
	 * @param header
	 */
	public synchronized void setHeader(String header) {
		this.header = header;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public void setLineCut(int lineCut) {
		this.lineCut = lineCut;
	}

	/**
	 * Defines the space between two columns.
	 * @param columnGap
	 */
	public synchronized void setColumnGap(float columnGap) {
		this.columnGap = columnGap;
	}

	synchronized void touchDown(float y) {
		int oldIndex = selectedIndex;

		int newIndex = (int)((getHeight() - y) / itemHeight);
		newIndex = Math.max(0, newIndex);
		newIndex = Math.min(items.length - 1, newIndex);

		if (header == null || newIndex > 0) {
			selectedIndex = newIndex;
			ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
			if (fire(changeEvent))
				selectedIndex = oldIndex;
			Pools.free(changeEvent);
		}
	}

	public synchronized void setStyle(TabbedListStyle style) {
		if (style == null)
			throw new IllegalArgumentException("style cannot be null.");
		this.style = style;
		if (items != null)
			setItems(items);
		else
			invalidateHierarchy();
	}

	/** Returns the list's style. Modifying the returned style may not have an effect until {@link #setStyle(ListStyle)} is called. */
	public synchronized TabbedListStyle getStyle() {
		return style;
	}

	@Override
	public synchronized void draw(SpriteBatch batch, float parentAlpha) {
		BitmapFont font = style.font;
		Drawable selectedDrawable = style.selection;
		Color fontColorSelected = style.fontColorSelected;
		Color fontColorUnselected = style.fontColorUnselected;

		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX();
		float y = getY();

		font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
		float itemY = getHeight();
		for (int i = 0; i < items.length; i++) {
			if (cullingArea == null || (itemY - itemHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
				if (selectable && selectedIndex == i) {
					selectedDrawable.draw(batch, x, y + itemY - itemHeight, Math.max(prefWidth, getWidth()), itemHeight);
					font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
				}

				String[] subItems = items[i].split(tabDelimiter);
				float tabOffset = 0;
				for (int j = 0; j < subItems.length; j++) {
					font.draw(batch, subItems[j], x + textOffsetX + tabOffset, y + itemY - textOffsetY);
					Float width = columnWidths.size() > j ? columnWidths.get(j) : 0;
					if (width != null) {
						tabOffset += width;
					}
				}

				if (selectedIndex == i) {
					font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
				}
			}
			else if (itemY < cullingArea.y) {
				break;
			}
			itemY -= itemHeight;
		}
	}

	/** @return The index of the currently selected item. The top item has an index of 0. */
	public synchronized int getSelectedIndex() {
		return selectedIndex;
	}

	public synchronized void setSelectedIndex(int index) {
		if (index < 0 || index >= items.length)
			throw new GdxRuntimeException("index must be >= 0 and < " + items.length + ": " + index);
		selectedIndex = index;
	}

	/** @return The text of the currently selected item or null if the list is empty. */
	public synchronized String getSelection() {
		if (items.length == 0)
			return null;
		return items[selectedIndex];
	}

	/** @return The index of the item that was selected, or -1. */
	public synchronized int setSelection(String item) {
		selectedIndex = -1;
		for (int i = 0, n = items.length; i < n; i++) {
			if (items[i].equals(item)) {
				selectedIndex = i;
				break;
			}
		}
		return selectedIndex;
	}

	public synchronized void setItems(Object[] objects) {
		if (objects == null)
			throw new IllegalArgumentException("items cannot be null.");

		int offset = header != null ? 1 : 0;
		String[] strings = new String[objects.length + offset];
		if (header != null) {
			strings[0] = header;
		}

		if (!(objects instanceof String[])) {
			for (int i = 0, n = objects.length; i < n; i++)
				strings[i + offset] = String.valueOf(objects[i]);
		}
		else {
			for (int i = 0, n = objects.length; i < n; i++)
				strings[i + offset] = ((String[])objects)[i];
		}
		items = strings;
		if (items.length == 0 || selectedIndex > items.length - 1) {
			selectedIndex = 0;
		}

		final BitmapFont font = style.font;
		final Drawable selectedDrawable = style.selection;

		itemHeight = font.getCapHeight() - lineCut - font.getDescent() * 2;
		itemHeight += selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();
		prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();
		textOffsetX = selectedDrawable.getLeftWidth();
		textOffsetY = selectedDrawable.getTopHeight() - font.getDescent();

		columnWidths.clear();

		prefWidth = 0;
		for (int i = 0; i < items.length; i++) {
			TextBounds bounds = font.getBounds(items[i]);
			prefWidth = Math.max(bounds.width, prefWidth);
			updateTabBounds(font, items[i]);
		}
		prefHeight = items.length * itemHeight;

		invalidateHierarchy();
	}

	private void updateTabBounds(BitmapFont font, String item) {
		String[] columns = item.split(tabDelimiter);
		for (int i = 0; i < columns.length; i++) {
			TextBounds columnBounds = font.getBounds(columns[i]);
			Float maxColumnWidth = columnWidths.size() > i ? columnWidths.get(i) : null;
			if (maxColumnWidth == null) {
				columnWidths.add(columnBounds.width + columnGap);
			}
			else {
				if (columnBounds.width + columnGap > maxColumnWidth) {
					columnWidths.set(i, columnBounds.width + columnGap);
				}
			}
		}
	}

	public synchronized String[] getItems() {
		return items;
	}

	@Override
	public synchronized float getPrefWidth() {
		return prefWidth;
	}

	@Override
	public synchronized float getPrefHeight() {
		return prefHeight;
	}

	@Override
	public synchronized void setCullingArea(Rectangle cullingArea) {
		this.cullingArea = cullingArea;
	}

	/** The style for a list, see {@link TabbedList}.
	 */
	static public class TabbedListStyle {

		public BitmapFont font;
		public Color fontColorSelected = new Color(1, 1, 1, 1);
		public Color fontColorUnselected = new Color(1, 1, 1, 1);
		public Drawable selection;

		public TabbedListStyle() {
		}

		public TabbedListStyle(BitmapFont font, Color fontColorSelected, Color fontColorUnselected, Drawable selection) {
			this.font = font;
			this.fontColorSelected.set(fontColorSelected);
			this.fontColorUnselected.set(fontColorUnselected);
			this.selection = selection;
		}

		public TabbedListStyle(ListStyle style) {
			this.font = style.font;
			this.fontColorSelected.set(style.fontColorSelected);
			this.fontColorUnselected.set(style.fontColorUnselected);
			this.selection = style.selection;
		}
	}
}
