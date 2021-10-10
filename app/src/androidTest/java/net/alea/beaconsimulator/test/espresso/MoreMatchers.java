package net.alea.beaconsimulator.test.espresso;

import android.view.View;
import android.widget.ImageButton;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.*;


public class MoreMatchers {

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @SuppressWarnings("SimplifiableIfStatement")
            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static Matcher<View> navigateBackToolbarButton() {
        return allOf(
                withParent(withClassName(is(Toolbar.class.getName()))),
                withClassName(anyOf(
                        is(ImageButton.class.getName()),
                        is(AppCompatImageButton.class.getName())
                )));
    }
}
