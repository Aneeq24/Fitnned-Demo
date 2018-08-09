package com.bwf.hiit.workout.abs.challenge.home.fitness.wheel;

import android.graphics.Typeface;

import java.util.List;


public interface IWheelPicker {


    int getVisibleItemCount();

    void setVisibleItemCount(int count);


    boolean isCyclic();

    void setCyclic(boolean isCyclic);


    void setOnItemSelectedListener(WheelPicker.OnItemSelectedListener listener);

    int getSelectedItemPosition();


    void setSelectedItemPosition(int position);

    int getCurrentItemPosition();

    List getData();


    void setData(List data);

    /**
     * 设置数据项是否有相同的宽度
     * 滚轮选择器在确定尺寸大小时会通过遍历数据源来计算每一条数据文本的宽度以找到最宽的文本作为滚轮选择器的
     * 最终宽度，当数据源的数据非常多时，这个过程可能会消耗大量的时间导致效率降低，而且在大部分数据量多情况
     * 下，数据文本大都有相同的宽度，这种情况下调用该方法告诉滚轮选择器数据宽度相同则可以免去上述计算时间，
     * 提升效率
     * 有些时候，你所加载的数据源确实是每条数据文本的宽度都不同，但是你知道最宽的数据文本在数据源中的位置，
     * 这时你可以调用{@link #setMaximumWidthTextPosition(int)}方法告诉滚轮选择器最宽的这条数据文本在数据
     * 源的什么位置，滚轮选择器则会根据该位置找到该条数据文本并将其宽度作为滚轮选择器的宽度。如果你不知道位
     * 置，但是知道最宽的数据文本，那么你也可以直接通过调用{@link #setMaximumWidthText(String)}告诉滚轮选
     * 择器最宽的文本是什么，滚轮选择器会根据这条文本计算宽度并将其作为滚轮选择器的宽度
     * <p>
     * Set items of WheelPicker if has same width
     * WheelPicker will traverse the data source to calculate each data text width to find out the
     * maximum text width for the final view width, this process maybe spends a lot of time and
     * reduce efficiency when data source has large amount data, in most large amount data case,
     * data text always has same width, you can call this method tell to WheelPicker your data
     * source has same width to save time and improve efficiency.
     * Sometimes the data source you set is positively has different text width, but maybe you know
     * the maximum width text's position in data source, then you can call
     * {@link #setMaximumWidthTextPosition(int)} tell to WheelPicker where is the maximum width text
     * in data source, WheelPicker will calculate its width base on this text which found by
     * position. If you don't know the position of maximum width text in data source, but you have
     * maximum width text, you can call {@link #setMaximumWidthText(String)} tell to WheelPicker
     * what maximum width text is directly, WheelPicker will calculate its width base on this text.
     *
     * @param hasSameSize 是否有相同的宽度
     */
    void setSameWidth(boolean hasSameSize);

    /**
     * 数据项是否有相同宽度
     * <p>
     * Whether items has same width or not
     *
     * @return 是否有相同宽度
     */
    boolean hasSameWidth();

    /**
     * 设置滚轮滚动状态改变监听器
     *
     * @param listener 滚轮滚动状态改变监听器
     * @see com.bwf.hiit.workout.abs.challenge.home.fitness.wheel.WheelPicker.OnWheelChangeListener
     */
    void setOnWheelChangeListener(WheelPicker.OnWheelChangeListener listener);

    /**
     * 获取最宽的文本
     * <p>
     * Get maximum width text
     *
     * @return 最宽的文本
     */
    String getMaximumWidthText();

    /**
     * 设置最宽的文本
     * <p>
     * Set maximum width text
     *
     * @param text 最宽的文本
     * @see #setSameWidth(boolean)
     */
    void setMaximumWidthText(String text);

    /**
     * 获取最宽的文本在数据源中的位置
     * <p>
     * Get the position of maximum width text in data source
     *
     * @return 最宽的文本在数据源中的位置
     */
    int getMaximumWidthTextPosition();

