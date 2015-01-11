/*
Copyright 2010-2013 Michael Shick

This file is part of 'Lock Pattern Generator'.

'Lock Pattern Generator' is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

'Lock Pattern Generator' is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
'Lock Pattern Generator'.  If not, see <http://www.gnu.org/licenses/>.
*/
package in.shick.lockpatterngenerator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextWallActivity extends BaseActivity
{
    public static final String EXTRA_LAYOUT_RESOURCE = "layout_res";
    public static final String EXTRA_HTML_RESOURCE = "html_source";
    public static final int DEFAULT_LAYOUT_RESOURCE = R.layout.text_wall_basic;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int layout = getIntent().getIntExtra(EXTRA_LAYOUT_RESOURCE,
                DEFAULT_LAYOUT_RESOURCE);
        setContentView(layout);

        TextView textWall = (TextView) findViewById(R.id.text_wall);
        String wallText = getString(R.string.text_wall_failure);

        int htmlResouce = getIntent().getIntExtra(EXTRA_HTML_RESOURCE,0);

        Resources resources = this.getResources();
        try
        {
            InputStream input = resources.openRawResource(htmlResouce);
            byte bytes[] = new byte[input.available()];
            input.read(bytes);
            wallText = new String(bytes);
        }
        catch(Throwable e)
        {
            wallText += e.toString();
        }

        textWall.setText(Html.fromHtml(wallText));
        textWall.setMovementMethod(new SafeLinkMovementMethod());
        //textWall.setMovementMethod(SafeLinkMovementMethod.getInstance());
        textWall.setClickable(false);
        textWall.setLongClickable(false);
    }

    /** Simple extension to LinkMovementMethod that eats
     * ActivityNotFoundExceptions.  Specifically handles bitcoin URIs more
     * gracefully.  Hackily implemented with exception flow control and
     * Throwable message matching.
     */
    protected class SafeLinkMovementMethod extends LinkMovementMethod
    {
        protected Pattern addressPattern;

        public SafeLinkMovementMethod()
        {
            super();
            addressPattern = Pattern.compile(
                    ".*dat=bitcoin:([13][1-9A-HJ-Za-km-z]{20,40}).*");
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable text,
                MotionEvent event) {
            try
            {
                return super.onTouchEvent(widget, text, event);
            }
            catch(ActivityNotFoundException e)
            {
                Matcher addressMatcher = addressPattern.matcher(e.getMessage());
                if(addressMatcher.matches())
                {
                    new BitcoinAddressDialogBuilder(TextWallActivity.this,
                            addressMatcher.group(1)).build().show();
                }
                else
                {
                    throw e;
                }
                return true;
            }
        }
    }
}
