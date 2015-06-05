package fr.exia.stentor.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.TextView;

import fr.exia.stentor.R;

public class TextViewRobotoNoto extends TextView {

	private static SparseArray<Typeface> mTypefaces;

	public TextViewRobotoNoto(Context context) {
		this(context, null);
	}

	public TextViewRobotoNoto(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextViewRobotoNoto(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		if (this.isInEditMode()) {
			return;
		}

		configureTypeface(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public TextViewRobotoNoto(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

		if (this.isInEditMode()) {
			return;
		}

		configureTypeface(context, attrs);
	}

	private void configureTypeface(Context context, AttributeSet attrs) {
		if (mTypefaces == null) {
			mTypefaces = configureFont(context.getAssets());
		}

		final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TypographicStyle);

		if (array != null) {
			final int typefaceInt = array.getInteger(R.styleable.TypographicStyle_typoStyle, -1);

			if (typefaceInt != -1) {
				Typeface typeface = mTypefaces.get(typefaceInt, mTypefaces.get(Font.ROBOTO_REGULAR.ordinal()));
				setTypeface(typeface);
			}
			array.recycle();
		}
	}

	private SparseArray<Typeface> configureFont(AssetManager assets) {
		SparseArray<Typeface> sparseArray = new SparseArray<>();

		for (Font fr : Font.values()) {
			sparseArray.put(fr.ordinal(), Typeface.createFromAsset(assets, fr.id));
		}

		return sparseArray;
	}

	private enum Font{
		NOTO_BOLD("fonts/NotoSans-Bold.ttf"),
		NOTO_REGULAR("fonts/NotoSans-Regular.ttf"),
		ROBOTO_LIGHT("fonts/Roboto-Light.ttf"),
		ROBOTO_MEDIUM("fonts/Roboto-Medium.ttf"),
		ROBOTO_REGULAR("fonts/Roboto-Regular.ttf");

		public String id;

		Font(String id) {
			this.id = id;
		}


	}
}