    /**
     * 设置最宽的文本在数据源中的位置
     * <p>
     * Set the position of maximum width text in data source
     *
     * @param position 最宽的文本在数据源中的位置
     * @see #setSameWidth(boolean)
     */
    void setMaximumWidthTextPosition(int position);

    /**
     * 获取当前选中的数据项文本颜色
     * <p>
     * Get text color of current selected item
     * For example 0xFF123456
     *
     * @return 当前选中的数据项文本颜色
     */
    int getSelectedItemTextColor();

    /**
     * 设置当前选中的数据项文本颜色
     * <p>
     * Set text color of current selected item
     * For example 0xFF123456
     *
     * @param color 当前选中的数据项文本颜色，16位颜色值
     */
    void setSelectedItemTextColor(int color);

    /**
     * 获取数据项文本颜色
     * <p>
     * Get text color of items
     * For example 0xFF123456
     *
     * @return 数据项文本颜色
     */
    int getItemTextColor();

    /**
     * 设置数据项文本颜色
     * <p>
     * Set text color of items
     * For example 0xFF123456
     *
     * @param color 数据项文本颜色，16位颜色值
     */
    void setItemTextColor(int color);

    /**
     * 获取数据项文本尺寸大小
     * <p>
     * Get text size of items
     * Unit in px
     *
     * @return 数据项文本尺寸大小
     */
    int getItemTextSize();

    /**
     * 设置数据项文本尺寸大小
     * <p>
     * Set text size of items
     * Unit in px
     *
     * @param size 设置数据项文本尺寸大小，单位：px
     */
    void setItemTextSize(int size);

    /**
     * 获取滚轮选择器数据项之间间距
     * <p>
     * Get space between items
     * Unit in px
     *
     * @return 滚轮选择器数据项之间间距
     */
    int getItemSpace();

    /**
     * 设置滚轮选择器数据项之间间距
     * <p>
     * Set space between items
     * Unit in px
     *
     * @param space 滚轮选择器数据项之间间距，单位：px
     */
    void setItemSpace(int space);

    /**
     * 设置滚轮选择器是否显示指示器
     * 如果设置滚轮选择器显示指示器，那么将会在滚轮选择器的当前选中数据项上下显示两根分割线
     * 需要注意的是指示器的尺寸并不参与滚轮选择器的尺寸计算，其会绘制在滚轮选择器的上方
     * <p>
     * Set whether WheelPicker display indicator or not
     * WheelPicker will draw two lines above an below current selected item if display indicator
     * Notice:Indicator's size will not participate in WheelPicker's size calculation, it will drawn
     * above the content
     *
     * @param hasIndicator 是否有指示器
     */
    void setIndicator(boolean hasIndicator);

    /**
     * 滚轮选择器是否有指示器
     * <p>
     * Whether WheelPicker display indicator or not
     *
     * @return 滚轮选择器是否有指示器
     */
    boolean hasIndicator();

    /**
     * 获取滚轮选择器指示器尺寸
     * <p>
     * Get size of indicator
     * Unit in px
     *
     * @return 滚轮选择器指示器尺寸
     */
    int getIndicatorSize();

    /**
     * 设置滚轮选择器指示器尺寸
     * <p>
     * Set size of indicator
     * Unit in px
     *
     * @param size 滚轮选择器指示器尺寸，单位：px
     */
    void setIndicatorSize(int size);

    /**
     * 获取滚轮选择器指示器颜色
     * <p>
     * Get color of indicator
     * For example 0xFF123456
     *
     * @return 滚轮选择器指示器颜色，16位颜色值
     */
    int getIndicatorColor();

    /**
     * 设置滚轮选择器指示器颜色
     * <p>
     * Set color of indicator
     * For example 0xFF123456
     *
     * @param color 滚轮选择器指示器颜色，16位颜色值
     */
    void setIndicatorColor(int color);

    /**
     * 设置滚轮选择器是否显示幕布
     * 设置滚轮选择器显示幕布的话将会在当前选中的项上方绘制一个与当前数据项大小一致的矩形区域并填充指定颜色
     * <p>
     * Set whether WheelPicker display curtain or not
     * WheelPicker will draw a rectangle as big as current selected item and fill specify color
     * above content if curtain display
     *
     * @param hasCurtain 滚轮选择器是否显示幕布
     */
    void setCurtain(boolean hasCurtain);

    /**
     * 滚轮选择器是否显示幕布
     * <p>
     * Whether WheelPicker display curtain or not
     *
     * @return 滚轮选择器是否显示幕布
     */
    boolean hasCurtain();

    /**
     * 获取滚轮选择器幕布颜色
     * <p>
     * Get color of curtain
     * For example 0xFF123456
     *
     * @return 滚轮选择器幕布颜色，16位颜色值
     */
    int getCurtainColor();

    /**
     * 设置滚轮选择器幕布颜色
     * <p>
     * Set color of curtain
     * For example 0xFF123456
     *
     * @param color 滚轮选择器幕布颜色，16位颜色值
     */
    void setCurtainColor(int color);

    /**
     * 设置滚轮选择器是否有空气感
     * 开启空气感的滚轮选择器将呈现中间不透明逐渐向两端透明过度的渐变效果
     * <p>
     * Set whether WheelPicker has atmospheric or not
     * WheelPicker's items will be transparent from center to ends if atmospheric display
     *
     * @param hasAtmospheric 滚轮选择器是否有空气感
     */
    void setAtmospheric(boolean hasAtmospheric);

    /**
     * 滚轮选择器是否有空气感
     * <p>
     * Whether WheelPicker has atmospheric or not
     *
     * @return 滚轮选择器是否有空气感
     */
    boolean hasAtmospheric();

    /**
     * 滚轮选择器是否开启卷曲效果
     * <p>
     * Whether WheelPicker enable curved effect or not
     *
     * @return 滚轮选择器是否开启卷曲效果
     */
    boolean isCurved();

    /**
     * 设置滚轮选择器是否开启卷曲效果
     * 开启滚轮选择器的滚轮效果会呈现一种滚轮两端向屏幕内弯曲的效果
     * 滚轮选择器的卷曲效果依赖于严格的几何模型，一些与尺寸相关的设置在该效果下可能会变得不再有效，例如在卷
     * 曲效果下每一条数据项的尺寸大小因为透视关系看起来都不再一样，数据项之间的间隔也会因为卷曲的关系有微妙
     * 的视觉差距
     * <p>
     * Set whether WheelPicker enable curved effect or not
     * If setCurved true, WheelPicker will display with curved effect looks like ends bend into
     * screen with perspective.
     * WheelPicker's curved effect base on strict geometric model, some parameters relate with size
     * maybe invalidated, for example each item size looks like different because of perspective in
     * curved, the space between items looks like have a little difference
     *
     * @param isCurved 滚轮选择器是否开启卷曲效果
     */
    void setCurved(boolean isCurved);

    /**
     * 获取滚轮选择器数据项的对齐方式
     * <p>
     * Get alignment of WheelPicker
     *
     * @return 滚轮选择器数据项的对齐方式
     */
    int getItemAlign();

    /**
     * 设置滚轮选择器数据项的对齐方式
     * 默认对齐方式为居中对齐{@link WheelPicker#ALIGN_CENTER}
     * <p>
     * Set alignment of WheelPicker
     * The default alignment of WheelPicker is {@link WheelPicker#ALIGN_CENTER}
     *
     * @param align 对齐方式标识值
     *              该值仅能是下列值之一：
     *              {@link WheelPicker#ALIGN_CENTER}
     *              {@link WheelPicker#ALIGN_LEFT}
     *              {@link WheelPicker#ALIGN_RIGHT}
     */
    void setItemAlign(int align);

    /**
     * 获取数据项文本字体对象
     * <p>
     * Get typeface of item text
     *
     * @return 文本字体对象
     */
    Typeface getTypeface();

    /**
     * 设置数据项文本字体对象
     * 数据项文本字体的设置可能会导致滚轮大小的改变
     * <p>
     * Set typeface of item text
     * Set typeface of item text maybe cause WheelPicker size change
     *
     * @param tf 字体对象
     */
    void setTypeface(Typeface tf);
}